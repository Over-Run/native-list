// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.arraybuffer;
import java.lang.foreign.*;
import java.util.*;
public class FloatArrayBuffer extends ArrayBuffer {
    public FloatArrayBuffer(Allocator allocator, long initialCapacity) {
        super(ValueLayout.JAVA_FLOAT, allocator, initialCapacity);
    }

    public FloatArrayBuffer(Allocator allocator) {
        super(ValueLayout.JAVA_FLOAT, allocator);
    }

    public float get(long index) {
        Objects.checkIndex(index, size);
        return data.getAtIndex(ValueLayout.JAVA_FLOAT, index);
    }

    public void add(float value) {
        ensureCapacity(size + 1);
        data.setAtIndex(ValueLayout.JAVA_FLOAT, size, value);
        size++;
    }

    public void add(long index, float value) {
        if (index == size) {
            add(value);
            return;
        }

        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        move(index, index + 1);
        data.setAtIndex(ValueLayout.JAVA_FLOAT, index, value);
        size++;
    }

    public void addAll(float[] values) {
        ensureCapacity(size + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT.scale(0, size), values.length);
        size += values.length;
    }

    public void addAll(long index, float[] values) {
        if (index == size) {
            addAll(values);
            return;
        }

        Objects.checkIndex(index, size + values.length);
        ensureCapacity(size + values.length);
        move(index, index + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_FLOAT, ValueLayout.JAVA_FLOAT.scale(0, index), values.length);
        size += values.length;
    }

    public float[] toArray() { return data().toArray(ValueLayout.JAVA_FLOAT); }
    @Override public ValueLayout.OfFloat elementLayout() { return ValueLayout.JAVA_FLOAT; }
}
