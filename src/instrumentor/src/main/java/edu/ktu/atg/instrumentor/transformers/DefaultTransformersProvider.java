package edu.ktu.atg.instrumentor.transformers;

import edu.ktu.atg.instrumentor.transformers.bodies.BranchesTransformer;
import edu.ktu.atg.instrumentor.transformers.bodies.OtherStatementsTransformer;
import edu.ktu.atg.instrumentor.transformers.bodies.ReturnValueTransformer;
import edu.ktu.atg.instrumentor.transformers.bodies.SwitchValueTransformer;
import edu.ktu.atg.instrumentor.transformers.bodies.Transformer;

public class DefaultTransformersProvider implements TransformersProvider {

    public Transformer[] getTransformers() {
        return new Transformer[] { new BranchesTransformer(), new ReturnValueTransformer(),
                new SwitchValueTransformer(), new OtherStatementsTransformer() };
    }
}
