package edu.ktu.atg.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.GenerationData;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.goals.GoalsProvider;
import edu.ktu.atg.common.goals.IGoal;
import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.monitors.MultiMonitor;
import edu.ktu.atg.generator.SequencesProvider.ClassContext;
import edu.ktu.atg.generator.ga.Evaluator;
import edu.ktu.atg.generator.ga.Executor;
import edu.ktu.atg.generator.ga.Generator;
import edu.ktu.atg.generator.ga.Solutions;

public class GaTestsGenerator {

    private final SequencesProvider sequencesProvider = new SequencesProvider();
    private final GoalsProvider provider = new GoalsProvider();

    public List<GenerationData> generate(ClasszInfo info, Object context) throws Throwable {

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String classzName = info.getName();
        Class<?> mainClass = loader.loadClass(classzName);
        ClassContext classContext = sequencesProvider.getSequences(mainClass);
        IGoal goal = provider.getGoals(info);
        List<CandidateSolution> population = classContext.getChromosomes();
        System.out.println("Staring " + info.getName() + " " + population.size());
        int timeout = 5;
        int delta = 10;
        Solutions data = new Solutions(timeout);
        data.solutionsToCheck.addAll(population);
        ExecutorService ex = Executors.newWorkStealingPool();
        ex.submit(Generator.create(data));
        ex.submit(Executor.create(data, loader));
        ex.submit(Evaluator.create(goal, data));
        Thread.sleep(5000);
        ex.shutdown();
        ex.awaitTermination(timeout + delta, TimeUnit.SECONDS);
        System.out.println(data.evaluatedSolutions.size());
        Collection<CandidateSolution> best = goal.getBestSolutions();
        System.out.println("Done " + best.size());
        List<GenerationData> response = new ArrayList<>();
        GenerationData main = new GenerationData();
        main.info = info;
        response.add(main);
        main.getSolutions().addAll(best);

        return response;

    }

}
