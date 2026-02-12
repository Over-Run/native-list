// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.arraybuffer;
import java.lang.foreign.*;
import java.util.*;
public class AddressArrayBuffer extends ArrayBuffer {
    public AddressArrayBuffer(Allocator allocator, long initialCapacity) {
        super(ValueLayout.ADDRESS, allocator, initialCapacity);
    }

    public AddressArrayBuffer(Allocator allocator) {
        super(ValueLayout.ADDRESS, allocator);
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
