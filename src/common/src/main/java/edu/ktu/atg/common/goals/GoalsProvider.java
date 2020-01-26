package edu.ktu.atg.common.goals;

import java.util.LinkedList;
import java.util.List;

import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.ExecutableStatement;
import edu.ktu.atg.common.models.ExecutableStatement.StatementTypes;
import edu.ktu.atg.common.models.MethodBranch;
import edu.ktu.atg.common.models.ParamInfo;

public class GoalsProvider {
	public GoalsEvaluator getGoals(ClasszInfo ci) {
		List<IGoal> primary = new LinkedList<IGoal>();
		List<IGoal> secondary = new LinkedList<IGoal>();
		for (MethodBranch branchToHit : ci.getAllbranches()) {
			// TODO check if numeric or boolean
			primary.add(new BranchHitGoal(branchToHit, DistanceCheckType.HITMAX));
			primary.add(new BranchHitGoal(branchToHit, DistanceCheckType.HITMIN));
			primary.add(new BranchHitGoal(branchToHit, DistanceCheckType.UNHITMAX));
			primary.add(new BranchHitGoal(branchToHit, DistanceCheckType.UNHITMIN));
		}
		List<ExecutableStatement> throwsStatements = new LinkedList<>();
		for (ExecutableStatement statement : ci.getStatements()) {
			if (statement.getType() == StatementTypes.THROWS) {
				throwsStatements.add(statement);
			}
			if (statement.getType() == StatementTypes.RETURN) {
				secondary.add(new DistinctValueReturnedGoal(DistanceCheckType.HITMAX, statement));
				continue;
			}

			secondary.add(new StatementExecutedGoal(statement));
		}
		for (ParamInfo statement : ci.getParams()) {
			secondary.add(new DistinctParamValueUsedGoal(statement));
			if (!statement.isComplex() || String.class.getName().equalsIgnoreCase(statement.getType())) {
				continue;
			}

			secondary.add(new ParamValueChangedGoal(statement));

		}

		primary.add(new UnexpectedExceptionsThrownGoal(throwsStatements.toArray(new ExecutableStatement[0])));

		return new GoalsEvaluator(primary, secondary);
	}
}
