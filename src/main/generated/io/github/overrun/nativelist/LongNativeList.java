// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// The `long` specialized version of [NativeList].
public class LongNativeList extends NativeList implements LongNativeListView {
    /// Constructor of [LongNativeList].
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    /// @param initialCapacity  the initial capacity of the native list; defaults to 8
    /// @throws IllegalArgumentException if `initialCapacity < 0`
    public LongNativeList(AllocatorFactory allocatorFactory, long initialCapacity) {
        super(ValueLayout.JAVA_LONG, allocatorFactory, initialCapacity);
    }
    /// Constructor of [LongNativeList].
    ///
    /// It is recommended to construct a native list with an initial capacity.
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    public LongNativeList(AllocatorFactory allocatorFactory) {
        super(ValueLayout.JAVA_LONG, allocatorFactory);
    }

    /// Constructor of [LongNativeList].
    ///
    /// This copies element layout and data from `list`.
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    /// @param list             the source native list
    public LongNativeList(AllocatorFactory allocatorFactory, LongNativeList list) {
        super(allocatorFactory, list);
    }

    @Override public long get(long index) {
        Objects.checkIndex(index, size);
        return data.getAtIndex(ValueLayout.JAVA_LONG, index);
    }

    /// Inserts the given element at the end.
    /// @param value the value
    public void add(long value) {
        ensureCapacity(size + 1);
        data.setAtIndex(ValueLayout.JAVA_LONG, size, value);
        size++;
    }

    /// Inserts the given element at the given index.
    /// @param index the index of the element to be inserted
    /// @param value the value
    /// @throws IndexOutOfBoundsException if the index is out of bounds
    public void add(long index, long value) {
        if (index == size) {
            add(value);
            return;
        }

        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        move(index, index + 1);
        data.setAtIndex(ValueLayout.JAVA_LONG, index, value);
        size++;
    }

    /// Inserts the given elements at the end.
    /// @param values the values
    public void addAll(long[] values) {
        if (values.length == 0) return;
        ensureCapacity(size + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG.scale(0, size), values.length);
        size += values.length;
    }

    /// Inserts the given elements at the given index.
    /// @param index  the index of the elements to be inserted
    /// @param values the values
    /// @throws IndexOutOfBoundsException if the index is out of bounds
    public void addAll(long index, long[] values) {
        if (index == size) {
            addAll(values);
            return;
        }

        Objects.checkIndex(index, size + values.length);
        if (values.length == 0) return;
        ensureCapacity(size + values.length);
        move(index, index + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG.scale(0, index), values.length);
        size += values.length;
    }

    @Override public long[] toArray() { return data().toArray(ValueLayout.JAVA_LONG); }

    @Override public ValueLayout.OfLong elementLayout() { return ValueLayout.JAVA_LONG; }
}
