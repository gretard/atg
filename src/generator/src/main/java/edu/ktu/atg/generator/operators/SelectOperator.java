package edu.ktu.atg.generator.operators;

import java.util.LinkedList;
import java.util.List;

import edu.ktu.atg.common.execution.CandidateSolution;

public class SelectOperator implements IOperator {

    private final List<CandidateSolution> archive = new LinkedList<>();
    private final int sampleAmount = 10;

    @Override
    public List<CandidateSolution> mutate(List<CandidateSolution> solutions) {

        return null;
    }

}
