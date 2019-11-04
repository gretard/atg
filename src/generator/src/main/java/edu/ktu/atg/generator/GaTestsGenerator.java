package edu.ktu.atg.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.GenerationData;
import edu.ktu.atg.common.goals.GoalsEvaluator;
import edu.ktu.atg.common.goals.GoalsProvider;
import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.OptionsRequest;
import edu.ktu.atg.generator.SequencesProvider.ClassContext;
import edu.ktu.atg.generator.ga.Evaluator;
import edu.ktu.atg.generator.ga.Executor;
import edu.ktu.atg.generator.ga.Generator;
import edu.ktu.atg.generator.ga.SolutionsContext;
import edu.ktu.atg.generator.ga.stopping.TimeBasedStoppingFuntion;

public class GaTestsGenerator implements IGenerator {

    private static final Logger LOGGER = Logger.getLogger(GaTestsGenerator.class.getName());

    private final SequencesProvider sequencesProvider = new SequencesProvider();
    private final GoalsProvider provider = new GoalsProvider();

    public List<GenerationData> generate(ClasszInfo info, OptionsRequest context) throws Throwable {

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final String classzName = info.getName();
        final Class<?> mainClass = loader.loadClass(classzName);
        final ClassContext classContext = sequencesProvider.getSequences(mainClass);
        final GoalsEvaluator goal = provider.getGoals(info);

        final List<CandidateSolution> population = classContext.getChromosomes();

        final SolutionsContext data = new SolutionsContext();
        data.solutionsToCheck.addAll(population);

        final TimeBasedStoppingFuntion stoppingFuntion = new TimeBasedStoppingFuntion(context.getTimeoutGlobal());
        final ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(Executor.create(data, loader, stoppingFuntion, context.getTimeoutInner()));
        executorService.submit(Evaluator.create(goal, stoppingFuntion, data));
        executorService.submit(Generator.create(goal, stoppingFuntion, data));

        Thread.sleep(1);
        executorService.shutdown();
        executorService.awaitTermination(context.getTimeoutGlobal() + context.getTimeoutDelta(), TimeUnit.SECONDS);
        executorService.shutdownNow();

        final Collection<CandidateSolution> best = goal.getBestSolutions();
        List<GenerationData> response = new ArrayList<>();
        GenerationData main = new GenerationData();
        main.info = info;
        main.getSolutions().addAll(best);
        response.add(main);
        LOGGER.info(String.format(
                "Finished generating for %s classz, with %s goals, initial pop size: %s, selected %s tests", classzName,
                goal.size(), population.size(), best.size()
        ));
        LOGGER.info(() -> goal.getGoalsInfo());

        return response;

    }

}
