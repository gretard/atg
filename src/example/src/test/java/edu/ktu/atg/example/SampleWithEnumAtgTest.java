package edu.ktu.atg.example;

public class SampleWithEnumAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithEnum sut_0 = edu.ktu.atg.example.SampleWithEnum.TWO;
        final java.lang.String sut_2 = sut_0.toSampleString();
        final edu.ktu.atg.example.SampleWithEnum[] sut_4 = edu.ktu.atg.example.SampleWithEnum.values();
        final edu.ktu.atg.example.SampleWithEnum sut_6 = edu.ktu.atg.example.SampleWithEnum.ONE;
        final edu.ktu.atg.example.SampleWithEnum sut_8 = edu.ktu.atg.example.SampleWithEnum.TWO;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((java.lang.String) "2", sut_2);
        org.junit.Assert.assertNotNull("Expected item sut_4 to be NOT null", sut_4);
        org.junit.Assert.assertNotNull("Expected item sut_6 to be NOT null", sut_6);
        org.junit.Assert.assertNotNull("Expected item sut_8 to be NOT null", sut_8);
    }

    @org.junit.Test(expected = java.lang.Throwable.class)
    public final void test1() throws java.lang.Throwable {
        edu.ktu.atg.example.SampleWithEnum.valueOf((java.lang.String) "a");
    }

    @org.junit.Test()
    public final void test2() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithEnum sut_0 = edu.ktu.atg.example.SampleWithEnum.ONE;
        final java.lang.String sut_2 = sut_0.toSampleString();
        final edu.ktu.atg.example.SampleWithEnum[] sut_4 = edu.ktu.atg.example.SampleWithEnum.values();
        final edu.ktu.atg.example.SampleWithEnum sut_6 = edu.ktu.atg.example.SampleWithEnum.ONE;
        final edu.ktu.atg.example.SampleWithEnum sut_8 = edu.ktu.atg.example.SampleWithEnum.TWO;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((java.lang.String) "1", sut_2);
        org.junit.Assert.assertNotNull("Expected item sut_4 to be NOT null", sut_4);
        org.junit.Assert.assertNotNull("Expected item sut_6 to be NOT null", sut_6);
        org.junit.Assert.assertNotNull("Expected item sut_8 to be NOT null", sut_8);
    }
}
