package edu.ktu.atg.common.executables;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public final class ExecutableMethod extends BaseExecutable implements IExecutableWithReturnValue {

    private final IExecutable[] parameters;
    private final transient Method method;
    private final IExecutable returnValue;
    private final boolean isStatic;
    private final String name;

    public IExecutable getReturnValue() {
        return returnValue;
    }

    public ExecutableMethod(Method method, IExecutable returnValue, IExecutable... parameters) {
        super(method.getDeclaringClass());
        this.method = method;
        this.returnValue = returnValue;
        this.parameters = parameters;
        this.isStatic = Modifier.isStatic(method.getModifiers());
        this.name = method.getName();
    }

    @Override
    public IExecutable copy() {
        List<IExecutable> params = new LinkedList<>();
        for (IExecutable p : parameters) {
            params.add(p.copy());
        }
        return new ExecutableMethod(this.method, this.returnValue.copy(), params.toArray(new IExecutable[0]));
    }

    @Override
    public <T> T accept(final IExecutable root, final IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

    public String getName() {
        return this.name;
    }

    public IExecutable[] getParameters() {
        return parameters;
    }

    public Method getMethod() {
        return method;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

}
