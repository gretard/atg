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
                if (type.matches(this.distance, branch.distance)) {
                    branchesToCheck.add(branch);
                    d = type.returnBetter(this.distance, branch.distance);
                }
            }
        }

        for (BranchHit branch : data.getBranchesHit()) {
            if (branch.getNo() == this.branchToHit.getNo() && Objects.equals(branch.getName(), branchToHit.getName())) {
                hitBranches.add(branch);
                break;
            }
        }

        if (!type.needsHit && !branchesToCheck.isEmpty() && hitBranches.isEmpty()) {
            this.distance = d;
            selected.clear();
            selected.add(solution);
            return true;
        }
        if (type.needsHit && !branchesToCheck.isEmpty() && !hitBranches.isEmpty()) {
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
}
