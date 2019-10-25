package edu.ktu.atg.instrumentor.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.MethodInfo;
import edu.ktu.atg.instrumentor.transformers.bodies.Transformer;
import edu.ktu.atg.instrumentor.transformers.bodies.TransformerContextt;
import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;
import soot.validation.ValidationException;

public class SootBodiesTransformer extends BodyTransformer {

    private final TransformersProvider transformersProvider;

    private final Map<String, ClasszInfo> infos = new HashMap<>();

    public SootBodiesTransformer(TransformersProvider transformersProvider) {
        this.transformersProvider = transformersProvider;
    }

    @Override
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        if (body.getMethod().isAbstract()) {
            return;
        }
        final String methodName = body.getMethod().getSignature();
        final String className = body.getMethod().getDeclaringClass().getName();

        final ClasszInfo ci = this.getInfos().getOrDefault(className, new ClasszInfo(className));
        final MethodInfo mi = ci.getMethods().getOrDefault(methodName, new MethodInfo(methodName));
        ci.getMethods().put(methodName, mi);
        infos.put(className, ci);

        final PatchingChain<Unit> units = body.getUnits();
        final Iterator<Unit> stmtIt = units.snapshotIterator();

        final TransformerContextt context = new TransformerContextt();
        context.classzInfo = ci;
        context.methodInfo = mi;
        context.body = body;

        final Transformer[] transformers = transformersProvider.getTransformers();

        Unit current = null;
        Unit prev = null;
        int no = 0;

        while (stmtIt.hasNext()) {
            current = stmtIt.next();
            no++;

            for (final Transformer transformer : transformers) {
                transformer.fillTransformations(context, current, prev, no);
            }

            prev = current;

        }

        for (final Transformation transformation : context.transformations) {
            transformation.apply(units);
        }

        final List<ValidationException> exceptionList = new ArrayList<>();
        body.validate(exceptionList);

    }

    public Map<String, ClasszInfo> getInfos() {
        return infos;
    }
}
