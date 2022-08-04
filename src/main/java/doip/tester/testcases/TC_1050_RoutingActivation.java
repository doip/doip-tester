package doip.tester.testcases;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.util.Helper;
import doip.library.util.StringConstants;
import doip.logging.LogManager;
import doip.logging.Logger;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.exception.RoutingActivationFailed;

public class TC_1050_RoutingActivation {
	
	private static Logger logger = LogManager.getLogger(TC_1050_RoutingActivation.class);
	
	private static TestSetup testSetup = null;
	
	private TesterTcpConnection conn = null;

	@BeforeAll
	public static void setUpBeforeClass() throws IOException, MissingProperty, EmptyPropertyValue {
		
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.LINE);
				logger.info(">>> public static void setUpBeforeClass()");
			}

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			testSetup = new TestSetup();
			testSetup.initialize("src/test/resources/tester.properties");
			// --- SET UP BEFORE CLASS END ----------------------------------
			
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
	public void setUp() throws IOException {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.LINE);
				logger.info(">>> public void setUp()");
			}
			
			// --- SET UP CODE BEGIN ----------------------------------------
			conn = testSetup.createTesterTcpConnection();
			// --- SET UP CODE END ------------------------------------------
			
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
			if (conn != null) {
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
	@DisplayName("TC-1050-01")
	public void testRoutingActivation() throws RoutingActivationFailed {
		String function = "public void testRoutingActivation()";
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.WALL);
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
				logger.info(StringConstants.WALL);
			}
		}
	}
}
