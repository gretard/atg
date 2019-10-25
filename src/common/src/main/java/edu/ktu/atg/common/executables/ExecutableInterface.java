package edu.ktu.atg.common.executables;

import java.util.LinkedList;
import java.util.List;

public final class ExecutableInterface extends BaseExecutable {

    public ExecutableAbstractMethod[] getMethodsToImplement() {
        return methodsToImplement;
    }

    private final IExecutable returnValue;
    private final ExecutableAbstractMethod[] methodsToImplement;

    public ExecutableInterface(Class<?> classz, IExecutable returnValue,
            ExecutableAbstractMethod... methodsToImplement) {
        super(classz);
        this.returnValue = returnValue;
        this.methodsToImplement = methodsToImplement;
    }

    @Override
    public IExecutable copy() {
        List<ExecutableAbstractMethod> items = new LinkedList<>();
        for (ExecutableAbstractMethod p : methodsToImplement) {
            items.add((ExecutableAbstractMethod) p.copy());
        }

        return new ExecutableInterface(this.classz, this.returnValue.copy(),
                items.toArray(new ExecutableAbstractMethod[0]));
    }

    @Override
    public <T> T accept(final IExecutable root, final IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

    public static ExecutableInterface create(final Class<?> classz, ExecutableAbstractMethod... methodsToImplement) {
        return new ExecutableInterface(classz, ExecutableValue.complexType(classz), methodsToImplement);
    }

}
