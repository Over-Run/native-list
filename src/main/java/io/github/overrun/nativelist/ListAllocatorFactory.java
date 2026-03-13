package io.github.overrun.nativelist;

/// Represents a factory of the [allocator][ListAllocator].
///
/// @since 1.0.0
@FunctionalInterface
public interface ListAllocatorFactory {
    /// {@return a newly created allocator}
    ListAllocator create();
}
