package doip.tester.unittests;

import static doip.junit.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import doip.library.message.DoipTcpDiagnosticMessageNegAck;
import doip.library.message.DoipUdpVehicleAnnouncementMessage;
import doip.tester.toolkit.CheckResult;
import doip.tester.toolkit.TestUtils;
import doip.tester.toolkit.event.DoipEventConnectionClosed;
import doip.tester.toolkit.event.DoipEventTcpAliveCheckResponse;
import doip.tester.toolkit.event.DoipEventTcpDiagnosticMessageNegAck;
import doip.tester.toolkit.event.DoipEventTcpRoutingActivationResponse;
import doip.tester.toolkit.event.DoipEventUdpVehicleAnnouncementMessage;


public class UT_4002_CheckEvent {
	
	@Test
	public void testNoErrorCaseIsNull() {
		assertEquals(CheckResult.NO_ERROR, TestUtils.checkEvent(
			null, 
			null).getCode());
	}
	
	@Test
	public void testNoErrorCaseIsNotNull() {
		assertEquals(CheckResult.NO_ERROR, TestUtils.checkEvent(
			new DoipEventTcpAliveCheckResponse(0, null), 
			DoipEventTcpAliveCheckResponse.class).getCode());
	}
	
	@Test
	public void testWrongEvent() {
		assertEquals(CheckResult.WRONG_EVENT, TestUtils.checkEvent(
				new DoipEventTcpAliveCheckResponse(0, null), 
				DoipEventTcpRoutingActivationResponse.class).getCode());
	}
	
	@Test
	public void testUnexpectedDoipMessage() {
		assertEquals(CheckResult.UNEXPECTED_DOIP_MESSAGE, TestUtils.checkEvent(
				new DoipEventTcpDiagnosticMessageNegAck(0, 
						new DoipTcpDiagnosticMessageNegAck(0, 0, 0, new byte[] {0x00})),
				null).getCode());
	}
	
	@Test
	public void testUnexpectedSocketClosed() {
		assertEquals(CheckResult.UNEXPECTED_SOCKET_CLOSED, TestUtils.checkEvent(
				new DoipEventConnectionClosed(0), 
				null).getCode());
	}
	
	@Test
	public void testNoTcpResponseReceived() {
		assertEquals(CheckResult.NO_TCP_RESPONSE_RECEIVED, TestUtils.checkEvent(
				null,
				DoipEventTcpAliveCheckResponse.class).getCode());
	}
	
	@Test
	public void testSocketNotClosed() {
		assertEquals(CheckResult.SOCKET_NOT_CLOSED, TestUtils.checkEvent(
				null, 
				DoipEventConnectionClosed.class).getCode());
	}
}
