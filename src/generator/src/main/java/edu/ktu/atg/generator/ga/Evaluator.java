package edu.ktu.atg.generator.ga;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.goals.IGoal;

public class Evaluator {
    private final Solutions data;
    private final IGoal goal;
    private long count = 0;

    public Evaluator(IGoal goal, Solutions data) {
        this.goal = goal;
        this.data = data;
    }

    public static Runnable create(IGoal goal, Solutions data) {
        return new Runnable() {

            @Override
            public void run() {
                Evaluator e = new Evaluator(goal, data);
                try {
                    e.start();
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }
        };
    }

    private final LinkedList<CandidateSolution> archive = new LinkedList<>();
    private final int maxArchiveSize = 100;
    private final int sampleSize = 10;
    private long noop = 0;

    public void start() throws Throwable {
        System.out.println("EV STARTED....");

        while (!data.shouldStop()) {
            Thread.sleep(1);
            while (!data.executedSolutions.isEmpty() && !data.shouldStop()) {
                final CandidateSolution solution = data.executedSolutions.poll();
                if (solution == null) {
                    continue;
                }
                if (archive.isEmpty() || archive.size() < maxArchiveSize) {
                    archive.add(solution);
                }
                goal.evalute(solution);
                data.evaluatedSolutions.add(solution);
                count++;

                if (ThreadLocalRandom.current().nextDouble() > 0.98 && !archive.isEmpty()) {
                    Collections.shuffle(archive);
                    data.evaluatedSolutions.add(archive.poll());
                }
                if (ThreadLocalRandom.current().nextDouble() > 0.8) {
                    Collection<CandidateSolution> sols = goal.getBestSolutions();
                    for (CandidateSolution s : sols) {
                        if (!s.data.getExceptionsThrown().isEmpty()) {
                            continue;
                        }
                        data.evaluatedSolutions.add(s);
                    }
                }
            }
            noop++;
        }
        System.out.println("Left: " + data.executedSolutions.size());
        while (!data.executedSolutions.isEmpty()) {
            final CandidateSolution solution = data.executedSolutions.poll();
            goal.evalute(solution);
            count++;
        }
        System.out.println("Evaluated: " + count + " " + noop);
    }
}
