package edu.ktu.atg.common.executables;

public enum ValueType {
    INT(0, true), LONG(0, true), SHORT(0, true), BYTE(0, true), DOUBLE(0, true), FLOAT(0, true), STRING("a", true), 
    CHAR('a', true), NULL(null, true), COMPLEX(null, false),
    ENUM(null, true), BOOLEAN(false, true);

    private final Object defaultValue;
    private final boolean simple;

    public boolean isSimple() {
        return simple;
    }
  
    public Object getDefaultValue() {
        return defaultValue;
    }

    private ValueType(Object defaultValue, boolean simple) {
        this.defaultValue = defaultValue;
        this.simple = simple;
    }
}