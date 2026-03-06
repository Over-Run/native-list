enum Type {
    BOOL("Bool", "ValueLayout.OfBoolean", "ValueLayout.JAVA_BOOLEAN", "boolean", false),
    BYTE("Byte", "ValueLayout.OfByte", "ValueLayout.JAVA_BYTE", "byte", true),
    CHAR("Char", "ValueLayout.OfChar", "ValueLayout.JAVA_CHAR", "char", true),
    SHORT("Short", "ValueLayout.OfShort", "ValueLayout.JAVA_SHORT", "short", true),
    INT("Int", "ValueLayout.OfInt", "ValueLayout.JAVA_INT", "int", true),
    LONG("Long", "ValueLayout.OfLong", "ValueLayout.JAVA_LONG", "long", true),
    FLOAT("Float", "ValueLayout.OfFloat", "ValueLayout.JAVA_FLOAT", "float", true),
    DOUBLE("Double", "ValueLayout.OfDouble", "ValueLayout.JAVA_DOUBLE", "double", true),
    ADDRESS("Address", "AddressLayout", "ValueLayout.ADDRESS", "MemorySegment", false),
    ;

    final String prefix;
    final String valueLayoutClass;
    final String valueLayout;
    final String typeName;
    final boolean hasArray;

    Type(String prefix, String valueLayoutClass, String valueLayout, String typeName, boolean hasArray) {
        this.prefix = prefix;
        this.valueLayoutClass = valueLayoutClass;
        this.valueLayout = valueLayout;
        this.typeName = typeName;
        this.hasArray = hasArray;
    }
}

static final class SB {
    final StringBuilder sb = new StringBuilder();

    SB append(Object o) {
        sb.append(o);
        return this;
    }

    void appendLine(Object o) {
        sb.append(o).append('\n');
    }

    void appendLine() {
        sb.append('\n');
    }
}

void generateViewClass(Path packageDir, Type type) throws IOException {
    SB sb = new SB();
    sb.appendLine("// This file is auto-generated. DO NOT EDIT!");
    sb.appendLine("package io.github.overrun.nativelist;");
    sb.appendLine("import java.lang.foreign.*;");
    sb.appendLine("import java.util.*;");
    sb.append("/// A readonly view of [").append(type.prefix).appendLine("NativeList].");
    sb.append("public interface ").append(type.prefix).appendLine("NativeListView extends NativeListView {");

    sb.appendLine("    /// {@return the element at the given index}");
    sb.appendLine("    /// @param index the index of the element");
    sb.append("    ").append(type.typeName).appendLine(" get(long index);");
    sb.appendLine();

    if (type.hasArray) {
        sb.appendLine("    /// {@return a new array containing the data in Java form}");
        sb.append("    default ").append(type.typeName).append("[] toArray() { return constData().toArray(").append(type.valueLayout).appendLine("); }");
        sb.appendLine();
    }

    sb.append("    @Override default ").append(type.valueLayoutClass).append(" elementLayout() { return ").append(type.valueLayout).appendLine("; }");

    sb.appendLine("}");

    Files.writeString(packageDir.resolve(type.prefix + "NativeListView.java"), sb.sb);
}

void generateClass(Path packageDir, Type type) throws IOException {
    SB sb = new SB();
    sb.appendLine("// This file is auto-generated. DO NOT EDIT!");
    sb.appendLine("package io.github.overrun.nativelist;");
    sb.appendLine("import java.lang.foreign.*;");
    sb.appendLine("import java.util.*;");
    sb.append("/// The `").append(type.typeName).appendLine("` specialized version of [NativeList].");
    sb.append("public class ").append(type.prefix).append("NativeList extends NativeList implements ").append(type.prefix).appendLine("NativeListView {");

    sb.append("    /// Constructor of [").append(type.prefix).appendLine("NativeList].");
    sb.appendLine("    /// @param allocatorFactory a factory of the [allocator][Allocator]");
    sb.appendLine("    /// @param initialCapacity  the initial capacity of the native list; defaults to 8");
    sb.appendLine("    /// @throws IllegalArgumentException if `initialCapacity < 0`");
    sb.append("    public ").append(type.prefix).appendLine("NativeList(AllocatorFactory allocatorFactory, long initialCapacity) {");
    sb.append("        super(").append(type.valueLayout).appendLine(", allocatorFactory, initialCapacity);");
    sb.append("    }");
    sb.appendLine();

    sb.append("    /// Constructor of [").append(type.prefix).appendLine("NativeList].");
    sb.appendLine("    ///");
    sb.appendLine("    /// It is recommended to construct a native list with an initial capacity.");
    sb.appendLine("    /// @param allocatorFactory a factory of the [allocator][Allocator]");
    sb.append("    public ").append(type.prefix).appendLine("NativeList(AllocatorFactory allocatorFactory) {");
    sb.append("        super(").append(type.valueLayout).appendLine(", allocatorFactory);");
    sb.appendLine("    }");
    sb.appendLine();

    sb.append("    /// Constructor of [").append(type.prefix).appendLine("NativeList].");
    sb.appendLine("    ///");
    sb.appendLine("    /// This copies element layout and data from `list`.");
    sb.appendLine("    /// @param allocatorFactory a factory of the [allocator][Allocator]");
    sb.appendLine("    /// @param list             the source native list");
    sb.append("    public ").append(type.prefix).append("NativeList(AllocatorFactory allocatorFactory, ").append(type.prefix).appendLine("NativeList list) {");
    sb.appendLine("        super(allocatorFactory, list);");
    sb.appendLine("    }");
    sb.appendLine();

    sb.append("    @Override public ").append(type.typeName).appendLine(" get(long index) {");
    sb.appendLine("        Objects.checkIndex(index, size);");
    sb.append("        return data.getAtIndex(").append(type.valueLayout).appendLine(", index);");
    sb.appendLine("    }");
    sb.appendLine();

    sb.appendLine("    /// Inserts the given element at the end.");
    sb.appendLine("    /// @param value the value");
    sb.append("    public void add(").append(type.typeName).appendLine(" value) {");
    sb.appendLine("        ensureCapacity(size + 1);");
    sb.append("        data.setAtIndex(").append(type.valueLayout).appendLine(", size, value);");
    sb.appendLine("        size++;");
    sb.appendLine("    }");
    sb.appendLine();

    sb.appendLine("    /// Inserts the given element at the given index.");
    sb.appendLine("    /// @param index the index of the element to be inserted");
    sb.appendLine("    /// @param value the value");
    sb.append("    public void add(long index, ").append(type.typeName).appendLine(" value) {");
    sb.appendLine("        if (index == size) {");
    sb.appendLine("            add(value);");
    sb.appendLine("            return;");
    sb.appendLine("        }");
    sb.appendLine();
    sb.appendLine("        Objects.checkIndex(index, size + 1);");
    sb.appendLine("        ensureCapacity(size + 1);");
    sb.appendLine("        move(index, index + 1);");
    sb.append("        data.setAtIndex(").append(type.valueLayout).appendLine(", index, value);");
    sb.appendLine("        size++;");
    sb.appendLine("    }");
    sb.appendLine();

    sb.appendLine("    /// Inserts the given elements at the end.");
    sb.appendLine("    /// @param values the values");
    sb.append("    public void addAll(").append(type.typeName).appendLine("[] values) {");
    if (type.hasArray) {
        sb.appendLine("        if (values.length == 0) return;");
        sb.appendLine("        ensureCapacity(size + values.length);");
        sb.append("        MemorySegment.copy(values, 0, data, ").append(type.valueLayout).append(", ").append(type.valueLayout).appendLine(".scale(0, size), values.length);");
        sb.appendLine("        size += values.length;");
    } else {
        sb.appendLine("        for (var v : values) { add(v); }");
    }
    sb.appendLine("    }");
    sb.appendLine();

    if (type.hasArray) {
        sb.appendLine("    /// Inserts the given elements at the given index.");
        sb.appendLine("    /// @param index  the index of the elements to be inserted");
        sb.appendLine("    /// @param values the values");
        sb.append("    public void addAll(long index, ").append(type.typeName).appendLine("[] values) {");
        sb.appendLine("        if (index == size) {");
        sb.appendLine("            addAll(values);");
        sb.appendLine("            return;");
        sb.appendLine("        }");
        sb.appendLine();
        sb.appendLine("        Objects.checkIndex(index, size + values.length);");
        sb.appendLine("        if (values.length == 0) return;");
        sb.appendLine("        ensureCapacity(size + values.length);");
        sb.appendLine("        move(index, index + values.length);");
        sb.append("        MemorySegment.copy(values, 0, data, ").append(type.valueLayout).append(", ").append(type.valueLayout).appendLine(".scale(0, index), values.length);");
        sb.appendLine("        size += values.length;");
        sb.appendLine("    }");
        sb.appendLine();

        sb.append("    @Override public ").append(type.typeName).append("[] toArray() { return data().toArray(").append(type.valueLayout).appendLine("); }");
        sb.appendLine();
    }

    sb.append("    @Override public ").append(type.valueLayoutClass).append(" elementLayout() { return ").append(type.valueLayout).appendLine("; }");
    sb.appendLine("}");

    Files.writeString(packageDir.resolve(type.prefix + "NativeList.java"), sb.sb);
}

void main() throws IOException {
    Path packageDir = Path.of("io/github/overrun/nativelist");
    Files.createDirectories(packageDir);

    for (Type type : Type.values()) {
        generateViewClass(packageDir, type);
        generateClass(packageDir, type);
    }
}
