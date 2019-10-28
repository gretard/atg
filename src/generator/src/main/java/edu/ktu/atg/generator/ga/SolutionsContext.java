package edu.ktu.atg.generator.ga;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import edu.ktu.atg.common.execution.CandidateSolution;

public class SolutionsContext {

    public final int maxArchiveSize = 100;
    public final Queue<CandidateSolution> solutionsToCheck = new LinkedList<>();

    public final Queue<CandidateSolution> executedSolutions = new LinkedList<>();

    public final Queue<CandidateSolution> evaluatedSolutions = new LinkedList<>();

    public final ConcurrentLinkedDeque<CandidateSolution> archive = new ConcurrentLinkedDeque<>();

    public volatile long archiveUsed = 0;

    public volatile long crossover = 0;

    public volatile long mutations = 0;

    public volatile long generations = 0;

    public volatile long evaluations = 0;

}
