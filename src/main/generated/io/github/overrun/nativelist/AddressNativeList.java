// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
public class AddressNativeList extends NativeList {
    public AddressNativeList(AllocatorFactory allocatorFactory, long initialCapacity) {
        super(ValueLayout.ADDRESS, allocatorFactory, initialCapacity);
    }

    public AddressNativeList(AllocatorFactory allocatorFactory) {
        super(ValueLayout.ADDRESS, allocatorFactory);
    }

    public AddressNativeList(AllocatorFactory allocatorFactory, AddressNativeList list) {
        super(allocatorFactory, list);
    }

    public MemorySegment get(long index) {
        Objects.checkIndex(index, size);
        return data.getAtIndex(ValueLayout.ADDRESS, index);
    }

    public void add(MemorySegment value) {
        ensureCapacity(size + 1);
        data.setAtIndex(ValueLayout.ADDRESS, size, value);
        size++;
    }

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

    @Override public AddressLayout elementLayout() { return ValueLayout.ADDRESS; }
}
