package spacek.quartz.result;

public interface ExecutionResultStoreObserver<T> {
	public void observed(ExecutionResult<T> result);
	public Class<T> getResultClass();
}
