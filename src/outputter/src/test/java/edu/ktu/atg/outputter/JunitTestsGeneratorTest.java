package edu.ktu.atg.outputter;

import org.junit.Assert;
import org.junit.Test;

import edu.ktu.atg.common.executables.ExecutableConstructor;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.execution.ClassesAnalyzer;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.DefinedValue;
import edu.ktu.atg.common.execution.models.ExecutablePair;

public class JunitTestsGeneratorTest {
    JunitTestsGenerator sut = new JunitTestsGenerator();
    ClassesAnalyzer cna = new ClassesAnalyzer();

    @Test
    public void testWork() {
        Class<?> classz = TestMain.class;

        ExecutableConstructor item = (ExecutableConstructor) cna.create(classz, classz, 3);

        SolutionExecutionData data = new SolutionExecutionData();
        for (IExecutable p : item.getParameters()) {
            data.getExecutedPairs().add(new ExecutablePair(item, p, null));
            data.defineValue(DefinedValue.createFixed(p, 1));
        }
        data.defineValue(DefinedValue.createExecutable(item));
        data.getExecutedPairs().add(new ExecutablePair(null, item, item.getReturnValue()));
        IExecutable in = item.getParameters()[0];
        data.getExecutedPairs().add(new ExecutablePair(null, in, null));

        OutputContext context = sut.work(data);
        System.out.println("FINAL");
        context.getStatements().forEach((k, v) -> {
            System.out.println(k + " " + v + " " + context.canBeInlined(item));
        });
        System.out.println("FINA2L");
        context.getFinalStatements().forEach(x -> {
            System.out.println(x);
        });
        Assert.assertEquals(2, context.getFinalStatements().size());
    }

    public static class TestMain {
        public TestMain(int a, int b) {

        }
    }
}
