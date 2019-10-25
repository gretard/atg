package edu.ktu.atg.common.executables;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ExecutableFieldWriter extends BaseExecutable {

    private final transient Field field;
    private final boolean isStatic;

    private final IExecutable inputValue;
    private final String name;

    public Field getField() {
        return field;
    }

    public IExecutable getInputValue() {
        return inputValue;
    }

    public ExecutableFieldWriter(Field field, IExecutable inputValue) {
        super(field.getDeclaringClass());
        this.field = field;
        this.inputValue = inputValue;
        this.isStatic = Modifier.isStatic(field.getModifiers());
        this.name = field.getName();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public IExecutable copy() {
        return new ExecutableFieldWriter(this.field, this.inputValue.copy());
    }

    @Override
    public <T> T accept(final IExecutable root, final IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

    public boolean isStatic() {
        return this.isStatic;
    }

}
