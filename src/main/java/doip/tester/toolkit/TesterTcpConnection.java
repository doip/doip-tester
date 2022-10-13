package doip.tester.toolkit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import doip.library.message.DoipTcpDiagnosticMessage;
import doip.library.message.DoipTcpDiagnosticMessagePosAck;
import doip.library.message.DoipTcpRoutingActivationRequest;
import doip.library.message.DoipTcpRoutingActivationResponse;
import doip.library.util.Helper;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventTcpDiagnosticMessage;
import doip.tester.toolkit.event.DoipEventTcpDiagnosticMessagePosAck;
import doip.tester.toolkit.event.DoipEventTcpRoutingActivationResponse;
import doip.tester.toolkit.exception.DiagnosticServiceExecutionFailed;
import doip.tester.toolkit.exception.RoutingActivationFailed;

public class TesterTcpConnection extends DoipTcpConnectionWithEventCollection {

	private static Logger logger = LogManager.getLogger(TesterTcpConnection.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT");

	private static int connectionCounter = 1;

	private TestConfig config = null;

	public TesterTcpConnection(TestConfig config) {
		super("TCP-TESTER-" + connectionCounter, 64);
		connectionCounter++;
		this.config = config;
	}

	/**
	 * Sends a routing activation request
	 *
	 * @param sourceAddress Source address of tester which is asking for
	 *                      routing activation
	 * @param activationType
	 * @param oemData OEM specific data
	 */

	public void sendRoutingActivationRequest(
			int sourceAddress, int activationType, long oemData) {

		try {
			logger.trace(enter, ">>> public void sendRoutingActivationRequest(int sourceAddress, int activationType, long oemData)");
	
			DoipTcpRoutingActivationRequest request = new DoipTcpRoutingActivationRequest(sourceAddress, activationType, oemData);
			logger.info("Send routing activation request");
			this.send(request);
		} finally {
			logger.trace(exit, "<<< public void sendRoutingActivationRequest(int sourceAddress, int activationType, long oemData)");
		}
	}

 	/**
	 * Performs a routing activation.
	 * @param activationType The activation type (see ISO 13400)
	 * @param expectedResponseCode The expected response code in the response.
	 *                             If the response code is different an AssertionError
	 *                             will be thrown.
	 * @return Returns true if routing activation was successful
	 * @throws InterruptedException
	 * @throws RoutingActivationFailed
	 */
	public DoipEventTcpRoutingActivationResponse performRoutingActivation(int address, int activationType) throws InterruptedException, RoutingActivationFailed {
		String function = "public DoipEventTcpRoutingActivationResponse performRoutingActivation(int activationType, int expectedResponseCode)";
		try {
			logger.trace(enter, ">>> " + function);

			// Clear the event queue
			this.clearEvents();

			this.sendRoutingActivationRequest(address, activationType, -1);

			// Wait for incoming TCP message
			DoipEvent event = null;
			try {
				event = this.waitForEvents(1, config.getRoutingActivationTimeout());
			} catch (InterruptedException e) {
				logger.error(Helper.getExceptionAsString(e));
				throw e;
			}
			if (event == null) {
				logger.error("No Routing Activation Response received");
				throw new RoutingActivationFailed(
						RoutingActivationFailed.NO_RESPONSE_RECEIVED,
						"No routing activation response received");
			}

			if (!(event instanceof DoipEventTcpRoutingActivationResponse)) {
				logger.error("Received event is not type of DoipEventTcpRoutingActivationResponse");
				throw new RoutingActivationFailed(
						RoutingActivationFailed.WRONG_RESPONSE_RECEIVED,
						"No routing activation response received");
			}

			// Check the response code which shall match to the expected response code
			DoipEventTcpRoutingActivationResponse eventRoutingActivationResponse = 
					(DoipEventTcpRoutingActivationResponse) event;
			return eventRoutingActivationResponse;
		} finally {
			logger.trace(exit, "<<< " + function);
		}
	}

	/**
	 * Override function and do nothing in the function because implementation
	 * in base class send negative acknowledge message. But tester should
	 * not do that.
	 */
	@Override
	public void onHeaderIncorrectPatternFormat() {
		String function = "public void onHeaderIncorrectPatternFormat()";
			logger.trace(">>> " + function);
			logger.trace("<<< " + function);
	}
	
	/*
	public void checkEvent(int index iint <?>  clazz) {
	xxx
		DoipEvent event = getEvent(0);
		if (clazz.isInstance(event)) {
			
		}
	}*/

	/**
	 * Executes a diagnostic service
	 * @param request
	 * @param responseExpected
	 * @return
	 * @throws DiagnosticServiceExecutionFailed
	 */

	public byte[] executeDiagnosticService(byte[] request) throws DiagnosticServiceExecutionFailed {

		try {
			logger.trace(enter, ">>> public byte[] executeDiagnosticService(byte[] request)");
			this.clearEvents();
			this.sendDiagnosticMessage(config.getTesterAddress(), config.getEcuAddressPhysical(), request);
			DoipEvent event = this.waitForEvents(1, config.get_A_DoIP_Diagnostic_Message());
			if (event == null) {
				DiagnosticServiceExecutionFailed ex =
						new DiagnosticServiceExecutionFailed(
								DiagnosticServiceExecutionFailed.NO_DIAG_MESSAGE_POS_ACK_RECEIVED,
								"No DoIP message received after sending diagnostic request");
				throw logger.throwing(Level.INFO, ex);
			}

			if (!(event instanceof DoipEventTcpDiagnosticMessagePosAck)) {
				DiagnosticServiceExecutionFailed ex =
						new DiagnosticServiceExecutionFailed(
								DiagnosticServiceExecutionFailed.NO_DIAG_MESSAGE_POS_ACK_RECEIVED,
								"Received Event was not of type DoipEventTcpDiagnosticMessagePosAck");
				throw logger.throwing(Level.INFO, ex);
			}

			DoipEventTcpDiagnosticMessagePosAck posAckEvent = (DoipEventTcpDiagnosticMessagePosAck) event;
			DoipTcpDiagnosticMessagePosAck posAckMsg = (DoipTcpDiagnosticMessagePosAck) posAckEvent.getDoipMessage();

			event = this.waitForEvents(2, config.get_A_DoIP_Diagnostic_Message());
			if (event == null) {
				DiagnosticServiceExecutionFailed ex =
						new DiagnosticServiceExecutionFailed(
								DiagnosticServiceExecutionFailed.NO_DIAG_MESSAGE_RECEIVED,
								"No event received after receiving the event DoipEventTcpDiagnosticMessagePosAck");
				throw logger.throwing(Level.INFO, ex);
			}

			if (!(event instanceof DoipEventTcpDiagnosticMessage)) {
				DiagnosticServiceExecutionFailed ex =
						new DiagnosticServiceExecutionFailed(
								DiagnosticServiceExecutionFailed.NO_DIAG_MESSAGE_RECEIVED,
								"Received Event was not of type DoipEventTcpDiagnosticMessage");
				throw logger.throwing(Level.INFO, ex);
			}

			DoipEventTcpDiagnosticMessage doipEventTcpDiagnosticMessage = (DoipEventTcpDiagnosticMessage) event;
			DoipTcpDiagnosticMessage doipTcpDiagnosticMessage = (DoipTcpDiagnosticMessage) doipEventTcpDiagnosticMessage.getDoipMessage();
			return doipTcpDiagnosticMessage.getDiagnosticMessage();

		} catch (InterruptedException e) {
			DiagnosticServiceExecutionFailed ex =
					new DiagnosticServiceExecutionFailed(
							DiagnosticServiceExecutionFailed.GENERAL_ERROR,
							TextBuilder.unexpectedException(e), e);
			throw logger.throwing(Level.FATAL, ex);
		} finally {
			logger.trace(exit, "<<< public byte[] executeDiagnosticService(byte[] request, boolean responseExpected)");
		}
	}

	public void sendDiagnosticMessage(int sourceAddress, int targetAddress, byte[] message) {
		try {
			logger.trace(enter, ">>> public void sendDiagnosticMessage(int sourceAddress, int targetAddress, byte[] message)");
			DoipTcpDiagnosticMessage request = new DoipTcpDiagnosticMessage(sourceAddress, targetAddress, message);
			this.send(request);
		} finally {
			logger.trace(exit, "<<< public void sendDiagnosticMessage(int sourceAddress, int targetAddress, byte[] message)");
		}
	}

}