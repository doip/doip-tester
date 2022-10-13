package doip.tester.unittests;
import static doip.junit.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventMessage;
import doip.tester.toolkit.event.DoipEventTcpAliveCheckRequest;
import doip.tester.toolkit.event.DoipEventTcpAliveCheckResponse;

public class UT_Evaluation {
	
	private static Logger logger = LogManager.getLogger(UT_Evaluation.class);
	private static Marker markerEnter = MarkerManager.getMarker("ENTER");
	private static Marker markerExit = MarkerManager.getMarker("EXIT"); 

	public static boolean checkEvent(DoipEvent event, Class<? extends DoipEvent> clazz) {
		return (DoipEventMessage.class.isAssignableFrom(clazz));
	}
	
	@Test
	public void test1() {
		assertTrue(checkEvent(null, DoipEventTcpAliveCheckRequest.class));
	}
	
	@Test
	public void test2() {
		try {
			logger.trace(markerEnter, ">>> public void test2()");
			assertTrue(false, "This line of code will throw an AssertionFailedError");
		} catch (Exception e) {
			logger.info("Assertion has been caught");
		} finally {
			logger.trace(markerExit, "<<< public void test2()");
		}
	}
	
	
}
