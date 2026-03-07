// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// The `boolean` specialized version of [NativeList].
public class BoolNativeList extends NativeList implements BoolNativeListView {
    /// Constructor of [BoolNativeList].
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    /// @param initialCapacity  the initial capacity of the native list; defaults to 8
    /// @throws IllegalArgumentException if `initialCapacity < 0`
    public BoolNativeList(AllocatorFactory allocatorFactory, long initialCapacity) {
        super(ValueLayout.JAVA_BOOLEAN, allocatorFactory, initialCapacity);
    }
    /// Constructor of [BoolNativeList].
    ///
    /// It is recommended to construct a native list with an initial capacity.
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    public BoolNativeList(AllocatorFactory allocatorFactory) {
        super(ValueLayout.JAVA_BOOLEAN, allocatorFactory);
    }

    /// Constructor of [BoolNativeList].
    ///
    /// This copies element layout and data from `list`.
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    /// @param list             the source native list
    public BoolNativeList(AllocatorFactory allocatorFactory, BoolNativeList list) {
        super(allocatorFactory, list);
    }

    @Override public boolean get(long index) {
        Objects.checkIndex(index, size);
        return data.getAtIndex(ValueLayout.JAVA_BOOLEAN, index);
    }

    /// Inserts the given element at the end.
    /// @param value the value
    public void add(boolean value) {
        ensureCapacity(size + 1);
        data.setAtIndex(ValueLayout.JAVA_BOOLEAN, size, value);
        size++;
    }

    /// Inserts the given element at the given index.
    /// @param index the index of the element to be inserted
    /// @param value the value
    /// @throws IndexOutOfBoundsException if the index is out of bounds
    public void add(long index, boolean value) {
        if (index == size) {
            add(value);
            return;
        }

        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        move(index, index + 1);
        data.setAtIndex(ValueLayout.JAVA_BOOLEAN, index, value);
        size++;
    }

    /// Inserts the given elements at the end.
    /// @param values the values
    public void addAll(boolean[] values) {
        for (var v : values) { add(v); }
    }

    @Override public ValueLayout.OfBoolean elementLayout() { return ValueLayout.JAVA_BOOLEAN; }
}
