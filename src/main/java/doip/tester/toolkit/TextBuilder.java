package doip.tester.toolkit;

public class TextBuilder {
	
	public String noMessageReceived(String message) {
		return "It was expected to receive a " + message + ", but no message has been received.";
	}
	
	public String wrongMessageReceived(String expected, String actual) {
		return "It was expected to receive a " + expected + ", but instead a " + actual + " has been received.";
	}

}
