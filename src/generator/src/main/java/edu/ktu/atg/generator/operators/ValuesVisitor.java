package edu.ktu.atg.generator.operators;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.ktu.atg.common.executables.ExecutableFieldObserver;
import edu.ktu.atg.common.executables.ExecutableFieldWriter;
import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.executables.ExecutableValue;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.executables.IVisitor;
import edu.ktu.atg.common.executables.ValueType;
import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.DefinedValue;

public class ValuesVisitor implements IVisitor<Object> {
    private final List<SolutionExecutionData> data = new LinkedList<>();
    private SolutionExecutionData initialData;
    private CandidateSolution solution;

    public ValuesVisitor(CandidateSolution initialSolution) {
        this.initialData = initialSolution.data.copy();
        this.solution = initialSolution;

    }

    private DefinedValue getValue(IExecutable item) {
        return this.initialData.getDefinedValues().getOrDefault(item.getId(), DefinedValue.createExecutable(item));
    }

    public List<CandidateSolution> getData() {
        List<CandidateSolution> outData = new LinkedList<>();

        this.solution.data.getExecutedPairs().forEach(pair -> {
            try {
                pair.getItem().accept(pair.getRoot(), this);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
        data.forEach(i -> {
            CandidateSolution sol = new CandidateSolution();
            sol.data = i;
            sol.sequence = solution.sequence;
            outData.add(sol);
        });
        return outData;
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

    @Override
    public Object visit(ExecutableValue item, IExecutable root) throws Throwable {
        ValueType type = item.getType();
        DefinedValue value = getValue(item);
        if (type == ValueType.ENUM || type == ValueType.COMPLEX || type == ValueType.STRING
                || type == ValueType.BOOLEAN) {
            return null;
        }
        String s = value.getValue() != null ? value.getValue().toString() : "0";

        switch (type) {
        case BYTE:
            getNumbers(Byte.parseByte(s)).stream().map(n -> n.byteValue()).distinct().forEach(n -> {
                data.add(initialData.copy().defineValue(DefinedValue.createFixed(item, n)));
            });
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
    public Object visit(ExecutableFieldWriter item, IExecutable root) throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(ExecutableFieldObserver item, IExecutable root) throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object visit(ExecutableSequence item, IExecutable root) throws Throwable {
        // TODO Auto-generated method stub
        return null;
    }
}