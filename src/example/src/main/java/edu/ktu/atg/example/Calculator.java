package edu.ktu.atg.example;

public class Calculator {
    public double result = 0;
    
    public static final int constantValue = 10;

    public Calculator add(double v) {
        result += v;
        return this;
    }

    public Calculator divide(double v) {
        if (v != 0) {
            result /= v;
        }
        return this;
    }

    public void reset() {

    }

    public double getResult() {
        return result;
    }
}
