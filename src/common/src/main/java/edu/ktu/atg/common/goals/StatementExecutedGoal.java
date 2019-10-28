package edu.ktu.atg.common.goals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.models.ExecutableStatement;
import edu.ktu.atg.common.monitors.ValuesMonitor.HitStatement;

public class StatementExecutedGoal implements IGoal {

    private final List<CandidateSolution> solutions = new LinkedList<>();
    private final ExecutableStatement statement;

    public StatementExecutedGoal(ExecutableStatement statement) {
        this.statement = statement;
    }

    @Override
    public boolean evalute(CandidateSolution solution) {
        SolutionExecutionData data = solution.getData();

        for (HitStatement hitStatement : data.getStatements()) {
            if (hitStatement == null) {
                continue;
            }
            if (hitStatement.getNo() == this.statement.getNo()
                    && Objects.equals(hitStatement.getName(), this.statement.getName())) {
                solutions.clear();
                solutions.add(solution);
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<CandidateSolution> getBestSolutions() {
        return solutions;
    }

    @Override
    public boolean isMet() {
        return this.solutions.size() >= 1;
    }

}
