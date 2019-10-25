package edu.ktu.atg.common.executables;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public final class ExecutableAbstractMethod extends BaseExecutable implements IExecutableWithReturnValue {

    private final IExecutable[] parameters;
    private final Method method;
    private final IExecutable returnValue;

    public IExecutable getReturnValue() {
        return returnValue;
    }

    public ExecutableAbstractMethod(Method method, IExecutable returnValue, IExecutable... parameters) {
        super(method.getDeclaringClass());
        this.method = method;
        this.returnValue = returnValue;
        this.parameters = parameters;
    }

    @Override
    public IExecutable copy() {
        List<IExecutable> params = new LinkedList<>();
        for (IExecutable p : parameters) {
            params.add(p.copy());
        }
        return new ExecutableAbstractMethod(this.method, this.returnValue.copy(), params.toArray(new IExecutable[0]));
    }

    @Override
    public <T> T accept(final IExecutable root, final IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

    public String getName() {
        return this.method.getName();
    }

    public IExecutable[] getParameters() {
        return parameters;
    }

    public Method getMethod() {
        return method;
    }
    
    public int getModifiers() {
        return this.method.getModifiers();
    }
}
