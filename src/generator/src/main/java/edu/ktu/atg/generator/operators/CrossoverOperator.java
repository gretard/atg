package edu.ktu.atg.generator.operators;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.execution.CandidateSolution;

public class CrossoverOperator implements IOperator {

    private CandidateSolution[] candidateSolutions;

    public CrossoverOperator(CandidateSolution... candidateSolutions) {
        this.candidateSolutions = candidateSolutions;

    }

    List<CandidateSolution> newSolutions = new LinkedList<>();

    @Override
    public List<CandidateSolution> getData() {
        return newSolutions;

    }

    @Override
    public CrossoverOperator work() {
        for (int i = 0; i < candidateSolutions.length; i++) {
            CandidateSolution f = candidateSolutions[ThreadLocalRandom.current().nextInt(candidateSolutions.length)];
            CandidateSolution s = candidateSolutions[ThreadLocalRandom.current().nextInt(candidateSolutions.length)];
            if (f == null || s == null) {
                continue;
            }
            // remove one writer
            if (!f.sequence.getWriters().isEmpty()) {
                CandidateSolution n = new CandidateSolution();
                n.sequence = f.sequence.copy();
                n.sequence.getWriters().remove(0);
                newSolutions.add(n);
            }
            // add one writer
            if (!s.sequence.getWriters().isEmpty()) {
                CandidateSolution n = new CandidateSolution();
                n.sequence = f.sequence.copy();
                n.sequence.getWriters().add(s.getSequence().getWriters().get(0).copy());
                newSolutions.add(n);
            }
            // add all from another 
            if (!f.sequence.getWriters().isEmpty()) {
                CandidateSolution n = new CandidateSolution();
                n.sequence = s.sequence.copy();
                for (IExecutable e : f.getSequence().getWriters()) {
                    n.sequence.getWriters().add(e.copy());
                }
                newSolutions.add(n);
            }
            // add one writer
            if (!s.sequence.getWriters().isEmpty()) {
                CandidateSolution n = new CandidateSolution();
                n.sequence = f.sequence.copy();
                for (IExecutable e : s.getSequence().getWriters()) {
                    n.sequence.getWriters().add(e.copy());
                }
                newSolutions.add(n);
            }

        }
        return this;
    }

}
