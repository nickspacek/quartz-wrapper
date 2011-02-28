package spacek.quartz.result;

import spacek.quartz.Execution;

public interface ExecutionResultStore {
	public void put(Execution execution, ExecutionResult<?> result);
	public ExecutionResult<?> get(Execution execution);
	public boolean contains(Execution execution);
}
