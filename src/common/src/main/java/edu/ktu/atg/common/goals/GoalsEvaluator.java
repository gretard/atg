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

    public int size() {
        return primary.size() + secondary.size();
    }

    public String getGoalsInfo() {
        StringBuilder sb = new StringBuilder();
        for (IGoal p : primary) {
            sb.append(String.format("%s - %s -%s%n", p.getClass().getSimpleName(), p.isMet(),
                    p.getBestSolutions().size()));
        }
        for (IGoal p : secondary) {
            sb.append(String.format("%s - %s -%s%n", p.getClass().getSimpleName(), p.isMet(),
                    p.getBestSolutions().size()));
        }
        return sb.toString();
    }

    @Override
    public boolean evalute(CandidateSolution data) {
        if (data == null) {
            return false;
        }
        boolean anyFound = false;
        for (IGoal goal : primary) {
            boolean r = goal.evalute(data);
            // System.out.println(data+" "+goal.getClass().getSimpleName()+" ss
            // selected"+r);
            if (r) {

                anyFound = true;
            }
        }
        for (IGoal goal : secondary) {
            if (anyFound || !goal.isMet()) {
                boolean r = goal.evalute(data);

                if (r) {

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

    @Override
    public boolean isMet() {
        // TODO Auto-generated method stub
        return false;
    }
}
