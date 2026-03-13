package io.github.overrun.nativelist;

import org.junit.jupiter.api.Test;

import java.lang.foreign.MemorySegment;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/// @since 1.0.0
public class AllocatorTest {
    @Test
    void testCAllocatorSizeAndAlignCheck() {
        assertThrowsExactly(IllegalArgumentException.class,
            () -> ListAllocator.c().allocate(-1, -1));
        assertThrowsExactly(IllegalArgumentException.class,
            () -> ListAllocator.c().allocate(0, -1));
        assertThrowsExactly(IllegalArgumentException.class,
            () -> ListAllocator.c().allocate(0, 3));
    }

    @Test
    void testCAllocatorRealloc() {
        assertDoesNotThrow(() -> {
            ListAllocator c = ListAllocator.c();

            MemorySegment segment = c.reallocate(MemorySegment.NULL, 1, 1);
            c.free(segment);

            segment = c.allocate(1, 1);
            c.reallocate(segment, 0, 1);
        });

        ListAllocator c = ListAllocator.c();
        MemorySegment segment1 = c.allocate(1, 1);
        MemorySegment segment2 = c.reallocate(segment1, 1, 1);
        try {
            assertEquals(segment1, segment2);
        } finally {
            c.free(segment2);
            if (!Objects.equals(segment1, segment2)) {
                c.free(segment1);
            }
        }

        MemorySegment segment3 = c.allocate(2, 1);
        MemorySegment segment4 = c.reallocate(segment3, 1, 1);
        try {
            assertEquals(segment3.address(), segment4.address());
            assertEquals(1, segment4.byteSize());
        } finally {
            c.free(segment4);
            if (!Objects.equals(segment3.address(), segment4.address())) {
                c.free(segment3);
            }
        }
    }

    @Test
    void testArenaAllocatorRealloc() {
        ListAllocator allocator = ListAllocator.ofAutoArena();

        // nullptr, > 0
        MemorySegment segment = allocator.reallocate(MemorySegment.NULL, 1, 1);
        try {
            assertNotEquals(MemorySegment.NULL.address(), segment.address());
        } finally {
            allocator.free(segment);
        }

        // non-null, > 0
        segment = allocator.allocate(1, 1);
        segment = allocator.reallocate(segment, 2, 1);
        try {
            assertEquals(2, segment.byteSize());
        } finally {
            allocator.free(segment);
        }

        // oldSize == newSize
        MemorySegment segment1 = allocator.allocate(1, 1);
        MemorySegment segment2 = allocator.reallocate(segment1, 1, 1);
        try {
            assertEquals(segment1, segment2);
        } finally {
            allocator.free(segment2);
            if (!Objects.equals(segment1, segment2)) {
                allocator.free(segment1);
            }
        }

        // non-null, 0
        segment = allocator.allocate(1, 1);
        segment = allocator.reallocate(segment, 0, 1);
        assertEquals(MemorySegment.NULL, segment);
    }

    @Test
    void testArenaAllocatorFree() {
        assertDoesNotThrow(() -> ListAllocator.ofAutoArena().free(MemorySegment.NULL));
        assertThrowsExactly(IllegalArgumentException.class,
            () -> ListAllocator.ofAutoArena().free(MemorySegment.ofAddress(1)));
    }
}
