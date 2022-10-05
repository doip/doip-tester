package doip.tester.testcases;

import static doip.junit.Assertions.*;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import doip.library.util.Conversion;
import doip.library.util.Helper;
import doip.library.util.StringConstants;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.exception.RoutingActivationFailed;

public class TC_9000_SplitMessages {
	
	private static Logger logger = LogManager.getLogger(TC_9000_SplitMessages.class);
	
	private static TestSetup testSetup = null;
	
	private static TesterTcpConnection conn = null;
	
	private static TestConfig config = null;

	@BeforeAll
	public static void setUpBeforeClass() throws InitializationError {
		
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.SINGLE_LINE);
				logger.info(">>> public static void setUpBeforeClass()");
			}

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			testSetup = new TestSetup();
			testSetup.initialize();
			config = testSetup.getConfig();
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public static void setUpBeforeClass()");
				logger.info(StringConstants.SINGLE_LINE);
			}
		}
	}

	@AfterAll
	public static void tearDownAfterClass() {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.SINGLE_LINE);
				logger.info(">>> public static void tearDownAfterClass()");
			}
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			if (testSetup != null) {
				testSetup.uninitialize();
				testSetup = null;
			}
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public static void tearDownAfterClass()");
			}
		}
	}

	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.SINGLE_LINE);
				logger.info(">>> public void setUp()");
			}
			
			// --- SET UP CODE BEGIN ----------------------------------------
			conn = this.testSetup.createTesterTcpConnection();
			conn.performRoutingActivation(0);
			// --- SET UP CODE END ------------------------------------------
			
		} catch (IOException | RoutingActivationFailed | InterruptedException e) {
			String error = "Unexpected " + e.getClass().getName() + ": " + e.getMessage();
			logger.error(error);
			logger.error(Helper.getExceptionAsString(e));
			throw logger.throwing(new InitializationError(error, e));
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public void setUp()");
			}	
		}
	}

	@AfterEach
	public void tearDown() {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.SINGLE_LINE);
				logger.info(">>> public void tearDown()");
			}
			
			// --- TEAR DOWN CODE BEGIN --------------------------------------
			if (conn != null) {
				testSetup.removeDoipTcpConnectionTest(conn);
				conn = null;
			}
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public void tearDown()");
				logger.info(StringConstants.SINGLE_LINE);
			}
		}
	}

	@Test
	@DisplayName("TC-9000-01")
	public void test() throws TestExecutionError {
		String function = "public void test()";
		TestCaseDescription desc = null;
		try {
			logger.trace(">>> " + function);
			
			desc = new TestCaseDescription(
					"TC-9000-01",
					"Send diagnostic message splitted into two messages",
					"Send a diagnostic message, but don't transmit all data at once. "
					+ "First send the header and then send the payload in a second message.",
					"The ECU sends a positive acknowledge after second message and then "
					+ "it sends the diagnostic response message.");
			desc.logHeader();
			
			// --- TEST CODE BEGIN --------------------------------------------
			
			conn.clearEvents();
			logger.info("Create a message object for sending a diagnostic message");
			DoipTcpDiagnosticMessage msg = new DoipTcpDiagnosticMessage(
					config.getTesterAddress(), config.getEcuAddressPhysical(),
					new byte[] {0x10, 0x03});
			byte[] raw = msg.getMessage();
			testSplitMessage(raw, 8);
			
			// --- TEST CODE END ----------------------------------------------
			
			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			throw e;
		} catch (InterruptedException e) {
			logger.fatal("Unexpected "+ e.getClass().getName() +": " + e.getMessage());
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(new TestExecutionError(e));
		} finally {
			logger.trace("<<< " + function);
		}
	}
	
	public void testSplitMessage(byte[] message, int splitIndex) throws InterruptedException {
		String function = "public void testSplitMessage(byte[] message, int splitIndex)";
		try {
			logger.info(">>> " + function);
			int totalSize = message.length;
			byte[] first = new byte[splitIndex];
			byte[] second = new byte[totalSize - splitIndex];
			System.arraycopy(message, 0, first, 0, splitIndex);
			System.arraycopy(message, splitIndex, second, 0, totalSize - splitIndex);
			logger.info("Sending diagnostic message in two parts");
			logger.info("First part: " + Conversion.byteArrayToHexString(first));
			logger.info("Second part: " + Conversion.byteArrayToHexString(second));
			conn.send(first);
			DoipEvent event = conn.waitForEvents(1, 10);

			assertNull(event, "Did receive a event, but no event was expected");
			conn.send(second);
			event = conn.waitForEvents(2, 10);
			assertNotNull(event, "Didn't receive two events on sending a diagnostic message splitted in two parts");

		} finally {
			logger.info("<<< " + function);
		}
	}
}
