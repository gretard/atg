package edu.ktu.atg.common.execution;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import edu.ktu.atg.common.executables.ExecutableAbstractClassz;
import edu.ktu.atg.common.executables.ExecutableAbstractMethod;
import edu.ktu.atg.common.executables.ExecutableArray;
import edu.ktu.atg.common.executables.ExecutableConstructor;
import edu.ktu.atg.common.executables.ExecutableFieldObserver;
import edu.ktu.atg.common.executables.ExecutableFieldWriter;
import edu.ktu.atg.common.executables.ExecutableInterface;
import edu.ktu.atg.common.executables.ExecutableMethod;
import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.executables.ExecutableValue;
import edu.ktu.atg.common.executables.ExecutableVoid;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.executables.IExecutableWithReturnValue;
import edu.ktu.atg.common.executables.ValueType;

public class ClassesAnalyzer {

    private final JavaObjectsProvider javaObjectsProvider = new JavaObjectsProvider();
    private final int maxLevel;

    private final int deltaToTake;

    public ClassesAnalyzer() {
        this(3, 1);
    }

    public ClassesAnalyzer(int maxLevel, int delta) {
        this.maxLevel = maxLevel;
        this.deltaToTake = delta;
    }

    public IExecutable create(Class<?> rootClassz, Class<?> classz, int level) {
        if (classz == null) {
            throw new IllegalArgumentException("Could not analyze class null for");
        }

        if (classz == void.class || classz == Void.class) {
            return new ExecutableVoid();
        }

        if (classz == int.class || classz == Integer.class) {
            return new ExecutableValue(classz, ValueType.INT);
        }
        if (classz == byte.class || classz == Byte.class) {
            return new ExecutableValue(classz, ValueType.BYTE);
        }

        if (classz == float.class || classz == Float.class) {
            return new ExecutableValue(classz, ValueType.FLOAT);
        }
        if (classz == short.class || classz == Short.class) {
            return new ExecutableValue(classz, ValueType.SHORT);
        }
        if (classz == double.class || classz == Double.class) {
            return new ExecutableValue(classz, ValueType.DOUBLE);
        }
        if (classz == long.class || classz == Long.class) {
            return new ExecutableValue(classz, ValueType.LONG);
        }
        if (classz == String.class) {
            return new ExecutableValue(classz, ValueType.STRING);
        }
        if (classz == char.class || classz == Character.class) {
            return new ExecutableValue(classz, ValueType.CHAR);
        }
        if (classz == boolean.class || classz == Boolean.class) {
            return new ExecutableValue(classz, ValueType.BOOLEAN);
        }
        if (classz.isEnum()) {
            return new ExecutableValue(classz, ValueType.ENUM);
        }

        if (level <= 0) {
            return new ExecutableValue(classz, ValueType.COMPLEX);
        }

        if (classz.isArray()) {
            int dim = 1;
            Class<?> root = classz.getComponentType();
            while (root.getComponentType() != null) {
                dim++;
                root = root.getComponentType();
            }
            final IExecutable componentType = create(rootClassz, root, level - 1);
            return new ExecutableArray(classz, dim, componentType);
        }

        if (classz.isInterface()) {
            List<ExecutableAbstractMethod> methodsToImplement = javaObjectsProvider.getAllMethods(classz).stream()
                    .map(method -> getAbstractMethod(classz, method, level)).collect(Collectors.toList());

            return new ExecutableInterface(classz, ExecutableValue.complexType(classz),
                    methodsToImplement.toArray(new ExecutableAbstractMethod[0]));
        }

        final List<Constructor<?>> constructors = javaObjectsProvider.getConstructors(rootClassz, classz);
        if (constructors.isEmpty()) {
            if (classz.equals(rootClassz)) {
                throw new IllegalArgumentException(
                        "Can't instantiate classz " + classz + " as no constructors were found...");
            }
            return new ExecutableValue(classz, ValueType.NULL);
        }
        final Constructor<?> constructor = constructors.get(0);

        if (Modifier.isAbstract(classz.getModifiers())) {
            List<ExecutableAbstractMethod> methodsToImplement = javaObjectsProvider.getAllMethods(classz).stream()
                    .filter(method -> Modifier.isAbstract(method.getModifiers()))
                    .map(method -> getAbstractMethod(classz, method, level)).collect(Collectors.toList());
            ExecutableConstructor constructorData = getConstructor(rootClassz, constructor, level - 1);
            return new ExecutableAbstractClassz(constructorData.getConstructor(), constructorData.getParameters(),
                    ExecutableValue.complexType(classz), methodsToImplement.toArray(new ExecutableAbstractMethod[0]));
        }

        return getConstructor(rootClassz, constructor, level - 1);

    }

    public IExecutableWithReturnValue getExecutableConstructor(final Class<?> rootClassz, final Class<?> classz,
            final IExecutableWithReturnValue root, int level) {

        int delta = maxLevel - level;
        if (delta > 0 && (maxLevel - level) <= deltaToTake) {
            ExecutableSequence s = new ExecutableSequence(classz, root);

            for (Method m : javaObjectsProvider.getWriters(rootClassz, classz)) {
                s.getWriters().add(getMethod(rootClassz, m, level));
            }
            for (Field m : javaObjectsProvider.getWriterFields(rootClassz, classz)) {
                s.getWriters().add(getWriterField(rootClassz, m, level));
            }
            for (Method m : javaObjectsProvider.getObservers(rootClassz, classz)) {
                s.getObservers().add(getMethod(rootClassz, m, level));
            }
            for (Field m : javaObjectsProvider.getObserverFields(rootClassz, classz)) {
                s.getObservers().add(getField(rootClassz, m, level));
            }
            return s;

        }
        return root;

    }

    public ExecutableConstructor getConstructor(final Class<?> classz, final Constructor<?> constructor, int level) {

        final List<IExecutable> parameters = new LinkedList<>();
        for (final Class<?> p : constructor.getParameterTypes()) {
            parameters.add(create(classz, p, level));
        }
        ExecutableConstructor root = new ExecutableConstructor(constructor,
                new ExecutableValue(constructor.getDeclaringClass(), ValueType.COMPLEX),
                parameters.toArray(new IExecutable[0]));
        return root;
    }

    public ExecutableMethod getMethod(final Class<?> classz, final Method method, int level) {
        final List<IExecutable> parameters = new LinkedList<>();
        for (final Class<?> p : method.getParameterTypes()) {
            parameters.add(create(classz, p, level));
        }
        return new ExecutableMethod(method, create(classz, method.getReturnType(), level),
                parameters.toArray(new IExecutable[0]));
    }

    public ExecutableAbstractMethod getAbstractMethod(final Class<?> classz, final Method method, int level) {
        final List<IExecutable> parameters = new LinkedList<>();
        for (final Class<?> p : method.getParameterTypes()) {
            parameters.add(create(classz, p, level));
        }
        return new ExecutableAbstractMethod(method, create(classz, method.getReturnType(), level),
                parameters.toArray(new IExecutable[0]));
    }

    public ExecutableFieldObserver getField(final Class<?> classz, final Field field, int level) {
        final Class<?> returnType = field.getType();
        return new ExecutableFieldObserver(field, create(classz, returnType, level));

    }

    public ExecutableFieldWriter getWriterField(final Class<?> classz, final Field field, int level) {
        final Class<?> returnType = field.getType();
        return new ExecutableFieldWriter(field, create(classz, returnType, level));

    }
}
