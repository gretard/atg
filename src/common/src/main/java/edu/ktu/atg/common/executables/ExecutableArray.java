package edu.ktu.atg.common.executables;

public final class ExecutableArray extends BaseExecutable {

    private final int dimension;
    private final IExecutable componentType;

    public ExecutableArray(Class<?> classz, int dimension, IExecutable componentType) {
        super(classz);
        this.dimension = dimension;
        this.componentType = componentType;
    }

    @Override
    public IExecutable copy() {
        return new ExecutableArray(this.getClassz(), this.dimension, this.componentType);
    }

    public IExecutable copyChild() {
        if (this.dimension - 1 == 0) {
            return this.componentType.copy();
        }
        return new ExecutableArray(this.classz.getComponentType(), this.dimension - 1, this.componentType.copy());
    }

    @Override
    public <T> T accept(IExecutable root, IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

    public IExecutable getComponentType() {
        return componentType;
    }

    public int getDimension() {
        return dimension;
    }
}
