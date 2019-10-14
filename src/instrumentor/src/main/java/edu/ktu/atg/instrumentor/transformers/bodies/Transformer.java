package edu.ktu.atg.instrumentor.transformers.bodies;

import soot.Unit;

public interface Transformer {
	void fillTransformations(TransformerContextt context, Unit current, Unit prev, int no);
}
