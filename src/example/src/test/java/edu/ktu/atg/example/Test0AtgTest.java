package edu.ktu.atg.example;

public class Test0AtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithComplexStructures.Test0 sut_0 = new edu.ktu.atg.example.SampleWithComplexStructures.Test0();
        sut_0.write();
        final int sut_4 = sut_0.getValue();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 1, sut_4);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithComplexStructures.Test0 sut_0 = new edu.ktu.atg.example.SampleWithComplexStructures.Test0();
        final int sut_2 = sut_0.getValue();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_2);
    }

    @org.junit.Test()
    public final void test2() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithComplexStructures.Test0 sut_0 = new edu.ktu.atg.example.SampleWithComplexStructures.Test0();
        sut_0.write();
        sut_0.write();
        final int sut_6 = sut_0.getValue();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 2, sut_6);
    }

    @org.junit.Test()
    public final void test3() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithComplexStructures.Test0 sut_0 = new edu.ktu.atg.example.SampleWithComplexStructures.Test0();
        sut_0.write();
        sut_0.write();
        sut_0.write();
        final int sut_8 = sut_0.getValue();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 3, sut_8);
    }
}
