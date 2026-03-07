// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// A readonly view of [ShortNativeList].
public interface ShortNativeListView extends NativeListView {
    /// {@return the element at the given index}
    /// @param index the index of the element
    /// @throws IndexOutOfBoundsException if the index is out of bounds
    short get(long index);

    /// {@return a new array containing the data in Java form}
    short[] toArray();

    @Override ValueLayout.OfShort elementLayout();
}
