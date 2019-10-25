package edu.ktu.atg.main;

import org.junit.Test;

public class MainTestsGeneratorTest {

    MainTestsGenerator sut = new MainTestsGenerator();

    @Test
    public void testGenerate() throws Throwable {
        OptionsRequest request = new OptionsRequest();
        request.setClassesDir(new String[] { "../example/target/classes" });
        request.setResultsDir("../example/src/test/java");
        request.setUseTimestampedReports(false);
        sut.generate(request);
    }

}
