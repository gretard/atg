package edu.ktu.atg.example;

public class ArrayCalculatorAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final int[] sut_0 = new int[] { 0, 0, 0 };
        final int sut_4 = edu.ktu.atg.example.ArrayCalculator.sum(sut_0);
        org.junit.Assert.assertEquals(0, sut_4);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        final edu.ktu.atg.example.ArrayCalculator sut_0 = new edu.ktu.atg.example.ArrayCalculator();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
    }
}
