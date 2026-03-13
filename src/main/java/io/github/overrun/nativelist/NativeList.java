package io.github.overrun.nativelist;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/// A resizable array backed by a [MemorySegment].
///
/// The user is responsible for closing the native list.
///
/// ## Allocator
///
/// An [allocator][ListAllocator] must be provided by the [allocator factory][ListAllocatorFactory]
/// to allocate memory for the native list.
///
/// ## Example
///
/// ```java
/// /// struct Point {
/// ///     int32 x;
/// ///     int32 y;
/// /// }
/// class Point {
///     static final StructLayout LAYOUT = MemoryLayout.structLayout(
///         ValueLayout.JAVA_INT.withName("x"),
///         ValueLayout.JAVA_INT.withName("y")
///     );
///     static final VarHandle VH_x = LAYOUT.varHandle(PathElement.groupElement("x"));
///     static final VarHandle VH_y = LAYOUT.varHandle(PathElement.groupElement("y"));
///     final MemorySegment segment;
///
///     Point(MemorySegment segment) { this.segment = segment; }
///     int x() { return (int) VH_x.get(segment, 0L); }
///     int y() { return (int) VH_y.get(segment, 0L); }
///     void set(int x, int y) {
///         VH_x.set(segment, 0L, x);
///         VH_y.set(segment, 0L, y);
///         return this;
///     }
/// }
///
/// void main() {
///     // create a native list with the struct layout and confined arena
///     try (var list = new NativeList(Point.LAYOUT, ListAllocator::ofConfinedArena)) {
///         // construct a point in place
///         list.add(seg -> new Point(seg).set(1, 2));
///         assertEquals(1, list.size());
///         // get the point
///         var p1 = new Point(list.getElementRef(0));
///         // should be the same as p1
///         var p2 = new Point(list.getElementRef(0));
///         assertEquals(1, p1.x());
///         assertEquals(2, p1.y());
///         p1.set(3, 4);
///         assertEquals(3, p2.x());
///         assertEquals(4, p2.y());
///     }
/// }
/// ```
///
/// @since 1.0.0
public class NativeList implements NativeListView, AutoCloseable {
    private final MemoryLayout elementLayout;
    private final ListAllocator allocator;
    /// The data.
    protected MemorySegment data;
    private long capacity;
    /// The element count.
    protected long size = 0;

    /// Constructor of [NativeList].
    ///
    /// @param elementLayout    the memory layout of the element
    /// @param allocatorFactory a factory of the [allocator][ListAllocator]
    /// @param initialCapacity  the initial capacity of the native list; defaults to 8
    /// @throws IllegalArgumentException if `initialCapacity < 0`
    public NativeList(MemoryLayout elementLayout, ListAllocatorFactory allocatorFactory, long initialCapacity) {
        this.elementLayout = elementLayout;
        this.capacity = initialCapacity;
        this.allocator = allocatorFactory.create();
        if (initialCapacity > 0) {
            this.data = allocator.allocate(elementLayout.scale(0, initialCapacity), elementLayout.byteAlignment());
        } else if (initialCapacity != 0) {
            throw new IllegalArgumentException("Illegal capacity: " + initialCapacity);
        } else {
            this.data = MemorySegment.NULL;
        }
    }

    /// Constructor of [NativeList].
    ///
    /// It is recommended to construct a native list with an initial capacity.
    ///
    /// @param elementLayout    the memory layout of the element
    /// @param allocatorFactory a factory of the [allocator][ListAllocator]
    /// @throws IllegalArgumentException if `initialCapacity < 0`
    /// @see NativeList#NativeList(MemoryLayout, ListAllocatorFactory, long)
    public NativeList(MemoryLayout elementLayout, ListAllocatorFactory allocatorFactory) {
        this(elementLayout, allocatorFactory, 8);
    }

    /// Constructor of [NativeList].
    ///
    /// This copies element layout and data from `list`.
    ///
    /// @param allocatorFactory a factory of the [allocator][ListAllocator]
    /// @param list             the source native list
    public NativeList(ListAllocatorFactory allocatorFactory, NativeList list) {
        this.elementLayout = list.elementLayout;
        this.capacity = list.capacity;
        this.size = list.size;
        this.allocator = allocatorFactory.create();
        this.data = allocator.allocateFrom(list.data, elementLayout.byteAlignment());
    }

    /// Creates a new native list with the given allocator factory,
    /// copies data from `list` and frees up `list`.
    ///
    /// @param allocatorFactory a factory of the [allocator][ListAllocator]
    /// @param list             the source native list
    /// @return a native list
    public static NativeList move(ListAllocatorFactory allocatorFactory, NativeList list) {
        NativeList nativeList = new NativeList(allocatorFactory, list);
        list.free();
        return nativeList;
    }

    @Override
    public MemorySegment getConstElementRef(long index) {
        return getElementRef(index).asReadOnly();
    }

    /// {@return a slice of the data containing the element at the given index}
    ///
    /// @param index the index of the element
    /// @throws IndexOutOfBoundsException if the index is out of bounds
    public MemorySegment getElementRef(long index) {
        Objects.checkIndex(index, size);
        return getElementRefNoCheck(index);
    }

    private MemorySegment getElementRefNoCheck(long index) {
        return data.asSlice(elementLayout.scale(0, index), elementLayout);
    }

    /// Constructs an element in place and inserts it at the end.
    ///
    /// @param elementRefConsumer the consumer accepting a slice of the data
    public void add(Consumer<MemorySegment> elementRefConsumer) {
        ensureCapacity(size + 1);
        elementRefConsumer.accept(getElementRefNoCheck(size));
        size++;
    }

    /// Constructs an element in place and inserts it at the given index.
    ///
    /// @param index              the index of the element to be inserted
    /// @param elementRefConsumer the consumer accepting a slice of the data
    /// @throws IndexOutOfBoundsException if the index is out of bounds
    public void add(long index, Consumer<MemorySegment> elementRefConsumer) {
        if (index == size) {
            add(elementRefConsumer);
            return;
        }

        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        move(index, index + 1);
        elementRefConsumer.accept(getElementRefNoCheck(index));
        size++;
    }

    /// Constructs `count` elements in place and inserts them at the end.
    ///
    /// @param count              the count of element
    /// @param elementRefConsumer the consumer accepting a slice of the data
    public void addAll(long count, Consumer<MemorySegment> elementRefConsumer) {
        ensureCapacity(size + count);
        elementRefConsumer.accept(asSliceNoCheck(size, count));
        size += count;
    }

    /// Constructs `count` elements in place and inserts them at the given index.
    ///
    /// @param index              the index of the elements to be inserted
    /// @param count              the count of element
    /// @param elementRefConsumer the consumer accepting a slice of the data
    /// @throws IndexOutOfBoundsException if the index is out of bounds
    public void addAll(long index, long count, Consumer<MemorySegment> elementRefConsumer) {
        if (index == size) {
            addAll(count, elementRefConsumer);
            return;
        }

        Objects.checkIndex(index, size + count);
        ensureCapacity(size + count);
        move(index, index + count);
        elementRefConsumer.accept(asSliceNoCheck(index, count));
        size += count;
    }

    /// Removes the specified element at the given index.
    ///
    /// @param index the index of the element to be removed
    /// @throws IndexOutOfBoundsException if the index is out of bounds
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

    /// Removes the first element.
    ///
    /// @throws NoSuchElementException if this list is empty
    public void removeFirst() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        if (size > 1) {
            move(1, 0);
        }
        size--;
    }

    /// Removes the last element.
    ///
    /// @throws NoSuchElementException if this list is empty
    public void removeLast() {
        long last = size - 1;
        if (last < 0) {
            throw new NoSuchElementException();
        }
        size--;
    }

    /// Removes the specified elements at the given range, from `fromIndex` (inclusive) to `toIndex` (exclusive).
    ///
    /// @param fromIndex the index of the first element to be removed
    /// @param toIndex   the index of the last element to be removed
    /// @throws IndexOutOfBoundsException if the sub-range is out of bounds
    public void removeAll(long fromIndex, long toIndex) {
        Objects.checkFromToIndex(fromIndex, toIndex, size);
        move(toIndex, fromIndex);
        size -= toIndex - fromIndex;
    }

    private void reallocate(long newCapacity) {
        data = allocator.reallocate(data, elementLayout.scale(0, newCapacity), elementLayout.byteAlignment());
        capacity = newCapacity;
    }

    /// Ensures the capacity. Grows 2x if the current capacity is less than the required one.
    ///
    /// @param required the required capacity
    protected void ensureCapacity(long required) {
        if (required > capacity) {
            long newCapacity = Math.max(capacity * 2, required);
            reallocate(newCapacity);
        }
    }

    /// Moves the data, ranging from `fromIndex` to the end, to `dstIndex`.
    ///
    /// @param fromIndex the index of the first element to be moved
    /// @param dstIndex  the new index of the elements to be moved to
    protected void move(long fromIndex, long dstIndex) {
        MemorySegment.copy(data,
            elementLayout.scale(0, fromIndex),
            data,
            elementLayout.scale(0, dstIndex),
            elementLayout.scale(0, size - fromIndex));
    }

    /// Reserves a specified capacity of elements.
    ///
    /// @param newCapacity the new capacity to reserve
    public void reserve(long newCapacity) {
        if (newCapacity > capacity) {
            reallocate(newCapacity);
        }
    }

    /// Trims the data to the size of this list.
    ///
    /// This may lead to significant memory overhead when employing an arena allocator.
    public void trimToSize() {
        if (size < capacity) {
            reallocate(size);
        }
    }

    /// Sets the element count to the given one.
    ///
    /// @param newSize the new size
    public void resize(long newSize) {
        if (newSize > capacity) {
            reallocate(newSize);
        }
        if (newSize > size) {
            asSliceNoCheck(size, newSize - size).fill((byte) 0);
        }
        size = newSize;
    }

    /// Sets the element count to the given one and initialize the new elements with the given value, if `newSize > size()`.
    ///
    /// @param newSize  the new size
    /// @param consumer a consumer accepting a slice of an element
    public void resize(long newSize, Consumer<MemorySegment> consumer) {
        if (newSize > capacity) {
            reallocate(newSize);
        }
        if (newSize > size) {
            asSliceNoCheck(size, newSize - size)
                .elements(elementLayout)
                .forEach(consumer);
        }
        size = newSize;
    }

    @Override
    public MemoryLayout elementLayout() {
        return elementLayout;
    }

    @Override
    public MemorySegment constData() {
        return data().asReadOnly();
    }

    /// {@return the data of this native list}
    public MemorySegment data() {
        return data.asSlice(0, elementLayout.scale(0, size));
    }

    @Override
    public MemorySegment asConstSlice(long index, long size) {
        return asSlice(index, size).asReadOnly();
    }

    /// {@return a slice of the data with the given size}
    ///
    /// @param index the index of the starting element
    /// @param size  the count of the elements
    /// @throws IndexOutOfBoundsException if the sub-range is out of bounds
    public MemorySegment asSlice(long index, long size) {
        Objects.checkFromIndexSize(index, size, this.size);
        return asSliceNoCheck(index, size);
    }

    private MemorySegment asSliceNoCheck(long index, long size) {
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

    /// Clears this list.
    public void clear() {
        size = 0;
    }

    /// Releases this list with [`Allocator::free`][ListAllocator#free(MemorySegment)].
    public void free() {
        allocator.free(data);
        data = MemorySegment.NULL;
    }

    @Override
    public void close() {
        free();
    }
}
