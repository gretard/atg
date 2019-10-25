package edu.ktu.atg.common.goals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.models.ExecutableStatement;
import edu.ktu.atg.common.monitors.BranchesMonitor;
import edu.ktu.atg.common.monitors.ValuesMonitor.HitStatement;

public class DistinctValueReturnedGoal implements IGoal {

    private final List<CandidateSolution> solutions = new LinkedList<>();
    private final ExecutableStatement statement;
    private DistanceCheckType type;
    private Object value = 0;
    private double distance;

    public DistinctValueReturnedGoal(DistanceCheckType type, ExecutableStatement statement) {
        this.type = type;
        this.statement = statement;
        this.distance = type.initialValue;

    }

    @Override
    public boolean evalute(CandidateSolution solution) {
        SolutionExecutionData data = solution.getData();

        Object firstNonNullValue = null;
        Object firstNonNullStringValue = null;
        List<HitStatement> statementsToCheck = new LinkedList<>();
        for (HitStatement hitStatement : data.getStatementWithValues()) {
            if (hitStatement.getNo() == this.statement.getNo()
                    && Objects.equals(hitStatement.getName(), this.statement.getName())) {
                statementsToCheck.add(hitStatement);
                if (firstNonNullStringValue == null && hitStatement.getValueStringRepresentation() != null) {
                    firstNonNullStringValue = hitStatement.getValueStringRepresentation();
                }
                if (firstNonNullValue == null && hitStatement.getValue() != null) {
                    firstNonNullValue = hitStatement.getValue();
                }
            }
        }
        if (statementsToCheck.isEmpty()) {
            return false;
        }

        if (value == null && firstNonNullValue != null) {
            value = firstNonNullValue;
        }
        if (value == null && firstNonNullStringValue != null) {
            value = firstNonNullStringValue;
        }
        boolean anyFound = false;
        for (HitStatement statement : statementsToCheck) {
            Object v2 = statement.getValue() == null ? statement.getValueStringRepresentation() : statement.getValue();
            double d = BranchesMonitor.calculateDistance(value, v2);

            if (type.matches(distance, d)) {
                anyFound = true;
                value = v2;
                distance = type.returnBetter(distance, d);
            }
        }
        if (anyFound) {
            solutions.clear();
            solutions.add(solution);
        }
        return anyFound;
    }

    @Override
    public Collection<CandidateSolution> getBestSolutions() {
        return solutions;
    }

}
