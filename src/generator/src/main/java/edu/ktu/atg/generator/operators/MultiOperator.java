package edu.ktu.atg.generator.operators;

import java.util.LinkedList;
import java.util.List;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.goals.IGoal;

public class MultiOperator implements IOperator {

    public MultiOperator(IGoal goal) {

    }

    private final IOperator[] operators = new IOperator[] { new SelectOperator(), new MutateOperator(),
            new CrossoverOperator() };

    @Override
    public List<CandidateSolution> mutate(List<CandidateSolution> solutions) {
        List<CandidateSolution> newSolutions = new LinkedList<CandidateSolution>(solutions);
        for (IOperator op : operators) {
            newSolutions = op.mutate(newSolutions);
        }
        return newSolutions;
    }

}
