package io.github.overrun.arraybuffer;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.function.Supplier;

/// @since 1.0.0
final class ArenaAllocator implements ArrayBuffer.Allocator {
    private final Supplier<Arena> arenaSupplier;
    private final boolean arenaCloseable;
    private Arena arena;

    ArenaAllocator(Supplier<Arena> arenaSupplier, boolean arenaCloseable) {
        this.arenaSupplier = arenaSupplier;
        this.arenaCloseable = arenaCloseable;
        this.arena = arenaSupplier.get();
    }

    @Override
    public MemorySegment allocate(long byteSize, long byteAlignment) {
        return arena.allocate(byteSize, byteAlignment);
    }

    @Override
    public MemorySegment reallocate(MemorySegment segment, long newByteSize) {
        Arena newArena = arenaSupplier.get();
        MemorySegment newSegment = newArena.allocate(newByteSize, 1);
        MemorySegment.copy(segment, 0, newSegment, 0, segment.byteSize());
        if (arenaCloseable) {
            arena.close();
        }
        arena = newArena;
        return newSegment;
    }

    @Override
    public void free(MemorySegment segment) {
        if (arenaCloseable) {
            arena.close();
        }
    }
}
