package edu.ktu.atg.common.execution;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ktu.atg.common.executables.ExecutableConstructor;
import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.executables.IExecutable;

public class ClassesAnalyzerTest {

    @Test
    public void testCreate() {
        ClassesAnalyzer sut = new ClassesAnalyzer();
        Class classz = CharSequence.class;
        IExecutable item = sut.create(classz, classz, 3);
        System.out.println(item.getClass() + " " + item.getClassz());
    }

    @Test
    public void testCreate2() {
        ClassesAnalyzer sut = new ClassesAnalyzer();
        Class classz = Test2.class;
        IExecutable item = sut.create(classz, classz, 3);
        ExecutableConstructor c = (ExecutableConstructor) item;
        System.out.println(item.getClass() + " " + item.getClassz());
    }
    @Test
    public void testCreate3() {
        ClassesAnalyzer sut = new ClassesAnalyzer(3,1);
        Class classz = Test2.class;
        IExecutable item = sut.create(classz, classz, 3);
        ExecutableSequence c = (ExecutableSequence) item;
        System.out.println(item.getClass() + " " + item.getClassz()+" "+c.getObservers().size()+" "+c.getWriters().size());
        for (IExecutable e : c.getWriters()) {
            System.out.println(e.getClass()+" "+e.getClassName());
        }

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
