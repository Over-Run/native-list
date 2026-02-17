// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
public class LongNativeList extends NativeList {
    public LongNativeList(Allocator allocator, long initialCapacity) {
        super(ValueLayout.JAVA_LONG, allocator, initialCapacity);
    }

    public LongNativeList(Allocator allocator) {
        super(ValueLayout.JAVA_LONG, allocator);
    }

    public long get(long index) {
        Objects.checkIndex(index, size);
        return data.getAtIndex(ValueLayout.JAVA_LONG, index);
    }

    public void add(long value) {
        ensureCapacity(size + 1);
        data.setAtIndex(ValueLayout.JAVA_LONG, size, value);
        size++;
    }

    public void add(long index, long value) {
        if (index == size) {
            add(value);
            return;
        }

        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        move(index, index + 1);
        data.setAtIndex(ValueLayout.JAVA_LONG, index, value);
        size++;
    }

    public void addAll(long[] values) {
        ensureCapacity(size + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG.scale(0, size), values.length);
        size += values.length;
    }

    public void addAll(long index, long[] values) {
        if (index == size) {
            addAll(values);
            return;
        }

        Objects.checkIndex(index, size + values.length);
        ensureCapacity(size + values.length);
        move(index, index + values.length);
        MemorySegment.copy(values, 0, data, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG.scale(0, index), values.length);
        size += values.length;
    }

    public long[] toArray() { return data().toArray(ValueLayout.JAVA_LONG); }
    @Override public ValueLayout.OfLong elementLayout() { return ValueLayout.JAVA_LONG; }
}
