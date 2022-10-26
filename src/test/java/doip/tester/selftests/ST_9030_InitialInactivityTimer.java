package doip.tester.selftests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.util.StringConstants;
import doip.tester.testcases.TC_9030_InitialInactivityTimer;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.server4unittest.DoipServer4UnitTest;
import doip.tester.toolkit.server4unittest.DoipTcpConnection4UnitTest;

public class ST_9030_InitialInactivityTimer implements Runnable {

	private static Logger logger = LogManager.getLogger(ST_9030_InitialInactivityTimer.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT)");

	private TC_9030_InitialInactivityTimer testcase = null;
	private static DoipServer4UnitTest server = null;
	
	private volatile Thread thread = null;
	
	private volatile int closeSocketTime = 0;
	
	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		try {
			logger.info(StringConstants.HASH_LINE);
			logger.trace(enter, ">>> public static void setUpBeforeAll()");
			server = new DoipServer4UnitTest();
			TC_9030_InitialInactivityTimer.setUpBeforeAll();
			logger.trace(enter, ">>> public static void setUpBeforeAll()");
		} finally {
			logger.trace(exit, "<<< public static void setUpBeforeAll()");
		}
	}
	
	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(enter, ">>> public static void tearDownAfterAll()");
			TC_9030_InitialInactivityTimer.tearDownAfterAll();
			if (server != null) {
				server.stop();
				server = null;
			}
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
			server.start();
			testcase = new TC_9030_InitialInactivityTimer();
			testcase.setUp();
		} catch (IOException e) {
			throw logger.throwing(new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< public void setUp()");
		}
	}
	
	@AfterEach
	public void tearDown() {
		try {
			logger.trace(enter, ">>> public void tearDown()");
			if (testcase != null) {
				testcase.tearDown();
				testcase = null;
			}
			if (server != null) {
				server.stop();
			}
		} finally {
			logger.trace(exit, "<<< public void tearDown()");
			logger.info(StringConstants.HASH_LINE);
		}
	}
	
	@Test
	@DisplayName("ST-9030-01-01")
	public void testGoodCase() throws TestExecutionError {
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> public void testGoodCase()");
			closeSocketTime = 2000;
			desc = new TestCaseDescription(
					"ST-9030-01-01",
					"Test the test case TC-9030-01, good case",
					"Run test case TC-9030-01.",
					"Test case did pass");
			desc.emphasize().logHeader();

			logger.debug("Start thread which will close all TCP connections in DoIP server in 2000 ms");
			thread = new Thread(this, "ST_9030");
			thread.start();
			
			logger.info("Execute test case TC-9030-01, test case should pass");
			testcase.test();
			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (TestExecutionError e) {
			desc.logFooter(TestResult.ERROR);
			throw e;
		} finally {
			logger.trace(exit, "<<< public void testGoodCase()");
			
		}
	}
	@Test
	@DisplayName("ST-9030-01-02")
	public void testConnectionNotClosed() throws TestExecutionError {
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> public void testConnectionNotClosed()");
			
			desc = new TestCaseDescription(
					"ST-9030-01-02",
					"Test test case TC-9030-01 in case that socket will not be closed.",
					"Execute test case TC-9030-01, but connection will not be cosed.",
					"Test case TC-9030-01 will fail.");
			desc.emphasize().logHeader();
			
			logger.info("Execute test case TC-9030-01, it should fail");
			assertThrows(AssertionFailedError.class, () -> testcase.test());
			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		}  finally {
			logger.trace(exit, "<<< public void testConnectionNotClosed()");
		}
	}
	
	@Test
	@DisplayName("ST-9030-01-03")
	public void testConnectionClosedTooReally() {
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> public void testConnectionClosedTooReally()");
			closeSocketTime = 1000;
			desc = new TestCaseDescription(
					"ST-9030-01-03",
					"Test test case TC-9030-01 in case that socket will be closed too early.",
					"Execute test case TC-9030-01 and close connection after " + closeSocketTime + ".",
					"Test case TC-9030-01 will fail.");
			desc.emphasize().logHeader();
			
			logger.debug("Start thread which will close all TCP connections in DoIP server in 1000 ms");
			thread = new Thread(this, "ST_9030");
			thread.start();
				
			logger.info("Execute test case TC-9030-01, it should fail");
			assertThrows(AssertionFailedError.class, () -> testcase.test(), "Test case didn't fail, but it should fail. That means test case didn't work a expected, therefore this test case here will fail.");
			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} finally {
			logger.trace(exit, "<<< public void testConnectionClosedTooReally()");
		}
	}

	@Override
	public void run() {
		try {
			logger.trace(enter, ">>> public void run()");
			logger.info("Thread to close socket from server side hs been started, now we just wait for " + closeSocketTime + " ms.");
			Thread.sleep(closeSocketTime);
			if (server != null) {
				int count = server.getConnectionCount();
				logger.info("Right now there are " + count + " connection(s) in the DoIP server.");
				
				if (server.getConnectionCount() > 0) {
					DoipTcpConnection4UnitTest conn  = server.getConnection(0);
					logger.info("TCP connection will be closed from server side right now.");
					conn.stop();
				}
			}
		} catch (InterruptedException e) {
			logger.fatal("The method Thread.sleep(...) in method run() has been interrupted.");
		} finally {
			logger.trace(exit, "<<< public void run()");
		}
	}
}
