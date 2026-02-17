package io.github.overrun.nativelist;

/// @since 1.0.0
final class Util {
    private Util() {
    }

    public static void checkAlignment(long byteAlignment) {
        if (byteAlignment <= 0 || (byteAlignment & (byteAlignment - 1)) != 0) {
            throw new IllegalArgumentException("Invalid alignment constraint: " + byteAlignment);
        }
    }

    public static void checkSizeAndAlignment(long byteSize, long byteAlignment) {
        if (byteSize < 0) {
            throw new IllegalArgumentException("The byte size is negative: " + byteSize);
        }
        checkAlignment(byteAlignment);
    }
}
