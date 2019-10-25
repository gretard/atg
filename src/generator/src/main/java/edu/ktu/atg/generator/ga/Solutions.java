package edu.ktu.atg.generator.ga;

import java.util.LinkedList;
import java.util.Queue;

import edu.ktu.atg.common.execution.CandidateSolution;

public class Solutions {

    private final long started = System.currentTimeMillis();

    private final long timeout;

    public Solutions(int timeout) {
        this.timeout = timeout * 1000;
    }

    public final Queue<CandidateSolution> solutionsToCheck = new LinkedList<>();

    public final Queue<CandidateSolution> executedSolutions = new LinkedList<>();

    public final Queue<CandidateSolution> evaluatedSolutions = new LinkedList<>();

    public boolean shouldStop() {
        return (System.currentTimeMillis() - started) > this.timeout;
    }
}
