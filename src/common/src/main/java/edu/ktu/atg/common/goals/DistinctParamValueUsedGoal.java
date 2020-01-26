package edu.ktu.atg.common.goals;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.models.ParamInfo;
import edu.ktu.atg.common.monitors.ValuesMonitor.HitStatement;

public class DistinctParamValueUsedGoal implements IGoal {

	private final Map<String, CandidateSolution> solutions = new HashMap<>();
	private final ParamInfo param;
	private int counter = 0;

	public DistinctParamValueUsedGoal(final ParamInfo param) {
		this.param = param;
	}

	@Override
	public boolean evalute(final CandidateSolution solution) {
		boolean solutionFound = false;
		final SolutionExecutionData data = solution.getData();
		final Set<String> valuesToCheck = new HashSet<>();

		for (final HitStatement hitStatement : data.getPreValues()) {
			if (hitStatement == null) {
				continue;
			}
			if (hitStatement.getUniqueNo() == this.param.getNo()
					&& Objects.equals(hitStatement.getName(), this.param.getMethodName())) {
				valuesToCheck.add(hitStatement.valueStringRepresentation);
			}
		}

		for (final String key : valuesToCheck) {
			if (!solutions.containsKey(key)) {
				this.solutions.putIfAbsent(key, solution);
				solutionFound = true;
			}
		}

		if (solutionFound) {
			counter += 1;
		}
		return solutionFound;
	}

	@Override
	public Collection<CandidateSolution> getBestSolutions() {
		return solutions.values();
	}

	@Override
	public boolean isMet() {
		return counter >= 2;
	}

}
