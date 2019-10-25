package edu.ktu.atg.common.executables;

public final class ExecutableValue extends BaseExecutable {

    private final ValueType type;

    public ExecutableValue(Class<?> classz, ValueType type) {
        super(classz);
        this.type = type;
    }

    @Override
    public IExecutable copy() {
        return new ExecutableValue(this.getClassz(), this.type);
    }

    @Override
    public <T> T accept(IExecutable root, IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

    public ValueType getType() {
        return type;
    }
    public static ExecutableValue complexType(Class<?> classz) {
        return new ExecutableValue(classz, ValueType.COMPLEX);
    }
}
