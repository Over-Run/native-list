package io.github.overrun.nativelist;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;

/// Provides methods for [NativeList] to allocate memory.
///
/// @since 1.0.0
public interface ListAllocator {
    /// Allocates a block of memory with the given size and alignment constraint.
    ///
    /// The implementation **MUST NOT** return [MemorySegment#NULL].
    /// They should instead raise [OutOfMemoryError].
    ///
    /// @param byteSize      the size of the memory to be allocated in bytes
    /// @param byteAlignment the alignment constraint in bytes
    /// @return a new native memory segment
    /// @throws IllegalArgumentException if `bytesSize < 0`, `byteAlignment <= 0`, or if `byteAlignment` is not a power of 2
    /// @throws OutOfMemoryError         if error occurs when allocating memory
    MemorySegment allocate(long byteSize, long byteAlignment);

    /// Allocates a block of memory with the contents of the given memory segment and the alignment constraint.
    ///
    /// The implementation **MUST NOT** return [MemorySegment#NULL].
    /// They should instead raise [OutOfMemoryError].
    ///
    /// @param segment       the memory segment to be copied
    /// @param byteAlignment the alignment constraint in bytes
    /// @return a new native memory segment
    /// @throws IllegalArgumentException if `byteAlignment <= 0`, or if `byteAlignment` is not a power of 2
    /// @throws OutOfMemoryError         if error occurs when allocating memory
    default MemorySegment allocateFrom(MemorySegment segment, long byteAlignment) {
        MemorySegment newSegment = allocate(segment.byteSize(), byteAlignment);
        MemorySegment.copy(segment, 0, newSegment, 0, segment.byteSize());
        return newSegment;
    }

    /// Reallocates the memory segment with a new size.
    ///
    /// The implementation **can only** return [MemorySegment#NULL] when `newByteSize == 0`.
    /// Otherwise, they should instead raise [OutOfMemoryError].
    ///
    /// @param segment       the memory segment to be reallocated, which **may** be [MemorySegment#NULL]
    /// @param newByteSize   the new size in bytes, which **may** be smaller than the previous one
    /// @param byteAlignment the alignment constraint in bytes
    /// @return a new native memory segment
    /// @throws IllegalArgumentException if `bytesSize < 0`, `byteAlignment <= 0`, or if `byteAlignment` is not a power of 2
    /// @throws OutOfMemoryError         if error occurs when reallocating memory
    MemorySegment reallocate(MemorySegment segment, long newByteSize, long byteAlignment);

    /// Releases the given memory segment or the arena associated to this allocator.
    ///
    /// This method does no operation if `segment` is [MemorySegment#NULL].
    ///
    /// @param segment the memory segment to be released, which **may** be [MemorySegment#NULL]
    void free(MemorySegment segment);

    /// Creates an allocator with the [confined arena][Arena#ofConfined()].
    ///
    /// @return the allocator
    static ListAllocator ofConfinedArena() {
        return ArenaAllocator.ofConfined();
    }

    /// Creates an allocator with the [shared arena][Arena#ofShared()].
    ///
    /// @return the allocator
    static ListAllocator ofSharedArena() {
        return ArenaAllocator.ofShared();
    }

    /// Creates an allocator with the [auto arena][Arena#ofAuto()].
    ///
    /// @return the allocator
    static ListAllocator ofAutoArena() {
        return ArenaAllocator.ofAuto();
    }

    /// Creates an allocator with C `malloc`, `realloc` and `free`.
    ///
    /// @return the allocator
    static ListAllocator c() {
        return CAllocator.of();
    }

    /// Checks whether `byteAlignment > 0` and `byteAlignment` is a power of 2.
    ///
    /// @param byteAlignment the alignment constraint in bytes
    /// @throws IllegalArgumentException if `byteAlignment <= 0`, or if `byteAlignment` is not a power of 2
    /// @since 1.1.0
    static void checkAlignment(long byteAlignment) {
        if (byteAlignment <= 0 || (byteAlignment & (byteAlignment - 1)) != 0) {
            throw new IllegalArgumentException("Invalid alignment constraint: " + byteAlignment);
        }
    }

    /// Checks whether `bytesSize >= 0`, `byteAlignment > 0` and `byteAlignment` is a power of 2.
    ///
    /// @param byteSize      the size of the memory to be allocated in bytes
    /// @param byteAlignment the alignment constraint in bytes
    /// @throws IllegalArgumentException if `bytesSize < 0`, `byteAlignment <= 0`, or if `byteAlignment` is not a power of 2
    /// @since 1.1.0
    static void checkSizeAndAlignment(long byteSize, long byteAlignment) {
        if (byteSize < 0) {
            throw new IllegalArgumentException("The byte size is negative: " + byteSize);
        }
        checkAlignment(byteAlignment);
    }
}
