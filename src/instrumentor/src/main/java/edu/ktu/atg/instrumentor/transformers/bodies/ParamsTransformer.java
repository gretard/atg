package edu.ktu.atg.instrumentor.transformers.bodies;

import java.util.ArrayList;
import java.util.List;

import edu.ktu.atg.common.models.MethodInfo;
import edu.ktu.atg.common.models.ParamInfo;
import edu.ktu.atg.common.monitors.ParamsMonitor;
import edu.ktu.atg.instrumentor.transformers.Transformation;
import soot.ArrayType;
import soot.Body;
import soot.IntType;
import soot.Local;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StringConstant;

/**
 * Adds statements to track array or reference type values before and after
 * method execution.
 * 
 * @author greta
 *
 */
public class ParamsTransformer extends BaseTransformer implements Transformer {
	private final String monitorClass = ParamsMonitor.class.getName();
	private final String monitorMethod = "pre";
	private final String monitorMethodPost = "post";

	private final String monitorClassField = "INSTANCE";

	private boolean isParamsAdded = false;

	@Override
	public void fillTransformations(TransformerContextt context, Unit current, Unit prev, int no) {
		if (current instanceof IdentityStmt) {
			return;
		}

		final MethodInfo methodInfo = context.methodInfo;
		final String methodName = context.methodInfo.getName();
		final Body body = context.body;

		if (!isParamsAdded) {
			isParamsAdded = true;
			final Transformation transformation = new Transformation(current, false);
			for (final Local param : body.getParameterLocals()) {
				final String name = param.getName();
				final int paramNo = param.getNumber();
				final String type = param.getType().toString();
				final boolean isComplex = param.getType() instanceof RefType || param.getType() instanceof ArrayType;
				methodInfo.getParameters().add(new ParamInfo(methodName, name, type, paramNo, isComplex));

				if (isComplex) {
					final List<Type> types = new ArrayList<>();
					types.add(RefType.v("java.lang.String"));
					types.add(IntType.v());
					types.add(IntType.v());
					types.add(RefType.v("java.lang.Object"));

					final List<Value> arguments = new ArrayList<>();
					arguments.add(StringConstant.v(methodName));
					arguments.add(IntConstant.v(no));
					arguments.add(IntConstant.v(paramNo));
					arguments.add(wrapPrimType(context.body, transformation, param));

					addCallToMonitor(context.body, transformation, monitorClass, monitorClassField, monitorMethod,
							arguments, types);
				}
			}
			context.transformations.add(transformation);
		}
		if (current instanceof ReturnVoidStmt || current instanceof ReturnStmt) {

			final Transformation transformation = new Transformation(current, false);

			for (final Local param : body.getParameterLocals()) {
				if (param.getType() instanceof RefType || param.getType() instanceof ArrayType) {

					final int paramNo = param.getNumber();

					final List<Type> types = new ArrayList<>();
					types.add(RefType.v("java.lang.String"));
					types.add(IntType.v());
					types.add(IntType.v());
					types.add(RefType.v("java.lang.Object"));

					final List<Value> arguments = new ArrayList<>();
					arguments.add(StringConstant.v(methodName));
					arguments.add(IntConstant.v(no));
					arguments.add(IntConstant.v(paramNo));
					arguments.add(wrapPrimType(context.body, transformation, param));

					addCallToMonitor(context.body, transformation, monitorClass, monitorClassField, monitorMethodPost,
							arguments, types);

				}

			}
			context.transformations.add(transformation);

		}
	}

}
