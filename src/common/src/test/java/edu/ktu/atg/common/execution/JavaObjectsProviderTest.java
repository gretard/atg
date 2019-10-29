package edu.ktu.atg.common.execution;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class JavaObjectsProviderTest {

    @Test
    public void testGetStaticMethodConstructors() {
        JavaObjectsProvider sut = new JavaObjectsProvider();
        Class classz = TestItem.class;
        List<Method> methods = sut.getStaticMethodConstructors(classz, classz);
        Assert.assertEquals(2, methods.size());
    }

    public static class Temp extends TestItem {

    }

    public static class TestItem {
        private static TestItem getItem() {
            return null;
        }

        public static Object getInstance2() {
            return null;
        }

        public static Temp getInstance3() {
            return null;
        }

        public static TestItem getInstance() {
            return null;
        }
    }

}
