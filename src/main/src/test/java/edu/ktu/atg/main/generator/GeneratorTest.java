package edu.ktu.atg.main.generator;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.OptionsRequest;
import edu.ktu.atg.generator.GaTestsGenerator;

public class GeneratorTest {

    @Test
    public void testGenerate() {
       GaTestsGenerator sut = new GaTestsGenerator();
       ClasszInfo info = new ClasszInfo(TestClasses.Test1.class.getName());
       try {
        sut.generate(info, new OptionsRequest());
    } catch (Throwable e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }

}
