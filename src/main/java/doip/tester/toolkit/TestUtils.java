package doip.tester.toolkit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventConnectionClosed;
import doip.tester.toolkit.event.DoipEventMessage;
import doip.tester.toolkit.event.DoipEventTcpMessage;
import doip.tester.toolkit.event.DoipEventUdpMessage;

public class TestUtils {

	private static Logger logger = LogManager.getLogger(TestUtils.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit  = MarkerManager.getMarker("EXIT");
	

	public static CheckResult checkEvent(DoipEvent event, Class<? extends DoipEvent> clazz) {
		try {
			logger.trace(enter, ">>> public void checkEvent(DoipEvent event, Class<? extends DoipEvent> clazz)");
		
			if (event != null) {
				return checkEventIsNotNull(event, clazz);
			} else {
				return checkEventIsNull(clazz);
			}

		} finally {
			logger.trace(exit, "<<< public void checkEvent(DoipEvent event, Class<? extends DoipEvent> clazz)");
			
		}
	}
	
	private static CheckResult checkEventIsNotNull(DoipEvent event, Class<? extends DoipEvent> clazz) {
		try {
			logger.trace(enter, ">>> private static int checkEventIsNotNull(DoipEvent event, Class<? extends DoipEvent> clazz)");
			if (clazz != null) {
				return checkEventIsNotNullAndClassIsNotNull(event, clazz);
			} else {
				return checkEventIsNotNullAndClassIsNull(event);
			}
		} finally {
			logger.trace(exit, "<<< private static int checkEventIsNotNull(DoipEvent event, Class<? extends DoipEvent> clazz)");
		}
	}
	
	private static CheckResult checkEventIsNotNullAndClassIsNotNull(DoipEvent event, Class<? extends DoipEvent> clazz) {
		if (clazz.isInstance(event)) {
			String text = "A event of type '" + event.getClass().getSimpleName() + "' has been receive which was the expected event"; 
			logger.info(text);
			return new CheckResult(CheckResult.NO_ERROR, text);
		} else {
			String text = "It was expected to receive a event of type '" + clazz.getSimpleName()+ "', but a event of type '" + event.getClass().getSimpleName() + "' has been received";
			logger.error(text);
			return new CheckResult(CheckResult.WRONG_EVENT, text);
		}		
	}
	
	public static CheckResult checkEventIsNotNullAndClassIsNull(DoipEvent event) {
		if (event instanceof DoipEventMessage) {
			DoipEventMessage eventMessage = (DoipEventMessage) event;
			String text = "It was expected to receive no response, but instead a '" + eventMessage.getDoipMessage().getMessageName() + "' has been received"; 
			logger.error(text);
			return new CheckResult(CheckResult.UNEXPECTED_DOIP_MESSAGE, text);
		} else if (event instanceof DoipEventConnectionClosed) {
			String text = "It was expected to receive no response, but instead the socket has been closed";
			logger.error(text);
			return new CheckResult(CheckResult.UNEXPECTED_SOCKET_CLOSED, text);
		} else {
			String text = "A unknown event did occur, class = " + event.getClass().getName();
			logger.fatal(text);
			return new CheckResult(CheckResult.UNKNOWN_EVENT, text);
		}		
	}
	
	private static CheckResult checkEventIsNull(Class<? extends DoipEvent> clazz) {
		if (clazz != null) {
			// 	Check if it a DoipEventMessage was expected
			if (DoipEventUdpMessage.class.isAssignableFrom(clazz)) {
				String text = "It was expected to receive a valid DoIP UDP message, but no valid DoIP UDP message has been received"; 
				logger.info(text);
				return new CheckResult(CheckResult.NO_UDP_RESPONSE_RECEIVED, text);
			} else if (DoipEventTcpMessage.class.isAssignableFrom(clazz)) {
				String text = "It was expected to receive a valid DoIP TCP message, but no valid DoIP TCP message has been received"; 
				logger.error(text);
				return new CheckResult(CheckResult.NO_TCP_RESPONSE_RECEIVED, text);
			} else if (DoipEventConnectionClosed.class.isAssignableFrom(clazz)) {
				String text = "It was expected that the socket has been closed, but it hasn't been closed"; 
				logger.error(text);
				return new CheckResult(CheckResult.SOCKET_NOT_CLOSED, text);
			} else {
				String text = "An unknown event class has been passed"; 
				logger.fatal(text);
				return new CheckResult(CheckResult.UNKOWN_EVENT_CLASS, text);
			}
		} else {
			String text = "No event did occur which is the expected result"; 
			logger.info(text);
			return new CheckResult(CheckResult.NO_ERROR, text);
		}
	}
}
