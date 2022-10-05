package doip.tester.testcases;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import doip.junit.InitializationError;
import doip.library.util.StringConstants;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.exception.RoutingActivationFailed;

public class TC_1050_RoutingActivation {
	
	private static Logger logger = LogManager.getLogger(TC_1050_RoutingActivation.class);
	
	private static TestSetup testSetup = null;
	
	private TesterTcpConnection conn = null;

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
			}
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public static void tearDownAfterClass()");
				logger.info(StringConstants.SINGLE_LINE);
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
			conn = testSetup.createTesterTcpConnection();
			// --- SET UP CODE END ------------------------------------------
			
		} catch (IOException e) {
			logger.fatal("Unexpected " + e.getClass().getName() + ": " + e.getMessage());
			throw logger.throwing(new InitializationError(e));
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public void setUp()");
				logger.info(StringConstants.SINGLE_LINE);
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
	@DisplayName("TC-1050-01")
	public void testRoutingActivation() throws RoutingActivationFailed {
		String function = "public void testRoutingActivation()";
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.DOUBLE_LINE);
				logger.info(">>> " + function);
			}
			
			// --- TEST CODE BEGIN --------------------------------------------
			try {
				conn.performRoutingActivation(0);
			} catch (InterruptedException e) {
			}
			// --- TEST CODE END ----------------------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< " + function);
				logger.info(StringConstants.DOUBLE_LINE);
			}
		}
	}
}
