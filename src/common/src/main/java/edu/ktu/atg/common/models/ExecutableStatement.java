package edu.ktu.atg.common.models;

import java.io.Serializable;

public class ExecutableStatement implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6091408305073018460L;
    private final String methodName;
    private final String expression;
    private final StatementTypes type;
    private final int no;
    private final String[] context;
    private final int uniqueNo;

    public static enum StatementTypes {
        RETURN, RETURNVOID, GOTO, SWITCH, BRANCH, THROWS, OTHER
    }

    public ExecutableStatement(String methodName, String expression, StatementTypes type, int no, int uniqueNo,
            String... context) {
        this.methodName = methodName;
        this.expression = expression;
        this.type = type;
        this.no = no;
        this.uniqueNo = uniqueNo;
        this.context = context;

    }

    public String getMethodName() {
        return methodName;
    }

    public String getExpression() {
        return expression;
    }

    public StatementTypes getType() {
        return type;
    }

    public int getNo() {
        return no;
    }

    public String[] getContext() {
        return context;
    }

    public int getUniqueNo() {
        return uniqueNo;
    }
}
