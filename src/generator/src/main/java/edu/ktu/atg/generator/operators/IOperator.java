package edu.ktu.atg.generator.operators;

import java.util.List;

import edu.ktu.atg.common.execution.CandidateSolution;

public interface IOperator {
    public IOperator work();
    public List<CandidateSolution> getData();
}
