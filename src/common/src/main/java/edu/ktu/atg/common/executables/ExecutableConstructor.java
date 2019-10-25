package edu.ktu.atg.common.executables;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

public final class ExecutableConstructor extends BaseExecutable implements IExecutableWithReturnValue {

    private final IExecutable[] parameters;
    private final transient Constructor<?> constructor;
    private final IExecutable returnValue;

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public ExecutableConstructor(Constructor<?> constructor, IExecutable returnValue, IExecutable... parameters) {
        super(constructor.getDeclaringClass());
        this.constructor = constructor;
        this.parameters = parameters;
        this.returnValue = returnValue;
    }

    @Override
    public IExecutable copy() {
        List<IExecutable> params = new LinkedList<>();
        for (IExecutable p : parameters) {
            params.add(p.copy());
        }

        return new ExecutableConstructor(this.constructor, this.returnValue.copy(), params.toArray(new IExecutable[0]));
    }

    @Override
    public <T> T accept(final IExecutable root, final IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

    public final IExecutable[] getParameters() {
        return parameters;
    }

    @Override
    public IExecutable getReturnValue() {
        return this.returnValue;
    }

}
