package doip.tester.testcases.tcp.segmentation;

import static doip.junit.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import doip.junit.SetUpBeforeClassFailed;
import doip.junit.SetUpFailed;
import doip.library.message.DoipTcpDiagnosticMessage;
import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.util.Conversion;
import doip.library.util.Helper;
import doip.library.util.StringConstants;
import doip.logging.LogManager;
import doip.logging.Logger;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.exception.RoutingActivationFailed;

public class TC_9000_SplitMessages {
	
	private static Logger logger = LogManager.getLogger(TC_9000_SplitMessages.class);
	
	private static TestSetup testSetup = null;
	
	private static TesterTcpConnection conn = null;

	@BeforeAll
	public static void setUpBeforeClass() throws SetUpBeforeClassFailed {
		
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.LINE);
				logger.info(">>> public static void setUpBeforeClass()");
			}

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			testSetup = new TestSetup();
			testSetup.initialize("src/test/resources/tester.properties");
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} catch (IOException e) {
			throw new SetUpBeforeClassFailed("Unexpected IOException had been thrown.", e);
		} catch (MissingProperty e) {
			throw new SetUpBeforeClassFailed("Unexpected MissingProperty had been thrown.", e); 
		} catch (EmptyPropertyValue e) {
			throw new SetUpBeforeClassFailed("Unexpected EmptyPropertyValue had been thrown.", e); 
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public static void setUpBeforeClass()");
				logger.info(StringConstants.LINE);
			}
		}
	}

	@AfterAll
	public static void tearDownAfterClass() {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.LINE);
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
				logger.info(StringConstants.LINE);
			}
		}
	}

	@BeforeEach
	public void setUp() throws SetUpFailed {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.LINE);
				logger.info(">>> public void setUp()");
			}
			
			// --- SET UP CODE BEGIN ----------------------------------------
			this.conn = this.testSetup.createTesterTcpConnection();
			conn.performRoutingActivation(0);
			// --- SET UP CODE END ------------------------------------------
			
		} catch (IOException | RoutingActivationFailed | InterruptedException e) {
			String error = "Unexpected " + e.getClass().getName();
			logger.error(error);
			logger.error(Helper.getExceptionAsString(e));
			throw new SetUpFailed(error);
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public void setUp()");
				logger.info(StringConstants.LINE);
			}	
		}
	}

	@AfterEach
	public void tearDown() {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.LINE);
				logger.info(">>> public void tearDown()");
			}
			
			// --- TEAR DOWN CODE BEGIN --------------------------------------
			if (this.conn != null) {
				testSetup.removeDoipTcpConnectionTest(conn);
				conn = null;
			}
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public void tearDown()");
				logger.info(StringConstants.LINE);
			}
		}
	}

	@Test
	@DisplayName("TC-9000-01")
	public void test() {
		String function = "public void test()";
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.WALL);
				logger.info(">>> " + function);
			}
		
			// --- TEST CODE BEGIN --------------------------------------------
			conn.clearEvents();
			TestConfig conf = testSetup.getConfig();
			
			logger.info("Create a message object for sending a diagnostic message");
			DoipTcpDiagnosticMessage msg = new DoipTcpDiagnosticMessage(
					conf.getTesterAddress(), conf.getEcuAddressPhysical(),
					new byte[] {0x10, 0x03});
			byte[] raw = msg.getMessage();
			testSplitMessage(raw, 8);
			// --- TEST CODE END ----------------------------------------------
			
		} catch (InterruptedException e) {
			String error = "Unexpected " + e.getClass().getName();
			fail(error);
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< " + function);
				logger.info(StringConstants.WALL);
			}
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
			boolean result = conn.waitForEvents(1, 10);

			assertFalse(result, "Did receive a event, but no event was expected");
			conn.send(second);
			result = conn.waitForEvents(2, 10);
			assertTrue(result, "Didn't receive two events on sending a diagnostic message splitted in two parts");

		} finally {
			logger.info("<<< " + function);
		}
	}
}
