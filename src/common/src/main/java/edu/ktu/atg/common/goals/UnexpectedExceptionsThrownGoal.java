package edu.ktu.atg.common.goals;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.SolutionExecutionData;
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

        for (Throwable e : data.getExceptionsThrown()) {
            int line = e.getStackTrace()[0].getLineNumber();
            String className = e.getStackTrace()[0].getClassName();
            boolean anyFound = false;

            for (ExecutableStatement st : statements) {
                if (st.getLine() == line && st.getName().contains(className)) {
                    anyFound = true;
                    break;
                }
            }
            if (!anyFound) {
                interesting.add(className + "@" + line);
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

}
