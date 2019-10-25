package edu.ktu.atg.common.goals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.models.MethodBranch;
import edu.ktu.atg.common.monitors.BranchesMonitor.BranchHit;
import edu.ktu.atg.common.monitors.BranchesMonitor.BranchInfo;

public class BranchHitGoal implements IGoal {

    private MethodBranch branchToHit;
    private DistanceCheckType type;
    private double distance;

    private final List<CandidateSolution> selected = new LinkedList<>();

    public BranchHitGoal(MethodBranch branchToHit, DistanceCheckType type) {
        this.branchToHit = branchToHit;
        this.type = type;
        this.distance = type.initialValue;

    }

    @Override
    public boolean evalute(CandidateSolution solution) {
        SolutionExecutionData data = solution.getData();
        List<BranchInfo> branchesToCheck = new LinkedList<>();
        List<BranchHit> hitBranches = new LinkedList<>();
        double d = distance;

        for (BranchInfo branch : data.getBranchesCalled()) {
            if (branch.getNo() == this.branchToHit.getNo() && Objects.equals(branch.getName(), branchToHit.getName())) {

                branchesToCheck.add(branch);
            }
        }

      
        for (BranchHit branch : data.getBranchesHit()) {
            if (branch.getNo() == this.branchToHit.getNo() && Objects.equals(branch.getName(), branchToHit.getName())) {
                hitBranches.add(branch);
            }
        }
        boolean found = false;
      //  System.out.println("0000SIZE: " + type + " " + distance + " " + hitBranches.size() + " " + branchesToCheck.size());
        for (BranchInfo bi : branchesToCheck) {
            if (type.matches(distance, bi.getDistance())) {
                found = true;
                d = type.returnBetter(d, bi.getDistance());
            }
        }
        if (!type.needsHit && found && hitBranches.isEmpty()) {
            this.distance = d;
            selected.clear();
            selected.add(solution);
            return true;
        }
        if (type.needsHit && found && !hitBranches.isEmpty()) {
            this.distance = d;
            selected.clear();
            selected.add(solution);
            return true;
        }

        return false;

    }

    @Override
    public Collection<CandidateSolution> getBestSolutions() {
        return selected;
    }

    @Override
    public boolean isMet() {
        return !this.selected.isEmpty();
    }
}
