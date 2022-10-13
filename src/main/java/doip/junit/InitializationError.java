package doip.junit;

public class InitializationError extends Exception {


	private static final long serialVersionUID = -6918593582179157084L;

	public InitializationError(String message, Exception e) {
		super(message, e);
	}

	public InitializationError(Throwable e) {
		super(e);
	}

	public InitializationError(String message) {
		super(message);
	}
}
