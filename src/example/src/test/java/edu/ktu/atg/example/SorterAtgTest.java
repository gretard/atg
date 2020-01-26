package edu.ktu.atg.example;

public class SorterAtgTest {

    @org.junit.Test()
    public final void test0() throws java.lang.Throwable {
        final int[] sut_0 = new int[] { (int) 2147483647, (int) 0, (int) 0 };
        edu.ktu.atg.example.Sorter.sort(sut_0);
        org.junit.Assert.assertArrayEquals(new int[] { (int) 0, (int) 0, (int) 2147483647 }, sut_0);
    }

    @org.junit.Test()
    public final void test1() throws java.lang.Throwable {
        edu.ktu.atg.example.Sorter.sort((int[]) null);
        final int[] sut_3 = new int[] { (int) 0, (int) 0, (int) 0 };
        edu.ktu.atg.example.Sorter.sort(sut_3);
        org.junit.Assert.assertArrayEquals(new int[] { (int) 0, (int) 0, (int) 0 }, sut_3);
    }

    @org.junit.Test()
    public final void test2() throws java.lang.Throwable {
        edu.ktu.atg.example.Sorter.sort((int[]) null);
    }

    @org.junit.Test()
    public final void test3() throws java.lang.Throwable {
        final int[] sut_0 = new int[] { (int) 1, (int) 0, (int) 0 };
        edu.ktu.atg.example.Sorter.sort(sut_0);
        org.junit.Assert.assertArrayEquals(new int[] { (int) 0, (int) 0, (int) 1 }, sut_0);
    }

    @org.junit.Test()
    public final void test4() throws java.lang.Throwable {
        final edu.ktu.atg.example.Sorter sut_0 = new edu.ktu.atg.example.Sorter();
        org.junit.Assert.assertNotNull("Expected item sut_0 to be NOT null", sut_0);
    }

    @org.junit.Test()
    public final void test5() throws java.lang.Throwable {
        final int[] sut_0 = new int[] { (int) 0, (int) 0, (int) 0 };
        edu.ktu.atg.example.Sorter.sort(sut_0);
        org.junit.Assert.assertArrayEquals(new int[] { (int) 0, (int) 0, (int) 0 }, sut_0);
    }
}
