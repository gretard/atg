package edu.ktu.atg.instrumentor.transformers.bodies;

import java.util.Arrays;
import java.util.List;

import edu.ktu.atg.instrumentor.transformers.Transformation;
import soot.Body;
import soot.Local;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethodRef;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.Expr;
import soot.jimple.Jimple;
import soot.jimple.Ref;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.internal.ImmediateBox;

public abstract class BaseTransformer {

	protected final static void addCallToMonitor(final Body body, final Transformation transformation,
			final String className, final String monitorClassField, final String method, List<Value> args,
			List<Type> params) {

		final SootClass classz = Scene.v().getSootClass(className);

		final SootFieldRef enumMonitorRef = Scene.v().makeFieldRef(classz, monitorClassField, classz.getType(), true);

		final int localsCount = body.getLocalCount();
		final Local local = Jimple.v().newLocal("$loc_" + localsCount, classz.getType());
		body.getLocals().add(local);

		final Value rhs0 = Jimple.v().newStaticFieldRef(enumMonitorRef);
		final AssignStmt assignSt = Jimple.v().newAssignStmt(local, rhs0);
		transformation.getUnitsToAdd().add(assignSt);

		final SootMethodRef monitorMethodRef = Scene.v().makeMethodRef(classz, method, params, VoidType.v(), false);
		final VirtualInvokeExpr ie0 = Jimple.v().newVirtualInvokeExpr(local, monitorMethodRef, args);
		final Stmt incStmt = Jimple.v().newInvokeStmt(ie0);
		transformation.getUnitsToAdd().add(incStmt);
	}

	protected static final boolean shouldAddUse(ValueBox box) {
		Value value = box.getValue();
		if (value instanceof Expr || value instanceof Ref || value.toString().equals("this")) {
			return false;
		}
		if (value.getType() instanceof VoidType) {
			return false;
		}
		if ((box instanceof ImmediateBox)
				|| (box.getClass().getName().equals("soot.jimple.internal.JAssignStmt$LinkedVariableBox"))) {
			return true;
		}
		if (box instanceof ThisRef) {
			return false;
		}

		return false;
	}

	protected static final Value wrapPrimType(final Body body, final Transformation transformation, final Value value) {
		if (!(value.getType() instanceof PrimType)) {

			if (value instanceof ClassConstant) {
				ClassConstant x = (ClassConstant) value;
				return StringConstant.v(x.toSootType().toString());
			}

			return value;
		}

		final PrimType type = (PrimType) value.getType();

		final SootMethodRef ref = Scene.v().makeMethodRef(type.boxedType().getSootClass(), "valueOf",
				Arrays.asList(type), type.boxedType(), true);

		final int newLocalsCount = body.getLocalCount();
		final Local newLocal = Jimple.v().newLocal("aug_test" + newLocalsCount, RefType.v(Object.class.getName()));
		body.getLocals().add(newLocal);

		final Value statement = Jimple.v().newStaticInvokeExpr(ref, value);
		final AssignStmt assignStatement = Jimple.v().newAssignStmt(newLocal, statement);

		transformation.getUnitsToAdd().add(assignStatement);
		return newLocal;
	}

}
