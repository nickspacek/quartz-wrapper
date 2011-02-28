package spacek.quartz.result;

import org.quartz.JobListener;

import spacek.quartz.Execution;

public interface ResultStoringJobListener extends JobListener, ExecutionResultStore {
	public void addObserver(Execution execution, ExecutionResultStoreObserver<?> observer);
}
