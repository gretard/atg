package edu.ktu.atg.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import edu.ktu.atg.common.executables.ExecutableEnum;
import edu.ktu.atg.common.executables.ExecutableFieldObserver;
import edu.ktu.atg.common.executables.ExecutableFieldWriter;
import edu.ktu.atg.common.executables.ExecutableMethod;
import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.executables.IExecutableWithReturnValue;
import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.ClassesAnalyzer;
import edu.ktu.atg.common.execution.JavaObjectsProvider;
import edu.ktu.atg.common.execution.SolutionExecutionData;

public class SequencesProvider {

    public static class ClassContext {

        private final List<CandidateSolution> chromosomes = new LinkedList<>();

        public List<CandidateSolution> getChromosomes() {
            return chromosomes;
        }

        public void addSequence(ExecutableSequence sequence) {
            addSequence(sequence, new SolutionExecutionData());
        }

        public void addSequence(ExecutableSequence sequence, SolutionExecutionData data) {
            CandidateSolution c = new CandidateSolution();
            c.data = data;
            c.sequence = sequence;
            this.chromosomes.add(c);
        }
    }

    private final JavaObjectsProvider provider = new JavaObjectsProvider();

    public ClassContext getSequences(Class<?> classz) {
        int level = 3;
        final ClassesAnalyzer cna = new ClassesAnalyzer(level, 0);

        ClassContext ctx = new ClassContext();

        final List<IExecutableWithReturnValue> constructors = new LinkedList<>();

        final List<IExecutable> writers = new LinkedList<>();
        final List<IExecutable> staticWriters = new LinkedList<>();

        final List<IExecutable> observers = new LinkedList<>();
        final List<IExecutable> staticObservers = new LinkedList<>();

        for (Constructor<?> constructor : provider.getConstructors(classz, classz)) {
            constructors.add(cna.getConstructor(classz, constructor, level));
        }

        for (Method m : provider.getStaticMethodConstructors(classz, classz)) {
            constructors.add(cna.getMethod(classz, m, 0));
        }
        if (classz.isEnum()) {
            if (!provider.getEnums(classz, classz).isEmpty()) {
                constructors.add(new ExecutableEnum(classz));
            }
        }
        for (Method method : provider.getWriters(classz, classz)) {
            ExecutableMethod m = cna.getMethod(classz, method, level);
            if (m.isStatic()) {
                staticWriters.add(m);
            } else {
                writers.add(m);
            }

        }
        for (Field method : provider.getWriterFields(classz, classz)) {
            ExecutableFieldWriter m = cna.getWriterField(classz, method, level);
            if (m.isStatic()) {
                staticWriters.add(m);
            } else {
                writers.add(m);
            }
        }
        for (Method method : provider.getObservers(classz, classz)) {
            ExecutableMethod m = cna.getMethod(classz, method, level);
            if (m.isStatic()) {
                staticObservers.add(m);
            } else {
                observers.add(m);
            }
        }
        for (Field method : provider.getObserverFields(classz, classz)) {
            ExecutableFieldObserver m = cna.getField(classz, method, level);
            if (m.isStatic()) {
                staticObservers.add(m);
            } else {
                observers.add(m);
            }
        }

        for (IExecutableWithReturnValue c : constructors) {
            for (IExecutable w : writers) {
                ExecutableSequence s = new ExecutableSequence(classz, (IExecutableWithReturnValue) c.copy());
                s.getWriters().add(w.copy());
                s.getStaticWriters().addAll(staticWriters);
                s.getObservers().addAll(observers);
                s.getObservers().addAll(staticObservers);
                ctx.addSequence(s);
            }
        }

        for (IExecutableWithReturnValue c : constructors) {
            ExecutableSequence s = new ExecutableSequence(classz, (IExecutableWithReturnValue) c.copy());
            s.getStaticWriters().addAll(staticWriters);
            s.getObservers().addAll(observers);
            s.getObservers().addAll(staticObservers);
            ctx.addSequence(s);
        }

        for (IExecutable w : staticWriters) {
            ExecutableSequence s = new ExecutableSequence(classz, null);
            s.getWriters().add(w.copy());
            s.getObservers().addAll(staticObservers);
            ctx.addSequence(s);

        }

        return ctx;
    }
}
