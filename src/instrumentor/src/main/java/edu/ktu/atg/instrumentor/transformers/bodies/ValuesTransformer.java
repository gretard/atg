package edu.ktu.atg.instrumentor.transformers.bodies;

import java.util.ArrayList;
import java.util.List;

import edu.ktu.atg.common.monitors.ValuesMonitor;
import edu.ktu.atg.instrumentor.transformers.Transformation;
import soot.IntType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.IntConstant;
import soot.jimple.StringConstant;
import soot.jimple.internal.JAssignStmt;

/**
 * Finds statements using/defining any values and adds probing statements.
 * Should be used with care as too big methods might be generated.
 * 
 * @author g.rudzioniene
 *
 */
public final class ValuesTransformer extends BaseTransformer implements Transformer {

	private final String monitorClass = ValuesMonitor.class.getName();
	private final String monitorMethod = "hitWithValue";
	private final String monitorClassField = "INSTANCE";

	@Override
	public void fillTransformations(final TransformerContextt context, final Unit current, final Unit prev,
			final int no) {

		if (current instanceof JAssignStmt || current.toString().contains(" new ")) {
			return;
		}

		final String methodName = context.methodInfo.getName();

		final Transformation beforeUses = new Transformation(current, false);
		final Transformation afterDefs = new Transformation(current, true);
		context.transformations.add(beforeUses);
		context.transformations.add(afterDefs);

		final boolean addAfter = current.toString().contains("specialinvoke");
		final Transformation transformation = addAfter ? afterDefs : beforeUses;

		int uniqueNo = 1;
		for (ValueBox v : current.getUseBoxes()) {
			if (!shouldAddUse(v)) {
				continue;
			}

			final List<Type> types = new ArrayList<Type>();
			types.add(RefType.v("java.lang.String"));
			types.add(IntType.v());
			types.add(IntType.v());
			types.add(RefType.v("java.lang.Object"));

			final List<Value> arguments = new ArrayList<Value>();
			arguments.add(StringConstant.v(methodName));
			arguments.add(IntConstant.v(no));
			arguments.add(IntConstant.v(uniqueNo));
			arguments.add(wrapPrimType(context.body, transformation, v.getValue()));

			addCallToMonitor(context.body, transformation, monitorClass, monitorClassField, monitorMethod, arguments,
					types);
			uniqueNo += 1;

		}
		for (ValueBox v : current.getDefBoxes()) {
			if (!shouldAddUse(v)) {
				continue;
			}

			final List<Type> types = new ArrayList<Type>();
			types.add(RefType.v("java.lang.String"));
			types.add(IntType.v());
			types.add(IntType.v());
			types.add(RefType.v("java.lang.Object"));

			final List<Value> arguments = new ArrayList<Value>();
			arguments.add(StringConstant.v(methodName));
			arguments.add(IntConstant.v(no));
			arguments.add(IntConstant.v(uniqueNo));
			arguments.add(wrapPrimType(context.body, afterDefs, v.getValue()));

			addCallToMonitor(context.body, afterDefs, monitorClass, monitorClassField, monitorMethod, arguments, types);

			uniqueNo += 1;

		}

	}

}
