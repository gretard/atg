package edu.ktu.atg.instrumentor.transformers;

import java.util.ArrayList;
import java.util.List;

import soot.PatchingChain;
import soot.Unit;
import soot.JastAddJ.ReturnStmt;

public class Transformation {

	private final List<Unit> unitsToAdd = new ArrayList<Unit>();

	private final boolean isAfter;

	private final Unit parent;

	public List<Unit> getUnitsToAdd() {
		return unitsToAdd;
	}

	public Transformation(final Unit parent, final boolean isAfter) {
		this.parent = parent;
		this.isAfter = isAfter;
	}

	public void apply(final PatchingChain<Unit> units) {
		if (this.unitsToAdd.isEmpty()) {
			return;
		}
		if (this.isAfter && !(parent instanceof ReturnStmt)) {
			units.insertAfter(this.unitsToAdd, this.parent);
			return;
		}

		units.insertBefore(this.unitsToAdd, this.parent);
	}

}
