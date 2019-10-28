package edu.ktu.atg.example;

public class ClassWithState {

    private int counter = 0;

    public int getValue(int a) {
        counter++;
        if (counter > 3) {
            return a;
        }
        return 0;
    }
}
