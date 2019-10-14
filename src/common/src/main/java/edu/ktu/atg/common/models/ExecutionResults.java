package edu.ktu.atg.common.models;

import java.util.LinkedList;
import java.util.List;

import edu.ktu.atg.common.monitors.BranchesMonitor.BranchHit;
import edu.ktu.atg.common.monitors.BranchesMonitor.BranchInfo;
import edu.ktu.atg.common.monitors.ValuesMonitor.HitStatement;

public class ExecutionResults {
    private final List<BranchInfo> branchesCalled = new LinkedList<BranchInfo>();

    private final List<BranchHit> branchesHit = new LinkedList<BranchHit>();

    public List<BranchInfo> getBranchesCalled() {
        return branchesCalled;
    }

    public List<BranchHit> getBranchesHit() {
        return branchesHit;
    }

    public List<HitStatement> getStatements() {
        return statements;
    }

    public List<HitStatement> getStatementWithValues() {
        return statementWithValues;
    }

    private final List<HitStatement> statements = new LinkedList<HitStatement>();
    private final List<HitStatement> statementWithValues = new LinkedList<HitStatement>();
}
