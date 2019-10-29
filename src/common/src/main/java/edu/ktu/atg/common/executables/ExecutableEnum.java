package edu.ktu.atg.common.executables;

public class ExecutableEnum extends BaseExecutable implements IExecutableWithReturnValue {

    private final ExecutableValue returnValue;

    public ExecutableEnum(Class<?> classz) {
        super(classz);
        this.returnValue = new ExecutableValue(classz, ValueType.COMPLEX);
    }

    @Override
    public IExecutable copy() {
        return new ExecutableEnum(this.classz);
    }

    @Override
    public <T> T accept(IExecutable root, IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

    @Override
    public IExecutable getReturnValue() {
        return returnValue;
    }

}
