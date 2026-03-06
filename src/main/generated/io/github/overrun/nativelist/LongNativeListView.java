// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// A readonly view of [LongNativeList].
public interface LongNativeListView extends NativeListView {
    /// {@return the element at the given index}
    /// @param index the index of the element
    long get(long index);

    /// {@return a new array containing the data in Java form}
    default long[] toArray() { return constData().toArray(ValueLayout.JAVA_LONG); }

    @Override default ValueLayout.OfLong elementLayout() { return ValueLayout.JAVA_LONG; }
}
