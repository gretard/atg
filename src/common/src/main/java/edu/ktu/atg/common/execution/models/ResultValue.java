package edu.ktu.atg.common.execution.models;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.ktu.atg.common.executables.ExecutableArray;
import edu.ktu.atg.common.executables.ExecutableValue;
import edu.ktu.atg.common.executables.ExecutableVoid;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.helpers.DumpUtilities;

public class ResultValue {
	private final IExecutable item;
	private final CheckType checkType;

	public IExecutable getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "ResultValue [item=" + item + ", checkType=" + checkType + ", stringRepresentation=" + value + "]";
	}

	public CheckType getCheckType() {
		return checkType;
	}

	public Object getValue() {
		return value;
	}

	private final Object value;

	public static enum CheckType {
		SIMPLE, NULL, NOTNULL, ARRAY, LEN;
	}

	public ResultValue(IExecutable item, CheckType checkType, Object value) {
		this.item = item;
		this.checkType = checkType;
		this.value = value;
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

		if (item instanceof ExecutableArray) {
			int len = Array.getLength(value);
			if (len > 10) {
				return Arrays.asList(new ResultValue(item, CheckType.LEN, len));
			}
			return Arrays.asList(new ResultValue(item, CheckType.ARRAY, value));

		}
		return Arrays.asList(new ResultValue(item, CheckType.NOTNULL, null));
	}
}
