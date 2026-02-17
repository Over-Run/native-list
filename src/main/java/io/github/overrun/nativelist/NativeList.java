package io.github.overrun.nativelist;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/// A resizable array backed by a [MemorySegment].
///
/// @since 1.0.0
public class NativeList implements NativeListView, AutoCloseable {
    private final MemoryLayout elementLayout;
    private final Allocator allocator;
    protected MemorySegment data;
    private long capacity;
    protected long size = 0;

    public interface Allocator {
        /// Allocates a block of memory with the given size and alignment constraint.
        ///
        /// The implementation **MUST NOT** return [MemorySegment#NULL].
        /// They should instead raise [OutOfMemoryError].
        ///
        /// @param byteSize      the size of the memory to be allocated in bytes
        /// @param byteAlignment the alignment constraint in bytes
        /// @throws IllegalArgumentException if `bytesSize < 0`, `byteAlignment <= 0`, or if `byteAlignment` is not a power of 2
        /// @throws OutOfMemoryError         if error occurs when allocating memory
        MemorySegment allocate(long byteSize, long byteAlignment);

        /// Allocates a block of memory with the contents of the given memory segment and the alignment constraint.
        ///
        /// The implementation **MUST NOT** return [MemorySegment#NULL].
        /// They should instead raise [OutOfMemoryError].
        ///
        /// @param segment       the memory segment to be copied
        /// @param byteAlignment the alignment constraint in bytes
        /// @throws IllegalArgumentException if `byteAlignment <= 0`, or if `byteAlignment` is not a power of 2
        /// @throws OutOfMemoryError         if error occurs when allocating memory
        MemorySegment allocateFrom(MemorySegment segment, long byteAlignment);

        /// Reallocates the memory segment with a new size.
        ///
        /// The implementation **can only** return [MemorySegment#NULL] when `newByteSize == 0`.
        /// Otherwise, they should instead raise [OutOfMemoryError].
        ///
        /// @param segment       the memory segment to be reallocated, which **may** be [MemorySegment#NULL]
        /// @param newByteSize   the new size in bytes, which **may** be smaller than the previous one
        /// @param byteAlignment the alignment constraint in bytes
        /// @throws IllegalArgumentException if `bytesSize < 0`, `byteAlignment <= 0`, or if `byteAlignment` is not a power of 2
        /// @throws OutOfMemoryError         if error occurs when reallocating memory
        MemorySegment reallocate(MemorySegment segment, long newByteSize, long byteAlignment);

        /// Releases the given memory segment or the arena associated to this allocator.
        ///
        /// This method does no operation if `segment` is [MemorySegment#NULL].
        ///
        /// @param segment the memory segment to be released, which **may** be [MemorySegment#NULL]
        void free(MemorySegment segment);

        static Allocator ofConfinedArena() {
            return ArenaAllocator.ofConfined();
        }

        static Allocator ofSharedArena() {
            return ArenaAllocator.ofShared();
        }

        static Allocator ofAutoArena() {
            return ArenaAllocator.ofAuto();
        }

        static Allocator c() {
            return CAllocator.of();
        }
    }

    @FunctionalInterface
    public interface AllocatorFactory {
        Allocator create();
    }

    public NativeList(MemoryLayout elementLayout, AllocatorFactory allocatorFactory, long initialCapacity) {
        this.elementLayout = elementLayout;
        this.capacity = initialCapacity;
        this.data = MemorySegment.NULL;
        this.allocator = allocatorFactory.create();
        if (initialCapacity > 0) {
            this.data = allocator.allocate(elementLayout.scale(0, initialCapacity), elementLayout.byteAlignment());
        } else if (initialCapacity != 0) {
            throw new IllegalArgumentException("Illegal capacity: " + initialCapacity);
        }
    }

    public NativeList(MemoryLayout elementLayout, AllocatorFactory allocatorFactory) {
        this(elementLayout, allocatorFactory, 8);
    }

    public NativeList(AllocatorFactory allocatorFactory, NativeList list) {
        this.elementLayout = list.elementLayout;
        this.capacity = list.capacity;
        this.size = list.size;
        this.data = MemorySegment.NULL;
        this.allocator = allocatorFactory.create();
        this.data = allocator.allocateFrom(list.data, elementLayout.byteAlignment());
    }

    public static NativeList move(AllocatorFactory allocatorFactory, NativeList list) {
        NativeList nativeList = new NativeList(allocatorFactory, list);
        list.free();
        return nativeList;
    }

    @Override
    public MemorySegment getConstElementRef(long index) {
        return getElementRef(index).asReadOnly();
    }

    public MemorySegment getElementRef(long index) {
        Objects.checkIndex(index, size);
        return data.asSlice(elementLayout.scale(0, index), elementLayout);
    }

    public void add(Consumer<MemorySegment> elementRefConsumer) {
        ensureCapacity(size + 1);
        elementRefConsumer.accept(getElementRef(size));
        size++;
    }

    public void add(long index, Consumer<MemorySegment> elementRefConsumer) {
        if (index == size) {
            add(elementRefConsumer);
            return;
        }

        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        move(index, index + 1);
        elementRefConsumer.accept(getElementRef(index));
        size++;
    }

    public void addAll(long count, Consumer<MemorySegment> elementRefConsumer) {
        ensureCapacity(size + count);
        elementRefConsumer.accept(asSlice(size, count));
        size += count;
    }

    public void addAll(long index, long count, Consumer<MemorySegment> elementRefConsumer) {
        if (index == size) {
            addAll(count, elementRefConsumer);
            return;
        }

        Objects.checkIndex(index, size + count);
        ensureCapacity(size + count);
        move(index, index + count);
        elementRefConsumer.accept(asSlice(index, count));
        size += count;
    }

    public void remove(long index) {
        if (index == 0) {
            removeFirst();
            return;
        }
        if (index == size - 1) {
            removeLast();
            return;
        }

        Objects.checkIndex(index, size);
        move(index + 1, index);
        size--;
    }

    public void removeFirst() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        if (size > 1) {
            move(1, 0);
        }
        size--;
    }

    public void removeLast() {
        long last = size - 1;
        if (last < 0) {
            throw new NoSuchElementException();
        }
        size--;
    }

    private void reallocate(long newCapacity) {
        data = allocator.reallocate(data, elementLayout.scale(0, newCapacity), elementLayout.byteAlignment());
        capacity = newCapacity;
    }

    protected void ensureCapacity(long required) {
        if (required > capacity) {
            long newCapacity = Math.max(capacity * 2, required);
            reallocate(newCapacity);
        }
    }

    protected void move(long fromIndex, long toIndex) {
        MemorySegment.copy(data,
            elementLayout.scale(0, fromIndex),
            data,
            elementLayout.scale(0, toIndex),
            elementLayout.scale(0, size - fromIndex));
    }

    public void reverse(long newCapacity) {
        if (newCapacity > capacity) {
            reallocate(newCapacity);
        }
    }

    public void trimToSize() {
        if (size < capacity) {
            reallocate(size);
        }
    }

    @Override
    public MemoryLayout elementLayout() {
        return elementLayout;
    }

    @Override
    public MemorySegment constData() {
        return data().asReadOnly();
    }

    public MemorySegment data() {
        return data.asSlice(0, elementLayout.scale(0, size));
    }

    @Override
    public MemorySegment asConstSlice(long index, long size) {
        return asSlice(index, size).asReadOnly();
    }

    public MemorySegment asSlice(long index, long size) {
        Objects.checkFromIndexSize(index, size, this.size);
        return data.asSlice(elementLayout.scale(0, index), elementLayout.scale(0, size));
    }

    @Override
    public long capacity() {
        return capacity;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        size = 0;
    }

    public void free() {
        allocator.free(data);
        data = MemorySegment.NULL;
    }

    @Override
    public void close() {
        free();
    }
}
