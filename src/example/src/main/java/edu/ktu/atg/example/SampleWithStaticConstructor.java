package edu.ktu.atg.example;

public final class SampleWithStaticConstructor {
    private SampleWithStaticConstructor() {

    }

    public static SampleWithStaticConstructor getInstance() {
        return new SampleWithStaticConstructor();
    }

    int c = 0;

    public void work(int a) {
        if (a > 10) {
            c++;
        }
    }

    public int getCounter() {
        return c;
    }
}
