package edu.ktu.atg.common.execution;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class JavaObjectsProvider {

    public List<Method> getAllMethods(Class<?> classz) {
        List<Method> methods = new LinkedList<>();
        for (Method m : classz.getMethods()) {
            if (m.getDeclaringClass().getName().startsWith("java.lang")|| Modifier.isPrivate(m.getModifiers())) {
                continue;
            }
            methods.add(m);
        }
        for (Method m : classz.getDeclaredMethods()) {
            if (m.getDeclaringClass().getName().startsWith("java.lang") || Modifier.isPrivate(m.getModifiers())
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
            if (m.getDeclaringClass().getName().startsWith("java.lang") || Modifier.isPrivate(m.getModifiers())
                    || m.getReturnType().equals(void.class) || m.getReturnType().equals(Void.class)
                    || m.getParameterCount() > 0) {
                continue;
            }
            if (isStaticMethodCOnstructor(root, classz, m)) {
                continue;
            }
            methods.add(m);
        }
        return methods;
    }

    public List<Constructor<?>> getConstructors(Class<?> root, Class<?> classz) {
        List<Constructor<?>> constructors = new LinkedList<>();
        for (Constructor<?> c : classz.getConstructors()) {
            if (c.getDeclaringClass().getName().startsWith("java.lang") || Modifier.isPrivate(c.getModifiers())) {
                continue;
            }
            constructors.add(c);
        }
        return constructors;
    }

    public boolean isStaticMethodCOnstructor(Class<?> root, Class<?> classz, Method m) {
        if (Modifier.isPrivate(m.getModifiers()) || !Modifier.isStatic(m.getModifiers())
                || !classz.isAssignableFrom(m.getReturnType())) {
            return false;
        }
        return true;
    }

    public List<Method> getStaticMethodConstructors(Class<?> root, Class<?> classz) {
        List<Method> constructors = new LinkedList<>();
        for (Method c : classz.getMethods()) {
            if (isStaticMethodCOnstructor(root, classz, c)) {
                constructors.add(c);
            }
        }
        return constructors;
    }
    public List<Field> getStaticFieldConstructors(Class<?> root, Class<?> classz) {
        List<Field> constructors = new LinkedList<>();
        for (Field c : classz.getFields()) {
           if (Modifier.isStatic(c.getModifiers()) &&  classz.isAssignableFrom(c.getType())){
               constructors.add(c);
           }
        }
        return constructors;
    }
    public List<Object> getEnums(Class<?> root, Class<?> classz) {
        if (classz.isEnum()) {
            return Arrays.asList(classz.getEnumConstants());
        }
        return Collections.emptyList();
    }

    public List<Field> getObserverFields(Class<?> root, Class<?> classz) {
        List<Field> fields = new LinkedList<>();
        for (Field m : classz.getFields()) {
            if (m.getDeclaringClass().getName().startsWith("java.lang")|| Modifier.isPrivate(m.getModifiers())) {
                continue;
            }
            fields.add(m);
        }
        return fields;
    }

    public List<Field> getWriterFields(Class<?> root, Class<?> classz) {
        List<Field> fields = new LinkedList<>();
        for (Field m : classz.getFields()) {
            if (m.getDeclaringClass().getName().startsWith("java.lang")|| Modifier.isPrivate(m.getModifiers())
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
            if (m.getDeclaringClass().getName().startsWith("java.lang") || Modifier.isPrivate(m.getModifiers())) {
                continue;
            }
            if (isStaticMethodCOnstructor(root, classz, m)) {
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
