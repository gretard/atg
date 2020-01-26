package edu.ktu.atg.common.monitors;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.helpers.DumpUtilities;

public enum ValuesMonitor implements IBaseMonitor {
	INSTANCE;

	private List<HitStatement> statements = new LinkedList<>();

	private List<HitStatement> statementWithValues = new LinkedList<>();

	public void clear() {
		statements = new LinkedList<>();
		statementWithValues = new LinkedList<>();
	}

	public synchronized void fill(SolutionExecutionData results) {
		HitStatement[] hits = this.statements.toArray(new HitStatement[0]);
		HitStatement[] hitsWithVals = this.statementWithValues.toArray(new HitStatement[0]);

		for (int i = 0; i < hits.length; i++) {

			results.getStatements().add(hits[i]);
		}
		for (int i = 0; i < hitsWithVals.length; i++) {
			results.getStatementWithValues().add(hitsWithVals[i]);
		}

	}

	public synchronized void hit(String methodUniqueName, int no, int uniqueNo) {
		HitStatement statement = new HitStatement();
		statement.name = methodUniqueName;
		statement.no = no;
		statement.uniqueNo = uniqueNo;
		statements.add(statement);
	}

	public synchronized void hitWithValue(String methodUniqueName, int no, int uniqueNo, Object value) {
		final HitStatement statement = new HitStatement();
		statement.name = methodUniqueName;
		statement.no = no;
		statement.uniqueNo = uniqueNo;

		if (value instanceof Number || value instanceof Boolean || value instanceof CharSequence) {
			statement.value = value;
		}
		statement.valueStringRepresentation = DumpUtilities.dump(value, 2);
		statement.address = Objects.hashCode(value);
		statementWithValues.add(statement);
		this.hit(methodUniqueName, no, uniqueNo);
	}

	public static class HitStatement {
		public String name;

		public String getName() {
			return name;
		}

		public int getNo() {
			return no;
		}

		public int getUniqueNo() {
			return uniqueNo;
		}

		public String getValueStringRepresentation() {
			return valueStringRepresentation;
		}

		public int getAddress() {
			return address;
		}

		public int no;

		public int uniqueNo;

		public String valueStringRepresentation;
		public Object value;
		public ValueTypes type = ValueTypes.SIMPLE;

		public ValueTypes getType() {
			return type;
		}

		public Object getValue() {
			return value;
		}

		public int address;
	}

	public enum ValueTypes {
		SIMPLE, POST, PRE;
	}
}
