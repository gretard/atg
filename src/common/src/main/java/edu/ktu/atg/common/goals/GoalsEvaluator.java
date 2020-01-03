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
		final StringBuilder sb = new StringBuilder();
		for (final IGoal p : primary) {
			sb.append(String.format("%s - %s - %s%n", p.getClass().getSimpleName(), p.isMet(),
					p.getBestSolutions().size()));
		}
		for (final IGoal p : secondary) {
			sb.append(String.format("%s - %s - %s%n", p.getClass().getSimpleName(), p.isMet(),
					p.getBestSolutions().size()));
		}
		return sb.toString();
	}

	@Override
	public boolean evalute(final CandidateSolution data) {
		if (data == null) {
			return false;
		}
		boolean anyFound = false;
		for (final IGoal goal : primary) {
			if (goal.evalute(data)) {
				anyFound = true;
			}
		}
		for (final IGoal goal : secondary) {
			if (anyFound || !goal.isMet()) {
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
		for (final IGoal goal : primary) {
			set.addAll(goal.getBestSolutions());
		}
		for (final IGoal goal : secondary) {
			set.addAll(goal.getBestSolutions());
		}
		return set;
	}

	@Override
	public boolean isMet() {
		return false;
	}
}
