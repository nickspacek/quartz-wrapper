package spacek.quartz.quartzwrapper;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.quartz.Scheduler;

import spacek.quartz.Execution;
import spacek.quartz.result.ExecutionResult;
import spacek.quartz.result.ExecutionResultStoreObserver;
import spacek.quartz.result.ResultStoringJobListener;

public class QuartzResult<T> implements Future<T>,
		ExecutionResultStoreObserver<T> {

	private Execution execution;
	private Scheduler scheduler;
	private ResultStoringJobListener listener;
	private T result;
	private Class<T> resultClass;
	private Exception error;
	private boolean done;
	private boolean cancelled;

	public QuartzResult(Class<T> resultClass, Execution execution,
			Scheduler scheduler, ResultStoringJobListener listener) {
		this.resultClass = resultClass;
		this.execution = execution;
		this.scheduler = scheduler;
		this.listener = listener;

		listener.addObserver(execution, this);
	}

	@Override
	public synchronized boolean cancel(boolean arg0) {
		// TODO implement cancelling
		return false;
	}

	@Override
	public synchronized T get() throws InterruptedException, ExecutionException {
		return getValue(null);
	}

	@Override
	public synchronized T get(long amount, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		// TODO convert amount to milliseconds
		return getValue(amount);
	}

	private T getValue(Long wait) throws InterruptedException,
			ExecutionException {
		while (!done && !cancelled) {
			if (wait == null) {
				wait();
			} else {
				wait(wait);
			}
		}

		if (cancelled) {
			// TODO determine correct exception
			throw new CancellationException("Job was cancelled");
		}

		if (error != null) {
			// TODO wrap job exception in executionexception
			throw new RuntimeException("Job threw exception", error);
		}

		return result;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public boolean isDone() {
		return done;
	}

	@Override
	public synchronized void observed(ExecutionResult<T> result) {
		this.result = result.getResult();
		this.error = result.getError();
		done = true;

		// TODO notifyAll the correct method?
		notifyAll();
	}

	@Override
	public Class<T> getResultClass() {
		return resultClass;
	}

}
