// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// A readonly view of [BoolNativeList].
public interface BoolNativeListView extends NativeListView {
    /// {@return the element at the given index}
    /// @param index the index of the element
    boolean get(long index);

    @Override default ValueLayout.OfBoolean elementLayout() { return ValueLayout.JAVA_BOOLEAN; }
}
