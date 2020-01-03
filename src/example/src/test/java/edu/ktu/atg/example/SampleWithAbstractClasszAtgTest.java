package edu.ktu.atg.example;

public class SampleWithAbstractClasszAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithAbstractClassz sut_0 = new edu.ktu.atg.example.SampleWithAbstractClassz();
        final int sut_5 = (int) -1;
        final edu.ktu.atg.example.SampleWithAbstractClassz.SampleAbstract sut_2 = new edu.ktu.atg.example.SampleWithAbstractClassz.SampleAbstract((int) 0, (int) 0) {

            public int getValue(int p_0, int p_1) {
                return sut_5;
            }
        };
        sut_0.work(sut_2);
        final int sut_10 = sut_0.getValue();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((int) -1, sut_10);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithAbstractClassz sut_0 = new edu.ktu.atg.example.SampleWithAbstractClassz();
        final edu.ktu.atg.example.SampleWithAbstractClassz.SampleAbstract sut_3 = (edu.ktu.atg.example.SampleWithAbstractClassz.SampleAbstract) null;
        sut_0.work(sut_3);
        final int sut_6 = sut_0.getValue();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNull("Expected item sut_3 to be NULL", sut_3);
        org.junit.Assert.assertEquals((int) 0, sut_6);
    }

    @org.junit.Test()
    public final void test2() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithAbstractClassz sut_0 = new edu.ktu.atg.example.SampleWithAbstractClassz();
        final int sut_5 = (int) 2147483647;
        final edu.ktu.atg.example.SampleWithAbstractClassz.SampleAbstract sut_2 = new edu.ktu.atg.example.SampleWithAbstractClassz.SampleAbstract((int) 0, (int) 0) {

            public int getValue(int p_0, int p_1) {
                return sut_5;
            }
        };
        sut_0.work(sut_2);
        final int sut_10 = sut_0.getValue();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((int) 2147483647, sut_10);
    }

    @org.junit.Test()
    public final void test3() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithAbstractClassz sut_0 = new edu.ktu.atg.example.SampleWithAbstractClassz();
        final int sut_5 = (int) 1;
        final edu.ktu.atg.example.SampleWithAbstractClassz.SampleAbstract sut_2 = new edu.ktu.atg.example.SampleWithAbstractClassz.SampleAbstract((int) 0, (int) 0) {

            public int getValue(int p_0, int p_1) {
                return sut_5;
            }
        };
        sut_0.work(sut_2);
        final int sut_10 = sut_0.getValue();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((int) 1, sut_10);
    }

    @org.junit.Test()
    public final void test4() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithAbstractClassz sut_0 = new edu.ktu.atg.example.SampleWithAbstractClassz();
        final int sut_5 = (int) 0;
        final edu.ktu.atg.example.SampleWithAbstractClassz.SampleAbstract sut_2 = new edu.ktu.atg.example.SampleWithAbstractClassz.SampleAbstract((int) 0, (int) 0) {

            public int getValue(int p_0, int p_1) {
                return sut_5;
            }
        };
        sut_0.work(sut_2);
        final int sut_10 = sut_0.getValue();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((int) 0, sut_10);
    }
}
