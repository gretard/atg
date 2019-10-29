package edu.ktu.atg.example;

public class StaticCalculatorAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.StaticCalculator.multiply((int) 0, (int) 0);
        org.junit.Assert.assertEquals((int) 0, sut_0);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.StaticCalculator.multiply((int) 1, (int) -1);
        org.junit.Assert.assertEquals((int) -1, sut_0);
    }

    @org.junit.Test()
    public final void test2() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.StaticCalculator.multiply((int) 1, (int) 1);
        org.junit.Assert.assertEquals((int) 1, sut_0);
    }

    @org.junit.Test()
    public final void test3() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.StaticCalculator.multiply((int) 1, (int) 2147483647);
        org.junit.Assert.assertEquals((int) 2147483647, sut_0);
    }
}
