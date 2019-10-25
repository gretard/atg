package edu.ktu.atg.instrumentor.transformers.bodies;

import java.util.ArrayList;
import java.util.List;

import edu.ktu.atg.common.models.ExecutableStatement;
import edu.ktu.atg.common.models.ExecutableStatement.StatementTypes;
import edu.ktu.atg.common.monitors.ValuesMonitor;
import edu.ktu.atg.instrumentor.transformers.Transformation;
import soot.IntType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.IntConstant;
import soot.jimple.StringConstant;
import soot.jimple.SwitchStmt;

/**
 * Finds all SwitchStmt statements and adds probing statements
 * 
 * @author g.rudzioniene
 *
 */
public final class SwitchValueTransformer extends BaseTransformer implements Transformer {

    private final String monitorClass = ValuesMonitor.class.getName();
    private final String monitorMethod = "hit";
    private final String monitorClassField = "INSTANCE";

    @Override
    public void fillTransformations(final TransformerContextt context, final Unit current, final Unit prev,
            final int no) {

        if (!(current instanceof SwitchStmt)) {
            return;
        }

        final SwitchStmt statement = (SwitchStmt) current;
        final String methodName = context.methodInfo.getName();
        int uniqueNo = 1;

        for (final Unit targetUnit : statement.getTargets()) {
            final boolean after = targetUnit.getDefBoxes().isEmpty() ? false : true;
            final Transformation transformation = new Transformation(targetUnit, after);

            final List<Type> types = new ArrayList<Type>();
            types.add(RefType.v("java.lang.String"));
            types.add(IntType.v());
            types.add(IntType.v());

            final List<Value> arguments = new ArrayList<Value>();
            arguments.add(StringConstant.v(methodName));
            arguments.add(IntConstant.v(no));
            arguments.add(IntConstant.v(uniqueNo));

            addCallToMonitor(context.body, transformation, monitorClass, monitorClassField, monitorMethod, arguments,
                    types);

            context.transformations.add(transformation);

            uniqueNo += 1;

            context.methodInfo.getStatements()
                    .add(new ExecutableStatement(methodName, current.toString(), StatementTypes.SWITCH, no, uniqueNo,
                            current.getJavaSourceStartLineNumber(), targetUnit.toString()));

        }

    }

}
