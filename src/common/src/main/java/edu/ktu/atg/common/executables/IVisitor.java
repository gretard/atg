package edu.ktu.atg.common.executables;

public interface IVisitor<T> {

    public default T visit(IVisitable item, IExecutable root) throws Throwable {
        return null;
    }

    public default T visit(ExecutableValue item, IExecutable root) throws Throwable {
        return null;
    }

    public default T visit(ExecutableConstructor item, IExecutable root) throws Throwable {
        return null;
    }

    public default T visit(ExecutableMethod item, IExecutable root) throws Throwable {
        return null;
    }

    public default T visit(ExecutableAbstractMethod item, IExecutable root) throws Throwable {
        return null;
    }

    public default T visit(ExecutableAbstractClassz item, IExecutable root) throws Throwable {
        return null;
    }

    public default T visit(ExecutableInterface item, IExecutable root) throws Throwable {
        return null;
    }

    public default T visit(ExecutableArray item, IExecutable root) throws Throwable {
        return null;
    }

    public T visit(ExecutableFieldWriter item, IExecutable root) throws Throwable;

    public T visit(ExecutableEnum item, IExecutable root) throws Throwable;

    
    public T visit(ExecutableFieldObserver item, IExecutable root) throws Throwable;

    public T visit(ExecutableSequence item, IExecutable root) throws Throwable;

}
