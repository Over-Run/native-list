package io.github.overrun.nativelist;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

/// A readonly view of [NativeList].
///
/// @since 1.0.0
public interface NativeListView {
    /// {@return the memory layout of this native list's elements}
    MemoryLayout elementLayout();

    /// {@return a read-only slice of the data containing the element at the given index}
    ///
    /// @param index the index of the element
    MemorySegment getConstElementRef(long index);

    /// {@return a read-only view of this native list's data}
    MemorySegment constData();

    /// {@return a read-only slice of the data with the given size}
    ///
    /// @param index the index of the starting element
    /// @param size  the count of the elements
    MemorySegment asConstSlice(long index, long size);

    /// {@return the maximum count of the elements}
    long capacity();

    /// {@return the current count of the elements}
    long size();

    /// {@return `size() == 0`}
    boolean isEmpty();
}
