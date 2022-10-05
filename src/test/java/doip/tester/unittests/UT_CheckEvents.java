package doip.tester.unittests;
import static doip.junit.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventMessage;
import doip.tester.toolkit.event.DoipEventTcpAliveCheckRequest;
import doip.tester.toolkit.event.DoipEventTcpAliveCheckResponse;

public class UT_CheckEvents {

	public static boolean checkEvent(DoipEvent event, Class<? extends DoipEvent> clazz) {
		return (DoipEventMessage.class.isAssignableFrom(clazz));
	}
	
	@Test
	public void test1() {
		assertTrue(checkEvent(null, DoipEventTcpAliveCheckRequest.class));
	}
	
	public void test2() {
	}
}
