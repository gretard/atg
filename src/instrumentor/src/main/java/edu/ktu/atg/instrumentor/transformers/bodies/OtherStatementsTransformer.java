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
import soot.jimple.GotoStmt;
import soot.jimple.IntConstant;
import soot.jimple.StringConstant;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.JReturnVoidStmt;

/**
 * Finds all other interesting statements and adds probing statements
 * 
 * @author g.rudzioniene
 *
 */
public final class OtherStatementsTransformer extends BaseTransformer implements Transformer {

	private final String monitorClass = ValuesMonitor.class.getName();
	private final String monitorMethod = "hit";
	private final String monitorClassField = "INSTANCE";

	@Override
	public void fillTransformations(final TransformerContextt context, final Unit current, final Unit prev,
			final int no) {

		if (current instanceof JReturnVoidStmt || current instanceof GotoStmt || current instanceof ThrowStmt) {

			StatementTypes type = StatementTypes.OTHER;
			if (current instanceof JReturnVoidStmt) {
				type = StatementTypes.RETURNVOID;
			}
			if (current instanceof ThrowStmt) {
				type = StatementTypes.THROWS;
			}

			final Transformation transformation = new Transformation(current, false);

			final String methodName = context.methodInfo.getName();

			final List<Type> types = new ArrayList<Type>();
			types.add(RefType.v("java.lang.String"));
			types.add(IntType.v());
			types.add(IntType.v());

			final List<Value> arguments = new ArrayList<Value>();
			arguments.add(StringConstant.v(methodName));
			arguments.add(IntConstant.v(no));
			arguments.add(IntConstant.v(0));

			addCallToMonitor(context.body, transformation, monitorClass, monitorClassField, monitorMethod, arguments,
					types);

			context.transformations.add(transformation);
			context.methodInfo.getStatements()
					.add(new ExecutableStatement(methodName, current.toString(), type, no, 0, current.getJavaSourceStartLineNumber()));

		}

	}

}
