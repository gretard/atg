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
import soot.jimple.ReturnStmt;
import soot.jimple.StringConstant;
import soot.jimple.internal.JReturnStmt;

/**
 * Finds all non void return statements and adds probing statements
 * 
 * @author g.rudzioniene
 *
 */
public final class ReturnValueTransformer extends BaseTransformer implements Transformer {

	private final String monitorClass = ValuesMonitor.class.getName();
	private final String monitorMethod = "hitWithValue";
	private final String monitorClassField = "INSTANCE";

	@Override
	public void fillTransformations(final TransformerContextt context, final Unit current, final Unit prev,
			final int no) {

		if (!(current instanceof JReturnStmt)) {
			return;
		}

		final ReturnStmt statement = (ReturnStmt) current;

		final Transformation transformation = new Transformation(current, false);

		final Value v = statement.getOp();

		final String methodName = context.methodInfo.getName();

		final List<Type> types = new ArrayList<Type>();
		types.add(RefType.v("java.lang.String"));
		types.add(IntType.v());
		types.add(IntType.v());
		types.add(RefType.v("java.lang.Object"));

		final List<Value> arguments = new ArrayList<Value>();
		arguments.add(StringConstant.v(methodName));
		arguments.add(IntConstant.v(no));
		arguments.add(IntConstant.v(0));
		arguments.add(wrapPrimType(context.body, transformation, v));

		addCallToMonitor(context.body, transformation, monitorClass, monitorClassField, monitorMethod, arguments,
				types);

		context.transformations.add(transformation);
		context.methodInfo.getStatements()
				.add(new ExecutableStatement(methodName, current.toString(), StatementTypes.RETURN, no, 0, current.getJavaSourceStartLineNumber()));
	}

}
