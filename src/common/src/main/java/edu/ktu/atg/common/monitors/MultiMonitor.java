package edu.ktu.atg.common.monitors;

import edu.ktu.atg.common.models.ExecutionResults;

public enum MultiMonitor implements IBaseMonitor {
	INSTANCE;
	private final IBaseMonitor[] monitors = { ValuesMonitor.INSTANCE, BranchesMonitor.INSTANCE };

	@Override
	public void clear() {
		for (IBaseMonitor monitor : monitors) {
			monitor.clear();
		}
	}

	@Override
	public void fill(ExecutionResults results) {
		for (IBaseMonitor monitor : monitors) {
			monitor.fill(results);
		}

	}
}
