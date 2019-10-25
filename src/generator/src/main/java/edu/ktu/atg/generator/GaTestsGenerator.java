package edu.ktu.atg.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
import edu.ktu.atg.generator.operators.MutateOperator;

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
        List<CandidateSolution> archive = new LinkedList<>();
        int itemsToSample = 10;
        System.out.println("Generating: "+info.getName());
        MutateOperator op = new MutateOperator();
        for (int i = 0; i < 3; i++) {
            execute(population);
            population.forEach(k -> {
                goal.evalute(k);
            });
            /*
             * List<CandidateSolution> temp = new LinkedList<>(goal.getBestSolutions()); if
             * (!temp.isEmpty()) { population = temp; }
             */
            population = op.mutate(population);

        }
        System.out.println("Done: "+info.getName());
        List<GenerationData> data = new ArrayList<>();
        GenerationData main = new GenerationData();
        main.info = info;
        data.add(main);
        goal.getBestSolutions().forEach(s -> {
            main.getSolutions().put(s.sequence, s.data);
        });
        return data;

    }

    private void execute(List<CandidateSolution> sequences) {
        for (CandidateSolution entry : sequences) {
            ExecutableSequence s = entry.getSequence();
            SolutionExecutionData trace = entry.getData();
            MultiMonitor.INSTANCE.clear();
            InstantiatingVisitor visitor = new InstantiatingVisitor(trace);
            try {
                IExecutable root = s.getRoot();
                visitor.execute(root, null);
                for (IExecutable item : s.getWriters()) {
                    visitor.execute(item, root);
                }
                for (IExecutable item : s.getObservers()) {
                    visitor.execute(item, root);
                }

            } catch (Throwable e) {
                trace.getExceptionsThrown().add(e);
             //   e.printStackTrace();
            } finally {
                MultiMonitor.INSTANCE.fill(trace);
            }

        }
    }
}
