package edu.ktu.atg.common.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class MethodInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5307557043879042835L;
    private final String name;

    private final List<ExecutableStatement> statements = new ArrayList<ExecutableStatement>();

    private final List<MethodBranch> branches = new ArrayList<MethodBranch>();

    public List<MethodBranch> getBranches() {
        return branches;
    }

    public List<ExecutableStatement> getStatements() {
        return statements;
    }

    public String getName() {
        return name;
    }

    public MethodInfo(String name) {
        this.name = name;

    }
}
