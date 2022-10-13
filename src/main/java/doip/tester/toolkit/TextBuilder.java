package doip.tester.toolkit;

public class TextBuilder {
	
	public static String noMessageReceived(String message) {
		return "It was expected to receive a " + message + ", but no message has been received.";
	}
	
	public static String wrongMessageReceived(String expected, String actual) {
		return "It was expected to receive a " + expected + ", but instead a " + actual + " has been received.";
	}
	
	public static String unexpectedException(Throwable e) {
		return "Unexpected " + e.getClass().getName() + ": " + e.getMessage();
	}

}
