package edu.ktu.atg.common.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClasszInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5307557043879042835L;
    private final String name;

    private final Map<String, MethodInfo> methods = new HashMap<>();

    public List<MethodBranch> getAllbranches() {
        final List<MethodBranch> branches = new ArrayList<>();
        methods.forEach((k, m) -> branches.addAll(m.getBranches()));
        return branches;
    }

    public List<ExecutableStatement> getStatements() {
        final List<ExecutableStatement> statements = new ArrayList<>();
        methods.forEach((k, m) -> statements.addAll(m.getStatements()));
        return statements;
    }

    public Map<String, MethodInfo> getMethods() {
        return methods;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        String temp = name.replace('$', '.');
        int i = temp.lastIndexOf('.');
        return temp.substring(i + 1);
    }

    public String getPackageName() {
        String temp = name;
        int i = temp.lastIndexOf('.');
        return temp.substring(0, i);
    }

    public ClasszInfo(String name) {
        this.name = name;

    }
}
