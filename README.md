# Native List

Native list is a resizable array backed by `MemorySegment`.

## Usage

```java
void main() {
    try (var list = new IntNativeList(ListAllocator::ofConfinedArena)) {
        list.add(42);
        list.add(43);
        assertEquals(42, list.get(0));
        assertEquals(43, list.get(1));
    } // the list is automatically released
}
```

Check [wiki](https://github.com/Over-Run/native-list/wiki) for details.

## Import as Dependency

This library requires JDK 25.

- Maven coordinate: `io.github.over-run:native-list`
- Version: ![Maven Central Version](https://img.shields.io/maven-central/v/io.github.over-run/native-list)

Maven:

```xml
<dependency>
    <groupId>io.github.over-run</groupId>
    <artifactId>native-list</artifactId>
    <version>${nativeListVersion}</version>
</dependency>
```

Gradle:

```kotlin
dependencies {
    implementation("io.github.over-run:native-list:${nativeListVersion}")
}
```
