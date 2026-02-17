enum Type {
    BYTE("Byte", "ValueLayout.OfByte", "ValueLayout.JAVA_BYTE", "byte", true),
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

void main() throws IOException {
    Path packageDir = Path.of("io/github/overrun/nativelist");
    Files.createDirectories(packageDir);

    for (Type type : Type.values()) {
        StringBuilder sb = new StringBuilder();
        sb.append("// This file is auto-generated. DO NOT EDIT!\n");
        sb.append("package io.github.overrun.nativelist;\n");
        sb.append("import java.lang.foreign.*;\n");
        sb.append("import java.util.*;\n");
        sb.append("public class ").append(type.prefix).append("NativeList extends NativeList {\n");

        sb.append("    public ").append(type.prefix).append("NativeList(Allocator allocator, long initialCapacity) {\n");
        sb.append("        super(").append(type.valueLayout).append(", allocator, initialCapacity);\n");
        sb.append("    }\n");
        sb.append('\n');

        sb.append("    public ").append(type.prefix).append("NativeList(Allocator allocator) {\n");
        sb.append("        super(").append(type.valueLayout).append(", allocator);\n");
        sb.append("    }\n");
        sb.append('\n');

        sb.append("    public ").append(type.typeName).append(" get(long index) {\n");
        sb.append("        Objects.checkIndex(index, size);\n");
        sb.append("        return data.getAtIndex(").append(type.valueLayout).append(", index);\n");
        sb.append("    }\n");
        sb.append('\n');

        sb.append("    public void add(").append(type.typeName).append(" value) {\n");
        sb.append("        ensureCapacity(size + 1);\n");
        sb.append("        data.setAtIndex(").append(type.valueLayout).append(", size, value);\n");
        sb.append("        size++;\n");
        sb.append("    }\n");
        sb.append('\n');

        sb.append("    public void add(long index, ").append(type.typeName).append(" value) {\n");
        sb.append("        if (index == size) {\n");
        sb.append("            add(value);\n");
        sb.append("            return;\n");
        sb.append("        }\n");
        sb.append('\n');
        sb.append("        Objects.checkIndex(index, size + 1);\n");
        sb.append("        ensureCapacity(size + 1);\n");
        sb.append("        move(index, index + 1);\n");
        sb.append("        data.setAtIndex(").append(type.valueLayout).append(", index, value);\n");
        sb.append("        size++;\n");
        sb.append("    }\n");
        sb.append('\n');

        if (type.hasArray) {
            sb.append("    public void addAll(").append(type.typeName).append("[] values) {\n");
            sb.append("        ensureCapacity(size + values.length);\n");
            sb.append("        MemorySegment.copy(values, 0, data, ").append(type.valueLayout).append(", ").append(type.valueLayout).append(".scale(0, size), values.length);\n");
            sb.append("        size += values.length;\n");
            sb.append("    }\n");
            sb.append('\n');

            sb.append("    public void addAll(long index, ").append(type.typeName).append("[] values) {\n");
            sb.append("        if (index == size) {\n");
            sb.append("            addAll(values);\n");
            sb.append("            return;\n");
            sb.append("        }\n");
            sb.append('\n');
            sb.append("        Objects.checkIndex(index, size + values.length);\n");
            sb.append("        ensureCapacity(size + values.length);\n");
            sb.append("        move(index, index + values.length);\n");
            sb.append("        MemorySegment.copy(values, 0, data, ").append(type.valueLayout).append(", ").append(type.valueLayout).append(".scale(0, index), values.length);\n");
            sb.append("        size += values.length;\n");
            sb.append("    }\n");
            sb.append('\n');

            sb.append("    public ").append(type.typeName).append("[] toArray() { return data().toArray(").append(type.valueLayout).append("); }");
            sb.append('\n');
        }

        sb.append("    @Override public ").append(type.valueLayoutClass).append(" elementLayout() { return ").append(type.valueLayout).append("; }\n");

        sb.append("}\n");

        Files.writeString(packageDir.resolve(type.prefix + "NativeList.java"), sb);
    }
}
