// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
public class DoubleNativeList extends NativeList {
    public DoubleNativeList(Allocator allocator, long initialCapacity) {
        super(ValueLayout.JAVA_DOUBLE, allocator, initialCapacity);
    }

    public DoubleNativeList(Allocator allocator) {
        super(ValueLayout.JAVA_DOUBLE, allocator);
    }

    public double get(long index) {
        Objects.checkIndex(index, size);
        return data.getAtIndex(ValueLayout.JAVA_DOUBLE, index);
    }

    public void add(double value) {
        ensureCapacity(size + 1);
        data.setAtIndex(ValueLayout.JAVA_DOUBLE, size, value);
        size++;
    }

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

    public void addAll(double[] values) {
        ensureCapacity(size + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE.scale(0, size), values.length);
        size += values.length;
    }

    public void addAll(long index, double[] values) {
        if (index == size) {
            addAll(values);
            return;
        }

        Objects.checkIndex(index, size + values.length);
        ensureCapacity(size + values.length);
        move(index, index + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_DOUBLE, ValueLayout.JAVA_DOUBLE.scale(0, index), values.length);
        size += values.length;
    }

    public double[] toArray() { return data().toArray(ValueLayout.JAVA_DOUBLE); }
    @Override public ValueLayout.OfDouble elementLayout() { return ValueLayout.JAVA_DOUBLE; }
}
