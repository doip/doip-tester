package doip.tester.selftests;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestResult;
import doip.tester.testcases.TC_1030_DoipEntityStatus;
import doip.tester.toolkit.server4unittest.DoipServer4UnitTest;

public class ST_1030_DoipEntityStatus {

	private static Logger logger = LogManager.getLogger(ST_1030_DoipEntityStatus.class);
	
	private static DoipServer4UnitTest server = null;
	
	private TC_1030_DoipEntityStatus testcase = null;
	
	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		try {
			logger.trace(">>> public static void setUpBeforeAll()");
			server = new DoipServer4UnitTest();
			TC_1030_DoipEntityStatus.setUpBeforeAll();
		} finally {
			logger.trace("<<< public static void setUpBeforeAll()");
		}
		
	}
	
	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(">>> public static void tearDownAfterAll()");
			TC_1030_DoipEntityStatus.tearDownAfterAll();
			server = null;
		} finally {
			logger.trace("<<< public static void tearDownAfterAll()");
		}
	}
	
	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.trace(">>> public void setUp()");
			server.start();
			testcase = new TC_1030_DoipEntityStatus();
			
		} catch (IOException e) {
			throw logger.throwing(new InitializationError(
					"Failed to prepare test setup for next test case in class "
							+ this.getClass().getName(), e));
		} finally {
			logger.trace("<<< public void setUp()");
		}
	}
	
	@AfterEach
	public void tearDown() {
		try {
			logger.trace(">>> public void tearDown()");
			server.stop();
		} finally {
			logger.trace("<<< public void tearDown()");
		}
	}
	
	@Test
	public void testGoodCase() throws Throwable {
		logger.trace(">>> public void testGoodCase()");
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
			throw logger.throwing(e);
		} catch (Throwable e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(e);
		} finally {
			logger.trace("<<< public void testGoodCase()");
		}
	}
}
