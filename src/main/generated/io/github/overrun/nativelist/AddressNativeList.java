// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// The `MemorySegment` specialized version of [NativeList].
public class AddressNativeList extends NativeList implements AddressNativeListView {
    /// Constructor of [AddressNativeList].
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    /// @param initialCapacity  the initial capacity of the native list; defaults to 8
    /// @throws IllegalArgumentException if `initialCapacity < 0`
    public AddressNativeList(AllocatorFactory allocatorFactory, long initialCapacity) {
        super(ValueLayout.ADDRESS, allocatorFactory, initialCapacity);
    }
    /// Constructor of [AddressNativeList].
    ///
    /// It is recommended to construct a native list with an initial capacity.
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    public AddressNativeList(AllocatorFactory allocatorFactory) {
        super(ValueLayout.ADDRESS, allocatorFactory);
    }

    /// Constructor of [AddressNativeList].
    ///
    /// This copies element layout and data from `list`.
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    /// @param list             the source native list
    public AddressNativeList(AllocatorFactory allocatorFactory, AddressNativeList list) {
        super(allocatorFactory, list);
    }

    @Override public MemorySegment get(long index) {
        Objects.checkIndex(index, size);
        return data.getAtIndex(ValueLayout.ADDRESS, index);
    }

    /// Inserts the given element at the end.
    /// @param value the value
    public void add(MemorySegment value) {
        ensureCapacity(size + 1);
        data.setAtIndex(ValueLayout.ADDRESS, size, value);
        size++;
    }

    /// Inserts the given element at the given index.
    /// @param index the index of the element to be inserted
    /// @param value the value
    /// @throws IndexOutOfBoundsException if the index is out of bounds
    public void add(long index, MemorySegment value) {
        if (index == size) {
            add(value);
            return;
        }

        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        move(index, index + 1);
        data.setAtIndex(ValueLayout.ADDRESS, index, value);
        size++;
    }

    /// Inserts the given elements at the end.
    /// @param values the values
    public void addAll(MemorySegment[] values) {
        for (var v : values) { add(v); }
    }

    @Override public AddressLayout elementLayout() { return ValueLayout.ADDRESS; }
}
