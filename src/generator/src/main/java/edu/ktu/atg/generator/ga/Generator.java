package edu.ktu.atg.generator.ga;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.goals.IGoal;
import edu.ktu.atg.generator.ga.stopping.IStoppingFuntion;
import edu.ktu.atg.generator.operators.CrossoverOperator;
import edu.ktu.atg.generator.operators.ValuesVisitor;

public class Generator {
    private final SolutionsContext data;
    private final IStoppingFuntion stoppingFuntion;
    private final IGoal goal;

    public Generator(IGoal goal, IStoppingFuntion stoppingFuntion, SolutionsContext data) {
        this.goal = goal;
        this.stoppingFuntion = stoppingFuntion;
        this.data = data;
    }

    public static Runnable create(IGoal goal, IStoppingFuntion stoppingFuntion, SolutionsContext data) {
        return new Runnable() {

            @Override
            public void run() {
                Generator e = new Generator(goal, stoppingFuntion, data);
                try {
                    e.start();
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }
        };
    }

    private long sampledArchive = 0;
    private long sampledBest = 0;
    private long total = 0;
    private long mutations = 0;
    private long crossover = 0;
    private int sample = 10;
    private int maxSize = 1000;

    public void start() throws Throwable {

        while (!stoppingFuntion.shouldStop()) {
            while (data.solutionsToCheck.size() > maxSize) {
                Thread.sleep(10);
            }
            try {
                total++;
                double p = ThreadLocalRandom.current().nextDouble();
                List<CandidateSolution> pop = new LinkedList<>();
                if (p > 0.8) {
                    // update candidates selected by archive
                    List<CandidateSolution> best = new LinkedList<CandidateSolution>(data.archive);
                    Collections.shuffle(best);
                    for (int i = 0; i < Math.min(sample, best.size()); i++) {
                        pop.add(best.get(i));
                    }
                    sampledArchive++;
                } else {
                    // update candidates selected by goals
                    List<CandidateSolution> best = new LinkedList<CandidateSolution>(goal.getBestSolutions());
                    Collections.shuffle(best);
                    for (int i = 0; i < Math.min(sample, best.size()); i++) {
                        pop.add(best.get(i));
                    }
                    sampledBest++;
                }
                double p2 = ThreadLocalRandom.current().nextDouble();

                if (p2 > 0.8) {
                    // crossover
                    List<CandidateSolution> sols = new CrossoverOperator(pop.toArray(new CandidateSolution[0])).invoke()
                            .getSolutions();
                    sols.forEach(s -> {
                        data.solutionsToCheck.add(s);
                    });
                    crossover++;
                } else {
                    // mutate
                    pop.forEach(solution -> {
                        List<CandidateSolution> results = new ValuesVisitor(solution).invoke().getSolutions();
                        results.forEach(s -> {
                            data.solutionsToCheck.add(s);
                        });
                    });
                    mutations++;
                }
                Thread.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Genreated: cross:" + crossover + " mut: " + mutations + " ar: " + sampledArchive + " best: "
                + sampledBest + " total: " + total);

    }
}
