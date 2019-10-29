package edu.ktu.atg.example;

public class ClassWithStateAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final edu.ktu.atg.example.ClassWithState sut_0 = new edu.ktu.atg.example.ClassWithState();
        final int sut_2 = sut_0.getValue((int) 0);
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_2);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        final edu.ktu.atg.example.ClassWithState sut_0 = new edu.ktu.atg.example.ClassWithState();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
    }
}
