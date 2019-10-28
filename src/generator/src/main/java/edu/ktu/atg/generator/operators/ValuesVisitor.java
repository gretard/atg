package edu.ktu.atg.generator.operators;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

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
import edu.ktu.atg.common.executables.IVisitor;
import edu.ktu.atg.common.executables.ValueType;
import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.DefinedValue;

public class ValuesVisitor implements IOperator, IVisitor<Object> {
    private final List<SolutionExecutionData> data = new LinkedList<>();
    private SolutionExecutionData initialData;
    private CandidateSolution solution;

    public ValuesVisitor(CandidateSolution initialSolution) {
        this.initialData = initialSolution.data.copy();
        this.solution = initialSolution;

    }

    public List<CandidateSolution> getData() {
        List<CandidateSolution> outData = new LinkedList<>();
        data.forEach(i -> {
            CandidateSolution sol = new CandidateSolution();
            sol.data = i;
            sol.sequence = solution.sequence;
            outData.add(sol);
        });
        return outData;
    }

    public ValuesVisitor work() {
        this.solution.data.getExecutedPairs().forEach(pair -> {
            try {
                pair.getItem().accept(pair.getRoot(), this);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        return this;
    }

    public void innerExecute(IExecutable root, IExecutable item) throws Throwable {
        item.accept(root, this);
    }

    @Override
    public Object visit(ExecutableValue item, IExecutable root) throws Throwable {
        ValueType type = item.getType();
        if (type == ValueType.COMPLEX) {
            return null;
        }
        DefinedValue value = initialData.getDefinedValue(item);

        String s = value.getValue() != null ? value.getValue().toString() : "0";

        switch (type) {
        case STRING:
            for (int i = 0; i < 10; i++) {
                data.add(initialData.copy()
                        .defineValue(DefinedValue.createFixed(item, RandomStringUtils.random(5, true, true))));
            }
            if (s.length() > 1) {
                data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, s.substring(1))));
                data.add(
                        initialData.copy().defineValue(DefinedValue.createFixed(item, s.substring(0, s.length() - 1))));
            }
            break;
        case BYTE:
            getNumbers(Byte.parseByte(s)).stream().map(n -> n.byteValue()).distinct().forEach(n -> {
                data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, n)));
            });
            break;
        case ENUM:
            for (Object enumConstant : item.getClassz().getEnumConstants()) {
                data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, enumConstant)));
            }
            break;
        case COMPLEX:
            break;
        case BOOLEAN:
            data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, Boolean.TRUE)));
            data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, Boolean.FALSE)));
            break;
        case DOUBLE:
            getNumbers(Double.parseDouble(s)).stream().map(n -> n.doubleValue()).distinct().forEach(n -> {
                data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, n)));
            });
            break;
        case INT:
            getNumbers(Integer.parseInt(s)).stream().map(n -> n.intValue()).distinct().forEach(n -> {
                data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, n)));
            });
            break;
        case LONG:
            getNumbers(Long.parseLong(s)).stream().map(n -> n.longValue()).distinct().forEach(n -> {
                data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, n)));
            });
            break;
        case FLOAT:
            getNumbers(Float.parseFloat(s)).stream().map(n -> n.floatValue()).distinct().forEach(n -> {
                data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, n)));
            });
            break;
        case SHORT:
            getNumbers(Short.parseShort(s)).stream().map(n -> n.shortValue()).distinct().forEach(n -> {
                data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, n)));
            });
            break;
        case CHAR:
            getNumbers(Integer.parseInt(s)).stream().map(n -> n.intValue()).distinct().forEach(n -> {
                data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, (char) n.intValue())));
            });
        default:
            break;
        }
        return null;
    }

    @Override
    public Object visit(ExecutableConstructor item, IExecutable root) throws Throwable {
        for (IExecutable param : item.getParameters()) {
            innerExecute(item, param);
        }
        if (!isRoot(item)) {
            data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, null)));
        }
        return null;
    }

    @Override
    public Object visit(ExecutableArray item, IExecutable root) throws Throwable {
        data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, null)));
        data.add(initialData.copy().defineValue(DefinedValue.createExecutable(item)));

        // empty array
        data.add(initialData.copy().defineValue(DefinedValue.createExecutableArr(item)));

        DefinedValue value = initialData.getDefinedValue(item);
        if (value.getChildren().length > 0) {

            {
                // remove one child
                List<IExecutable> children = new LinkedList<IExecutable>(Arrays.asList(value.getChildren()));
                children.remove(0);
                data.add(initialData.copy().defineValue(DefinedValue.createExecutableArr(item, children)));
            }
            {
                // add child
                List<IExecutable> children = new LinkedList<IExecutable>(Arrays.asList(value.getChildren()));
                children.add(value.getChildren()[0].copy());
                data.add(initialData.copy().defineValue(DefinedValue.createExecutableArr(item, children)));
            }
        } else {
            // add item with a single child
            data.add(initialData.copy().defineValue(DefinedValue.createExecutableArr(item, item.copyChild())));
        }

        // visit children
        for (IExecutable child : value.getChildren()) {
            innerExecute(item, child);
        }
        return null;
    }

    @Override
    public Object visit(ExecutableInterface item, IExecutable root) throws Throwable {
        data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, null)));
        data.add(initialData.copy().defineValue(DefinedValue.createExecutable(item)));

        for (IExecutable method : item.getMethodsToImplement()) {
            innerExecute(item, method);
        }
        return null;
    }

    @Override
    public Object visit(ExecutableAbstractMethod item, IExecutable root) throws Throwable {
        innerExecute(item, item.getReturnValue());
        return null;
    }

    @Override
    public Object visit(ExecutableAbstractClassz item, IExecutable root) throws Throwable {
        data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, null)));
        data.add(initialData.copy().defineValue(DefinedValue.createExecutable(item)));

        for (IExecutable method : item.getMethodsToImplement()) {
            innerExecute(item, method);
        }
        return null;
    }

    @Override
    public Object visit(ExecutableMethod item, IExecutable root) throws Throwable {
        for (IExecutable param : item.getParameters()) {
            innerExecute(item, param);
        }
        return null;
    }

    @Override
    public Object visit(ExecutableFieldWriter item, IExecutable root) throws Throwable {
        data.add(initialData.copy().defineValue(DefinedValue.createSkip(item)));
        innerExecute(item, item.getInputValue());
        return null;
    }

    @Override
    public Object visit(ExecutableFieldObserver item, IExecutable root) throws Throwable {
        return null;
    }

    @Override
    public Object visit(ExecutableSequence item, IExecutable root) throws Throwable {
        return null;
    }

    private boolean isRoot(IExecutable item) {

        return this.solution.sequence != null && this.solution.sequence.getRoot() == item;
    }

    private static Set<Number> getNumbers(Number initial) {
        final Set<Number> numbers = new HashSet<>();
        double d = initial.doubleValue();

        numbers.add(d + 1.0);
        numbers.add(d - 1.0);
        numbers.add(d * 2.0);
        numbers.add(d / 2.0);

        numbers.add(d * 1.5);
        numbers.add(d * 1.1);
        numbers.add(d * 0.5);
        numbers.add(d * 0.1);
        numbers.add(d * 1.1);
        numbers.add(d * -1.0);
        numbers.add(Math.floor(d));
        numbers.add(Math.ceil(d));
        if (d != 0) {
            numbers.add(1.0 / d);
            numbers.add(d + (1.0 / d));
        }

        if (initial instanceof Double) {
            numbers.add(Double.MAX_VALUE);
            numbers.add(Double.MIN_VALUE);
            numbers.add(Double.POSITIVE_INFINITY);
            numbers.add(Double.NEGATIVE_INFINITY);
            numbers.add(Double.NaN);
        }
        if (initial instanceof Float) {
            numbers.add(Float.MAX_VALUE);
            numbers.add(Float.MIN_VALUE);
            numbers.add(Float.POSITIVE_INFINITY);
            numbers.add(Float.NEGATIVE_INFINITY);
            numbers.add(Float.NaN);
        }
        if (initial instanceof Long) {
            numbers.add(Long.MAX_VALUE);
            numbers.add(Long.MIN_VALUE);
        }
        if (initial instanceof Byte) {
            numbers.add(Byte.MAX_VALUE);
            numbers.add(Byte.MIN_VALUE);
        }

        if (initial instanceof Integer) {
            numbers.add(Integer.MAX_VALUE);
            numbers.add(Integer.MIN_VALUE);
        }
        if (initial instanceof Short) {
            numbers.add(Short.MAX_VALUE);
            numbers.add(Short.MIN_VALUE);
        }
        return numbers;

    }
}