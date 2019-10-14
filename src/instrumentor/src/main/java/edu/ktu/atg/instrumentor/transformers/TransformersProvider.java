package edu.ktu.atg.instrumentor.transformers;

import edu.ktu.atg.instrumentor.transformers.bodies.Transformer;

public interface TransformersProvider {
	Transformer[] getTransformers();
}
