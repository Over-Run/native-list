package io.github.overrun.nativelist;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Objects;

/// @since 1.0.0
final class ArenaAllocator implements NativeList.Allocator {
    private final Arena arena;
    private final boolean arenaCloseable;

    private ArenaAllocator(Arena arena, boolean arenaCloseable) {
        this.arena = arena;
        this.arenaCloseable = arenaCloseable;
    }

    static ArenaAllocator ofConfined() {
        return new ArenaAllocator(Arena.ofConfined(), true);
    }

    static ArenaAllocator ofShared() {
        return new ArenaAllocator(Arena.ofShared(), true);
    }

    static ArenaAllocator ofAuto() {
        return new ArenaAllocator(Arena.ofAuto(), false);
    }

    @Override
    public MemorySegment allocate(long byteSize, long byteAlignment) {
        return arena.allocate(byteSize, byteAlignment);
    }

    @Override
    public MemorySegment allocateFrom(MemorySegment segment, long byteAlignment) {
        long byteSize = segment.byteSize();
        MemorySegment newSegment = allocate(byteSize, byteAlignment);
        MemorySegment.copy(segment, 0, newSegment, 0, byteSize);
        return newSegment;
    }

    @Override
    public MemorySegment reallocate(MemorySegment segment, long newByteSize, long byteAlignment) {
        if (MemorySegment.NULL.equals(segment)) {
            return allocate(newByteSize, byteAlignment);
        }
        if (newByteSize == 0) {
            return MemorySegment.NULL;
        }
        long oldByteSize = segment.byteSize();
        if (newByteSize == oldByteSize) {
            return segment;
        }
        MemorySegment newSegment = allocate(newByteSize, byteAlignment);
        MemorySegment.copy(segment, 0, newSegment, 0, Math.min(oldByteSize, newByteSize));
        return newSegment;
    }

    @Override
    public void free(MemorySegment segment) {
        if (MemorySegment.NULL.equals(segment)) {
            return;
        }
        if (!Objects.equals(arena.scope(), segment.scope())) {
            throw new IllegalArgumentException("The specified memory segment " + segment + " was not allocated by this allocator");
        }
        if (arenaCloseable) {
            arena.close();
        }
    }
}
