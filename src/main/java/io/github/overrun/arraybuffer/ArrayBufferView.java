package io.github.overrun.arraybuffer;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;

/// @since 1.0.0
public interface ArrayBufferView {
    MemoryLayout elementLayout();

    MemorySegment getConstElementRef(long index);

    MemorySegment constData();

    MemorySegment asConstSlice(long index, long size);

    long capacity();

    long size();

    boolean isEmpty();
}
