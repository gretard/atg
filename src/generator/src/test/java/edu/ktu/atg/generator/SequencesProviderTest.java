package edu.ktu.atg.generator;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.ClassesAnalyzer;
import edu.ktu.atg.generator.SequencesProvider.ClassContext;

public class SequencesProviderTest {

    @Test
    public void testGetSequences() {
        SequencesProvider sut = new SequencesProvider();
        ClassContext context = sut.getSequences(TestEnum.class);
        System.out.println(context.getChromosomes().size());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(context));
        //ClassesAnalyzer cna = new ClassesAnalyzer(5,2);
        //IExecutable e = cna.create(TestEnum.class, TestEnum.class, 5);
    }
    @Test
    public void testGetSequences2() {
        SequencesProvider sut = new SequencesProvider();
        ClassContext context = sut.getSequences(Test2.class);
        System.out.println(context.getChromosomes().size()); 
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(context));
        for (CandidateSolution sol : context.getChromosomes()) {
            System.out.println(sol.getSequence().getRoot());
        }
    }

    public static enum TestEnum {
        ONE;
    }

    public static class Test0 {
        public int getValueo() {
            return 1;
        }

        public void writeValueo(int x) {

        }
    }

    public static class Test1 {
        public int getValue1() {
            return 1;
        }

        public void writeValue1(int x, Test0 test0) {

        }
    }

    public static class Test2 {
        public void work(Test1 t) {

        }
    }

}
