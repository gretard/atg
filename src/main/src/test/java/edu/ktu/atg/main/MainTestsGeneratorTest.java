package edu.ktu.atg.main;

import org.junit.Test;

public class MainTestsGeneratorTest {

    MainTestsGenerator sut = new MainTestsGenerator();

    @Test
    public void testGenerate() throws Throwable {
        OptionsRequest request = new OptionsRequest();
        request.setClasses(new String[] { "edu.ktu.atg.example.SampleWithInterface"});
        request.setClassesDir(new String[] { "../example/target/classes" });
        request.setResultsDir("../example/src/test/java");
        request.setUseTimestampedReports(false);
        request.setDebug(true);
        sut.generate(request);
    }

}
