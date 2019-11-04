package edu.ktu.atg.common.execution;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class JavaObjectsProvider {

    public boolean isObjectOrPrivate(Class<?> rootClass, Member member) {
        return member.getDeclaringClass().equals(Object.class) 
                || Modifier.isPrivate(member.getDeclaringClass().getModifiers())
                //|| (member.getDeclaringClass().getName().contains())
                || Modifier.isPrivate(member.getModifiers());
    }

    public List<Method> getAllMethods(Class<?> classz) {
        Set<Method> methods = new HashSet<>();
        for (Method m : classz.getMethods()) {
            if (isObjectOrPrivate(classz, m)) {
                continue;
            }
            methods.add(m);
        }
        for (Method m : classz.getDeclaredMethods()) {
            if (isObjectOrPrivate(classz, m)) {
                continue;
            }
            methods.add(m);
        }
        return new LinkedList<Method>(methods);
    }

    public List<Method> getObservers(Class<?> root, Class<?> classz) {
        Set<Method> methods = new HashSet<>();
        for (Method m : getAllMethods(classz)) {
            if (isObjectOrPrivate(root, m)) {
                continue;
            }
            if (m.getReturnType().equals(void.class) || m.getReturnType().equals(Void.class)
                    || m.getParameterCount() > 0) {
                continue;
            }
            if (isStaticMethodCOnstructor(root, classz, m)) {
                continue;
            }
            methods.add(m);
        }
        return new LinkedList<Method>(methods);
    }

    public List<Constructor<?>> getConstructors(Class<?> root, Class<?> classz) {
        List<Constructor<?>> constructors = new LinkedList<>();
        for (Constructor<?> m : classz.getConstructors()) {
            if (isObjectOrPrivate(root, m)) {
                continue;
            }
            constructors.add(m);
        }
        return constructors;
    }

    public boolean isStaticMethodCOnstructor(Class<?> root, Class<?> classz, Method m) {
        if (isObjectOrPrivate(root, m) || !classz.isAssignableFrom(m.getReturnType()) || !Modifier.isStatic(m.getModifiers())) {
            return false;
        }
        return true;
    }

    public List<Method> getStaticMethodConstructors(Class<?> root, Class<?> classz) {
        List<Method> constructors = new LinkedList<>();
        for (Method m : classz.getMethods()) {
            if (isStaticMethodCOnstructor(root, classz, m)) {
                constructors.add(m);
            }
        }
        return constructors; 
    }

    public List<Field> getStaticFieldConstructors(Class<?> root, Class<?> classz) {
        List<Field> constructors = new LinkedList<>();
        for (Field m : classz.getFields()) {
            if (isObjectOrPrivate(root, m)) {
                continue;
            }
            if (Modifier.isStatic(m.getModifiers()) && classz.isAssignableFrom(m.getType())) {
                constructors.add(m);
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
            if (isObjectOrPrivate(root, m)) {
                continue;
            }

            fields.add(m);
        }
        return fields;
    }

    public List<Field> getWriterFields(Class<?> root, Class<?> classz) {
        List<Field> fields = new LinkedList<>();
        for (Field m : classz.getFields()) {
            if (isObjectOrPrivate(root, m) || Modifier.isFinal(m.getModifiers())) {
                continue;
            }
            fields.add(m);
        }
        return fields;
    }

    public List<Method> getWriters(Class<?> root, Class<?> classz) {
        List<Method> methods = new LinkedList<>();
        for (Method m : getAllMethods(classz)) {
            if (isObjectOrPrivate(root, m)) {
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
