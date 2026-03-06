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
    long[] toArray();

    @Override ValueLayout.OfLong elementLayout();
}
