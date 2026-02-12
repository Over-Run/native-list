// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.arraybuffer;
import java.lang.foreign.*;
import java.util.*;
public class ShortArrayBuffer extends ArrayBuffer {
    public ShortArrayBuffer(Allocator allocator, long initialCapacity) {
        super(ValueLayout.JAVA_SHORT, allocator, initialCapacity);
    }

    public ShortArrayBuffer(Allocator allocator) {
        super(ValueLayout.JAVA_SHORT, allocator);
    }

    public short get(long index) {
        Objects.checkIndex(index, size);
        return data.getAtIndex(ValueLayout.JAVA_SHORT, index);
    }

    public void add(short value) {
        ensureCapacity(size + 1);
        data.setAtIndex(ValueLayout.JAVA_SHORT, size, value);
        size++;
    }

    public void add(long index, short value) {
        if (index == size) {
            add(value);
            return;
        }

        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        move(index, index + 1);
        data.setAtIndex(ValueLayout.JAVA_SHORT, index, value);
        size++;
    }

    public void addAll(short[] values) {
        ensureCapacity(size + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_SHORT, ValueLayout.JAVA_SHORT.scale(0, size), values.length);
        size += values.length;
    }

    public void addAll(long index, short[] values) {
        if (index == size) {
            addAll(values);
            return;
        }

        Objects.checkIndex(index, size + values.length);
        ensureCapacity(size + values.length);
        move(index, index + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_SHORT, ValueLayout.JAVA_SHORT.scale(0, index), values.length);
        size += values.length;
    }

    public short[] toArray() { return data().toArray(ValueLayout.JAVA_SHORT); }
    @Override public ValueLayout.OfShort elementLayout() { return ValueLayout.JAVA_SHORT; }
}
