package edu.ktu.atg.example;

public class SampleWithInterfaceAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithInterface sut_0 = new edu.ktu.atg.example.SampleWithInterface();
        sut_0.calculate(new edu.ktu.atg.example.SampleWithInterface.Provider() {

            public int getValue(int p_0) {
                return (int) 0;
            }
        });
        final int sut_7 = sut_0.getResult();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_7);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithInterface sut_0 = new edu.ktu.atg.example.SampleWithInterface();
        sut_0.calculate(new edu.ktu.atg.example.SampleWithInterface.Provider() {

            public int getValue(int p_0) {
                return (int) 1;
            }
        });
        final int sut_7 = sut_0.getResult();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 1, sut_7);
    }

    @org.junit.Test()
    public final void test2() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithInterface sut_0 = new edu.ktu.atg.example.SampleWithInterface();
        sut_0.calculate(new edu.ktu.atg.example.SampleWithInterface.Provider() {

            public int getValue(int p_0) {
                return (int) -1;
            }
        });
        final int sut_7 = sut_0.getResult();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) -1, sut_7);
    }

    @org.junit.Test()
    public final void test3() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithInterface sut_0 = new edu.ktu.atg.example.SampleWithInterface();
        sut_0.calculate(new edu.ktu.atg.example.SampleWithInterface.Provider() {

            public int getValue(int p_0) {
                return (int) 2147483647;
            }
        });
        final int sut_7 = sut_0.getResult();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 2147483647, sut_7);
    }

    @org.junit.Test()
    public final void test4() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithInterface sut_0 = new edu.ktu.atg.example.SampleWithInterface();
        sut_0.calculate((edu.ktu.atg.example.SampleWithInterface.Provider) null);
        final int sut_4 = sut_0.getResult();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_4);
    }

    @org.junit.Test()
    public final void test5() throws java.lang.Throwable {
        final edu.ktu.atg.example.SampleWithInterface sut_0 = new edu.ktu.atg.example.SampleWithInterface();
        sut_0.calculate((edu.ktu.atg.example.SampleWithInterface.Provider) null);
        sut_0.calculate(new edu.ktu.atg.example.SampleWithInterface.Provider() {

            public int getValue(int p_0) {
                return (int) 0;
            }
        });
        final int sut_9 = sut_0.getResult();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((int) 0, sut_9);
    }
}
