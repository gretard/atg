package edu.ktu.atg.generator.ga;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.ThrownException;
import edu.ktu.atg.common.monitors.MultiMonitor;
import edu.ktu.atg.generator.ga.stopping.IStoppingFuntion;
import edu.ktu.atg.generator.visitors.InstantiatingVisitor;

public class Executor {

    private SolutionsContext data;
    private ClassLoader loader;
    private int timeout;
    private IStoppingFuntion stoppingFuntion;

    public Executor(SolutionsContext data, ClassLoader loader, IStoppingFuntion stoppingFuntion, int timeout) {
        this.data = data;
        this.loader = loader;
        this.stoppingFuntion = stoppingFuntion;
        this.timeout = timeout;
    }

    public static Runnable create(SolutionsContext data, ClassLoader loader, IStoppingFuntion stoppingFuntion,
            int timeout) {
        return new Runnable() {

            @Override
            public void run() {
                Executor e = new Executor(data, loader, stoppingFuntion, timeout);
                try {
                    e.start();
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }
        };
    }

    private final ExecutorService service = Executors.newSingleThreadExecutor();
    private long noop = 0;

    private long total = 0;

    private long count = 0;

    public void start() throws Throwable {
        System.out.println("EX STARTED....");

        while (!stoppingFuntion.shouldStop()) {
            while (!data.solutionsToCheck.isEmpty() && !stoppingFuntion.shouldStop()) {
                final CandidateSolution solution = data.solutionsToCheck.poll();
                if (solution == null) {
                    continue;
                }
                count++;
                updateArchive(solution);
                submit(solution);
            }
            noop++;
            Thread.sleep(20);
        }
        while (!data.solutionsToCheck.isEmpty()) {
            final CandidateSolution solution = data.solutionsToCheck.poll();
            if (solution == null) {
                break;
            }
            submit(solution);
        }

        close();
        System.out.println("EX finished: " + count + " " + total + " archive size: " + data.archive.size());
    }

    private void close() throws InterruptedException {
        service.shutdown();
        service.awaitTermination(timeout, TimeUnit.SECONDS);
        service.shutdownNow();
    }

    private void updateArchive(final CandidateSolution solution) {
        if (data.archive.size() < data.maxArchiveSize) {
            data.archive.add(solution);
        }

        if (data.archive.size() >= data.maxArchiveSize && ThreadLocalRandom.current().nextDouble() > 0.95) {
            LinkedList<CandidateSolution> temp = new LinkedList<CandidateSolution>(data.archive);
            Collections.shuffle(temp);
            data.archive.clear();
            temp.pop();
            temp.add(solution);
            data.archive.addAll(temp);
        }
    }

    private void submit(final CandidateSolution solution) {
        if (solution == null) {
            return;
        }
        Future<?> future = service.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    execute(solution);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        try {
            future.get(timeout, TimeUnit.SECONDS);
            if (future.isDone()) {
                MultiMonitor.INSTANCE.fill(solution.data);
                data.executedSolutions.add(solution);
                total++;
            }
        } catch (TimeoutException e) {
            future.cancel(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private final void execute(final CandidateSolution solution) {
        final ExecutableSequence s = solution.getSequence();
        final SolutionExecutionData trace = solution.getData();
        MultiMonitor.INSTANCE.clear();
        final InstantiatingVisitor visitor = new InstantiatingVisitor(trace, loader);
        try {
            final IExecutable root = s.getRoot();
            visitor.execute(root, null);
            for (final IExecutable item : s.getWriters()) {
                visitor.execute(item, root);
            }
            Thread.sleep(1);
            for (final IExecutable item : s.getObservers()) {
                visitor.execute(item, root);
            }
        }

        catch (final Throwable e) {
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            StackTraceElement[] els = cause.getStackTrace();
            if (els != null && els.length > 0) {
                int line = cause.getStackTrace()[0].getLineNumber();
                String classz = cause.getStackTrace()[0].getClassName();
                trace.getExceptionsThrown().add(new ThrownException(classz, cause.getClass().getName(), line));
            }
            if (trace.getExceptionsThrown().isEmpty()) {
                trace.getExceptionsThrown().add(new ThrownException(s.getClassName(), cause.getClass().getName(), -1));
            }

        }
    }
}
