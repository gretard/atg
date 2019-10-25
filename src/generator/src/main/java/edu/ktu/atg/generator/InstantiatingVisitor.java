package edu.ktu.atg.generator;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.executables.IExecutableWithReturnValue;
import edu.ktu.atg.common.executables.IVisitor;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.DefinedValue;
import edu.ktu.atg.common.execution.models.DefinedValue.ValueState;
import edu.ktu.atg.common.execution.models.ExecutablePair;
import edu.ktu.atg.common.execution.models.ResultValue;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

public class InstantiatingVisitor implements IVisitor<Object> {

    private final Map<String, Object> cache = new HashMap<>();

    private final List<ExecutablePair> pairs;

    private final Map<String, DefinedValue> values;
    private final Map<String, List<ResultValue>> results;

    private final ClassLoader loader;

    public InstantiatingVisitor(SolutionExecutionData trace, ClassLoader loader) {
        this.loader = loader;
        this.values = trace.getDefinedValues();
        this.pairs = trace.getExecutedPairs();
        this.results = trace.getResults();
    }

    public InstantiatingVisitor(SolutionExecutionData trace) {
        this.values = trace.getDefinedValues();
        this.pairs = trace.getExecutedPairs();
        this.results = trace.getResults();
        this.loader = this.getClass().getClassLoader();
    }

    public InstantiatingVisitor() {
        this.values = new HashMap<>();
        this.pairs = new LinkedList<>();
        this.results = new HashMap<>();
        this.loader = this.getClass().getClassLoader();

    }

    public Map<String, Object> getCache() {
        return cache;
    }

    public Object execute(IExecutable item, IExecutable root) throws Throwable {
        if (item == null && root == null) {
            return null;
        }
        if (cache.containsKey(item.getId())) {
            return cache.get(item.getId());
        }
        if (!values.containsKey(item.getId())) {
            values.put(item.getId(), DefinedValue.createExecutable(item));
        }
        DefinedValue definedValue = values.get(item.getId());

        if (definedValue.getState() == ValueState.SKIP) {
            return null;
        }
        Object returnValue = null;
       
        try {
            IExecutable returnValueDefinition = null;
            if (definedValue.getState() == ValueState.REF) {
                returnValue = execute(definedValue.getRef(), root);

            } else {
                returnValue = definedValue.getState() == ValueState.FIXED ? definedValue.getValue()
                        : item.accept(root, this);
            }

            if (item instanceof IExecutableWithReturnValue) {
                returnValueDefinition = ((IExecutableWithReturnValue) item).getReturnValue();
                this.cache.put(returnValueDefinition.getId(), returnValue);
                List<ResultValue> results = ResultValue.fromValue(returnValueDefinition, returnValue);
                List<ResultValue> savedResults = this.results.getOrDefault(returnValueDefinition.getId(),
                        new LinkedList<ResultValue>());
                savedResults.addAll(results);
                this.results.put(item.getId(), savedResults);
                pairs.add(ExecutablePair.ok(root, item, returnValueDefinition));
                cache.put(item.getId(), returnValue);
            }
        } catch (Exception e) {
            pairs.add(ExecutablePair.ko(root, item));
            throw e;
        }

        return returnValue;
    }

    @Override
    public Object visit(ExecutableConstructor item, IExecutable root) throws Throwable {

        List<Object> values = new LinkedList<Object>();
        for (IExecutable p : item.getParameters()) {
            Object value = execute(p, item);
            values.add(value);
        }

        return item.getConstructor().newInstance(values.toArray());

    }

    @Override
    public Object visit(ExecutableAbstractClassz item, IExecutable root) throws Throwable {

        List<Object> arguments = new LinkedList<Object>();
        for (IExecutable p : item.getParameters()) {
            Object value = execute(p, item);
            arguments.add(value);
        }
        Builder<?> buddy = mockMethods(item, this, item.getMethodsToImplement());
        Class<?> dynamicType = buddy.make().load(loader).getLoaded();

        return dynamicType.getConstructor(item.getConstructor().getParameterTypes()).newInstance(arguments.toArray());

    }

    private static Builder<?> mockMethods(final IExecutable root, final InstantiatingVisitor visitor,
            final ExecutableAbstractMethod[] methods) throws Throwable {
        final List<Method> mockecMethods = new LinkedList<>();
        final Map<String, ExecutableAbstractMethod> methodsMap = new HashMap<>();
        for (ExecutableAbstractMethod m : methods) {
            methodsMap.put(m.getMethod().toGenericString(), m);
            mockecMethods.add(m.getMethod());
            visitor.execute(m, root);
        }
        return new ByteBuddy().subclass(root.getClassz())
                .method(ElementMatchers.anyOf(mockecMethods.toArray(new Method[0])))

                .intercept(InvocationHandlerAdapter.of(new InvocationHandler() {

                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return visitor.execute(methodsMap.get(method.toGenericString()), root);
                    };
                }));

    }

    @Override
    public Object visit(final ExecutableInterface item, IExecutable root) throws Throwable {
        Builder<?> buddy = mockMethods(item, this, item.getMethodsToImplement());
        Class<?> dynamicType = buddy.make().load(this.loader).getLoaded();
        return dynamicType.getConstructor().newInstance();
    }

    @Override
    public Object visit(ExecutableArray item, IExecutable root) throws Throwable {
        DefinedValue definedValue = this.values.getOrDefault(item.getId(), DefinedValue.createExecutable(item));
        // If array has not been executed yet and is not fixed
        if (definedValue.getState() == ValueState.EXECUTABLE) {
            int l = 3;
            final Object object = Array.newInstance(item.getClassz().getComponentType(), l);
            List<IExecutable> children = new LinkedList<>();
            for (int i = 0; i < l; i++) {
                IExecutable child = item.copyChild();
                Object value = this.execute(child, item);
                children.add(child);
                Array.set(object, i, value);
            }
            this.values.put(item.getId(), DefinedValue.createExecutableArr(item, children.toArray(new IExecutable[0])));
            return object;
        }
        IExecutable[] children = definedValue.getChildren();
        final Object object = Array.newInstance(item.getClassz().getComponentType(), children.length);
        for (int i = 0; i < children.length; i++) {
            IExecutable child = children[i];
            Object value = this.execute(child, item);
            Array.set(object, i, value);
        }
        return object;

    }

    @Override
    public Object visit(ExecutableSequence item, IExecutable root) throws Throwable {
        Object rootValue = execute(item.getRoot(), item);
        for (IExecutable p : item.getWriters()) {
            try {
                execute(p, item.getRoot());
            } catch (Throwable e) {
                this.pairs.add(ExecutablePair.ko(root, item));
            }
        }
        for (IExecutable p : item.getObservers()) {
            try {
                execute(p, item.getRoot());
            } catch (Throwable e) {
                this.pairs.add(ExecutablePair.ko(root, item));
            }
        }
        return rootValue;
    }

    @Override
    public Object visit(ExecutableValue item, IExecutable root) throws Throwable {
        Object value = item.getType().getDefaultValue();
        this.values.put(item.getId(), DefinedValue.createFixed(item, value));
        return value;
    }

    @Override
    public Object visit(ExecutableAbstractMethod item, IExecutable root) throws Throwable {
        return execute(item.getReturnValue(), item);
    }

    @Override
    public Object visit(ExecutableMethod item, IExecutable root) throws Throwable {
        Object rootValue = null;
        if (!item.isStatic()) {
            rootValue = execute(root, null);
        }
        List<Object> values = new LinkedList<Object>();
        for (IExecutable p : item.getParameters()) {
            Object value = execute(p, item);
            values.add(value);
        }
        return item.getMethod().invoke(rootValue, values.toArray());
    }

    @Override
    public Object visit(ExecutableFieldWriter item, IExecutable root) throws Throwable {
        Object rootValue = null;
        if (!item.isStatic()) {
            rootValue = execute(root, null);
        }
        Object value = execute(item.getInputValue(), item);
        item.getField().set(rootValue, value);
        return value;
    }

    @Override
    public Object visit(ExecutableFieldObserver item, IExecutable root) throws Throwable {
        Object rootValue = null;
        if (!item.isStatic()) {
            rootValue = execute(root, null);
        }
        return item.getField().get(rootValue);
    }

}
