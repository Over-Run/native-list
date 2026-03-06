// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// The `double` specialized version of [NativeList].
public class DoubleNativeList extends NativeList implements DoubleNativeListView {
    /// Constructor of [DoubleNativeList].
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    /// @param initialCapacity  the initial capacity of the native list; defaults to 8
    /// @throws IllegalArgumentException if `initialCapacity < 0`
    public DoubleNativeList(AllocatorFactory allocatorFactory, long initialCapacity) {
        super(ValueLayout.JAVA_DOUBLE, allocatorFactory, initialCapacity);
    }
    /// Constructor of [DoubleNativeList].
    ///
    /// It is recommended to construct a native list with an initial capacity.
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    public DoubleNativeList(AllocatorFactory allocatorFactory) {
        super(ValueLayout.JAVA_DOUBLE, allocatorFactory);
    }

    /// Constructor of [DoubleNativeList].
    ///
    /// This copies element layout and data from `list`.
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    /// @param list             the source native list
    public DoubleNativeList(AllocatorFactory allocatorFactory, DoubleNativeList list) {
        super(allocatorFactory, list);
    }

    @Override public double get(long index) {
        Objects.checkIndex(index, size);
        return data.getAtIndex(ValueLayout.JAVA_DOUBLE, index);
    }

    /// Inserts the given element at the end.
    /// @param value the value
    public void add(double value) {
        ensureCapacity(size + 1);
        data.setAtIndex(ValueLayout.JAVA_DOUBLE, size, value);
        size++;
    }

    /// Inserts the given element at the given index.
    /// @param index the index of the element to be inserted
    /// @param value the value
    public void add(long index, double value) {
        if (index == size) {
            add(value);
            return;
        }

        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        move(index, index + 1);
        data.setAtIndex(ValueLayout.JAVA_DOUBLE, index, value);
        size++;
    }

    /// Inserts the given elements at the end.
    /// @param values the values
    public void addAll(double[] values) {
        if (values.length == 0) return;
        ensureCapacity(size + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE.scale(0, size), values.length);
        size += values.length;
    }

    /// Inserts the given elements at the given index.
    /// @param index  the index of the elements to be inserted
    /// @param values the values
    public void addAll(long index, double[] values) {
        if (index == size) {
            addAll(values);
            return;
        }

        Objects.checkIndex(index, size + values.length);
        if (values.length == 0) return;
        ensureCapacity(size + values.length);
        move(index, index + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE.scale(0, index), values.length);
        size += values.length;
    }

    @Override public double[] toArray() { return data().toArray(ValueLayout.JAVA_DOUBLE); }

    @Override public ValueLayout.OfDouble elementLayout() { return ValueLayout.JAVA_DOUBLE; }
}
