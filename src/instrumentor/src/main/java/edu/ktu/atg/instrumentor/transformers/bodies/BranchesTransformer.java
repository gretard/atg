package edu.ktu.atg.instrumentor.transformers.bodies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import edu.ktu.atg.common.models.MethodBranch;
import edu.ktu.atg.common.monitors.BranchesMonitor;
import edu.ktu.atg.instrumentor.transformers.Transformation;
import soot.Body;
import soot.IntType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.StringConstant;

public class BranchesTransformer extends BaseTransformer implements Transformer {

    private final String monitorClass = BranchesMonitor.class.getName();
    private final String monitorMethod = "hitWithValue";
    private final String monitorMethodAfter = "hit";
    private final String monitorClassField = "INSTANCE";

    @Override
    public void fillTransformations(final TransformerContextt context, final Unit current, final Unit prev,
            final int no) {

        if (!(current instanceof IfStmt)) {
            return;
        }

        final IfStmt statement = (IfStmt) current;

        final Transformation before = new Transformation(current, false);
        final Transformation after = new Transformation(current, true);

        context.transformations.add(after);
        context.transformations.add(before);

        final String methodName = context.methodInfo.getName();

        {
            // add statement to check if branch predicate was evaluated

            ValueBox box1 = statement.getUseBoxes().get(0);
            ValueBox box2 = statement.getUseBoxes().get(1);

            List<ValueBox> boxes1 = find(box1, context.body);
            List<ValueBox> boxes2 = find(box2, context.body);

            if (!boxes1.isEmpty()) {
                box1 = boxes1.get(0);
                box2 = boxes1.get(1);
            }
            if (!boxes2.isEmpty()) {
                box1 = boxes2.get(0);
                box2 = boxes2.get(1);
            }

            final List<Type> types = new ArrayList<>();
            types.add(RefType.v("java.lang.String"));
            types.add(IntType.v());
            types.add(RefType.v("java.lang.Object"));
            types.add(RefType.v("java.lang.Object"));

            final List<Value> arguments = new ArrayList<>();
            arguments.add(StringConstant.v(methodName));
            arguments.add(IntConstant.v(no));
            arguments.add(wrapPrimType(context.body, before, box1.getValue()));
            arguments.add(wrapPrimType(context.body, before, box2.getValue()));

            addCallToMonitor(context.body, before, monitorClass, monitorClassField, monitorMethod, arguments, types);
            context.methodInfo.getBranches().add(new MethodBranch(methodName, statement.toString(),
                    no, box1.getValue().getType().toString(), box2.getValue().getType().toString()));

        }

        {
            // add statement to indicate that branch predicate was satisfied and after code
            // was executed
            final List<Type> types = new ArrayList<>();
            types.add(RefType.v("java.lang.String"));
            types.add(IntType.v());

            final List<Value> arguments = new ArrayList<>();
            arguments.add(StringConstant.v(methodName));
            arguments.add(IntConstant.v(no));

            addCallToMonitor(context.body, after, monitorClass, monitorClassField, monitorMethodAfter, arguments,
                    types);
        }

    }

    private static List<ValueBox> find(ValueBox box1, Body body) {
        final String name1 = box1.getValue().toString();
        if (!name1.startsWith("$stack")) {
            return Collections.emptyList();
        }
        List<ValueBox> s = body.getUseBoxes();
        int i = s.indexOf(box1);
        if (i < 2) {
            return Collections.emptyList();
        }
        List<ValueBox> boxes = Arrays.asList(s.get(i - 1), s.get(i - 2));
        for (ValueBox b : boxes) {

            String n = b.getClass().getName();
            if ((!"soot.jimple.internal.ImmediateBox".equals(n) && !"soot.jimple.internal.JimpleLocalBox".equals(n))
                    || n.contains("$LinkedRValueBox") || n.contains("Expr")) {

                return Collections.emptyList();
            }
        }
        return boxes;

    }

}
