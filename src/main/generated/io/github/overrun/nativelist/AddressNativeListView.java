// This file is auto-generated. DO NOT EDIT!
package io.github.overrun.nativelist;
import java.lang.foreign.*;
import java.util.*;
/// A readonly view of [AddressNativeList].
public interface AddressNativeListView extends NativeListView {
    /// {@return the element at the given index}
    /// @param index the index of the element
    MemorySegment get(long index);

    @Override default AddressLayout elementLayout() { return ValueLayout.ADDRESS; }
}
