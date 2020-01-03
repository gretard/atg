package edu.ktu.atg.generator.operators;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import edu.ktu.atg.common.executables.ExecutableValue;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.executables.ValueType;
import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.models.DefinedValue;

public class ValuesVisitorTest {

    @Test
    public void testGetDataForString() throws Throwable {
        IExecutable item = new ExecutableValue(String.class, ValueType.STRING);
        CandidateSolution sol = new CandidateSolution();
        sol.data.defineValue(DefinedValue.createFixed(item, "aaaa"));
        ValuesVisitor visitor = new ValuesVisitor(sol);
        visitor.innerExecute(null, item);
        List<CandidateSolution> sols = visitor.getSolutions();
        Assert.assertEquals(12, sols.size());
    }
    
    @Test
    public void testGetDataForInt() throws Throwable {
        IExecutable item = new ExecutableValue(int.class, ValueType.INT);
        CandidateSolution sol = new CandidateSolution();
        sol.data.defineValue(DefinedValue.createFixed(item, "12"));
        ValuesVisitor visitor = new ValuesVisitor(sol);
        visitor.innerExecute(null, item);
        List<CandidateSolution> sols = visitor.getSolutions();
        for (CandidateSolution solution : sols) {
            System.out.println(solution.getData().getDefinedValue(item).getValue());
        }
        Assert.assertEquals(11, sols.size());
    }

}
