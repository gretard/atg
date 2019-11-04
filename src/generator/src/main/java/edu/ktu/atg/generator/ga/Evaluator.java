package edu.ktu.atg.generator.ga;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.goals.IGoal;
import edu.ktu.atg.generator.ga.stopping.IStoppingFuntion;

public class Evaluator {
    private final SolutionsContext data;
    private final IGoal goal;
    private final IStoppingFuntion stoppingFuntion;

    public Evaluator(IGoal goal, IStoppingFuntion stoppingFuntion, SolutionsContext data) {
        this.goal = goal;
        this.stoppingFuntion = stoppingFuntion;
        this.data = data;
    }

    public static Runnable create(IGoal goal, IStoppingFuntion stoppingFuntion, SolutionsContext data) {
        return new Runnable() {

            @Override
            public void run() {
                Evaluator e = new Evaluator(goal, stoppingFuntion, data);
                try {
                    e.start();
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }
        };
    }

    public void start() throws Throwable {

        while (!stoppingFuntion.shouldStop()) {
            while (!data.executedSolutions.isEmpty() && !stoppingFuntion.shouldStop()) {
                final CandidateSolution solution = data.executedSolutions.poll();
                if (solution == null) {
                    continue;
                }
                goal.evalute(solution);
            }
            Thread.sleep(5);
        }
        while (!data.executedSolutions.isEmpty()) {
            final CandidateSolution solution = data.executedSolutions.poll();
            if (solution == null) {
                continue;
            }
            goal.evalute(solution);
        }
    }
}
