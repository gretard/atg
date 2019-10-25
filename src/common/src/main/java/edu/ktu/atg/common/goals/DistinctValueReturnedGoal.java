package edu.ktu.atg.common.goals;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.models.ExecutableStatement;
import edu.ktu.atg.common.monitors.BranchesMonitor;
import edu.ktu.atg.common.monitors.ValuesMonitor.HitStatement;

public class DistinctValueReturnedGoal implements IGoal {

    private final ExecutableStatement statement;
    private DistanceCheckType type;
    private int minNoOfSolutions = 3;
    private final Map<String, CandidateSolution> values = new HashMap<>();

    public DistinctValueReturnedGoal(DistanceCheckType type, ExecutableStatement statement) {
        this.type = type;
        this.statement = statement;

    }

    @Override
    public boolean evalute(CandidateSolution solution) {
        SolutionExecutionData data = solution.getData();

      
        List<HitStatement> statementsToCheck = new LinkedList<>();
        for (HitStatement hitStatement : data.getStatementWithValues()) {
            if (hitStatement.getNo() == this.statement.getNo()
                    && Objects.equals(hitStatement.getName(), this.statement.getName())) {
                statementsToCheck.add(hitStatement);
            }
        }
        if (statementsToCheck.isEmpty()) {
            return false;
        }
        boolean anyFound = false;
        for (HitStatement statement : statementsToCheck) {
            String v2 = statement.getValue() == null ? statement.getValueStringRepresentation()
                    : statement.getValue().toString();
            if (values.containsKey(v2)) {
                continue;
            }
            values.put(v2, solution);
            anyFound = true;
        }

        return anyFound;
    }

    @Override
    public Collection<CandidateSolution> getBestSolutions() {
        return values.values();
    }

    @Override
    public boolean isMet() {
        return this.values.size() > this.minNoOfSolutions;
    }

}
