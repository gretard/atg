package edu.ktu.atg.generator.operators;

import java.util.List;

import edu.ktu.atg.common.execution.CandidateSolution;

public interface IOperator {
    public List<CandidateSolution> mutate(List<CandidateSolution> solutions);
}
