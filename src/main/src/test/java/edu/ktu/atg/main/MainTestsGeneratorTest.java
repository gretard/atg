package edu.ktu.atg.main;

import org.junit.Test;

import edu.ktu.atg.common.models.OptionsRequest;

public class MainTestsGeneratorTest {

    MainTestsGenerator sut = new MainTestsGenerator();

    @Test
    public void testGenerate() throws Throwable {
        OptionsRequest request = new OptionsRequest();
    //    request.setClasses(new String[] { "edu.ktu.atg.example.StaticCalculator"});
        request.setClassesDir(new String[] { "../example/target/classes" });
        request.setResultsDir("../example/src/test/java");
        request.setUseTimestampedReports(false);
        request.setMode(1);
        request.setDebug(true);
        sut.generate(request);
    }

}
