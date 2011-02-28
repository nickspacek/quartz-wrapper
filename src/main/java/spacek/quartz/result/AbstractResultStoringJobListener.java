package spacek.quartz.result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import spacek.quartz.Execution;

public class AbstractResultStoringJobListener implements
		ResultStoringJobListener {

	private ConcurrentMap<Execution, ExecutionResult<?>> resultMap = new ConcurrentHashMap<Execution, ExecutionResult<?>>();
	private ConcurrentMap<Execution, List<ExecutionResultStoreObserver<?>>> observers = new ConcurrentHashMap<Execution, List<ExecutionResultStoreObserver<?>>>();

	@Override
	public String getName() {
		return ResultStoringJobListener.class.getName();
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException exception) {
		Execution execution = new Execution(context.getJobDetail(),
				context.getTrigger());
		ExecutionResult<?> result = new ExecutionResult<Object>(
				context.getResult(), exception);
		put(execution, result);
	}

	@SuppressWarnings("unchecked")
	private <T> void notifyObservers(Execution execution,
			ExecutionResult<T> result) {
		if (observers.containsKey(execution)) {
			List<ExecutionResultStoreObserver<?>> executionObservers = observers
					.get(execution);
			for (ExecutionResultStoreObserver<?> observer : executionObservers) {
				if (result.getResult() == null
						|| observer.getResultClass().isAssignableFrom(
								result.getResult().getClass())) {
					((ExecutionResultStoreObserver<T>) observer)
							.observed(result);
				} else {
					// TODO something better
					throw new RuntimeException(
							"Observer encountered that can't handle result type");
				}
			}
		}
	}

	@Override
	public void addObserver(Execution execution,
			ExecutionResultStoreObserver<?> observer) {
		if (execution == null) {
			throw new NullPointerException("execution");
		}
		if (observer == null) {
			throw new NullPointerException("observer");
		}

		List<ExecutionResultStoreObserver<?>> executionObservers;
		if (observers.containsKey(execution)) {
			executionObservers = observers.get(execution);
		} else {
			executionObservers = new ArrayList<ExecutionResultStoreObserver<?>>();
			observers.put(execution, executionObservers);
		}

		executionObservers.add(observer);
	}

	@Override
	public void put(Execution execution, ExecutionResult<?> result) {
		resultMap.put(execution, result);
		notifyObservers(execution, result);
	}

	@Override
	public ExecutionResult<?> get(Execution execution) {
		return resultMap.get(execution);
	}

	@Override
	public boolean contains(Execution execution) {
		return resultMap.containsKey(execution);
	}
}
