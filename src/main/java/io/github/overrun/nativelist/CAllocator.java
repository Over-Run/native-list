package io.github.overrun.nativelist;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

/// @since 1.0.0
final class CAllocator implements NativeList.Allocator {
    private static final long ADDRESS_SIZE = ValueLayout.ADDRESS.byteSize();
    private static final long MAX_MALLOC_ALIGN = 2 * ADDRESS_SIZE;
    private static final long OFFSET_alignedPtrRaw = -MAX_MALLOC_ALIGN;
    private static final long OFFSET_alignedPtrSize = -MAX_MALLOC_ALIGN + ADDRESS_SIZE;
    private static final Linker LINKER = Linker.nativeLinker();
    private static final SymbolLookup SYMBOL_LOOKUP = LINKER.defaultLookup();
    private static final MemoryLayout SIZE_T = LINKER.canonicalLayouts().get("size_t");
    private static final MethodHandle MH_malloc = LINKER.downcallHandle(SYMBOL_LOOKUP.findOrThrow("malloc"), FunctionDescriptor.of(ValueLayout.ADDRESS, SIZE_T));
    private static final MethodHandle MH_free = LINKER.downcallHandle(SYMBOL_LOOKUP.findOrThrow("free"), FunctionDescriptor.ofVoid(ValueLayout.ADDRESS));

    private CAllocator() {
    }

    public static CAllocator of() {
        final class Holder {
            private static final CAllocator INSTANCE = new CAllocator();
        }
        return Holder.INSTANCE;
    }

    private static MemorySegment malloc(long size) {
        try {
            switch (SIZE_T) {
                case ValueLayout.OfInt _ -> {
                    return (MemorySegment) MH_malloc.invokeExact(Math.toIntExact(size));
                }
                case ValueLayout.OfLong _ -> {
                    return (MemorySegment) MH_malloc.invokeExact(size);
                }
                default -> throw new AssertionError("size_t: " + SIZE_T);
            }
        } catch (Throwable e) {
            throw new RuntimeException("error in malloc", e);
        }
    }

    private static void free_(MemorySegment segment) {
        try {
            MH_free.invokeExact(segment);
        } catch (Throwable e) {
            throw new RuntimeException("error in free", e);
        }
    }

    private static void setAlignedPtrRaw(long aligned, MemorySegment raw) {
        MemorySegment.ofAddress(aligned + OFFSET_alignedPtrRaw)
            .reinterpret(ADDRESS_SIZE)
            .set(ValueLayout.ADDRESS, 0, raw);
    }

    private static MemorySegment getAlignedPtrRaw(long aligned) {
        return MemorySegment.ofAddress(aligned + OFFSET_alignedPtrRaw)
            .reinterpret(ADDRESS_SIZE)
            .get(ValueLayout.ADDRESS, 0);
    }

    private static void setAlignedPtrSize(long aligned, long size) {
        MemorySegment sizePtr = MemorySegment.ofAddress(aligned + OFFSET_alignedPtrSize)
            .reinterpret(ADDRESS_SIZE);
        switch (SIZE_T) {
            case ValueLayout.OfInt layout -> sizePtr.set(layout, 0, Math.toIntExact(size));
            case ValueLayout.OfLong layout -> sizePtr.set(layout, 0, size);
            default -> throw new AssertionError("size_t: " + SIZE_T);
        }
    }

    private static long getAlignedPtrSize(long aligned) {
        MemorySegment sizePtr = MemorySegment.ofAddress(aligned + OFFSET_alignedPtrSize)
            .reinterpret(ADDRESS_SIZE);
        return switch (SIZE_T) {
            case ValueLayout.OfInt layout -> sizePtr.get(layout, 0);
            case ValueLayout.OfLong layout -> sizePtr.get(layout, 0);
            default -> throw new AssertionError("size_t: " + SIZE_T);
        };
    }

    private static MemorySegment alignedMalloc(long size, long alignment) {
        long totalSize = size + alignment + MAX_MALLOC_ALIGN;
        MemorySegment raw = malloc(totalSize);
        if (MemorySegment.NULL.equals(raw)) {
            throw new OutOfMemoryError();
        }

        long rawAddress = raw.address();
        long start = rawAddress + MAX_MALLOC_ALIGN;
        long aligned = (start + alignment - 1) & -alignment;
        MemorySegment alignedPtr = MemorySegment.ofAddress(aligned).reinterpret(size);

        setAlignedPtrRaw(aligned, raw);
        setAlignedPtrSize(aligned, size);

        return alignedPtr;
    }

    private static MemorySegment alignedRealloc(MemorySegment segment, long newSize, long alignment) {
        if (MemorySegment.NULL.equals(segment)) {
            return alignedMalloc(newSize, alignment);
        }
        if (newSize == 0) {
            alignedFree(segment);
            return MemorySegment.NULL;
        }

        long aligned = segment.address();
        long oldSize = getAlignedPtrSize(aligned);
        if (newSize == oldSize) {
            return segment;
        }
        if (newSize < oldSize) {
            setAlignedPtrSize(aligned, newSize);
            return segment.reinterpret(newSize);
        }

        MemorySegment newAligned = alignedMalloc(newSize, alignment);
        MemorySegment.copy(segment, 0, newAligned, 0, oldSize);
        alignedFree(segment);
        return newAligned;
    }

    private static void alignedFree(MemorySegment segment) {
        if (MemorySegment.NULL.equals(segment)) {
            return;
        }
        MemorySegment raw = getAlignedPtrRaw(segment.address());
        free_(raw);
    }

    @Override
    public MemorySegment allocate(long byteSize, long byteAlignment) {
        Util.checkSizeAndAlignment(byteSize, byteAlignment);
        return alignedMalloc(byteSize, byteAlignment);
    }

    @Override
    public MemorySegment reallocate(MemorySegment segment, long newByteSize, long byteAlignment) {
        Util.checkSizeAndAlignment(newByteSize, byteAlignment);
        return alignedRealloc(segment, newByteSize, byteAlignment);
    }

    @Override
    public void free(MemorySegment segment) {
        alignedFree(segment);
    }
}
