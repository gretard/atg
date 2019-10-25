package edu.ktu.atg.generator;

import java.util.ArrayList;
import java.util.List;

import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.GenerationData;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.monitors.MultiMonitor;
import edu.ktu.atg.generator.SequencesProvider.ClassContext;

public class SimpleTestsGenerator2 {

    private final SequencesProvider sequencesProvider = new SequencesProvider();

    public List<GenerationData> generate(ClasszInfo info, Object context) throws Throwable {
        List<GenerationData> data = new ArrayList<>();
        GenerationData main = new GenerationData();
        main.info = info;
        data.add(main);

        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String classzName = info.getName();
        Class<?> mainClass = loader.loadClass(classzName);
        ClassContext classContext = sequencesProvider.getSequences(mainClass);
        List<CandidateSolution> solutions = classContext.getChromosomes();
        for (CandidateSolution entry : solutions) {
            ExecutableSequence s = entry.getSequence();
            SolutionExecutionData trace = entry.getData();
            MultiMonitor.INSTANCE.clear();
            InstantiatingVisitor visitor = new InstantiatingVisitor(trace, loader);
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
              //  trace.getExceptionsThrown().add(e);
                e.printStackTrace();
            } finally {
                MultiMonitor.INSTANCE.fill(trace);
                main.getSolutions().add(entry);
            }

        }
        return data;

    }
}
