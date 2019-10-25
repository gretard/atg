package edu.ktu.atg.common.executables;

public abstract class BaseExecutable implements IExecutable {

    protected final String classzName;
    protected final Class<?> classz;
    protected final String id = System.nanoTime() + "";

    public BaseExecutable(Class<?> classz) {
        this.classzName = classz.getName();
        this.classz = classz;
    }

    @Override
    public Class<?> getClassz() {
        return this.classz;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getClassName() {
        return this.classzName;
    }

}
