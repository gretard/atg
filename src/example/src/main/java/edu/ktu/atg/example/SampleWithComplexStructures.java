package edu.ktu.atg.example;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class SampleWithComplexStructures {

    public PrintWriter execute(java.lang.CharSequence sequence, Test0 test0) throws FileNotFoundException {
        return new PrintWriter("test");
    }

    public static class Test0 {
        int c = 0;

        public int getValue() {
            return c;
        }

        public void write() {
            c++;
        }
    }
}
