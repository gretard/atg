package edu.ktu.atg.instrumentor.transformers.bodies;

import java.util.ArrayList;
import java.util.List;

import edu.ktu.atg.common.models.ClasszInfo;
import edu.ktu.atg.common.models.MethodInfo;
import edu.ktu.atg.instrumentor.transformers.Transformation;
import soot.Body;

public class TransformerContextt {

    public MethodInfo methodInfo;

    public ClasszInfo classzInfo;

    public Body body;

    public final List<Transformation> transformations = new ArrayList<>();
}
