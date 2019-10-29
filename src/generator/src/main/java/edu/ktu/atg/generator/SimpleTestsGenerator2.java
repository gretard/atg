package edu.ktu.atg.generator;

import java.util.ArrayList;
import java.util.List;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.GenerationData;
import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.OptionsRequest;
import edu.ktu.atg.generator.SequencesProvider.ClassContext;
import edu.ktu.atg.generator.ga.Executor;
import edu.ktu.atg.generator.ga.SolutionsContext;
import edu.ktu.atg.generator.ga.stopping.AnySolutionExistsForCheckingStoppingFuntion;

public class SimpleTestsGenerator2 implements IGenerator {

    private final SequencesProvider sequencesProvider = new SequencesProvider();

    public List<GenerationData> generate(ClasszInfo info, OptionsRequest context) throws Throwable {
        List<GenerationData> data = new ArrayList<>();
        GenerationData main = new GenerationData();
        main.info = info;
        data.add(main);

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String classzName = info.getName();
        Class<?> mainClass = loader.loadClass(classzName);
        ClassContext classContext = sequencesProvider.getSequences(mainClass);
        
        List<CandidateSolution> solutions = classContext.getChromosomes();
       
        
        SolutionsContext solutionContext = new SolutionsContext();
        solutionContext.solutionsToCheck.addAll(solutions);
        Executor executor = new Executor(solutionContext, loader,
                new AnySolutionExistsForCheckingStoppingFuntion(solutionContext), context.getTimeoutInner());
        executor.start();
        main.getSolutions().addAll(solutionContext.executedSolutions);
        return data;

    }
}
