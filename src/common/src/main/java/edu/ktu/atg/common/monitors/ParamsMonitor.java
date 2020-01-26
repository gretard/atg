package edu.ktu.atg.common.monitors;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.helpers.DumpUtilities;
import edu.ktu.atg.common.monitors.ValuesMonitor.HitStatement;
import edu.ktu.atg.common.monitors.ValuesMonitor.ValueTypes;

public enum ParamsMonitor implements IBaseMonitor {
	INSTANCE;

	private List<HitStatement> postValueStatements = new LinkedList<>();
	private List<HitStatement> preValueStatements = new LinkedList<>();

	public void clear() {
		postValueStatements = new LinkedList<>();
		preValueStatements = new LinkedList<>();
	}

	public synchronized void fill(SolutionExecutionData results) {
		HitStatement[] postValueStatements = this.postValueStatements.toArray(new HitStatement[0]);
		HitStatement[] preValueStatements = this.preValueStatements.toArray(new HitStatement[0]);

		for (int i = 0; i < preValueStatements.length; i++) {
			results.getPreValues().add(preValueStatements[i]);
		}
		for (int i = 0; i < postValueStatements.length; i++) {
			results.getPostValues().add(postValueStatements[i]);

		}

	}

	public synchronized void pre(String methodUniqueName, int no, int uniqueNo, Object value) {
		final HitStatement statement = new HitStatement();
		statement.name = methodUniqueName;
		statement.no = no;
		statement.uniqueNo = uniqueNo;
		statement.type = ValueTypes.PRE;

		if (value instanceof Number || value instanceof Boolean || value instanceof CharSequence) {
			statement.value = value;
		}
		statement.valueStringRepresentation = DumpUtilities.dump(value, 2);
		statement.address = Objects.hashCode(value);
		preValueStatements.add(statement);

	}

	public synchronized void post(String methodUniqueName, int no, int uniqueNo, Object value) {
		final HitStatement statement = new HitStatement();
		statement.name = methodUniqueName;
		statement.no = no;
		statement.uniqueNo = uniqueNo;
		statement.type = ValueTypes.POST;
		if (value instanceof Number || value instanceof Boolean || value instanceof CharSequence) {
			statement.value = value;
		}
		statement.valueStringRepresentation = DumpUtilities.dump(value, 2);
		statement.address = Objects.hashCode(value);
		postValueStatements.add(statement);

	}

}
