package edu.ktu.atg.common.goals;

import java.util.Collection;

import edu.ktu.atg.common.execution.CandidateSolution;

public interface IGoal {
    public boolean evalute(CandidateSolution data);

    public Collection<CandidateSolution> getBestSolutions();

    public boolean isMet();
}
