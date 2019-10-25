package edu.ktu.atg.generator.operators;

import java.util.LinkedList;
import java.util.List;

import edu.ktu.atg.common.execution.CandidateSolution;

public class MutateOperator implements IOperator {

    @Override
    public List<CandidateSolution> mutate(List<CandidateSolution> solutions) {

        List<CandidateSolution> newSolutions = new LinkedList<>();
        solutions.forEach(sol -> {
            newSolutions.addAll(new ValuesVisitor(sol).work().getData());
        });
        return newSolutions;

    }

}
