package edu.ktu.atg.example;

public class StaticCalculatorAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.StaticCalculator.multiply(-1, -1);
        org.junit.Assert.assertEquals(1, sut_0);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.StaticCalculator.multiply(0, 0);
        org.junit.Assert.assertEquals(0, sut_0);
    }

    @org.junit.Test()
    public final void test2() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.StaticCalculator.multiply(-1, 1);
        org.junit.Assert.assertEquals(-1, sut_0);
    }

    @org.junit.Test()
    public final void test3() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.StaticCalculator.multiply(-2147483648, 1);
        org.junit.Assert.assertEquals(-2147483648, sut_0);
    }
}
