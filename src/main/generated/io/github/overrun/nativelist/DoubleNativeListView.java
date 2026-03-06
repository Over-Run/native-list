// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// A readonly view of [DoubleNativeList].
public interface DoubleNativeListView extends NativeListView {
    /// {@return the element at the given index}
    /// @param index the index of the element
    double get(long index);

    /// {@return a new array containing the data in Java form}
    default double[] toArray() { return constData().toArray(ValueLayout.JAVA_DOUBLE); }

    @Override default ValueLayout.OfDouble elementLayout() { return ValueLayout.JAVA_DOUBLE; }
}
