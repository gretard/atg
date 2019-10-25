package edu.ktu.atg.common.executables;

public final class ExecutableVoid extends BaseExecutable {

    public ExecutableVoid() {
        super(void.class);
    }

    @Override
    public IExecutable copy() {
        return new ExecutableVoid();
    }

    @Override
    public <T> T accept(IExecutable root, IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

}
