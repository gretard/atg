package edu.ktu.atg.common.execution;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ktu.atg.common.executables.ExecutableAbstractClassz;
import edu.ktu.atg.common.executables.ExecutableAbstractMethod;
import edu.ktu.atg.common.executables.ExecutableArray;
import edu.ktu.atg.common.executables.ExecutableConstructor;
import edu.ktu.atg.common.executables.ExecutableEnum;
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
        this(3, 0);
    }

    private final Map<Class<?>, Class<?>> knownClasses = new HashMap<>();

    public ClassesAnalyzer(int maxLevel, int delta) {
        this.maxLevel = maxLevel;
        this.deltaToTake = delta;
        knownClasses.put(CharSequence.class, String.class);
        knownClasses.put(Iterable.class, ArrayList.class);
        knownClasses.put(java.lang.Appendable.class, StringBuilder.class);

        //
    }

    public IExecutable create(Class<?> rootClassz, Class<?> classz, int level) {
        if (classz == null) {
            throw new IllegalArgumentException("Could not analyze class null for");
        }
        if (knownClasses.containsKey(classz)) {
            System.out.println("Returning " + classz + " " + knownClasses.get(classz));
            return create(rootClassz, knownClasses.get(classz), level);
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
            return new ExecutableEnum(classz);
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

        if (Modifier.isAbstract(classz.getModifiers())) {
            List<ExecutableAbstractMethod> methodsToImplement = javaObjectsProvider.getAllMethods(classz).stream()
                    .filter(method -> Modifier.isAbstract(method.getModifiers()))
                    .map(method -> getAbstractMethod(classz, method, level)).collect(Collectors.toList());
            final List<Constructor<?>> constructors = javaObjectsProvider.getConstructors(rootClassz, classz);
            if (constructors.isEmpty()) {
                return new ExecutableValue(classz, ValueType.NULL);
            }
            final ExecutableConstructor c = getConstructor(rootClassz, constructors.get(0), level);
            return new ExecutableAbstractClassz(c.getConstructor(), c.getParameters(),
                    ExecutableValue.complexType(classz), methodsToImplement.toArray(new ExecutableAbstractMethod[0]));
        }

        IExecutableWithReturnValue constructor = selectConstructor(rootClassz, classz, level);
        if (constructor == null) {
            if (classz.equals(rootClassz)) {
                throw new IllegalArgumentException(
                        "Can't instantiate classz " + classz + " as no constructors were found...");
            }
            return new ExecutableValue(classz, ValueType.NULL);
        }

        return getExecutableConstructor(rootClassz, classz, constructor, level);

    }

    public IExecutableWithReturnValue selectConstructor(final Class<?> rootClassz, final Class<?> classz, int level) {
        final List<Constructor<?>> constructors = javaObjectsProvider.getConstructors(rootClassz, classz);
        if (!constructors.isEmpty()) {
            return getConstructor(rootClassz, constructors.get(0), level);
        }
        final List<Method> methods = javaObjectsProvider.getStaticMethodConstructors(rootClassz, classz);

        if (!methods.isEmpty()) {
            return getMethod(rootClassz, methods.get(0), level);
        }
        final List<Field> fields = javaObjectsProvider.getStaticFieldConstructors(rootClassz, classz);
        if (!fields.isEmpty()) {
            return getField(rootClassz, fields.get(0), level);
        }
        final List<Object> enumConstants = javaObjectsProvider.getEnums(rootClassz, classz);
        if (!enumConstants.isEmpty()) {
            return new ExecutableEnum(classz);
        }
        return null;

    }

    public IExecutableWithReturnValue getExecutableConstructor(final Class<?> rootClassz, final Class<?> classz,
            final IExecutableWithReturnValue root, int level) {

        int delta = maxLevel - level;
        System.out.println("Levevl " + level + " " + delta + " ");
        if (level > 0 && delta < deltaToTake && deltaToTake > 0) {
System.out.println("OK");
            ExecutableSequence s = new ExecutableSequence(classz, root);

            for (Method m : javaObjectsProvider.getWriters(rootClassz, classz)) {
                if (!Modifier.isPublic(m.getModifiers())) {
                    continue;
                }
                s.getWriters().add(getMethod(rootClassz, m, level));

            }
            for (Field m : javaObjectsProvider.getWriterFields(rootClassz, classz)) {
                if (!Modifier.isPublic(m.getModifiers())) {
                    continue;
                }
                s.getWriters().add(getWriterField(rootClassz, m, level));
            }
            for (Method m : javaObjectsProvider.getObservers(rootClassz, classz)) {
                if (!Modifier.isPublic(m.getModifiers())) {
                    continue;
                }
                s.getObservers().add(getMethod(rootClassz, m, level));
            }
            for (Field m : javaObjectsProvider.getObserverFields(rootClassz, classz)) {
                if (!Modifier.isPublic(m.getModifiers())) {
                    continue;
                }
                s.getObservers().add(getField(rootClassz, m, level));
            }
            return s;

        }
        return root;

    }

    public ExecutableConstructor getConstructor(final Class<?> classz, final Constructor<?> constructor, int level) {

        final List<IExecutable> parameters = new LinkedList<>();
        for (final Class<?> p : constructor.getParameterTypes()) {
            parameters.add(create(classz, p, level - 1));
        }
        ExecutableConstructor root = new ExecutableConstructor(constructor,
                new ExecutableValue(constructor.getDeclaringClass(), ValueType.COMPLEX),
                parameters.toArray(new IExecutable[0]));

        return root;
    }

    public IExecutableWithReturnValue getMethod(final Class<?> classz, final Method method, int level) {
        final List<IExecutable> parameters = new LinkedList<>();
        for (final Class<?> p : method.getParameterTypes()) {
            parameters.add(create(classz, p, level - 1));
        }

        ExecutableMethod m = new ExecutableMethod(method, create(classz, method.getReturnType(), level - 1),
                parameters.toArray(new IExecutable[0]));

        return m; // getExecutableConstructor(classz, method.getDeclaringClass(), m, level - 1);
    }

    public ExecutableAbstractMethod getAbstractMethod(final Class<?> classz, final Method method, int level) {
        final List<IExecutable> parameters = new LinkedList<>();
        for (final Class<?> p : method.getParameterTypes()) {
            parameters.add(create(classz, p, 0));
        }
        return new ExecutableAbstractMethod(method, create(classz, method.getReturnType(), level - 1),
                parameters.toArray(new IExecutable[0]));
    }

    public ExecutableFieldObserver getField(final Class<?> classz, final Field field, int level) {
        final Class<?> returnType = field.getType();
        return new ExecutableFieldObserver(field, create(classz, returnType, level - 1));
    }

    public ExecutableFieldWriter getWriterField(final Class<?> classz, final Field field, int level) {
        final Class<?> returnType = field.getType();
        return new ExecutableFieldWriter(field, create(classz, returnType, level - 1));

    }
}
