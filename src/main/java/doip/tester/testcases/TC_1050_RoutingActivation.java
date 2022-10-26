package doip.tester.testcases;

import static doip.junit.Assertions.fail;

import java.io.IOException;

import org.apache.logging.log4j.Level;
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
import doip.library.util.StringConstants;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.exception.RoutingActivationFailed;

public class TC_1050_RoutingActivation {
	
	private static Logger logger = LogManager.getLogger(TC_1050_RoutingActivation.class);
	
	private static TestSetup testSetup = null;
	private static TestConfig config = null;
	
	private TesterTcpConnection conn = null;

	@BeforeAll
	public static void setUpBeforeClass() throws InitializationError {
		
		try {
				logger.info(StringConstants.SINGLE_LINE);
				logger.trace(">>> public static void setUpBeforeClass()");

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			testSetup = new TestSetup();
			testSetup.initialize();
			config = testSetup.getConfig();
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} finally {
			logger.trace("<<< public static void setUpBeforeClass()");
		}
	}

	@AfterAll
	public static void tearDownAfterClass() {
		try {
			logger.trace(">>> public static void tearDownAfterClass()");
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			if (testSetup != null) {
				testSetup.uninitialize();
			}
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			logger.trace("<<< public static void tearDownAfterClass()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(">>> public void setUp()");
			
			// --- SET UP CODE BEGIN ----------------------------------------
			conn = testSetup.createTesterTcpConnection();
			// --- SET UP CODE END ------------------------------------------
			
		} catch (IOException e) {
			logger.fatal("Unexpected " + e.getClass().getName() + ": " + e.getMessage());
			throw logger.throwing(new InitializationError(e));
		} finally {
			logger.trace("<<< public void setUp()");
		}
	}

	@AfterEach
	public void tearDown() {
		try {
			logger.trace(">>> public void tearDown()");
			
			// --- TEAR DOWN CODE BEGIN --------------------------------------
			if (conn != null) {
				conn = null;
			}
			
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			logger.trace("<<< public void tearDown()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

	@Test
	@DisplayName("TC-1050-01")
	public void testRoutingActivation() throws TestExecutionError {
		String function = "public void testRoutingActivation()";
		TestCaseDescription desc = null;
		try {
			logger.info(">>> " + function);
			
			// --- TEST CODE BEGIN --------------------------------------------
			desc = new TestCaseDescription(
					"TC-1050-01",
					"Perform Routing Activation",
					"Send a routing activation request",
					"ECU sends a routing activation response");
			desc.logHeader();
			conn.performRoutingActivation(config.getTesterAddress(), 0);
			desc.logFooter(TestResult.PASSED);
			// --- TEST CODE END ----------------------------------------------
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (RoutingActivationFailed e) {
			desc.logFooter(TestResult.FAILED);
			fail(e.getMessage());
		} catch (InterruptedException e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.info("<<< " + function);
		}
	}
}
