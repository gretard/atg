package edu.ktu.atg.instrumentor;

import java.net.MalformedURLException;

import org.junit.Test;

import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.ExecutableStatement;
import edu.ktu.atg.common.models.MethodBranch;
import edu.ktu.atg.common.models.MethodInfo;
import edu.ktu.atg.instrumentor.SootInstrumenter.SootInstrumenterRequest;
import edu.ktu.atg.instrumentor.SootInstrumenter.SootInstrumenterResponse;
import edu.ktu.atg.instrumentor.samples.InstructionsSample;

public class SootInstrumenterTest {

	@Test
	public void testInstrument()
			throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		String className = InstructionsSample.class.getName();
		SootInstrumenter sut = new SootInstrumenter();
		SootInstrumenterRequest request = new SootInstrumenterRequest();
		request.getClasses().add(className);
		request.outputFormat = "d";
		SootInstrumenterResponse response = sut.instrument(request);
		ClasszInfo ci = response.getClasses().get(className);
		for (MethodInfo mi : ci.getMethods().values()) {
			System.out.println(mi.getName());
			for (ExecutableStatement st : mi.getStatements()) {
				System.out.println(st.getName() + " " + st.getExpression() + " " + st.getType());
			}
			for (MethodBranch st : mi.getBranches()) {
				System.out.println(st.getExpression() + " " + st.getLeftType() + " " + st.getRightType());
			}
		}
		

	}

}
