package edu.ktu.atg.common.executables;

public interface IExecutable extends IVisitable {

    public String getId();

    public String getClassName();

    public Class<?> getClassz();

    public IExecutable copy();

}
