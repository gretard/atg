package edu.ktu.atg.common.goals;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.models.MethodBranch;
import edu.ktu.atg.common.monitors.BranchesMonitor.BranchHit;
import edu.ktu.atg.common.monitors.BranchesMonitor.BranchInfo;

public class BranchUnhitMinTest {
    MethodBranch branchToHit = new MethodBranch("test", null, 1, null, null);

    DistanceCheckType type = DistanceCheckType.UNHITMIN;

    @Test
    public void testEvalute() {
        BranchHitGoal goal = new BranchHitGoal(branchToHit, type);
        CandidateSolution solution = new CandidateSolution();
        Assert.assertFalse(goal.evalute(solution));
    }

    @Test
    public void testEvaluteGivenNotHit() {
        BranchHitGoal goal = new BranchHitGoal(branchToHit, type);
        CandidateSolution solution = new CandidateSolution();
        BranchInfo bi = new BranchInfo();
        bi.distance = 10;
        bi.name = "test";
        bi.no = 1;
        solution.getData().getBranchesCalled().add(bi);
        Assert.assertTrue(goal.evalute(solution));

    }

    @Test
    public void testEvaluteGivenHit() {
        BranchHitGoal goal = new BranchHitGoal(branchToHit, type);
        CandidateSolution solution = new CandidateSolution();
        solution.getData().getBranchesCalled().add(new BranchInfo("test", 1, 10));
        solution.getData().getBranchesHit().add(new BranchHit("test", 1));
        Assert.assertFalse(goal.evalute(solution));
    }

    @Test
    public void testEvaluteGivenNoHit() {
        BranchHitGoal goal = new BranchHitGoal(branchToHit, type);
        CandidateSolution solution = new CandidateSolution();
        solution.getData().getBranchesCalled().add(new BranchInfo("test", 1, 10));
        Assert.assertTrue(goal.evalute(solution));
        solution.getData().getBranchesCalled().add(new BranchInfo("test", 1, 12));
        Assert.assertFalse(goal.evalute(solution));
        solution.getData().getBranchesCalled().add(new BranchInfo("test", 1, 5));
        Assert.assertTrue(goal.evalute(solution));

    }
}
