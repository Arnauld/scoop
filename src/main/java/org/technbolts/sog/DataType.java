package org.technbolts.sog;

public enum DataType {
    Boolean,
    Bytes,
    Date,
    Double,
    Float,
    Int,
    Long,
    String,
    Node;

    public boolean matches(String type) {
        return name().equalsIgnoreCase(type);
    }
}
