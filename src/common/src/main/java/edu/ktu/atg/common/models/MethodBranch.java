package edu.ktu.atg.common.models;

import java.io.Serializable;

public class MethodBranch implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8710293364730550305L;

    private final String expression;

    private final int no;
    private final String leftType;
    private final String rightType;
    private final String name;

    public MethodBranch(String name, String expression, int no, String leftType, String rightType) {
        this.name = name;
        this.expression = expression;
        this.no = no;
        this.leftType = leftType;
        this.rightType = rightType;

    }

    public String getExpression() {
        return expression;
    }

    public int getNo() {
        return no;
    }

    public String getLeftType() {
        return leftType;
    }

    public String getRightType() {
        return rightType;
    }

    public String getName() {
        return name;
    }

}
