package edu.ktu.atg.common.executables;

public interface IBaseVisitor<T> {
    public T innerVisit(IExecutable root, IExecutable item) throws Throwable;
}
