package edu.ktu.atg.outputter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.Statement;

import edu.ktu.atg.common.executables.ExecutableAbstractClassz;
import edu.ktu.atg.common.executables.ExecutableAbstractMethod;
import edu.ktu.atg.common.executables.ExecutableFieldWriter;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.executables.IExecutableWithReturnValue;

public class OutputContext {

    private final List<Statement> finalStatements = new LinkedList<>();

    private final Map<String, String> names = new HashMap<>();

    private final Map<String, Node> statements = new HashMap<>();

    private final Map<String, Integer> counts = new HashMap<>();

    public boolean canBeInlined(IExecutable item) {
    	 if (item.getClassz() == void.class || item.getClassz() == Void.class) {
         	return true;
         }
        if (item instanceof ExecutableAbstractMethod) {
            return true;
        }
        if (item instanceof IExecutableWithReturnValue || item instanceof ExecutableFieldWriter) {
            return false;
        }
       
        return counts.getOrDefault(item.getId(), 1) <= 1;
    }

    public void generateName(IExecutable item) {
        if (item == null) {
            return;
        }
        Integer count = counts.getOrDefault(item.getId(), 0);
        count += 1;
        counts.put(item.getId(), count);
        if (!names.containsKey(item.getId())) {
            names.put(item.getId(), "sut_" + names.size());
        }
    }
    public void generateName(IExecutable root, IExecutable item) {
        if (item == null) {
            return;
        }
        Integer count = counts.getOrDefault(item.getId(), 0);
        count += 1;
        counts.put(item.getId(), count);
        if (names.containsKey(root.getId())) {
            names.put(item.getId(), names.get(root.getId()));
            return;
        }
        if (!names.containsKey(item.getId())) {
            names.put(item.getId(), "sut_" + names.size());
        }
    }

    public Node getNodeValue(IExecutable item) {
        boolean inline = canBeInlined(item);
        if (inline) {
            Node v = this.statements.get(item.getId());
            return v;
        }
        return new NameExpr(names.get(item.getId()));

    }

    public void save(IExecutable item, Node value) {
        if (!this.names.containsKey(item.getId())) {
            this.names.put(item.getId(), "sut_" + this.names.size());
        }
        this.statements.put(item.getId(), value);
    }

    public List<Statement> getFinalStatements() {
        return finalStatements;
    }

    public Map<String, String> getNames() {
        return names;
    }

    public Map<String, Node> getStatements() {
        return statements;
    }
}
