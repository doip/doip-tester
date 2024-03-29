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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.message.DoipTcpDiagnosticMessage;
import doip.library.message.DoipTcpHeaderNegAck;
import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.properties.MissingSystemProperty;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.event.DoipEventTcpDiagnosticMessage;
import doip.tester.toolkit.exception.DiagnosticServiceExecutionFailed;
import doip.tester.toolkit.exception.RoutingActivationFailed;

public class TC_9020_LongTcpMessage {
	
	private static Logger logger = LogManager.getLogger(TC_9020_LongTcpMessage.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT");
	
	private static TestSetup setup = null;
	private static TestConfig config = null;
	private TesterTcpConnection conn = null;
	
	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		try {
			logger.trace(enter, ">>> public void setUpBeforeAll()");
			setup = new TestSetup();
			setup.initialize();
			config = setup.getConfig();
		} catch (IOException | EmptyPropertyValue | MissingProperty | MissingSystemProperty e) {
			throw logger.throwing(new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< public void setUpBeforeAll()");
		}
	}
	
	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(enter,  ">>> public void tearDownAfterAll()");
			if (setup != null) {
				setup.uninitialize();
				setup = null;
			}
		} finally {
			logger.trace(exit,  "<<< public void tearDownAfterAll()");
			
		}
	}
	
	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.trace(enter, ">>> public void setUp()");
			conn = setup.createTesterTcpConnection();
		} catch (IOException e) {
			throw logger.throwing(Level.FATAL, new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< public void setUp()");
		}
	}
	
	@AfterEach
	public void tearDown() {
		try {
			logger.trace(enter, ">>> public void tearDown()");
			if (conn != null) {
				setup.removeDoipTcpConnectionTest(conn);
				conn = null;
			}
		} finally {
			logger.trace(exit, "<<< public void tearDown()");
		}
	}
	
	@Test
	@DisplayName("TC-9020-01")
	@Disabled
	public void test() throws TestExecutionError {
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> public void test()");
			
			desc = new TestCaseDescription(
					"TC-9020-01",
					"Send large diagnostic message",
					"Open TCP connection and perform routing activation, " +
					"Then send a long diagnostic message with size " +
					"of 10.000.000 bytes. After that a normal session change " +
					"will be send.",
					"At the end the session change will be answered with a positive response.");
			desc.logHeader();
		
			testImpl();
			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (TestExecutionError e) {
			desc.logFooter(TestResult.ERROR);
			throw e;
		} finally {
			logger.trace(exit, "<<< public void test()");
		}
	}
	
	public void testImpl() throws TestExecutionError {
		
		TestFunctions.performRoutingActivation(conn, config, 0, -1);
		
		byte[] response = null;
		byte[] largeDiagMessage = new byte[10000000];
		largeDiagMessage[0] = 0x22;
		
		DoipTcpDiagnosticMessage doipMsg = new DoipTcpDiagnosticMessage(config.getTesterAddress(), config.getEcuAddressPhysical(), largeDiagMessage);
		
		int timeout = config.get_A_DoIP_Diagnostic_Message();
		DoipTcpHeaderNegAck negAck = TestFunctions.sendDataAndCheckForGenericDoipHeaderNegAck(conn, doipMsg.getMessage(), timeout);
		assertEquals(DoipTcpHeaderNegAck.NACK_MESSAGE_TOO_LARGE, negAck.getCode(), "It was expected that the NACK code in the '" + DoipTcpHeaderNegAck.getMessageNameOfClass() + "' is 0x02, but it is " + String.format("0x%02X", negAck.getCode()) + ".");
		
		doipMsg = TestFunctions.executeDiagnosticServiceAndCheckForPosAckWithDiagResponse(conn, config, new byte[] {0x10, 0x03});
		response = doipMsg.getDiagnosticMessage();
		
		assertNotNull(response, "The response was null.");
		assertTrue(response.length >= 2, "The response was too short. There should be at least two bytes.");
		assertEquals(0x50, response[0], "Didn't receive a positive response.");
		assertEquals(0x03, response[1], "The diagnostic session in the response is wrong.");
		
	}
}
