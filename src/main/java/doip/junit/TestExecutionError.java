package doip.junit;

public class TestExecutionError extends Exception {

	private static final long serialVersionUID = -7601713880434036686L;

	public TestExecutionError(String message) {
		super(message);
	}
	
	public TestExecutionError(String message, Throwable cause) {
		super(message, cause);
	}
	
	public TestExecutionError(Throwable cause) {
		super(cause);
	}
}
