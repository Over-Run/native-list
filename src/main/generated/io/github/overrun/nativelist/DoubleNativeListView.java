// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// A readonly view of [DoubleNativeList].
public interface DoubleNativeListView extends NativeListView {
    /// {@return the element at the given index}
    /// @param index the index of the element
    /// @throws IndexOutOfBoundsException if the index is out of bounds
    double get(long index);

    /// {@return a new array containing the data in Java form}
    double[] toArray();

    @Override ValueLayout.OfDouble elementLayout();
}
