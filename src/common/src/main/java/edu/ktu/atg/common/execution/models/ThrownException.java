package edu.ktu.atg.common.execution.models;

public class ThrownException {
    private String method;
    public String getMethod() {
        return method;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "ThrownException [method=" + method + ", line=" + line + "]";
    }

    private int line;

    public ThrownException(String method, int line) {
        this.method = method;
        this.line = line;
        
    }
}
