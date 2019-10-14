package edu.ktu.atg.common.monitors;

import edu.ktu.atg.common.models.ExecutionResults;

public interface IBaseMonitor {
	void clear();

	void fill(ExecutionResults results);
}
