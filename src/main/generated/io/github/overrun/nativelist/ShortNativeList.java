// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
public class ShortNativeList extends NativeList {
    public ShortNativeList(AllocatorFactory allocatorFactory, long initialCapacity) {
        super(ValueLayout.JAVA_SHORT, allocatorFactory, initialCapacity);
    }

    public ShortNativeList(AllocatorFactory allocatorFactory) {
        super(ValueLayout.JAVA_SHORT, allocatorFactory);
    }

    public ShortNativeList(AllocatorFactory allocatorFactory, ShortNativeList list) {
        super(allocatorFactory, list);
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
