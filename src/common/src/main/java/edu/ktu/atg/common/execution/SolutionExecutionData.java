package edu.ktu.atg.common.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.ktu.atg.common.execution.models.DefinedValue;
import edu.ktu.atg.common.execution.models.ExecutablePair;
import edu.ktu.atg.common.execution.models.ResultValue;
import edu.ktu.atg.common.monitors.BranchesMonitor.BranchHit;
import edu.ktu.atg.common.monitors.BranchesMonitor.BranchInfo;
import edu.ktu.atg.common.monitors.ValuesMonitor.HitStatement;

public class SolutionExecutionData {

    public SolutionExecutionData copy() {
        SolutionExecutionData trace = new SolutionExecutionData();
        trace.definedValues.putAll(this.definedValues);
        return trace;
    }

    public SolutionExecutionData deepCopy() {
        SolutionExecutionData trace = new SolutionExecutionData();
        trace.definedValues.putAll(this.definedValues);
        trace.executedPairs.addAll(this.executedPairs);
        trace.exceptionsThrown.addAll(this.exceptionsThrown);
        return trace;
    }

    private final List<ExecutablePair> executedPairs = new ArrayList<>();

    public List<ExecutablePair> getExecutedPairs() {
        return executedPairs;
    }

    public SolutionExecutionData defineValue(DefinedValue value) {
        this.definedValues.put(value.getItem().getId(), value);
        return this;
    }

    public Map<String, DefinedValue> getDefinedValues() {
        return definedValues;
    }

    private final Map<String, List<ResultValue>> results = new HashMap<>();

    public Map<String, List<ResultValue>> getResults() {
        return results;
    }

    private final Map<String, DefinedValue> definedValues = new HashMap<>();

    private final List<BranchInfo> branchesCalled = new LinkedList<>();

    private final List<BranchHit> branchesHit = new LinkedList<>();

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

    private final List<Throwable> exceptionsThrown = new LinkedList<>();
    public List<Throwable> getExceptionsThrown() {
        return exceptionsThrown;
    }

    private final List<HitStatement> statements = new LinkedList<>();
    private final List<HitStatement> statementWithValues = new LinkedList<>();
}
