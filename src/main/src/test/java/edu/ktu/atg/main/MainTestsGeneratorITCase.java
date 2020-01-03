package edu.ktu.atg.main;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.Test;

import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.OptionsRequest;
import edu.ktu.atg.generator.GaTestsGenerator;
import edu.ktu.atg.generator.SequencesProvider;
import edu.ktu.atg.generator.SequencesProvider.ClassContext;
import edu.ktu.atg.main.generator.TestClasses;

public class MainTestsGeneratorITCase {

	MainTestsGenerator sut = new MainTestsGenerator();

	@Test
	public void testGenerate() throws Throwable {
		OptionsRequest request = new OptionsRequest();
		// request.setClasses(new String[] { "edu.ktu.atg.example.Calculator" });
		request.setClassesDir(new String[] { "../example/target/classes" });
		request.setResultsDir("../example/src/test/java");
		request.setUseTimestampedReports(false);
		request.setMode(1);
		request.setDebug(false);
		sut.generate(request);
	}

	@Test
	public void testGenerateTest1Classz() {
		GaTestsGenerator sut = new GaTestsGenerator();
		ClasszInfo info = new ClasszInfo(TestClasses.Test1.class.getName());
		try {
			sut.generate(info, new OptionsRequest());
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void test1() {
		SequencesProvider sut = new SequencesProvider();
		ClassContext ctx = sut.getSequences(SampleWithComplexStructures.class);
		System.out.println(ctx.getChromosomes());
	}

	public static class SampleWithComplexStructures {

		public PrintWriter execute(java.lang.CharSequence sequence, Test0 test0) throws FileNotFoundException {
			return new PrintWriter("test");
		}

	}

	public static class Test0 {
		int c = 0;

		public int getValue() {
			return c;
		}

		public void write() {
			c++;
		}
	}

}
