// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// The `char` specialized version of [NativeList].
public class CharNativeList extends NativeList implements CharNativeListView {
    /// Constructor of [CharNativeList].
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    /// @param initialCapacity  the initial capacity of the native list; defaults to 8
    /// @throws IllegalArgumentException if `initialCapacity < 0`
    public CharNativeList(AllocatorFactory allocatorFactory, long initialCapacity) {
        super(ValueLayout.JAVA_CHAR, allocatorFactory, initialCapacity);
    }
    /// Constructor of [CharNativeList].
    ///
    /// It is recommended to construct a native list with an initial capacity.
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    public CharNativeList(AllocatorFactory allocatorFactory) {
        super(ValueLayout.JAVA_CHAR, allocatorFactory);
    }

    /// Constructor of [CharNativeList].
    ///
    /// This copies element layout and data from `list`.
    /// @param allocatorFactory a factory of the [allocator][Allocator]
    /// @param list             the source native list
    public CharNativeList(AllocatorFactory allocatorFactory, CharNativeList list) {
        super(allocatorFactory, list);
    }

    @Override public char get(long index) {
        Objects.checkIndex(index, size);
        return data.getAtIndex(ValueLayout.JAVA_CHAR, index);
    }

    /// Inserts the given element at the end.
    /// @param value the value
    public void add(char value) {
        ensureCapacity(size + 1);
        data.setAtIndex(ValueLayout.JAVA_CHAR, size, value);
        size++;
    }

    /// Inserts the given element at the given index.
    /// @param index the index of the element to be inserted
    /// @param value the value
    public void add(long index, char value) {
        if (index == size) {
            add(value);
            return;
        }

        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        move(index, index + 1);
        data.setAtIndex(ValueLayout.JAVA_CHAR, index, value);
        size++;
    }

    /// Inserts the given elements at the end.
    /// @param values the values
    public void addAll(char[] values) {
        if (values.length == 0) return;
        ensureCapacity(size + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_CHAR, ValueLayout.JAVA_CHAR.scale(0, size), values.length);
        size += values.length;
    }

    /// Inserts the given elements at the given index.
    /// @param index  the index of the elements to be inserted
    /// @param values the values
    public void addAll(long index, char[] values) {
        if (index == size) {
            addAll(values);
            return;
        }

        Objects.checkIndex(index, size + values.length);
        if (values.length == 0) return;
        ensureCapacity(size + values.length);
        move(index, index + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_CHAR, ValueLayout.JAVA_CHAR.scale(0, index), values.length);
        size += values.length;
    }

    @Override public char[] toArray() { return data().toArray(ValueLayout.JAVA_CHAR); }

    @Override public ValueLayout.OfChar elementLayout() { return ValueLayout.JAVA_CHAR; }
}
