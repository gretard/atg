package edu.ktu.atg.outputter;

import org.junit.Assert;
import org.junit.Test;

import com.github.javaparser.ast.Node;

import edu.ktu.atg.common.executables.ExecutableAbstractClassz;
import edu.ktu.atg.common.executables.ExecutableArray;
import edu.ktu.atg.common.executables.ExecutableInterface;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.execution.ClassesAnalyzer;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.DefinedValue;

public class GeneratingVisitorInterfacesTest {

    ClassesAnalyzer cna = new ClassesAnalyzer();

    @Test
    public void testSimpleIntCase() throws Throwable {
        OutputContext context = new OutputContext();
        SolutionExecutionData execution = new SolutionExecutionData();
        GeneratingVisitor visitor = new GeneratingVisitor(context, execution);
        Class<?> classz = int.class;
        IExecutable item = cna.create(classz, classz, 3);
        execution.defineValue(DefinedValue.createFixed(item, 11));
        visitor.innerVisit(null, item);
        context.getStatements().forEach((k, v) -> {
            System.out.println(v);
        });

    }

    @Test
    public void testNullArray() throws Throwable {
        OutputContext context = new OutputContext();
        SolutionExecutionData execution = new SolutionExecutionData();
        GeneratingVisitor visitor = new GeneratingVisitor(context, execution);
        Class<?> classz = int[].class;
        ExecutableArray item = (ExecutableArray) cna.create(classz, classz, 3);
        execution.defineValue(DefinedValue.createFixed(item, null));
        visitor.innerVisit(null, item);
        context.getStatements().forEach((k, v) -> {
            System.out.println(v);
        });

    }

    @Test
    public void testArray() throws Throwable {
        OutputContext context = new OutputContext();
        SolutionExecutionData execution = new SolutionExecutionData();
        GeneratingVisitor visitor = new GeneratingVisitor(context, execution);

        Class<?> classz = int[].class;
        ExecutableArray item = (ExecutableArray) cna.create(classz, classz, 3);

        IExecutable child = item.getComponentType().copy();
        IExecutable child2 = item.getComponentType().copy();
        execution.defineValue(DefinedValue.createExecutableArr(item, child, child2));
        execution.defineValue(DefinedValue.createFixed(child, 11));
        execution.defineValue(DefinedValue.createFixed(child2, 12));
        visitor.innerVisit(null, item);
        context.getStatements().forEach((k, v) -> {
            System.out.println(v);
        });

        Assert.assertEquals("new int[] { 11, 12 }", context.getStatements().get(item.getId()).toString());

    }

    @Test
    public void testInlining() throws Throwable {
        OutputContext context = new OutputContext();
        SolutionExecutionData execution = new SolutionExecutionData();
        GeneratingVisitor visitor = new GeneratingVisitor(context, execution);
        Class<?> classz = int.class;
        IExecutable item = cna.create(classz, classz, 3);
        execution.defineValue(DefinedValue.createFixed(item, 11));
        context.generateName(item);
        context.generateName(item);
        context.generateName(item);
        Node node = visitor.innerVisit(null, item);
        Node node2 = visitor.innerVisit(null, item);
        System.out.println("Statements");
        context.getStatements().forEach((k, v) -> {
            System.out.println(v);
        });

    }

    @Test
    public void testInterfaceGeneration() throws Throwable {
        OutputContext context = new OutputContext();
        SolutionExecutionData execution = new SolutionExecutionData();
        GeneratingVisitor visitor = new GeneratingVisitor(context, execution);
        Class<?> classz = TestInterfaceClass.class;
        ExecutableInterface item = (ExecutableInterface) cna.create(classz, classz, 3);
        IExecutable returnValue = item.getMethodsToImplement()[0].getReturnValue();
        execution.defineValue(DefinedValue.createFixed(returnValue, 11));
        visitor.innerVisit(null, item);
        System.out.println("STATEMENTS");
        context.getStatements().forEach((k, v) -> {
            System.out.println(v);
        });

    }

    @Test
    public void testAbstractClasszGeneration() throws Throwable {
        OutputContext context = new OutputContext();
        SolutionExecutionData execution = new SolutionExecutionData();
        GeneratingVisitor visitor = new GeneratingVisitor(context, execution);
        Class<?> classz = TestAbstractClass.class;
        ExecutableAbstractClassz item = (ExecutableAbstractClassz) cna.create(classz, classz, 3);
        for (IExecutable p : item.getParameters()) {
            execution.defineValue(DefinedValue.createFixed(p, 11));
        }
        visitor.innerVisit(null, item);
        context.getStatements().forEach((k, v) -> {
            System.out.println(v);
        });

    }

    @Test
    public void testRefGeneration() throws Throwable {
        OutputContext context = new OutputContext();
        SolutionExecutionData execution = new SolutionExecutionData();
        GeneratingVisitor visitor = new GeneratingVisitor(context, execution);
        Class<?> classz = TestAbstractClass.class;
        ExecutableAbstractClassz item = (ExecutableAbstractClassz) cna.create(classz, classz, 3);
        for (IExecutable p : item.getParameters()) {
            execution.defineValue(DefinedValue.createFixed(p, 11));
        }
        ExecutableAbstractClassz ref = (ExecutableAbstractClassz) item.copy();
        execution.defineValue(DefinedValue.createRef(ref, item));

        visitor.innerVisit(null, item);
        visitor.innerVisit(null, ref);
        context.getStatements().forEach((k, v) -> {
            System.out.println("ST:" + v);
        });
        Assert.assertEquals(context.getNames().get(item.getId()).toString(),
                context.getStatements().get(ref.getId()).toString());

    }

    public static interface TestInterfaceClass {
        public int getValue(int v);
    }

    public static abstract class TestAbstractClass {

        private int a;
        private int b;

        public TestAbstractClass(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public abstract void init(int a, int b);

        public void work() {
            this.init(this.a, this.b);
        }
    }

}
