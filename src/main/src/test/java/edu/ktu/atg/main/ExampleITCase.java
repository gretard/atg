package edu.ktu.atg.main;

import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

import com.google.common.base.Objects;

import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.ExecutableStatement;
import edu.ktu.atg.common.models.MethodBranch;
import edu.ktu.atg.common.monitors.BranchesMonitor.BranchInfo;
import edu.ktu.atg.common.monitors.MultiMonitor;
import edu.ktu.atg.common.monitors.ValuesMonitor.HitStatement;
import edu.ktu.atg.instrumentor.SootInstrumenter;
import edu.ktu.atg.instrumentor.SootInstrumenter.SootInstrumenterRequest;
import edu.ktu.atg.instrumentor.SootInstrumenter.SootInstrumenterResponse;

public class ExampleITCase {

    @Test
    public void test() throws Throwable {
        String className = "edu.ktu.atg.example.Calculator";
        SootInstrumenterRequest request = new SootInstrumenterRequest();
        request.getClasses().add(className);
        request.outputFormat = "c";
        request.getSupportingPaths().add("./../example/target/classes");
        SootInstrumenter sut = new SootInstrumenter();
        SootInstrumenterResponse response = sut.instrument(request);
        try (URLClassLoader loader = new URLClassLoader(new URL[] { request.getTempDir().toURI().toURL() },
                this.getClass().getClassLoader())) {
            Class<?> c = loader.loadClass(className);
            Object x = c.newInstance();
            c.getMethod("divide", double.class).invoke(x, 0);
            c.getMethod("divide", double.class).invoke(x, 100);
        }
        SolutionExecutionData results = new SolutionExecutionData();
        MultiMonitor.INSTANCE.fill(results);

        ClasszInfo ci = response.getClasses().get(className);

        for (ExecutableStatement statement : ci.getStatements()) {
            boolean executed = false;
            for (HitStatement hitStatement : results.getStatements()) {
                if (Objects.equal(statement.getName(), hitStatement.getName())
                        && statement.getNo() == hitStatement.getNo()) {
                    executed = true;
                    break;
                }
            }
            System.out.println("Statement: " + statement.getName() + " " + statement.getExpression()
                    + " of type: " + statement.getType() + " executed: " + executed);
        }
        for (MethodBranch statement : ci.getAllbranches()) {
            boolean executed = false;
            for (BranchInfo hitStatement : results.getBranchesCalled()) {
                if (Objects.equal(statement.getName(), hitStatement.getName())
                        && statement.getNo() == hitStatement.getNo()) {
                    executed = true;
                    break;
                }
            }

            System.out.println(
                    "Branch: " + statement.getName() + " " + statement.getExpression() + " executed: " + executed);
        }

    }

}
