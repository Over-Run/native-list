// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// A readonly view of [ShortNativeList].
public interface ShortNativeListView extends NativeListView {
    /// {@return the element at the given index}
    /// @param index the index of the element
    short get(long index);

    /// {@return a new array containing the data in Java form}
    default short[] toArray() { return constData().toArray(ValueLayout.JAVA_SHORT); }

    @Override default ValueLayout.OfShort elementLayout() { return ValueLayout.JAVA_SHORT; }
}
