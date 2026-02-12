package io.github.overrun.arraybuffer;

import java.lang.foreign.Arena;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/// @since 1.0.0
public class ArrayBuffer implements ArrayBufferView, AutoCloseable {
    private final MemoryLayout elementLayout;
    private final Allocator allocator;
    protected MemorySegment data;
    private long capacity;
    protected long size = 0;

    public interface Allocator {
        MemorySegment allocate(long byteSize, long byteAlignment);

        MemorySegment reallocate(MemorySegment segment, long newByteSize);

        void free(MemorySegment segment);

        static Allocator ofConfinedArena() {
            return new ArenaAllocator(Arena::ofConfined, true);
        }

        static Allocator ofSharedArena() {
            return new ArenaAllocator(Arena::ofShared, true);
        }

        static Allocator ofAutoArena() {
            return new ArenaAllocator(Arena::ofAuto, true);
        }
    }

    public ArrayBuffer(MemoryLayout elementLayout, Allocator allocator, long initialCapacity) {
        if (initialCapacity > 0) {
            this.data = allocator.allocate(elementLayout.scale(0, initialCapacity), elementLayout.byteAlignment());
        } else if (initialCapacity == 0) {
            this.data = MemorySegment.NULL;
        } else {
            throw new IllegalArgumentException("Illegal capacity: " + initialCapacity);
        }
        this.elementLayout = elementLayout;
        this.allocator = allocator;
        this.capacity = initialCapacity;
    }

    public ArrayBuffer(MemoryLayout elementLayout, Allocator allocator) {
        this(elementLayout, allocator, 8);
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
        data = allocator.reallocate(data, elementLayout.scale(0, newCapacity));
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
    }

    @Override
    public void close() {
        free();
    }
}
