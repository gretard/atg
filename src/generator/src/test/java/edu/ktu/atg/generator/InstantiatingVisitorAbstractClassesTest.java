package edu.ktu.atg.generator;

import org.junit.Assert;
import org.junit.Test;

import edu.ktu.atg.common.executables.ExecutableAbstractClassz;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.execution.ClassesAnalyzer;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.DefinedValue;
import edu.ktu.atg.generator.visitors.InstantiatingVisitor;

public class InstantiatingVisitorAbstractClassesTest {

    ClassesAnalyzer cna = new ClassesAnalyzer();

    @Test
    public void testOverrideMainReturnExpectedValue() throws Throwable {
        SolutionExecutionData data = new SolutionExecutionData();
        InstantiatingVisitor sut = new InstantiatingVisitor(data);
        Class<?> classz = TestClass.class;
        ExecutableAbstractClassz item = (ExecutableAbstractClassz) cna.create(classz, classz, 3);
        IExecutable main = item.getMethodsToImplement()[0].getReturnValue();
        data.defineValue(DefinedValue.createFixed(main, 10));
        Object x = sut.visit(item, null);
        TestClass r = (TestClass) x;
        Assert.assertEquals(10, r.getValue());
        data.getExecutedPairs().forEach((p)-> {
          System.out.println(p.getItem()+" "+p.getRoot()+" "+p.getReturnValue());  
        });
    }

    @Test
    public void testExecutionTrace() throws Throwable {
        SolutionExecutionData data = new SolutionExecutionData();
        InstantiatingVisitor sut = new InstantiatingVisitor(data);
        Class<?> classz = TestClass.class;
        ExecutableAbstractClassz item = (ExecutableAbstractClassz) cna.create(classz, classz, 3);

        Object x = sut.execute(item, null);
        TestClass r = (TestClass) x;
        r.getValue();
        r.getValue();
        System.out.println("Trace..");
        data.getExecutedPairs().forEach(pair -> {
            System.out.println(
                    pair.getItem().getClass() + " " + pair.getItem().getClassz() + " " + pair.getItem().getId());
        });
        Assert.assertEquals(4, data.getExecutedPairs().size());
    }

    public static abstract class TestClass {
        public TestClass(int v) {

        }

        public abstract int getValue();
    }

}
