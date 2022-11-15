package doip.tester.testcases;

import static doip.junit.Assertions.*;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.tester.toolkit.CheckResult;
import doip.tester.toolkit.EventChecker;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventTcpDiagnosticMessageNegAck;
import doip.tester.toolkit.event.DoipEventTcpDiagnosticMessagePosAck;
import doip.tester.toolkit.exception.DiagnosticServiceExecutionFailed;
import doip.tester.toolkit.exception.RoutingActivationFailed;
import doip.junit.Assertions;
import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.message.DoipMessage;
import doip.library.message.DoipTcpDiagnosticMessage;
import doip.library.message.DoipTcpDiagnosticMessageNegAck;
import doip.library.message.DoipTcpRoutingActivationRequest;
import doip.library.util.StringConstants;

public class TC_2070_DiagnosticMessage {
	
	public static final String BASE_ID = "2070";

	private static Logger logger = LogManager.getLogger(TC_2000_VehicleIdentification.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT");
	
	private static TestSetup testSetup = null;
	private static TestConfig config = null;

	@BeforeAll
	public static void setUpBeforeClass() throws InitializationError {
		String function = "static void setUpBeforeClass()"; 
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(enter, ">>> " + function);
			testSetup = new TestSetup();
			testSetup.initialize();
			config = testSetup.getConfig();
		} finally {
			logger.trace(exit, "<<< " + function);
		}
	}

	@AfterAll
	public static void tearDownAfterClass() {
		String function = "static void tearDownAfterClass()";
		try {
			logger.trace(enter, ">>> " + function);
			if (testSetup != null) {
				testSetup.uninitialize();
				testSetup = null;
			}
		} finally {
			logger.trace(exit, "<<< " + function);
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

	@BeforeEach
	public void setUp() {
	}

	@AfterEach
	public void tearDown() {
	}

	@Test
	@DisplayName("TC-" + BASE_ID + "-01")
	public void testDiagnosticMessage() throws TestExecutionError {
		String function = "void testDiagnosticMessage()";
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> " + function);
			
			desc = new TestCaseDescription(
					"TC-" + BASE_ID + "-01", 
					"Send diagnostic mesage after successful routing activation", 
					"1. Send routing activation 2. Send diagnostic message 0x10 0x03", 
					"1. Routing activation was successful 2. Response from DoIP server is 0x50 0x03 ...");
			desc.logHeader();
			
			testDiagnosticMessageImpl();
			
			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} finally { 
			logger.trace(exit, "<<< " + function);
		}
	}
	
	public void testDiagnosticMessageImpl() throws TestExecutionError {
		TesterTcpConnection conn = null;
		try {
			logger.trace(enter, ">>> public void testDiagnosticMessageImpl()");
			int sourceAddress = config.getTesterAddress();
			int targetAddress = config.getEcuAddressPhysical();
			
			conn = testSetup.createTesterTcpConnection();
			conn.performRoutingActivation(sourceAddress, 0);
			
			TestFunctions.executeDiagnosticServiceAndCheckForPosAckWithDiagResponse(conn, config, new byte[] {0x10, 0x03 });
			
			
		} catch (RoutingActivationFailed e) {
			fail("It was expected that routing activation will succeed, but it seems that it wasn't successfull");
		} catch (IOException | InterruptedException e) {
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			if (conn != null) {
				testSetup.removeDoipTcpConnectionTest(conn);
			}
			logger.trace(exit, ">>> public void testDiagnosticMessageImpl()");
		}
	}
	
	@Test
	@DisplayName("TC-" + BASE_ID + "-02")
	public void testDiagnosticessageWithoutRoutingActivation() throws TestExecutionError{
		TestCaseDescription desc = null;
		String function  = "void testDiagnosticessageWithoutRoutingActivation()";
		try {
			logger.trace(enter, ">>> " + function);
			
			desc = new TestCaseDescription(
				"TC-" + BASE_ID + "-02",
				"Diagnostic message without routing activation",
				
				"Open a TCP connection to the DoIP server and send a '" 
				+ DoipTcpDiagnosticMessage.getMessageNameOfClass() 
				+ "' (so we skip sending the '" 
				+ DoipTcpRoutingActivationRequest.getMessageNameOfClass() + "')",
				
				"The DoIP server sends a '" 
				+ DoipTcpDiagnosticMessageNegAck.getMessageNameOfClass() 
				+ "' with NACK code 0x02 (Invalid source address)");
			
			desc.logHeader();
			
			TesterTcpConnection conn = testSetup.createTesterTcpConnection();
			
			int testerAddress = config.getTesterAddress();
			int ecuAdress = config.getEcuAddressPhysical();
			byte[] diagRequest = new byte[] {0x10, 0x30};
			
			conn.sendDiagnosticMessage(testerAddress, ecuAdress, diagRequest);
			DoipEvent event = conn.waitForEvents(1, config.get_A_DoIP_Diagnostic_Message());
			CheckResult checkResult = EventChecker.checkEvent(event, DoipEventTcpDiagnosticMessageNegAck.class);
			if (checkResult.getCode() != CheckResult.NO_ERROR) {
				fail(checkResult.getText());
			}
			DoipEventTcpDiagnosticMessageNegAck negAckEvent =
					(DoipEventTcpDiagnosticMessageNegAck) event;
			DoipMessage doipMsg = negAckEvent.getDoipMessage();
			DoipTcpDiagnosticMessageNegAck doipMsgNegAck = (DoipTcpDiagnosticMessageNegAck) doipMsg;
			assertEquals(0x02, doipMsgNegAck.getAckCode(), 
					"The NACK code in the '" + DoipTcpDiagnosticMessageNegAck.getMessageNameOfClass() 
					+ "' is wrong. It was expected that the NACK code is "
					+ "0x02 (invalid source address), but the actual NACK code is "
					+ String.format("0x%02X", doipMsgNegAck.getAckCode()) + ".");
			
			// TODO: Check data of repeated request message in the neg. ack. message
			
			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (IOException e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} catch (InterruptedException e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(Level.FATAL, new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally { 
			logger.trace(exit, "<<< " + function);
		}
	}
}