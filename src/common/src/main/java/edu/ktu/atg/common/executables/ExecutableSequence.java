package edu.ktu.atg.common.executables;

import java.util.LinkedList;
import java.util.List;

public class ExecutableSequence extends BaseExecutable implements IExecutableWithReturnValue {
    private final IExecutableWithReturnValue root;

    private final List<IExecutable> writers = new LinkedList<>();
    private final List<IExecutable> staticWriters = new LinkedList<>();
    private final List<IExecutable> observers = new LinkedList<>();

    public List<IExecutable> getStaticWriters() {
        return staticWriters;
    }

    public ExecutableSequence(Class<?> classz, IExecutableWithReturnValue root) {
        super(classz);
        this.root = root;
    }

    public IExecutable getRoot() {
        return root;
    }

    public List<IExecutable> getWriters() {
        return writers;
    }

    public List<IExecutable> getObservers() {
        return observers;
    }

    @Override
    public <T> T accept(IExecutable root, IVisitor<T> visitor) throws Throwable {
        return visitor.visit(this, root);
    }

    @Override
    public IExecutable getReturnValue() {
        if (root == null) {
            return this;
        }
        return root.getReturnValue();
    }

    @Override
    public ExecutableSequence copy() {

        ExecutableSequence copy = new ExecutableSequence(classz,
                root == null ? null : (IExecutableWithReturnValue) root.copy());
        for (IExecutable e : this.writers) {
            copy.writers.add(e.copy());
        }
        for (IExecutable e : this.observers) {
            copy.observers.add(e.copy());
        }
        return copy;
    }
}
