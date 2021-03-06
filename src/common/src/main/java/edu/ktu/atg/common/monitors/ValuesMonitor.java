package edu.ktu.atg.common.monitors;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import edu.ktu.atg.common.helpers.DumpUtilities;
import edu.ktu.atg.common.models.ExecutionResults;

public enum ValuesMonitor implements IBaseMonitor {
    INSTANCE;

    private List<HitStatement> statements = new LinkedList<HitStatement>();

    private List<HitStatement> statementWithValues = new LinkedList<HitStatement>();

    public void clear() {
        statements = new LinkedList<ValuesMonitor.HitStatement>();
        statementWithValues = new LinkedList<ValuesMonitor.HitStatement>();
    }

    public void fill(ExecutionResults results) {
        results.getStatements().addAll(this.statements);
        results.getStatementWithValues().addAll(this.statementWithValues);
    }

    public void hit(String methodUniqueName, int no, int uniqueNo) {
        HitStatement statement = new HitStatement();
        statement.name = methodUniqueName;
        statement.no = no;
        statement.uniqueNo = uniqueNo;
        statements.add(statement);
    }

    public void hitWithValue(String methodUniqueName, int no, int uniqueNo, Object value) {
        final HitStatement statement = new HitStatement();
        statement.name = methodUniqueName;
        statement.no = no;
        statement.uniqueNo = uniqueNo;
        statement.valueStringRepresentation = DumpUtilities.dump(value, 2);
        statement.address = Objects.hashCode(value);
        statementWithValues.add(statement);
        this.hit(methodUniqueName, no, uniqueNo);
    }

    public static class HitStatement {
        private String name;

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

        private int no;

        private int uniqueNo;

        private String valueStringRepresentation;

        private int address;
    }
}
