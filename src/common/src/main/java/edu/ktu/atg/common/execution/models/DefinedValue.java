package edu.ktu.atg.common.execution.models;

import edu.ktu.atg.common.executables.IExecutable;

public class DefinedValue {
    public enum ValueState {
        EXECUTABLE, REF, FIXED, SKIP, ARRAY;
    }

    private IExecutable[] children;
    public IExecutable[] getChildren() {
        return children;
    }

    private IExecutable item;

    public IExecutable getItem() {
        return item;
    }

    private IExecutable ref;

    public IExecutable getRef() {
        return ref;
    }

    private Object value;

    public Object getValue() {
        return value;
    }

    private ValueState state = ValueState.EXECUTABLE;

    public ValueState getState() {
        return state;
    }

    public static DefinedValue createRef(IExecutable item, IExecutable ref) {
        DefinedValue value = new DefinedValue();
        value.item = item;
        value.ref = ref;
        value.state = ValueState.REF;
        return value;
    }

    public static DefinedValue createSkip(IExecutable item) {
        DefinedValue value = new DefinedValue();
        value.item = item;
        value.state = ValueState.SKIP;
        return value;
    }

    public static DefinedValue createExecutable(IExecutable item) {
        DefinedValue value = new DefinedValue();
        value.item = item;
        value.state = ValueState.EXECUTABLE;
        return value;
    }
    public static DefinedValue createExecutableArr(IExecutable item, IExecutable...children) {
        DefinedValue value = new DefinedValue();
        value.item = item;
        value.state = ValueState.ARRAY;
        value.children = children;
        return value;
    }
    public static DefinedValue createFixed(IExecutable main, Object obj) {
        DefinedValue value = new DefinedValue();
        value.item = main;
        value.state = ValueState.FIXED;
        value.value = obj;
        return value;
    }
}
