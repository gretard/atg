package edu.ktu.atg.example;

public class SampleWithAbstractClassz {

    int c = 0;
    int r = 0;

    public void work(SampleAbstract obj) {
        if (obj == null) {
            return;
        }
        c++;
        r = obj.getValue(c, c);
    }

    public int getValue() {
        return r;
    }

    public static abstract class SampleAbstract {
        public SampleAbstract(int a, int y) {

        }

        public abstract int getValue(int x, int x2);
    }
}
