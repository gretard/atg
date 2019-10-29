package edu.ktu.atg.example;

public class ClassWithStateAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final edu.ktu.atg.example.ClassWithState sut_0 = new edu.ktu.atg.example.ClassWithState();
        final int sut_2 = sut_0.getValue((int) 1);
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_2);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        final edu.ktu.atg.example.ClassWithState sut_0 = new edu.ktu.atg.example.ClassWithState();
        final int sut_2 = sut_0.getValue((int) 0);
        final int sut_4 = sut_0.getValue((int) 0);
        final int sut_6 = sut_0.getValue((int) 0);
        final int sut_8 = sut_0.getValue((int) -2147483648);
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_2);
        org.junit.Assert.assertEquals((int) 0, sut_4);
        org.junit.Assert.assertEquals((int) 0, sut_6);
        org.junit.Assert.assertEquals((int) -2147483648, sut_8);
    }

    @org.junit.Test()
    public final void test2() throws java.lang.Throwable {
        final edu.ktu.atg.example.ClassWithState sut_0 = new edu.ktu.atg.example.ClassWithState();
        final int sut_2 = sut_0.getValue((int) 0);
        final int sut_4 = sut_0.getValue((int) 0);
        final int sut_6 = sut_0.getValue((int) 0);
        final int sut_8 = sut_0.getValue((int) 0);
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_2);
        org.junit.Assert.assertEquals((int) 0, sut_4);
        org.junit.Assert.assertEquals((int) 0, sut_6);
        org.junit.Assert.assertEquals((int) 0, sut_8);
    }

    @org.junit.Test()
    public final void test3() throws java.lang.Throwable {
        final edu.ktu.atg.example.ClassWithState sut_0 = new edu.ktu.atg.example.ClassWithState();
        final int sut_2 = sut_0.getValue((int) 0);
        final int sut_4 = sut_0.getValue((int) 0);
        final int sut_6 = sut_0.getValue((int) 0);
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_2);
        org.junit.Assert.assertEquals((int) 0, sut_4);
        org.junit.Assert.assertEquals((int) 0, sut_6);
    }

    @org.junit.Test()
    public final void test4() throws java.lang.Throwable {
        final edu.ktu.atg.example.ClassWithState sut_0 = new edu.ktu.atg.example.ClassWithState();
        final int sut_2 = sut_0.getValue((int) 0);
        final int sut_4 = sut_0.getValue((int) 0);
        final int sut_6 = sut_0.getValue((int) 0);
        final int sut_8 = sut_0.getValue((int) 1);
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_2);
        org.junit.Assert.assertEquals((int) 0, sut_4);
        org.junit.Assert.assertEquals((int) 0, sut_6);
        org.junit.Assert.assertEquals((int) 1, sut_8);
    }

    @org.junit.Test()
    public final void test5() throws java.lang.Throwable {
        final edu.ktu.atg.example.ClassWithState sut_0 = new edu.ktu.atg.example.ClassWithState();
        final int sut_2 = sut_0.getValue((int) 0);
        final int sut_4 = sut_0.getValue((int) 0);
        final int sut_6 = sut_0.getValue((int) 0);
        final int sut_8 = sut_0.getValue((int) -1);
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_2);
        org.junit.Assert.assertEquals((int) 0, sut_4);
        org.junit.Assert.assertEquals((int) 0, sut_6);
        org.junit.Assert.assertEquals((int) -1, sut_8);
    }
}
