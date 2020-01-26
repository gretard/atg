package edu.ktu.atg.common.goals;

import org.junit.Assert;
import org.junit.Test;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.models.ParamInfo;
import edu.ktu.atg.common.monitors.ValuesMonitor.HitStatement;

public class DistinctParamValueUsedGoalTest {

	@Test
	public void testEvalute() {
		ParamInfo param = new ParamInfo("test", "a", "String", 2, true);
		DistinctParamValueUsedGoal sut = new DistinctParamValueUsedGoal(param);
		CandidateSolution solution = new CandidateSolution();
		HitStatement statement = new HitStatement();
		statement.valueStringRepresentation = "aaa";
		statement.no = 1;
		statement.uniqueNo = 2;
		statement.name = "test";

		solution.getData().getPreValues().add(statement);
		solution.getData().getPreValues().add(null);

		Assert.assertTrue(sut.evalute(solution));
		Assert.assertFalse(sut.evalute(solution));
	}

}
