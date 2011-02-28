package spacek.quartz.result;

public class ExecutionResult<T> {
	private T result;
	private Exception error;

	public ExecutionResult() {
	}

	public ExecutionResult(T result, Exception error) {
		this.result = result;
		this.error = error;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Exception getError() {
		return error;
	}

	public void setError(Exception error) {
		this.error = error;
	}
}
