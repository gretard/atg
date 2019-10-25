package edu.ktu.atg.common.execution;

import edu.ktu.atg.common.executables.ExecutableSequence;

public class CandidateSolution {
    public ExecutableSequence sequence;

    public ExecutableSequence getSequence() {
        return sequence;
    }

    public SolutionExecutionData getData() {
        return data;
    }

    public SolutionExecutionData data = new SolutionExecutionData();
}