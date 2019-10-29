package edu.ktu.atg.example;

public class ArrayCalculatorAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.ArrayCalculator.sum(new int[] { (int) 0, (int) 0, (int) 0, (int) 0, (int) 0 });
        org.junit.Assert.assertEquals((int) 0, sut_0);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.ArrayCalculator.sum(new int[] { (int) 1, (int) 0, (int) 0 });
        org.junit.Assert.assertEquals((int) 1, sut_0);
    }

    @org.junit.Test()
    public final void test2() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.ArrayCalculator.sum(new int[] { (int) 2147483647, (int) 0, (int) 0 });
        org.junit.Assert.assertEquals((int) 2147483647, sut_0);
    }

    @org.junit.Test(expected = java.lang.Throwable.class)
    public final void test3() throws java.lang.Throwable {
        edu.ktu.atg.example.ArrayCalculator.sum((int[]) null);
    }

    @org.junit.Test()
    public final void test4() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.ArrayCalculator.sum(new int[] { (int) 0, (int) 0, (int) 0 });
        org.junit.Assert.assertEquals((int) 0, sut_0);
    }

    @org.junit.Test()
    public final void test5() throws java.lang.Throwable {
        final int sut_0 = edu.ktu.atg.example.ArrayCalculator.sum(new int[] { (int) -1, (int) 0, (int) 0 });
        org.junit.Assert.assertEquals((int) -1, sut_0);
    }

    @org.junit.Test()
    public final void test6() throws java.lang.Throwable {
        final edu.ktu.atg.example.ArrayCalculator sut_0 = new edu.ktu.atg.example.ArrayCalculator();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
    }
}
