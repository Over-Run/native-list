// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// A readonly view of [FloatNativeList].
public interface FloatNativeListView extends NativeListView {
    /// {@return the element at the given index}
    /// @param index the index of the element
    float get(long index);

    /// {@return a new array containing the data in Java form}
    float[] toArray();

    @Override ValueLayout.OfFloat elementLayout();
}
