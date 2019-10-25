package edu.ktu.atg.generator.ga;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.generator.operators.ValuesVisitor;

public class Generator {
    private final Solutions data;

    public Generator(Solutions data) {
        this.data = data;
    }

    public static Runnable create(Solutions data) {
        return new Runnable() {

            @Override
            public void run() {
                Generator e = new Generator(data);
                try {
                    e.start();
                } catch (Throwable e1) {
                    e1.printStackTrace();
                }
            }
        };
    }

    private long count = 0;
    private long noop = 0;
    private long total = 0;

    public void start() throws Throwable {
        System.out.println("GA STARTED....");
        while (!data.shouldStop()) {
            Thread.sleep(1);
            while (!data.evaluatedSolutions.isEmpty() && !data.shouldStop()) {
                final CandidateSolution solution = data.evaluatedSolutions.poll();
                if (solution == null) {
                    continue;
                }
                List<CandidateSolution> results = new ValuesVisitor(solution).work().getData();
                total += results.size();
                results.forEach(s -> {
                    data.solutionsToCheck.add(s);
                });
//                data.solutionsToCheck.addAll(results);
                count++;
            }
            noop++;

        }
        System.out.println("Genreated: " + count + " " + noop + " " + total);

    }
}
