package edu.ktu.atg.generator;

import org.junit.Assert;
import org.junit.Test;

import edu.ktu.atg.common.executables.ExecutableArray;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.execution.ClassesAnalyzer;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.DefinedValue;
import edu.ktu.atg.generator.visitors.InstantiatingVisitor;

public class InstantiatingVisitorArraysTest {

    ClassesAnalyzer cna = new ClassesAnalyzer();

    @Test
    public void testOverrideMainReturnExpectedValue() throws Throwable {
        SolutionExecutionData data = new SolutionExecutionData();
        InstantiatingVisitor sut = new InstantiatingVisitor(data);
        Class<?> classz = int[].class;
        IExecutable item = cna.create(classz, classz, 3);
        Object x = sut.execute(item, null);
        Assert.assertArrayEquals(new int[] { 0, 0, 0 }, (int[]) x);
        Assert.assertEquals(4, data.getExecutedPairs().size());
    }

    @Test
    public void testGivenEmptyChildren() throws Throwable {
        SolutionExecutionData data = new SolutionExecutionData();
        InstantiatingVisitor sut = new InstantiatingVisitor(data);
        Class<?> classz = int[].class;
        ExecutableArray item = (ExecutableArray) cna.create(classz, classz, 3);
        sut.getCache().clear();
        data.defineValue(DefinedValue.createExecutableArr(item));
        Object x2 = sut.execute(item, null);
        Assert.assertArrayEquals(new int[] {}, (int[]) x2);
        Assert.assertEquals(1, data.getExecutedPairs().size());
    }

    @Test
    public void testGivenEmptyFixedValue() throws Throwable {
        SolutionExecutionData data = new SolutionExecutionData();
        InstantiatingVisitor sut = new InstantiatingVisitor(data);
        Class<?> classz = int[].class;
        ExecutableArray item = (ExecutableArray) cna.create(classz, classz, 3);
        sut.getCache().clear();
        data.defineValue(DefinedValue.createFixed(item, null));
        Object x2 = sut.execute(item, null);
        Assert.assertNull(x2);
        Assert.assertEquals(1, data.getExecutedPairs().size());
    }

    @Test
    public void test2DimArray() throws Throwable {
        SolutionExecutionData data = new SolutionExecutionData();
        InstantiatingVisitor sut = new InstantiatingVisitor(data);
        Class<?> classz = int[][].class;
        IExecutable item = cna.create(classz, classz, 3);
        int[][] x = (int[][]) sut.execute(item, null);
        Assert.assertEquals(3, x.length);
        Assert.assertEquals(13, data.getExecutedPairs().size());

    }

}
