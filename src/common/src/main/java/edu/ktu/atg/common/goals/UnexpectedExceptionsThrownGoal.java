package edu.ktu.atg.common.goals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.ThrownException;
import edu.ktu.atg.common.models.ExecutableStatement;

public class UnexpectedExceptionsThrownGoal implements IGoal {

    private final List<CandidateSolution> solutions = new LinkedList<>();
    private ExecutableStatement[] statements;
    private final List<String> covered = new LinkedList<>();

    public UnexpectedExceptionsThrownGoal(ExecutableStatement... statements) {
        this.statements = statements;
    }

    @Override
    public boolean evalute(CandidateSolution solution) {

        SolutionExecutionData data = solution.getData();
        if (data.getExceptionsThrown().isEmpty()) {
            return false;
        }
        List<String> interesting = new LinkedList<>();

        for (ThrownException e : data.getExceptionsThrown()) {

            boolean anyFound = false;

            for (ExecutableStatement st : statements) {
                if (st.getLine() == e.getLine() && st.getName().contains(e.getMethod())) {
                    anyFound = true;
                    break;
                }
            }
            if (!anyFound) {
                interesting.add(e.toString());
            }
        }
        boolean anyAdded = false;
        for (String inString : interesting) {
            if (!covered.contains(inString)) {
                covered.add(inString);
                anyAdded = true;
            }
        }
        if (anyAdded) {
            solutions.add(solution);
        }

        return anyAdded;
    }

    @Override
    public Collection<CandidateSolution> getBestSolutions() {
        return solutions;
    }

    @Override
    public boolean isMet() {
        return false;
    }

}
