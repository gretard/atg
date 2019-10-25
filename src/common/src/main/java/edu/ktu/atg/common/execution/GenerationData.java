package edu.ktu.atg.common.execution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.models.ClasszInfo;

public class GenerationData {

    public ClasszInfo info;

    private final List<CandidateSolution> solutions = new LinkedList<>();

    public List<CandidateSolution> getSolutions() {
        return solutions;
    }

    public ClasszInfo getInfo() {
        return info;
    }

}
