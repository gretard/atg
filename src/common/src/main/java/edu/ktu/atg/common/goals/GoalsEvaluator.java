package edu.ktu.atg.common.goals;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.ktu.atg.common.execution.CandidateSolution;

public class GoalsEvaluator implements IGoal {

    private List<IGoal> secondary;
    private List<IGoal> primary;

    public GoalsEvaluator(List<IGoal> primary, List<IGoal> secondary) {
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public boolean evalute(CandidateSolution data) {
        boolean anyFound = false;
        for (IGoal goal : primary) {
            if (goal.evalute(data)) {
                anyFound = true;
            }
        }
        for (IGoal goal : secondary) {
            if (anyFound || goal.getBestSolutions().isEmpty()) {
                if (goal.evalute(data)) {
                    anyFound = true;
                }
            }
        }

        return anyFound;
    }

    @Override
    public Collection<CandidateSolution> getBestSolutions() {
        final Set<CandidateSolution> set = new HashSet<>();
        for (IGoal goal : primary) {
            set.addAll(goal.getBestSolutions());
        }
        for (IGoal goal : secondary) {
            set.addAll(goal.getBestSolutions());
        }
        return set;
    }
}
