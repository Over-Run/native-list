// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// A readonly view of [ByteNativeList].
public interface ByteNativeListView extends NativeListView {
    /// {@return the element at the given index}
    /// @param index the index of the element
    /// @throws IndexOutOfBoundsException if the index is out of bounds
    byte get(long index);

    /// {@return a new array containing the data in Java form}
    byte[] toArray();

    @Override ValueLayout.OfByte elementLayout();
}
