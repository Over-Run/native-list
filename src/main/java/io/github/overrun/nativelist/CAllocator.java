package io.github.overrun.nativelist;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

/// @since 1.0.0
final class CAllocator implements ListAllocator {
    private static final Linker LINKER = Linker.nativeLinker();
    private static final SymbolLookup SYMBOL_LOOKUP = LINKER.defaultLookup();
    private static final MemoryLayout SIZE_T = LINKER.canonicalLayouts().get("size_t");
    private static final MethodHandle MH_malloc = LINKER.downcallHandle(SYMBOL_LOOKUP.findOrThrow("malloc"), FunctionDescriptor.of(ValueLayout.ADDRESS, SIZE_T));
    private static final MethodHandle MH_realloc = LINKER.downcallHandle(SYMBOL_LOOKUP.findOrThrow("realloc"), FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS, SIZE_T));
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

    private static MemorySegment realloc(MemorySegment ptr, long size) {
        try {
            switch (SIZE_T) {
                case ValueLayout.OfInt _ -> {
                    return (MemorySegment) MH_realloc.invokeExact(ptr, Math.toIntExact(size));
                }
                case ValueLayout.OfLong _ -> {
                    return (MemorySegment) MH_realloc.invokeExact(ptr, size);
                }
                default -> throw new AssertionError("size_t: " + SIZE_T);
            }
        } catch (Throwable e) {
            throw new RuntimeException("error in realloc", e);
        }
    }

    private static void free_(MemorySegment segment) {
        try {
            MH_free.invokeExact(segment);
        } catch (Throwable e) {
            throw new RuntimeException("error in free", e);
        }
    }

    @Override
    public MemorySegment allocate(long byteSize, long byteAlignment) {
        ListAllocator.checkSizeAndAlignment(byteSize, byteAlignment);
        return malloc(byteSize).reinterpret(byteSize);
    }

    @Override
    public MemorySegment reallocate(MemorySegment segment, long newByteSize, long byteAlignment) {
        ListAllocator.checkSizeAndAlignment(newByteSize, byteAlignment);
        return realloc(segment, newByteSize).reinterpret(newByteSize);
    }

    @Override
    public void free(MemorySegment segment) {
        free_(segment);
    }
}
