package doip.tester.selftests;

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
import doip.library.util.StringConstants;
import doip.tester.testcases.TC_1030_DoipEntityStatus;
import doip.tester.toolkit.server4unittest.DoipServer4UnitTest;

public class ST_1030_DoipEntityStatus {

	private static Logger logger = LogManager.getLogger(ST_1030_DoipEntityStatus.class);
	private static Marker markerEnter = MarkerManager.getMarker("ENTER");
	private static Marker markerExit = MarkerManager.getMarker("EXIT");
	
	private static DoipServer4UnitTest server = null;
	
	private TC_1030_DoipEntityStatus testcase = null;
	
	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(markerEnter, ">>> public static void setUpBeforeAll()");
			server = new DoipServer4UnitTest();
			TC_1030_DoipEntityStatus.setUpBeforeAll();
		} finally {
			logger.trace(markerExit, "<<< public static void setUpBeforeAll()");
		}
		
	}
	
	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(markerEnter, ">>> public static void tearDownAfterAll()");
			TC_1030_DoipEntityStatus.tearDownAfterAll();
			server = null;
		} finally {
			logger.trace(markerExit, "<<< public static void tearDownAfterAll()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}
	
	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(markerEnter, ">>> public void setUp()");
			server.start();
			testcase = new TC_1030_DoipEntityStatus();
			
		} catch (IOException e) {
			throw logger.throwing(new InitializationError(
					"Failed to prepare test setup for next test case in class "
							+ this.getClass().getName(), e));
		} finally {
			logger.trace(markerExit, "<<< public void setUp()");
		}
	}
	
	@AfterEach
	public void tearDown() {
		try {
			logger.trace(markerEnter, ">>> public void tearDown()");
			server.stop();
		} finally {
			logger.trace(markerExit, "<<< public void tearDown()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}
	
	@Test
	@DisplayName("ST_1030-01-01")
	public void testGoodCase() throws TestExecutionError {
		logger.trace(markerEnter, ">>> public void testGoodCase()");
		TestCaseDescription desc = null;
		try {
			
			desc = new TestCaseDescription(
					"ST-1030-01-01",
					"Test test case TC-1030-01",
					"Execute test case TC-1030-01",
					"Test case succeeds");
			desc.emphasize().logHeader();
		
			testcase.test();

			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (TestExecutionError e) {
			desc.logFooter(TestResult.ERROR);
			throw e;
		} catch (Exception e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(new TestExecutionError(e));
		} finally {
			logger.trace(markerExit, "<<< public void testGoodCase()");
		}
	}
	
	@Test
	@DisplayName("ST_1030-01-02")
	public void testNoResponse() {
		try {
			logger.trace(markerEnter, ">>> public void testNoResponse()");
		} finally {
			logger.trace(markerExit, "<<< public void testNoResponse()");
		}
	}
}
