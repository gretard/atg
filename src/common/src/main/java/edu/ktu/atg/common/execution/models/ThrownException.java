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
        return "ThrownException [method=" + method + ", line=" + line + ", exception=" + exception + "]";
    }



    private int line;
    private String exception;

    public ThrownException(String method, String exception,  int line) {
        this.method = method;
        this.exception = exception;
        this.line = line;
        
    }
}
