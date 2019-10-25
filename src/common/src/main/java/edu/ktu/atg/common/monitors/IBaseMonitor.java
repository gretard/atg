package edu.ktu.atg.common.monitors;

import edu.ktu.atg.common.execution.SolutionExecutionData;

public interface IBaseMonitor {
	void clear();

	void fill(SolutionExecutionData results);
}
