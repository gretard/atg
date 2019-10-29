package edu.ktu.atg.example;

public class SampleWithStaticConstructorAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithStaticConstructor sut_0 = edu.ktu.atg.example.SampleWithStaticConstructor.getInstance();
        sut_0.work((int) 0);
        final int sut_4 = sut_0.getCounter();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_4);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithStaticConstructor sut_0 = edu.ktu.atg.example.SampleWithStaticConstructor.getInstance();
        sut_0.work((int) 214748364);
        final int sut_4 = sut_0.getCounter();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 1, sut_4);
    }

    @org.junit.Test()
    public final void test2() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithStaticConstructor sut_0 = edu.ktu.atg.example.SampleWithStaticConstructor.getInstance();
        sut_0.work((int) 2);
        final int sut_4 = sut_0.getCounter();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_4);
    }

    @org.junit.Test()
    public final void test3() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithStaticConstructor sut_0 = edu.ktu.atg.example.SampleWithStaticConstructor.getInstance();
        sut_0.work((int) 2147483647);
        final int sut_4 = sut_0.getCounter();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 1, sut_4);
    }

    @org.junit.Test()
    public final void test4() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithStaticConstructor sut_0 = edu.ktu.atg.example.SampleWithStaticConstructor.getInstance();
        sut_0.work((int) -2147483648);
        final int sut_4 = sut_0.getCounter();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_4);
    }
}
