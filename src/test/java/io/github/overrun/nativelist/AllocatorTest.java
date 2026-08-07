package io.github.overrun.nativelist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.foreign.MemorySegment;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/// @since 1.0.0
public class AllocatorTest {
    static Stream<ListAllocator> allocators() {
        return NativeListTest.allocators();
    }

    @Test
    void testCAllocatorSizeAndAlignCheck() {
        assertThrowsExactly(IllegalArgumentException.class,
            () -> ListAllocator.c().allocate(-1, -1));
        assertThrowsExactly(IllegalArgumentException.class,
            () -> ListAllocator.c().allocate(0, -1));
        assertThrowsExactly(IllegalArgumentException.class,
            () -> ListAllocator.c().allocate(0, 3));
    }

    @ParameterizedTest
    @MethodSource("allocators")
    void testCAllocatorRealloc(ListAllocator allocator) {
        assertDoesNotThrow(() -> {
            MemorySegment segment = allocator.reallocate(MemorySegment.NULL, 1, 1);
            allocator.free(segment);

            segment = allocator.allocate(1, 1);
            allocator.reallocate(segment, 0, 1);
        });
    }
}
