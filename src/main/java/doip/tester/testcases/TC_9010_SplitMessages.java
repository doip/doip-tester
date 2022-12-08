package doip.tester.testcases;

import static doip.junit.Assertions.*;

import java.io.IOException;

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

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.message.DoipTcpDiagnosticMessage;
import doip.library.message.DoipTcpDiagnosticMessagePosAck;
import doip.library.util.Conversion;
import doip.library.util.Helper;
import doip.library.util.StringConstants;
import doip.tester.toolkit.CheckResult;
import doip.tester.toolkit.EventChecker;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventTcpDiagnosticMessage;
import doip.tester.toolkit.event.DoipEventTcpDiagnosticMessagePosAck;
import doip.tester.toolkit.exception.RoutingActivationFailed;

public class TC_9010_SplitMessages {
	
	public static final String BASE_ID = "9010";
	
	private static Logger logger = LogManager.getLogger(TC_9010_SplitMessages.class);
	private static Marker markerEnter = MarkerManager.getMarker("ENTER");
	private static Marker markerExit  = MarkerManager.getMarker("EXIT");
	
	private static TestSetup testSetup = null;
	
	private static TesterTcpConnection conn = null;
	
	private static TestConfig config = null;

	@BeforeAll
	public static void setUpBeforeClass() throws InitializationError {
		
		try {
			logger.trace(markerEnter, ">>> public static void setUpBeforeClass()");
			logger.info(StringConstants.SINGLE_LINE);

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			testSetup = new TestSetup();
			testSetup.initialize();
			config = testSetup.getConfig();
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} finally {
			logger.trace(markerExit, "<<< public static void setUpBeforeClass()");
		}
	}

	@AfterAll
	public static void tearDownAfterClass() {
		try {
			logger.trace(markerEnter, ">>> public static void tearDownAfterClass()");
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			if (testSetup != null) {
				testSetup.uninitialize();
				testSetup = null;
			}
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(markerExit, "<<< public static void tearDownAfterClass()");
		}
	}

	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.trace(markerEnter, ">>> public void setUp()");
			logger.info(StringConstants.SINGLE_LINE);
			
			// --- SET UP CODE BEGIN ----------------------------------------
			conn = this.testSetup.createTesterTcpConnection();
			// --- SET UP CODE END ------------------------------------------
			
		} catch (IOException e) {
			throw logger.throwing(new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(markerExit, "<<< public void setUp()");
		}
	}

	@AfterEach
	public void tearDown() {
		try {
			logger.trace(markerEnter, ">>> public void tearDown()");
			
			// --- TEAR DOWN CODE BEGIN --------------------------------------
			if (conn != null) {
				testSetup.removeDoipTcpConnectionTest(conn);
				conn = null;
			}
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(markerExit, "<<< public void tearDown()");
		}
	}

	@Test
	@DisplayName("TC-" + BASE_ID + "-01")
	public void test_01_SplitMessage() throws TestExecutionError {
		String function = "public void test()";
		TestCaseDescription desc = null;
		try {
			logger.trace(markerEnter, ">>> " + function);
			
			desc = new TestCaseDescription(
					"TC-" + BASE_ID + "-01",
					"Send diagnostic message splitted into two messages",
					"Send a diagnostic message, but don't transmit all data at once. "
					+ "First send the header and then send the payload in a second message.",
					"The ECU sends a positive acknowledge after second message and then "
					+ "it sends the diagnostic response message.");
			desc.logHeader();
			
			// --- TEST CODE BEGIN --------------------------------------------
			
			TestFunctions.performRoutingActivation(conn, config, 0, -1);
			
			logger.info("Create a message object for sending a diagnostic message");
			DoipTcpDiagnosticMessage msg = new DoipTcpDiagnosticMessage(
					config.getTesterAddress(), config.getEcuAddressPhysical(),
					new byte[] {0x10, 0x03});
			byte[] raw = msg.getMessage();
			testSplitMessage(raw, 8);
			
			// --- TEST CODE END ----------------------------------------------
			
			desc.logFooter(TestResult.PASSED);
		} catch (AssertionError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (Exception e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(markerExit, "<<< " + function);
		}
	}
	
	private void testSplitMessage(byte[] message, int splitIndex) throws InterruptedException {
		String function = "public void testSplitMessage(byte[] message, int splitIndex)";
		try {
			logger.info(markerEnter, ">>> " + function);
			int totalSize = message.length;
			byte[] first = new byte[splitIndex];
			byte[] second = new byte[totalSize - splitIndex];
			System.arraycopy(message, 0, first, 0, splitIndex);
	 		System.arraycopy(message, splitIndex, second, 0, totalSize - splitIndex);
			logger.info("Sending diagnostic message in two parts");
			logger.info("First part: " + Conversion.byteArrayToHexString(first));
			logger.info("Second part: " + Conversion.byteArrayToHexString(second));
			
			conn.clearEvents();
			conn.send(first);
			DoipEvent event = conn.waitForEvents(1, config.get_A_DoIP_Diagnostic_Message());
			CheckResult result = EventChecker.checkEvent(event, null);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}

			conn.send(second);
			event = conn.waitForEvents(1, 2000);
			result = EventChecker.checkEvent(event, DoipEventTcpDiagnosticMessagePosAck.class);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}
			
			event = conn.waitForEvents(2, 2000);
			result = EventChecker.checkEvent(event, DoipEventTcpDiagnosticMessage.class);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}

		} finally {
			logger.info(markerExit, "<<< " + function);
		}
	}
}
