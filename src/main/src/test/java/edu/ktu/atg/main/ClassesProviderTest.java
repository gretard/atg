package edu.ktu.atg.main;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ClassesProviderTest {

    @Test
    public void testGetClasses() {
        ClassesProvider sut = new ClassesProvider();
        OptionsRequest request = new OptionsRequest();
        request.setClassesDir(new String[] { "../example/target/classes" });
        request.setResultsDir("../example/src/test/java");
        request.setUseTimestampedReports(false);
        List<String> classes = sut.getClasses(request);
        System.out.println(classes);
    }

}
