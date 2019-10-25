package edu.ktu.atg.generator.ga;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.ThrownException;
import edu.ktu.atg.common.monitors.MultiMonitor;
import edu.ktu.atg.generator.InstantiatingVisitor;

public class Executor {

    private Solutions data;
    private long count = 0;
    private ClassLoader loader;

    public Executor(Solutions data, ClassLoader loader) {
        this.data = data;
        this.loader = loader;
    }

    public static Runnable create(Solutions data, ClassLoader loader) {
        return new Runnable() {

            @Override
            public void run() {
                Executor e = new Executor(data, loader);
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

    public void start() throws Throwable {
        System.out.println("EX STARTED....");
        while (!data.shouldStop()) {
            Thread.sleep(1);
            while (!data.solutionsToCheck.isEmpty() && !data.shouldStop()) {
                final CandidateSolution solution = data.solutionsToCheck.poll();
                if (solution == null) {
                    continue;
                }
                Future<?> future = service.submit(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            execute(solution);
                            MultiMonitor.INSTANCE.fill(solution.data);
                            data.executedSolutions.add(solution);
                            count++;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                try {
                        //long s =                     System.currentTimeMillis();
                    Object o = future.get(5, TimeUnit.SECONDS);
//                    System.out.println("Took "+(System.currentTimeMillis() - s));
                  //  if (o == null) {
                      
                        total++;
                    //}
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            noop++;

        }

        service.shutdown();
        service.awaitTermination(3, TimeUnit.SECONDS);
        System.out.println("Executed: " + count + " " + noop+" "+total);
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
            e.printStackTrace();
          //  e.fillInStackTrace();

            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            StackTraceElement[] els = cause.getStackTrace();
            if (els != null && els.length > 0) {
                int line = cause.getStackTrace()[0].getLineNumber();
                String classz = cause.getStackTrace()[0].getClassName();
                trace.getExceptionsThrown().add(new ThrownException(classz, line));
            }
            if (trace.getExceptionsThrown().isEmpty()) {
                e.printStackTrace();
                trace.getExceptionsThrown().add(new ThrownException(s.getClassName(), -1));
            }

        }
    }
}
