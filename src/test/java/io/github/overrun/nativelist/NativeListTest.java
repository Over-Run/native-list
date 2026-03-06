package io.github.overrun.nativelist;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.StructLayout;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.VarHandle;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/// @since 1.0.0
public class NativeListTest {
    static Stream<NativeList.AllocatorFactory> allocatorFactories() {
        return Stream.of(
            NativeList.Allocator::ofConfinedArena,
            NativeList.Allocator::ofSharedArena,
            NativeList.Allocator::ofAutoArena,
            NativeList.Allocator::c
        );
    }

    @Test
    void testConstructorCheck() {
        assertThrowsExactly(IllegalArgumentException.class,
            () -> new IntNativeList(NativeList.Allocator::ofConfinedArena, -1).close());
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testGet(NativeList.AllocatorFactory allocatorFactory) {
        try (var list = new ByteNativeList(allocatorFactory)) {
            list.add((byte) 42);
            list.add((byte) 43);
            assertEquals((byte) 42, list.get(0));
            assertEquals((byte) 43, list.get(1));
        }

        try (var list = new ShortNativeList(allocatorFactory)) {
            list.add((short) 42);
            list.add((short) 43);
            assertEquals((short) 42, list.get(0));
            assertEquals((short) 43, list.get(1));
        }

        try (var list = new IntNativeList(allocatorFactory)) {
            list.add(42);
            list.add(43);
            assertEquals(42, list.get(0));
            assertEquals(43, list.get(1));
        }

        try (var list = new LongNativeList(allocatorFactory)) {
            list.add(42);
            list.add(43);
            assertEquals(42, list.get(0));
            assertEquals(43, list.get(1));
        }

        try (var list = new FloatNativeList(allocatorFactory)) {
            list.add(42);
            list.add(43);
            assertEquals(42, list.get(0));
            assertEquals(43, list.get(1));
        }

        try (var list = new DoubleNativeList(allocatorFactory)) {
            list.add(42);
            list.add(43);
            assertEquals(42, list.get(0));
            assertEquals(43, list.get(1));
        }

        try (var list = new BoolNativeList(allocatorFactory)) {
            list.add(true);
            list.add(false);
            assertTrue(list.get(0));
            assertFalse(list.get(1));
        }

        try (var list = new CharNativeList(allocatorFactory)) {
            list.add('A');
            list.add('Z');
            assertEquals('A', list.get(0));
            assertEquals('Z', list.get(1));
        }

        try (var list = new AddressNativeList(allocatorFactory)) {
            list.add(MemorySegment.ofAddress(42));
            list.add(MemorySegment.ofAddress(43));
            assertEquals(MemorySegment.ofAddress(42), list.get(0));
            assertEquals(MemorySegment.ofAddress(43), list.get(1));
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testAddAtIndex(NativeList.AllocatorFactory allocatorFactory) {
        try (var list = new ByteNativeList(allocatorFactory)) {
            list.add(0, (byte) 42);
            list.add(0, (byte) 43);
            assertEquals((byte) 43, list.get(0));
            assertEquals((byte) 42, list.get(1));
        }

        try (var list = new ShortNativeList(allocatorFactory)) {
            list.add(0, (short) 42);
            list.add(0, (short) 43);
            assertEquals((short) 43, list.get(0));
            assertEquals((short) 42, list.get(1));
        }

        try (var list = new IntNativeList(allocatorFactory)) {
            list.add(0, 42);
            list.add(0, 43);
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
        }

        try (var list = new LongNativeList(allocatorFactory)) {
            list.add(0, 42);
            list.add(0, 43);
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
        }

        try (var list = new FloatNativeList(allocatorFactory)) {
            list.add(0, 42);
            list.add(0, 43);
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
        }

        try (var list = new DoubleNativeList(allocatorFactory)) {
            list.add(0, 42);
            list.add(0, 43);
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
        }

        try (var list = new BoolNativeList(allocatorFactory)) {
            list.add(0, true);
            list.add(0, false);
            assertFalse(list.get(0));
            assertTrue(list.get(1));
        }

        try (var list = new CharNativeList(allocatorFactory)) {
            list.add(0, 'A');
            list.add(0, 'Z');
            assertEquals('Z', list.get(0));
            assertEquals('A', list.get(1));
        }

        try (var list = new AddressNativeList(allocatorFactory)) {
            list.add(0, MemorySegment.ofAddress(42));
            list.add(0, MemorySegment.ofAddress(43));
            assertEquals(MemorySegment.ofAddress(43), list.get(0));
            assertEquals(MemorySegment.ofAddress(42), list.get(1));
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testAddAll(NativeList.AllocatorFactory allocatorFactory) {
        try (var list = new ByteNativeList(allocatorFactory)) {
            list.addAll(new byte[0]);
            list.addAll(new byte[]{43, 42});
            assertEquals((byte) 43, list.get(0));
            assertEquals((byte) 42, list.get(1));
        }

        try (var list = new ShortNativeList(allocatorFactory)) {
            list.addAll(new short[0]);
            list.addAll(new short[]{43, 42});
            assertEquals((short) 43, list.get(0));
            assertEquals((short) 42, list.get(1));
        }

        try (var list = new IntNativeList(allocatorFactory)) {
            list.addAll(new int[0]);
            list.addAll(new int[]{43, 42});
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
        }

        try (var list = new LongNativeList(allocatorFactory)) {
            list.addAll(new long[0]);
            list.addAll(new long[]{43, 42});
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
        }

        try (var list = new FloatNativeList(allocatorFactory)) {
            list.addAll(new float[0]);
            list.addAll(new float[]{43, 42});
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
        }

        try (var list = new DoubleNativeList(allocatorFactory)) {
            list.addAll(new double[0]);
            list.addAll(new double[]{43, 42});
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
        }

        try (var list = new BoolNativeList(allocatorFactory)) {
            list.addAll(new boolean[0]);
            list.addAll(new boolean[]{true, false});
            assertTrue(list.get(0));
            assertFalse(list.get(1));
        }

        try (var list = new CharNativeList(allocatorFactory)) {
            list.addAll(new char[0]);
            list.addAll(new char[]{'A', 'Z'});
            assertEquals('A', list.get(0));
            assertEquals('Z', list.get(1));
        }

        try (var list = new AddressNativeList(allocatorFactory)) {
            list.addAll(new MemorySegment[0]);
            list.addAll(new MemorySegment[]{MemorySegment.ofAddress(43), MemorySegment.ofAddress(42)});
            assertEquals(MemorySegment.ofAddress(43), list.get(0));
            assertEquals(MemorySegment.ofAddress(42), list.get(1));
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testAddAllAtIndex(NativeList.AllocatorFactory allocatorFactory) {
        try (var list = new ByteNativeList(allocatorFactory)) {
            list.add((byte) 44);
            list.addAll(0, new byte[0]);
            list.addAll(0, new byte[]{43, 42});
            list.addAll(3, new byte[]{45, 46});
            assertEquals((byte) 43, list.get(0));
            assertEquals((byte) 42, list.get(1));
            assertEquals((byte) 44, list.get(2));
            assertEquals((byte) 45, list.get(3));
            assertEquals((byte) 46, list.get(4));
        }

        try (var list = new ShortNativeList(allocatorFactory)) {
            list.add((short) 44);
            list.addAll(0, new short[0]);
            list.addAll(0, new short[]{43, 42});
            list.addAll(3, new short[]{45, 46});
            assertEquals((short) 43, list.get(0));
            assertEquals((short) 42, list.get(1));
            assertEquals((short) 44, list.get(2));
            assertEquals((short) 45, list.get(3));
            assertEquals((short) 46, list.get(4));
        }

        try (var list = new IntNativeList(allocatorFactory)) {
            list.add(44);
            list.addAll(0, new int[0]);
            list.addAll(0, new int[]{43, 42});
            list.addAll(3, new int[]{45, 46});
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
            assertEquals(44, list.get(2));
            assertEquals(45, list.get(3));
            assertEquals(46, list.get(4));
        }

        try (var list = new LongNativeList(allocatorFactory)) {
            list.add(44);
            list.addAll(0, new long[0]);
            list.addAll(0, new long[]{43, 42});
            list.addAll(3, new long[]{45, 46});
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
            assertEquals(44, list.get(2));
            assertEquals(45, list.get(3));
            assertEquals(46, list.get(4));
        }

        try (var list = new FloatNativeList(allocatorFactory)) {
            list.add(44);
            list.addAll(0, new float[0]);
            list.addAll(0, new float[]{43, 42});
            list.addAll(3, new float[]{45, 46});
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
            assertEquals(44, list.get(2));
            assertEquals(45, list.get(3));
            assertEquals(46, list.get(4));
        }

        try (var list = new DoubleNativeList(allocatorFactory)) {
            list.add(44);
            list.addAll(0, new double[0]);
            list.addAll(0, new double[]{43, 42});
            list.addAll(3, new double[]{45, 46});
            assertEquals(43, list.get(0));
            assertEquals(42, list.get(1));
            assertEquals(44, list.get(2));
            assertEquals(45, list.get(3));
            assertEquals(46, list.get(4));
        }

        try (var list = new CharNativeList(allocatorFactory)) {
            list.add('A');
            list.addAll(0, new char[0]);
            list.addAll(0, new char[]{'B', 'C'});
            list.addAll(3, new char[]{'D', 'E'});
            assertEquals('B', list.get(0));
            assertEquals('C', list.get(1));
            assertEquals('A', list.get(2));
            assertEquals('D', list.get(3));
            assertEquals('E', list.get(4));
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testExpansion(NativeList.AllocatorFactory allocatorFactory) {
        try (var list = new ByteNativeList(allocatorFactory, 1)) {
            list.add((byte) 42);
            list.add((byte) 43);
            assertEquals(2, list.size());
            assertEquals(2, list.capacity());
            assertEquals((byte) 42, list.get(0));
            assertEquals((byte) 43, list.get(1));
        }

        try (var list = new ShortNativeList(allocatorFactory, 1)) {
            list.add((short) 42);
            list.add((short) 43);
            assertEquals(2, list.size());
            assertEquals(2, list.capacity());
            assertEquals((short) 42, list.get(0));
            assertEquals((short) 43, list.get(1));
        }

        try (var list = new IntNativeList(allocatorFactory, 1)) {
            list.add(42);
            list.add(43);
            assertEquals(2, list.size());
            assertEquals(2, list.capacity());
            assertEquals(42, list.get(0));
            assertEquals(43, list.get(1));
        }

        try (var list = new LongNativeList(allocatorFactory, 1)) {
            list.add(42);
            list.add(43);
            assertEquals(2, list.size());
            assertEquals(2, list.capacity());
            assertEquals(42, list.get(0));
            assertEquals(43, list.get(1));
        }

        try (var list = new FloatNativeList(allocatorFactory, 1)) {
            list.add(42);
            list.add(43);
            assertEquals(2, list.size());
            assertEquals(2, list.capacity());
            assertEquals(42, list.get(0));
            assertEquals(43, list.get(1));
        }

        try (var list = new DoubleNativeList(allocatorFactory, 1)) {
            list.add(42);
            list.add(43);
            assertEquals(2, list.size());
            assertEquals(2, list.capacity());
            assertEquals(42, list.get(0));
            assertEquals(43, list.get(1));
        }

        try (var list = new BoolNativeList(allocatorFactory, 1)) {
            list.add(true);
            list.add(false);
            assertEquals(2, list.size());
            assertEquals(2, list.capacity());
            assertTrue(list.get(0));
            assertFalse(list.get(1));
        }

        try (var list = new CharNativeList(allocatorFactory, 1)) {
            list.add('A');
            list.add('Z');
            assertEquals(2, list.size());
            assertEquals(2, list.capacity());
            assertEquals('A', list.get(0));
            assertEquals('Z', list.get(1));
        }

        try (var list = new AddressNativeList(allocatorFactory, 1)) {
            list.add(MemorySegment.ofAddress(42));
            list.add(MemorySegment.ofAddress(43));
            assertEquals(2, list.size());
            assertEquals(2, list.capacity());
            assertEquals(MemorySegment.ofAddress(42), list.get(0));
            assertEquals(MemorySegment.ofAddress(43), list.get(1));
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testSizeAndCapacity(NativeList.AllocatorFactory allocatorFactory) {
        try (var list = new IntNativeList(allocatorFactory, 8)) {
            assertTrue(list.isEmpty());
            assertEquals(8, list.capacity());
            list.add(42);
            list.add(43);
            assertEquals(2, list.size());
            assertEquals(8, list.capacity());
        }

        try (var list = new IntNativeList(allocatorFactory)) {
            list.add(42);
            assertEquals(42, list.get(0));
            list.clear();
            assertTrue(list.isEmpty());
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testZeroCapacity(NativeList.AllocatorFactory allocatorFactory) {
        try (var list = new IntNativeList(allocatorFactory, 0)) {
            assertEquals(0, list.capacity());
            assertEquals(MemorySegment.NULL, list.data());
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testCopyConstructor(NativeList.AllocatorFactory allocatorFactory) {
        try (var list0 = new ByteNativeList(allocatorFactory)) {
            list0.add((byte) 42);
            list0.add((byte) 43);
            try (var list1 = new ByteNativeList(allocatorFactory, list0)) {
                assertEquals((byte) 42, list1.get(0));
                assertEquals((byte) 43, list1.get(1));
            }
        }

        try (var list0 = new ShortNativeList(allocatorFactory)) {
            list0.add((short) 42);
            list0.add((short) 43);
            try (var list1 = new ShortNativeList(allocatorFactory, list0)) {
                assertEquals((short) 42, list1.get(0));
                assertEquals((short) 43, list1.get(1));
            }
        }

        try (var list0 = new IntNativeList(allocatorFactory)) {
            list0.add(42);
            list0.add(43);
            try (var list1 = new IntNativeList(allocatorFactory, list0)) {
                assertEquals(42, list1.get(0));
                assertEquals(43, list1.get(1));
            }
        }

        try (var list0 = new LongNativeList(allocatorFactory)) {
            list0.add(42);
            list0.add(43);
            try (var list1 = new LongNativeList(allocatorFactory, list0)) {
                assertEquals(42, list1.get(0));
                assertEquals(43, list1.get(1));
            }
        }

        try (var list0 = new FloatNativeList(allocatorFactory)) {
            list0.add(42);
            list0.add(43);
            try (var list1 = new FloatNativeList(allocatorFactory, list0)) {
                assertEquals(42, list1.get(0));
                assertEquals(43, list1.get(1));
            }
        }

        try (var list0 = new DoubleNativeList(allocatorFactory)) {
            list0.add(42);
            list0.add(43);
            try (var list1 = new DoubleNativeList(allocatorFactory, list0)) {
                assertEquals(42, list1.get(0));
                assertEquals(43, list1.get(1));
            }
        }

        try (var list0 = new BoolNativeList(allocatorFactory)) {
            list0.add(true);
            list0.add(false);
            try (var list1 = new BoolNativeList(allocatorFactory, list0)) {
                assertTrue(list1.get(0));
                assertFalse(list1.get(1));
            }
        }

        try (var list0 = new CharNativeList(allocatorFactory)) {
            list0.add('A');
            list0.add('Z');
            try (var list1 = new CharNativeList(allocatorFactory, list0)) {
                assertEquals('A', list1.get(0));
                assertEquals('Z', list1.get(1));
            }
        }

        try (var list0 = new AddressNativeList(allocatorFactory)) {
            list0.add(MemorySegment.ofAddress(42));
            list0.add(MemorySegment.ofAddress(43));
            try (var list1 = new AddressNativeList(allocatorFactory, list0)) {
                assertEquals(MemorySegment.ofAddress(42), list1.get(0));
                assertEquals(MemorySegment.ofAddress(43), list1.get(1));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testToArray(NativeList.AllocatorFactory allocatorFactory) {
        try (var list = new ByteNativeList(allocatorFactory)) {
            list.add((byte) 42);
            list.add((byte) 43);
            assertArrayEquals(new byte[]{42, 43}, list.toArray());
        }

        try (var list = new ShortNativeList(allocatorFactory)) {
            list.add((short) 42);
            list.add((short) 43);
            assertArrayEquals(new short[]{42, 43}, list.toArray());
        }

        try (var list = new IntNativeList(allocatorFactory)) {
            list.add(42);
            list.add(43);
            assertArrayEquals(new int[]{42, 43}, list.toArray());
        }

        try (var list = new LongNativeList(allocatorFactory)) {
            list.add(42);
            list.add(43);
            assertArrayEquals(new long[]{42, 43}, list.toArray());
        }

        try (var list = new FloatNativeList(allocatorFactory)) {
            list.add(42);
            list.add(43);
            assertArrayEquals(new float[]{42, 43}, list.toArray());
        }

        try (var list = new DoubleNativeList(allocatorFactory)) {
            list.add(42);
            list.add(43);
            assertArrayEquals(new double[]{42, 43}, list.toArray());
        }

        try (var list = new CharNativeList(allocatorFactory)) {
            list.add('A');
            list.add('Z');
            assertArrayEquals(new char[]{'A', 'Z'}, list.toArray());
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testElementLayout(NativeList.AllocatorFactory allocatorFactory) {
        try (var list = new ByteNativeList(allocatorFactory, 0)) {
            assertEquals(ValueLayout.JAVA_BYTE, list.elementLayout());
        }
        try (var list = new ShortNativeList(allocatorFactory, 0)) {
            assertEquals(ValueLayout.JAVA_SHORT, list.elementLayout());
        }
        try (var list = new IntNativeList(allocatorFactory, 0)) {
            assertEquals(ValueLayout.JAVA_INT, list.elementLayout());
        }
        try (var list = new LongNativeList(allocatorFactory, 0)) {
            assertEquals(ValueLayout.JAVA_LONG, list.elementLayout());
        }
        try (var list = new FloatNativeList(allocatorFactory, 0)) {
            assertEquals(ValueLayout.JAVA_FLOAT, list.elementLayout());
        }
        try (var list = new DoubleNativeList(allocatorFactory, 0)) {
            assertEquals(ValueLayout.JAVA_DOUBLE, list.elementLayout());
        }
        try (var list = new BoolNativeList(allocatorFactory, 0)) {
            assertEquals(ValueLayout.JAVA_BOOLEAN, list.elementLayout());
        }
        try (var list = new CharNativeList(allocatorFactory, 0)) {
            assertEquals(ValueLayout.JAVA_CHAR, list.elementLayout());
        }
        try (var list = new AddressNativeList(allocatorFactory, 0)) {
            assertEquals(ValueLayout.ADDRESS, list.elementLayout());
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testRemove(NativeList.AllocatorFactory allocatorFactory) {
        try (var list = new IntNativeList(allocatorFactory)) {
            for (int i = 1; i <= 10; i++) {
                list.add(i);
            }

            list.remove(1);
            assertEquals(9, list.size());
            assertEquals(1, list.get(0));
            assertEquals(3, list.get(1));

            list.remove(0);
            assertEquals(8, list.size());
            assertEquals(3, list.get(0));
            assertEquals(4, list.get(1));

            list.remove(7);
            assertEquals(7, list.size());
            assertEquals(8, list.get(5));
            assertEquals(9, list.get(6));
        }

        try (var list = new IntNativeList(allocatorFactory, 1)) {
            list.add(1);
            list.remove(0);
            assertTrue(list.isEmpty());
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testRemoveException(NativeList.AllocatorFactory allocatorFactory) {
        try (var list = new IntNativeList(allocatorFactory, 0)) {
            assertThrowsExactly(NoSuchElementException.class, list::removeFirst);
            assertThrowsExactly(NoSuchElementException.class, list::removeLast);
        }
    }

    // non-specialized version

    static class Point {
        public static final StructLayout LAYOUT = MemoryLayout.structLayout(
            ValueLayout.JAVA_INT.withName("x"),
            ValueLayout.JAVA_INT.withName("y")
        );
        private static final VarHandle VH_x = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("x"));
        private static final VarHandle VH_y = LAYOUT.varHandle(MemoryLayout.PathElement.groupElement("y"));
        private final MemorySegment segment;

        Point(MemorySegment segment) {
            this.segment = segment;
        }

        public int x() {
            return (int) VH_x.get(segment, 0L);
        }

        public int y() {
            return (int) VH_y.get(segment, 0L);
        }

        public Point x(int value) {
            VH_x.set(segment, 0L, value);
            return this;
        }

        public Point y(int value) {
            VH_y.set(segment, 0L, value);
            return this;
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testElementRef(NativeList.AllocatorFactory allocatorFactory) {
        try (NativeList list = new NativeList(Point.LAYOUT, allocatorFactory)) {
            list.add(0, segment -> new Point(segment).x(1).y(2));
            list.add(0, segment -> new Point(segment).x(3).y(4));
            list.addAll(2, 2, segment -> {
                new Point(segment).x(5).y(6);
                new Point(segment.asSlice(Point.LAYOUT.byteSize())).x(7).y(8);
            });
            list.addAll(2, 2, segment -> {
                new Point(segment).x(9).y(10);
                new Point(segment.asSlice(Point.LAYOUT.byteSize())).x(11).y(12);
            });

            assertEquals(6, list.size());
            Point[] points = new Point[6];
            for (int i = 0; i < 6; i++) {
                points[i] = new Point(list.getConstElementRef(i));
            }
            assertPointEquals(3, 4, points[0]);
            assertPointEquals(1, 2, points[1]);
            assertPointEquals(9, 10, points[2]);
            assertPointEquals(11, 12, points[3]);
            assertPointEquals(5, 6, points[4]);
            assertPointEquals(7, 8, points[5]);
        }
    }

    @ParameterizedTest
    @MethodSource("allocatorFactories")
    void testMoveMethod(NativeList.AllocatorFactory allocatorFactory) {
        NativeList list1 = new NativeList(Point.LAYOUT, allocatorFactory);
        list1.add(segment -> new Point(segment).x(1).y(2));
        list1.add(segment -> new Point(segment).x(3).y(4));
        try (NativeList list2 = NativeList.move(allocatorFactory, list1)) {
            assertPointEquals(1, 2, new Point(list2.getElementRef(0)));
            assertPointEquals(3, 4, new Point(list2.getElementRef(1)));
        }
        assertEquals(MemorySegment.NULL, list1.data);
    }

    static void assertPointEquals(int expectedX, int expectedY, Point point) {
        assertEquals(expectedX, point.x());
        assertEquals(expectedY, point.y());
    }
}
