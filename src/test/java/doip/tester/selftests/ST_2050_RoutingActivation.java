package doip.tester.selftests;

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

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.util.StringConstants;
import doip.tester.testcases.TC_2050_RoutingActivation;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.server4unittest.DoipServer4UnitTest;

public class ST_2050_RoutingActivation {
	
	public static final String BASE_ID = "2050";
	
	private static Logger logger = LogManager.getLogger(ST_2050_RoutingActivation.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT)");
	
	private static TestSetup setup = null;
	private static DoipServer4UnitTest server = null;
	private TC_2050_RoutingActivation testcase = null;

	@BeforeAll
	public static void setUpBeforeClass() throws InitializationError {
		
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(enter, ">>> public static void setUpBeforeClass()");

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			
			server = new DoipServer4UnitTest();
			server.start();
			
			setup = new TestSetup();
			setup.initialize();
			
			TC_2050_RoutingActivation.setUpBeforeClass();
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} catch (IOException e) {
			throw logger.throwing(Level.FATAL, new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< public static void setUpBeforeClass()");
		}
	}

	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(enter, ">>> public static void tearDownAfterClass()");
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			TC_2050_RoutingActivation.tearDownAfterClass();
			
			if (setup != null) {
				setup.uninitialize();
				setup = null;
			}
			
			if (server != null) {
				server.stop();
				server = null;
			}
			
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			logger.trace(exit, "<<< public static void tearDownAfterClass()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(enter, ">>> public void setUp()");
			
			// --- SET UP CODE BEGIN ----------------------------------------
			testcase = new TC_2050_RoutingActivation();
			testcase.setUp();
			// --- SET UP CODE END ------------------------------------------

		} finally {
			logger.info(exit, "<<< public void setUp()");
		}
	}

	@AfterEach
	public void tearDown() {
		try {
			logger.info(enter, ">>> public void tearDown()");
			
			// --- TEAR DOWN CODE BEGIN --------------------------------------
			
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			logger.info(exit, "<<< public void tearDown()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

	@Test
	@DisplayName("ST-" + BASE_ID + "-01-01")
	public void test() throws TestExecutionError {
		String function = "public void test()";
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> " + function);

			// --- TEST CODE BEGIN --------------------------------------------
			desc = new TestCaseDescription(
					"ST-" + BASE_ID + "-01-01",
					"Test test case TC-" + BASE_ID + "-01 good case",
					"Run test case TC-" + BASE_ID + "-01",
					"Test case will pass");
			desc.logHeader();
			testImpl_01_goodCase();
			desc.logFooter(TestResult.PASSED);
			// --- TEST CODE END ----------------------------------------------
			
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (TestExecutionError e) {
			desc.logFooter(TestResult.ERROR);
			throw e;
		} catch (Exception e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(Level.FATAL, new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< " + function);
		}
	}
	
	public void testImpl_01_goodCase() throws TestExecutionError {
		try {
			testcase.testRoutingActivation();
		} finally {
			
		}
	}
}
