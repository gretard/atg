package edu.ktu.atg.common.goals;

import org.junit.Assert;
import org.junit.Test;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.models.ParamInfo;
import edu.ktu.atg.common.monitors.ValuesMonitor.HitStatement;

public class ParamValueChangedGoalTest {

	@Test
	public void testEvaluteIsMet() {

		ParamInfo param = new ParamInfo("test", "a", "String", 2, true);
		ParamValueChangedGoal sut = new ParamValueChangedGoal(param);
		CandidateSolution solution = new CandidateSolution();
		HitStatement statement = new HitStatement();
		statement.valueStringRepresentation = "aaa";
		statement.no = 1;
		statement.uniqueNo = 2;
		statement.name = "test";

		HitStatement statement2 = new HitStatement();
		statement2.valueStringRepresentation = "aaa3";
		statement2.no = 1;
		statement2.uniqueNo = 2;
		statement2.name = "test";
		solution.getData().getPreValues().add(statement);
		solution.getData().getPreValues().add(null);
		solution.getData().getPostValues().add(statement2);
		solution.getData().getPostValues().add(null);
		Assert.assertTrue(sut.evalute(solution));
	}

	@Test
	public void testEvaluteIsNotMet() {

		ParamInfo param = new ParamInfo("test", "a2", "String", 1, true);
		ParamValueChangedGoal sut = new ParamValueChangedGoal(param);
		CandidateSolution solution = new CandidateSolution();
		HitStatement statement = new HitStatement();
		statement.valueStringRepresentation = "aaa";
		statement.no = 1;
		statement.name = "test";

		HitStatement statement2 = new HitStatement();
		statement2.valueStringRepresentation = "aaa3";
		statement2.no = 1;
		statement2.name = "test";
		solution.getData().getPreValues().add(statement);
		solution.getData().getPostValues().add(statement2);

		Assert.assertFalse(sut.evalute(solution));
	}

}
