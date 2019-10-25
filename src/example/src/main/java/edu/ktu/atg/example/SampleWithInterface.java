package edu.ktu.atg.example;

public class SampleWithInterface {
    private int result = 0;

    public void calculate(Provider provider) {
        if (provider == null) {
            return;
        }
        result += provider.getValue(result);
    }

    public int getResult() {
        return result;
    }

    public static interface Provider {
        int getValue(int a);
    }

}
