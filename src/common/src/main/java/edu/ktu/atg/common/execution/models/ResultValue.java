package edu.ktu.atg.common.execution.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.ktu.atg.common.executables.ExecutableValue;
import edu.ktu.atg.common.executables.ExecutableVoid;
import edu.ktu.atg.common.executables.IExecutable;

public class ResultValue {
    private final IExecutable item;
    private final CheckType checkType;

    public IExecutable getItem() {
        return item;
    }

    @Override
    public String toString() {
        return "ResultValue [item=" + item + ", checkType=" + checkType + ", stringRepresentation="
                + stringRepresentation + "]";
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    private final String stringRepresentation;

    public static enum CheckType {
        SIMPLE, NULL, NOTNULL, ARRAY;
    }

    public ResultValue(IExecutable item, CheckType checkType, String stringRepresentation) {
        this.item = item;
        this.checkType = checkType;
        this.stringRepresentation = stringRepresentation;
    }

    public static List<ResultValue> fromValue(IExecutable item, Object value) {

        if (item instanceof ExecutableVoid || item == null) {
            return Collections.emptyList();
        }
        if (value == null) {
            return Arrays.asList(new ResultValue(item, CheckType.NULL, null));
        }

        if (item instanceof ExecutableValue) {
            if (((ExecutableValue) item).getType().isSimple()) {
                return Arrays.asList(new ResultValue(item, CheckType.SIMPLE, value.toString()));
            }
        }
        return Arrays.asList(new ResultValue(item, CheckType.NOTNULL, null));
    }
}
