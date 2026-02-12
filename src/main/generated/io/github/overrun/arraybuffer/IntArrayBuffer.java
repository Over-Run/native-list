// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.arraybuffer;
import java.lang.foreign.*;
import java.util.*;
public class IntArrayBuffer extends ArrayBuffer {
    public IntArrayBuffer(Allocator allocator, long initialCapacity) {
        super(ValueLayout.JAVA_INT, allocator, initialCapacity);
    }

    public IntArrayBuffer(Allocator allocator) {
        super(ValueLayout.JAVA_INT, allocator);
    }

    public int get(long index) {
        Objects.checkIndex(index, size);
        return data.getAtIndex(ValueLayout.JAVA_INT, index);
    }

    public void add(int value) {
        ensureCapacity(size + 1);
        data.setAtIndex(ValueLayout.JAVA_INT, size, value);
        size++;
    }

    public void add(long index, int value) {
        if (index == size) {
            add(value);
            return;
        }

        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        move(index, index + 1);
        data.setAtIndex(ValueLayout.JAVA_INT, index, value);
        size++;
    }

    public void addAll(int[] values) {
        ensureCapacity(size + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT.scale(0, size), values.length);
        size += values.length;
    }

    public void addAll(long index, int[] values) {
        if (index == size) {
            addAll(values);
            return;
        }

        Objects.checkIndex(index, size + values.length);
        ensureCapacity(size + values.length);
        move(index, index + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT.scale(0, index), values.length);
        size += values.length;
    }

    public int[] toArray() { return data().toArray(ValueLayout.JAVA_INT); }
    @Override public ValueLayout.OfInt elementLayout() { return ValueLayout.JAVA_INT; }
}
