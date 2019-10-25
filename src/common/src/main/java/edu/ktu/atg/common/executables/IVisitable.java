package edu.ktu.atg.common.executables;

public interface IVisitable {
    public <T> T accept(IExecutable root, IVisitor<T> visitor) throws Throwable;
}
