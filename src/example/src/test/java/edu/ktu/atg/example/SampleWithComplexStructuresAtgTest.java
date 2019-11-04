package edu.ktu.atg.example;

public class SampleWithComplexStructuresAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithComplexStructures sut_0 = new edu.ktu.atg.example.SampleWithComplexStructures();
        final edu.ktu.atg.example.SampleWithComplexStructures.Test0 sut_4 = new edu.ktu.atg.example.SampleWithComplexStructures.Test0();
        final java.io.PrintWriter sut_2 = sut_0.execute((java.lang.String) "a", sut_4);
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_4 to be NOT null", sut_4);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
    }
}
