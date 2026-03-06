// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// A readonly view of [IntNativeList].
public interface IntNativeListView extends NativeListView {
    /// {@return the element at the given index}
    /// @param index the index of the element
    int get(long index);

    /// {@return a new array containing the data in Java form}
    default int[] toArray() { return constData().toArray(ValueLayout.JAVA_INT); }

    @Override default ValueLayout.OfInt elementLayout() { return ValueLayout.JAVA_INT; }
}
