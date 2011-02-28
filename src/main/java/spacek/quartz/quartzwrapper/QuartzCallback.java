package spacek.quartz.quartzwrapper;

import spacek.quartz.result.ExecutionResult;

public interface QuartzCallback<T> {
	public void call(ExecutionResult<T> result);
}
