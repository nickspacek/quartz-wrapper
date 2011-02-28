package spacek.quartz.quartzwrapper;

import java.util.concurrent.Callable;

import spacek.quartz.result.ExecutionResult;
import spacek.quartz.result.ExecutionResultStoreObserver;

public class QuartzCallbackWrapper<T> implements Callable<T>,
		ExecutionResultStoreObserver<T> {

	private QuartzCallback<T> callback;
	private Class<T> resultClass;

	public QuartzCallbackWrapper(QuartzCallback<T> callback,
			Class<T> resultClass) {
		this.callback = callback;
		this.resultClass = resultClass;
	}

	@Override
	public void observed(ExecutionResult<T> result) {
		// TODO Execute in a new thread 
		callback.call(result);
	}

	@Override
	public Class<T> getResultClass() {
		return resultClass;
	}

	@Override
	public T call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
