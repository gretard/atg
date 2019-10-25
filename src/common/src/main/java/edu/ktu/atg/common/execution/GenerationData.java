package edu.ktu.atg.common.execution;

import java.util.HashMap;
import java.util.Map;

import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.models.ClasszInfo;

public class GenerationData {

    public ClasszInfo info;

    private final Map<ExecutableSequence, SolutionExecutionData> solutions = new HashMap<>();

    public Map<ExecutableSequence, SolutionExecutionData> getSolutions() {
        return solutions;
    }

    public ClasszInfo getInfo() {
        return info;
    }

}
