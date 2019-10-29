package edu.ktu.atg.example;

public class CalculatorAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final edu.ktu.atg.example.Calculator sut_0 = new edu.ktu.atg.example.Calculator();
        final edu.ktu.atg.example.Calculator sut_2 = sut_0.divide((double) 1.7976931348623157E308d);
        final double sut_4 = sut_0.getResult();
        final double sut_6 = sut_0.result;
        final int sut_8 = edu.ktu.atg.example.Calculator.constantValue;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((double) 0.0d, sut_4, 0.001d);
        org.junit.Assert.assertEquals((double) 0.0d, sut_6, 0.001d);
        org.junit.Assert.assertEquals((int) 10, sut_8);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        final edu.ktu.atg.example.Calculator sut_0 = new edu.ktu.atg.example.Calculator();
        final edu.ktu.atg.example.Calculator sut_2 = sut_0.divide((double) 1.0d);
        final edu.ktu.atg.example.Calculator sut_4 = sut_0.divide((double) 0d);
        final double sut_6 = sut_0.getResult();
        final double sut_8 = sut_0.result;
        final int sut_10 = edu.ktu.atg.example.Calculator.constantValue;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertNotNull("Expected item sut_4 to be NOT null", sut_4);
        org.junit.Assert.assertEquals((double) 0.0d, sut_6, 0.001d);
        org.junit.Assert.assertEquals((double) 0.0d, sut_8, 0.001d);
        org.junit.Assert.assertEquals((int) 10, sut_10);
    }

    @org.junit.Test()
    public final void test2() throws java.lang.Throwable {
        final edu.ktu.atg.example.Calculator sut_0 = new edu.ktu.atg.example.Calculator();
        final edu.ktu.atg.example.Calculator sut_2 = sut_0.add((double) 1.0d);
        final double sut_4 = sut_0.getResult();
        final double sut_6 = sut_0.result;
        final int sut_8 = edu.ktu.atg.example.Calculator.constantValue;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((double) 1.0d, sut_4, 0.001d);
        org.junit.Assert.assertEquals((double) 1.0d, sut_6, 0.001d);
        org.junit.Assert.assertEquals((int) 10, sut_8);
    }

    @org.junit.Test()
    public final void test3() throws java.lang.Throwable {
        final edu.ktu.atg.example.Calculator sut_0 = new edu.ktu.atg.example.Calculator();
        final edu.ktu.atg.example.Calculator sut_2 = sut_0.add((double) -1.0d);
        final double sut_4 = sut_0.getResult();
        final double sut_6 = sut_0.result;
        final int sut_8 = edu.ktu.atg.example.Calculator.constantValue;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((double) -1.0d, sut_4, 0.001d);
        org.junit.Assert.assertEquals((double) -1.0d, sut_6, 0.001d);
        org.junit.Assert.assertEquals((int) 10, sut_8);
    }

    @org.junit.Test()
    public final void test4() throws java.lang.Throwable {
        final edu.ktu.atg.example.Calculator sut_0 = new edu.ktu.atg.example.Calculator();
        final edu.ktu.atg.example.Calculator sut_2 = sut_0.divide((double) -1.0d);
        final double sut_4 = sut_0.getResult();
        final double sut_6 = sut_0.result;
        final int sut_8 = edu.ktu.atg.example.Calculator.constantValue;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((double) -0.0d, sut_4, 0.001d);
        org.junit.Assert.assertEquals((double) -0.0d, sut_6, 0.001d);
        org.junit.Assert.assertEquals((int) 10, sut_8);
    }

    @org.junit.Test()
    public final void test5() throws java.lang.Throwable {
        final edu.ktu.atg.example.Calculator sut_0 = new edu.ktu.atg.example.Calculator();
        final edu.ktu.atg.example.Calculator sut_2 = sut_0.add((double) 0d);
        final double sut_4 = sut_0.getResult();
        final double sut_6 = sut_0.result;
        final int sut_8 = edu.ktu.atg.example.Calculator.constantValue;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((double) 0.0d, sut_4, 0.001d);
        org.junit.Assert.assertEquals((double) 0.0d, sut_6, 0.001d);
        org.junit.Assert.assertEquals((int) 10, sut_8);
    }

    @org.junit.Test()
    public final void test6() throws java.lang.Throwable {
        final edu.ktu.atg.example.Calculator sut_0 = new edu.ktu.atg.example.Calculator();
        final edu.ktu.atg.example.Calculator sut_2 = sut_0.divide((double) 0d);
        final double sut_4 = sut_0.getResult();
        final double sut_6 = sut_0.result;
        final int sut_8 = edu.ktu.atg.example.Calculator.constantValue;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((double) 0.0d, sut_4, 0.001d);
        org.junit.Assert.assertEquals((double) 0.0d, sut_6, 0.001d);
        org.junit.Assert.assertEquals((int) 10, sut_8);
    }

    @org.junit.Test()
    public final void test7() throws java.lang.Throwable {
        final edu.ktu.atg.example.Calculator sut_0 = new edu.ktu.atg.example.Calculator();
        final edu.ktu.atg.example.Calculator sut_2 = sut_0.add((double) 1.7976931348623157E308d);
        final double sut_4 = sut_0.getResult();
        final double sut_6 = sut_0.result;
        final int sut_8 = edu.ktu.atg.example.Calculator.constantValue;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((double) 1.7976931348623157E308d, sut_4, 0.001d);
        org.junit.Assert.assertEquals((double) 1.7976931348623157E308d, sut_6, 0.001d);
        org.junit.Assert.assertEquals((int) 10, sut_8);
    }

    @org.junit.Test()
    public final void test8() throws java.lang.Throwable {
        final edu.ktu.atg.example.Calculator sut_0 = new edu.ktu.atg.example.Calculator();
        final edu.ktu.atg.example.Calculator sut_2 = sut_0.add((double) 1.0d);
        final edu.ktu.atg.example.Calculator sut_4 = sut_0.divide((double) 0d);
        final double sut_6 = sut_0.getResult();
        final double sut_8 = sut_0.result;
        final int sut_10 = edu.ktu.atg.example.Calculator.constantValue;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertNotNull("Expected item sut_4 to be NOT null", sut_4);
        org.junit.Assert.assertEquals((double) 1.0d, sut_6, 0.001d);
        org.junit.Assert.assertEquals((double) 1.0d, sut_8, 0.001d);
        org.junit.Assert.assertEquals((int) 10, sut_10);
    }

    @org.junit.Test()
    public final void test9() throws java.lang.Throwable {
        final edu.ktu.atg.example.Calculator sut_0 = new edu.ktu.atg.example.Calculator();
        final edu.ktu.atg.example.Calculator sut_2 = sut_0.divide((double) Double.NaN);
        final double sut_4 = sut_0.getResult();
        final double sut_6 = sut_0.result;
        final int sut_8 = edu.ktu.atg.example.Calculator.constantValue;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertNotNull("Expected item sut_2 to be NOT null", sut_2);
        org.junit.Assert.assertEquals((double) Double.NaN, sut_4, 0.001d);
        org.junit.Assert.assertEquals((double) Double.NaN, sut_6, 0.001d);
        org.junit.Assert.assertEquals((int) 10, sut_8);
    }

    @org.junit.Test()
    public final void test10() throws java.lang.Throwable {
        final edu.ktu.atg.example.Calculator sut_0 = new edu.ktu.atg.example.Calculator();
        sut_0.reset();
        final double sut_4 = sut_0.getResult();
        final double sut_6 = sut_0.result;
        final int sut_8 = edu.ktu.atg.example.Calculator.constantValue;
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
        org.junit.Assert.assertEquals((double) 0.0d, sut_4, 0.001d);
        org.junit.Assert.assertEquals((double) 0.0d, sut_6, 0.001d);
        org.junit.Assert.assertEquals((int) 10, sut_8);
    }
}
