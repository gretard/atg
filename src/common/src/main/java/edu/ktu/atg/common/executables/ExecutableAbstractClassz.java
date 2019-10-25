package edu.ktu.atg.common.executables;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

public final class ExecutableAbstractClassz extends BaseExecutable implements IExecutableWithReturnValue {

    public ExecutableAbstractMethod[] getMethodsToImplement() {
        return methodsToImplement;
    }

    private final ExecutableAbstractMethod[] methodsToImplement;
    private IExecutable returnValue;
    private Constructor<?> constructor;
    private IExecutable[] parameters;

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public ExecutableAbstractClassz(Constructor<?> constructor, IExecutable[] parameters, IExecutable returnValue,
            ExecutableAbstractMethod... methodsToImplement) {
        super(constructor.getDeclaringClass());
        this.constructor = constructor;
        this.parameters = parameters;
        this.returnValue = returnValue;
        this.methodsToImplement = methodsToImplement;
    }

    @Override
    public IExecutable copy() {
        List<ExecutableAbstractMethod> items = new LinkedList<>();
        for (ExecutableAbstractMethod p : methodsToImplement) {
            items.add((ExecutableAbstractMethod) p.copy());
        }
        List<IExecutable> params = new LinkedList<>();
        for (IExecutable p : methodsToImplement) {
            params.add((IExecutable) p.copy());
        }
        return new ExecutableAbstractClassz(this.constructor, params.toArray(new IExecutable[0]),
                this.returnValue.copy(), items.toArray(new ExecutableAbstractMethod[0]));
    }

    public IExecutable[] getParameters() {
        return parameters;
    }

    @Override
    public <T> T accept(final IExecutable root, final IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

    @Override
    public IExecutable getReturnValue() {
        return returnValue;
    }

}
