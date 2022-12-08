package doip.tester.testcases;

import static doip.junit.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import doip.junit.TestExecutionError;
import doip.library.message.DoipTcpDiagnosticMessage;
import doip.library.message.DoipTcpDiagnosticMessageNegAck;
import doip.library.message.DoipTcpDiagnosticMessagePosAck;
import doip.library.message.DoipTcpHeaderNegAck;
import doip.library.message.DoipTcpRoutingActivationRequest;
import doip.library.message.DoipTcpRoutingActivationResponse;
import doip.tester.toolkit.CheckResult;
import doip.tester.toolkit.DoipTcpConnectionWithEventCollection;
import doip.tester.toolkit.EventChecker;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventTcpDiagnosticMessage;
import doip.tester.toolkit.event.DoipEventTcpDiagnosticMessageNegAck;
import doip.tester.toolkit.event.DoipEventTcpDiagnosticMessagePosAck;
import doip.tester.toolkit.event.DoipEventTcpHeaderNegAck;
import doip.tester.toolkit.event.DoipEventTcpRoutingActivationResponse;

public class TestFunctions {
	
	private static Logger logger = LogManager.getLogger(TestFunctions.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT");
	
	public static DoipTcpRoutingActivationResponse 
			performRoutingActivation(
					DoipTcpConnectionWithEventCollection conn,
					TestConfig config,
					int activationType,
					long oemData) 
							throws TestExecutionError {
		try {
			logger.trace(enter, ">>> public static DoipTcpRoutingActivationResponse performRoutingActivation(DoipTcpConnectionWithEventCollection conn, TestConfig config, int activationType, long oemData)");
			conn.clearEvents();
			
			int sourceAddress = config.getTesterAddress();
			DoipTcpRoutingActivationRequest requestMsg = new DoipTcpRoutingActivationRequest(sourceAddress, activationType, oemData);
			conn.send(requestMsg);
			DoipEvent event = conn.waitForEvents(1, 2000);
			CheckResult result = EventChecker.checkEvent(event, DoipEventTcpRoutingActivationResponse.class);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}
			DoipEventTcpRoutingActivationResponse eventRoutingActivationResponse =
					(DoipEventTcpRoutingActivationResponse) event;
			DoipTcpRoutingActivationResponse response = 
					(DoipTcpRoutingActivationResponse) 
					eventRoutingActivationResponse.getDoipMessage();
			return response;
		} catch (InterruptedException e) {
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));		
		} finally {
			logger.trace(exit, "<<< public static DoipTcpRoutingActivationResponse performRoutingActivation(DoipTcpConnectionWithEventCollection conn, TestConfig config, int activationType, long oemData)");
		}
	}
	
	public static DoipTcpHeaderNegAck 
			sendDataAndCheckForGenericDoipHeaderNegAck(
					DoipTcpConnectionWithEventCollection conn,
					byte[] message,
					int timeout) 
							throws TestExecutionError {
		try {
			logger.trace(enter, ">>> public static DoipTcpHeaderNegAck sendDataAndCheckForGenericDoipHeaderNegAck(DoipTcpConnectionWithEventCollection conn, byte[] message, int timeout)");
			conn.clearEvents();
			conn.send(message);
			DoipEvent event = conn.waitForEvents(1, timeout);
			CheckResult result = EventChecker.checkEvent(event, DoipEventTcpHeaderNegAck.class);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}
			DoipEventTcpHeaderNegAck eventNegAck = (DoipEventTcpHeaderNegAck) event;
			DoipTcpHeaderNegAck msgNegAck = (DoipTcpHeaderNegAck) eventNegAck.getDoipMessage();
			return msgNegAck;
		} catch (InterruptedException e) {
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));		
		} finally {
			logger.trace(exit, "<<< public static DoipTcpHeaderNegAck sendDataAndCheckForGenericDoipHeaderNegAck(DoipTcpConnectionWithEventCollection conn, byte[] message, int timeout)");
		}
	}
	
	public static DoipTcpDiagnosticMessage executeDiagnosticServiceAndCheckForPosAckWithDiagResponse(
			DoipTcpConnectionWithEventCollection conn,
			TestConfig config,
			byte[] diagRequestMessage) throws TestExecutionError {
		
			try {
				logger.trace(enter, ">>> public static DoipTcpDiagnosticMessage executeDiagnosticServiceAndCheckForPosAckWithDiagResponse("
						+ "DoipTcpConnectionWithEventCollection conn,"
						+ " TestConfig config,"
						+ "	byte[] diagRequestMessage)");
				int sourceAddress = config.getTesterAddress();
				int targetAdress = config.getEcuAddressPhysical();
				int timeoutPosAck = config.get_A_DoIP_Diagnostic_Message();
				int timeoutDiagResponse = 2000;
			
				return executeDiagnosticServiceAndCheckForPosAckWithDiagResponse(conn, sourceAddress, targetAdress, diagRequestMessage, timeoutPosAck, timeoutDiagResponse);
			} finally {
				logger.trace(exit, "<<< public static DoipTcpDiagnosticMessage executeDiagnosticServiceAndCheckForPosAckWithDiagResponse("
						+ "	DoipTcpConnectionWithEventCollection conn,"
						+ " TestConfig config,"
						+ "	byte[] diagRequestMessage)");
				
			}
	}

	public static DoipTcpDiagnosticMessage executeDiagnosticServiceAndCheckForPosAckWithDiagResponse(
			DoipTcpConnectionWithEventCollection conn,
			int sourceAddress,
			int targetAddress,
			byte[] diagRequestMessage,
			int timeoutPosAck,
			int timeoutDiagResponse) throws TestExecutionError {
		
		String function = "public static DoipTcpDiagnosticMessage executeDiagnosticServiceAndCheckForPosAckWithDiagResponse("
				+ "DoipTcpConnectionWithEventCollection conn,"
				+ "	int sourceAddress,"
				+ "	int targetAddress,"
				+ "	byte[] diagRequestMessage,"
				+ "	int timeoutPosAck,"
				+ "	int timeoutDiagResponse)";
		try {
			logger.trace(enter, ">>> " + function);
			conn.clearEvents();
			DoipTcpDiagnosticMessage doipMessage = new DoipTcpDiagnosticMessage(sourceAddress, targetAddress, diagRequestMessage);
			conn.send(doipMessage);
			DoipEvent event = conn.waitForEvents(1, timeoutPosAck);
			CheckResult result = EventChecker.checkEvent(event, DoipEventTcpDiagnosticMessagePosAck.class);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}
			DoipEventTcpDiagnosticMessagePosAck eventPosAck = (DoipEventTcpDiagnosticMessagePosAck) event;
			DoipTcpDiagnosticMessagePosAck msgPosAck = (DoipTcpDiagnosticMessagePosAck) eventPosAck.getDoipMessage();
			int ackCode = msgPosAck.getAckCode();
			assertEquals(0x00, ackCode, "The ACK code in the message '" + DoipTcpDiagnosticMessagePosAck.getMessageNameOfClass() + "' isn't 0x00, which was expected to be 0x00. Instead it is " + String.format("0x%02X", ackCode) + ".");
			
			DoipTcpDiagnosticMessage diagResponse = waitForTcpDiagnosticMessage(conn, 2, timeoutDiagResponse);
			return diagResponse;		
			
			
		} catch (InterruptedException e) {
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< " + function);
		}
	}
	
	public static DoipTcpDiagnosticMessage waitForTcpDiagnosticMessage(
			DoipTcpConnectionWithEventCollection conn,
			int expectedNumberOfEventsInBuffer,
			int timeout) throws TestExecutionError {
		try {
			DoipEvent event = conn.waitForEvents(expectedNumberOfEventsInBuffer, timeout);
			CheckResult result = EventChecker.checkEvent(event, DoipEventTcpDiagnosticMessage.class);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}
			DoipTcpDiagnosticMessage diagResponse = (DoipTcpDiagnosticMessage) ((DoipEventTcpDiagnosticMessage) event).getDoipMessage();
			return diagResponse;		
		} catch (InterruptedException e) {
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			
		}
	}
	
	public static DoipTcpDiagnosticMessageNegAck executeDiagnosticServiceAndCheckforNegAck(
			DoipTcpConnectionWithEventCollection conn,
			TestConfig config,
			byte[] diagRequestMessage) throws TestExecutionError {
	
			try {
				logger.trace(enter, ">>> public static DoipTcpDiagnosticMessageNegAck executeDiagnosticServiceAndCheckforNegAck("
						+ "DoipTcpConnectionWithEventCollection conn,"
						+ "	TestConfig config,"
						+ " byte[] diagRequestMessage");
				int sourceAddress = config.getTesterAddress();
				int targetAddress = config.getEcuAddressPhysical();
				int timeoutNegAck = config.get_A_DoIP_Diagnostic_Message();
				
				return executeDiagnosticServiceAndCheckforNegAck(
						conn, sourceAddress, targetAddress, diagRequestMessage, timeoutNegAck);
			} finally {
				logger.trace(exit, "<<< public static DoipTcpDiagnosticMessageNegAck executeDiagnosticServiceAndCheckforNegAck("
						+ "DoipTcpConnectionWithEventCollection conn,"
						+ "	TestConfig config,"
						+ " byte[] diagRequestMessage");
			}
	}	
	
	public static DoipTcpDiagnosticMessageNegAck executeDiagnosticServiceAndCheckforNegAck(
			DoipTcpConnectionWithEventCollection conn,
			int sourceAddress,
			int targetAddress,
			byte[] diagRequestMessage,
			int timeoutNegAck) throws TestExecutionError {
		
		String function = "public static DoipTcpDiagnosticMessageNegAck executeDiagnosticServiceAndCheckforNegAck("
				+ "DoipTcpConnectionWithEventCollection conn,"
				+ "	int sourceAddress,"
				+ "	int targetAddress,"
				+ "	byte[] diagRequestMessage,"
				+ "	int timeoutNegAck)";
		try {
			logger.trace(enter, ">>> " + function);
			conn.clearEvents();
			DoipTcpDiagnosticMessage doipMessage = new DoipTcpDiagnosticMessage(sourceAddress, targetAddress, diagRequestMessage);
			conn.send(doipMessage);
			DoipEvent event = conn.waitForEvents(1, timeoutNegAck);
			CheckResult result = EventChecker.checkEvent(event, DoipEventTcpDiagnosticMessageNegAck.class);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}
			DoipEventTcpDiagnosticMessageNegAck eventNegAck = (DoipEventTcpDiagnosticMessageNegAck) event;
			DoipTcpDiagnosticMessageNegAck msgNegAck = (DoipTcpDiagnosticMessageNegAck) eventNegAck.getDoipMessage();
			return msgNegAck;
		} catch (InterruptedException e) {
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< " + function);
		}
	}
}
  