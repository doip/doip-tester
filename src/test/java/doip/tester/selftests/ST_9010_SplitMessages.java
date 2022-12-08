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
import org.opentest4j.MultipleFailuresError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.util.StringConstants;
import doip.tester.testcases.TC_9010_SplitMessages;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.server4unittest.DoipServer4UnitTest;

public class ST_9010_SplitMessages {
	
	public static final String BASE_ID = "9010";
	
	private static Logger logger = LogManager.getLogger(ST_9010_SplitMessages.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT)");
	
	public static DoipServer4UnitTest server = null;
	private TC_9010_SplitMessages testcase = null;

	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		
		try {
			logger.info(StringConstants.HASH_LINE);
			logger.trace(enter, ">>> public static void setUpBeforeAll()");

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			server = new DoipServer4UnitTest();
			server.start();
			
			TC_9010_SplitMessages.setUpBeforeClass();
			
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} catch (IOException e) {
			throw logger.throwing(new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< public static void setUpBeforeAll()");
		}
	}

	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(enter, ">>> public static void tearDownAfterAll()");
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			
			TC_9010_SplitMessages.tearDownAfterClass();
			
			if (server != null) {
				server.stop();
				server = null;
			}
			
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			logger.trace(exit, "<<< public static void tearDownAfterAll()");
			logger.info(StringConstants.HASH_LINE);
		}
	}

	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.info(StringConstants.HASH_LINE);
			logger.trace(enter, ">>> public void setUp()");
			
			// --- SET UP CODE BEGIN ----------------------------------------
			
			testcase = new TC_9010_SplitMessages();
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
			testcase.tearDown();
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			logger.info(exit, "<<< public void tearDown()");
			logger.info(StringConstants.HASH_LINE);
		}
	}

	// TODO: remove back slashes in next line 
	@Test
	@DisplayName("ST-" + BASE_ID + "-01-01")
	public void test_01_SplitMessage_01_GoodCase() throws TestExecutionError {
		String function = "public void test()";
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> " + function);

			// --- TEST CODE BEGIN --------------------------------------------
			desc = new TestCaseDescription(
					"ST-" + BASE_ID + "-01-01",
					"Execute test case TC-9010-01",
					"---",
					"The test case TC-9010-01 will pass");
			desc.emphasize().logHeader();
			testImpl_01_SplitMessage_01_GoodCase();
			desc.logFooter(TestResult.PASSED);
			// --- TEST CODE END ----------------------------------------------
		} catch (AssertionError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (Exception e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(Level.FATAL, new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< " + function);
		}
	}
	
	public void testImpl_01_SplitMessage_01_GoodCase() throws TestExecutionError {
		testcase.test_01_SplitMessage();
	}
}
