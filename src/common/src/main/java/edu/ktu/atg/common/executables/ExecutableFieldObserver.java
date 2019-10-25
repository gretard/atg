package edu.ktu.atg.common.executables;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class ExecutableFieldObserver extends BaseExecutable implements IExecutableWithReturnValue {

    private final transient Field field;
    private final boolean isStatic;
    private final IExecutable returnValue;
    private final String name;

    public IExecutable getReturnValue() {
        return returnValue;
    }

    public ExecutableFieldObserver(Field field, IExecutable returnValue) {
        super(field.getDeclaringClass());
        this.field = field;
        this.returnValue = returnValue;
        this.isStatic = Modifier.isStatic(field.getModifiers());
        this.name = field.getName();
    }

    @Override
    public IExecutable copy() {
        return new ExecutableFieldObserver(this.field, this.returnValue.copy());
    }

    public String getName() {
        return this.name;
    }

    @Override
    public <T> T accept(final IExecutable root, final IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

    public Field getField() {
        return field;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

}
