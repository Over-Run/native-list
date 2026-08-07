package io.github.overrun.nativelist.lwjgl;

import io.github.overrun.nativelist.ListAllocator;

import java.lang.foreign.MemorySegment;

import static org.lwjgl.system.MemoryUtil.*;

/// A [ListAllocator], which allocates memory with [`MemoryUtil`][org.lwjgl.system.MemoryUtil].
///
/// @since 1.0.0
public final class Lwjgl3ListAllocator implements ListAllocator {
    private static final Lwjgl3ListAllocator INSTANCE = new Lwjgl3ListAllocator();

    /// {@return the instance of `Lwjgl3ListAllocator`}
    public static Lwjgl3ListAllocator of() {
        return INSTANCE;
    }

    private Lwjgl3ListAllocator() {
    }

    @Override
    public MemorySegment allocate(long byteSize, long byteAlignment) {
        ListAllocator.checkSizeAndAlignment(byteSize, byteAlignment);
        return MemorySegment.ofAddress(nmemAllocChecked(byteSize)).reinterpret(byteSize);
    }

    @Override
    public MemorySegment reallocate(MemorySegment segment, long newByteSize, long byteAlignment) {
        ListAllocator.checkSizeAndAlignment(newByteSize, byteAlignment);
        long address = nmemRealloc(segment.address(), newByteSize);
        if (newByteSize != 0 && address == NULL) {
            throw new OutOfMemoryError();
        }
        return MemorySegment.ofAddress(address).reinterpret(newByteSize);
    }

    @Override
    public void free(MemorySegment segment) {
        nmemFree(segment.address());
    }
}
