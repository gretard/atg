package edu.ktu.atg.instrumentor.transformers;

import edu.ktu.atg.instrumentor.transformers.bodies.Transformer;

public class DefinedTransformersProvider implements TransformersProvider {

	private final Transformer[] transformers;

	public DefinedTransformersProvider(Transformer... transformers) {
		this.transformers = transformers;

	}

	public Transformer[] getTransformers() {
		return transformers;
	}
}
