package edu.ktu.atg.common.execution;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class JavaObjectsProvider {

    public List<Method> getAllMethods(Class<?> classz) {
        List<Method> methods = new LinkedList<>();
        for (Method m : classz.getMethods()) {
            if (m.getDeclaringClass().equals(Object.class) || Modifier.isPrivate(m.getModifiers())) {
                continue;
            }
            methods.add(m);
        }
        for (Method m : classz.getDeclaredMethods()) {
            if (m.getDeclaringClass().equals(Object.class) || Modifier.isPrivate(m.getModifiers())
                    || methods.contains(m)) {
                continue;
            }
            methods.add(m);
        }
        return methods;
    }

    public List<Method> getObservers(Class<?> root, Class<?> classz) {
        List<Method> methods = new LinkedList<>();
        for (Method m : getAllMethods(classz)) {
            if (m.getDeclaringClass().equals(Object.class) || Modifier.isPrivate(m.getModifiers())
                    || m.getReturnType().equals(void.class) || m.getReturnType().equals(Void.class)
                    || m.getParameterCount() > 0) {
                continue;
            }
            methods.add(m);
        }
        return methods;
    }

    public List<Constructor<?>> getConstructors(Class<?> root, Class<?> classz) {
        List<Constructor<?>> constructors = new LinkedList<>();
        for (Constructor<?> c : classz.getConstructors()) {
            if (c.getDeclaringClass().equals(Object.class) || Modifier.isPrivate(c.getModifiers())) {
                continue;
            }
            constructors.add(c);
        }
        return constructors;
    }

    public List<Field> getObserverFields(Class<?> root, Class<?> classz) {
        List<Field> fields = new LinkedList<>();
        for (Field m : classz.getFields()) {
            if (m.getDeclaringClass().equals(Object.class) || Modifier.isPrivate(m.getModifiers())) {
                continue;
            }
            fields.add(m);
        }
        return fields;
    }

    public List<Field> getWriterFields(Class<?> root, Class<?> classz) {
        List<Field> fields = new LinkedList<>();
        for (Field m : classz.getFields()) {
            if (m.getDeclaringClass().equals(Object.class) || Modifier.isPrivate(m.getModifiers())
                    || Modifier.isFinal(m.getModifiers())) {
                continue;
            }
            fields.add(m);
        }
        return fields;
    }

    public List<Method> getWriters(Class<?> root, Class<?> classz) {
        List<Method> methods = new LinkedList<>();
        for (Method m : getAllMethods(classz)) {
            if (m.getDeclaringClass().equals(Object.class) || Modifier.isPrivate(m.getModifiers())) {
                continue;
            }
            if (m.getReturnType().equals(void.class) || m.getReturnType().equals(Void.class)
                    || m.getParameterCount() > 0) {
                methods.add(m);
            }
        }
        return methods;
    }
}
