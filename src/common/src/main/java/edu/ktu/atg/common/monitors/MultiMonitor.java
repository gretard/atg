package edu.ktu.atg.common.monitors;

import edu.ktu.atg.common.execution.SolutionExecutionData;

public enum MultiMonitor implements IBaseMonitor {
	INSTANCE;
	private final IBaseMonitor[] monitors = { ValuesMonitor.INSTANCE, BranchesMonitor.INSTANCE };

	@Override
	public synchronized void  clear() {
		for (IBaseMonitor monitor : monitors) {
			monitor.clear();
		}
	}

	@Override
	public synchronized void fill(SolutionExecutionData results) {
		for (IBaseMonitor monitor : monitors) {
			monitor.fill(results);
		}

	}
}
