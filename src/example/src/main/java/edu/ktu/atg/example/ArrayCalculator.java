package edu.ktu.atg.example;

public class ArrayCalculator {
    public static int sum(int... arr) {
        int r = 0;
        for (int v : arr) {
            r += v;
        }
        return r;
    }
}
