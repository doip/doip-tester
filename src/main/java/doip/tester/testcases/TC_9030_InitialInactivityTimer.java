package doip.tester.testcases;

import static doip.junit.Assertions.*;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.properties.MissingSystemProperty;
import doip.library.util.StringConstants;
import doip.tester.toolkit.CheckResult;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.EventChecker;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventConnectionClosed;

public class TC_9030_InitialInactivityTimer {
	
	public static final String BASE_ID = "TC-9030";

	private static Logger logger = LogManager.getLogger(TC_9030_InitialInactivityTimer.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT)");
	
	private static TestSetup setup = null;

	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(enter, ">>> public static void setUpBeforeClass()");

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			setup = new TestSetup();
			setup.initialize();
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} catch (IOException | EmptyPropertyValue | MissingProperty | MissingSystemProperty e) {
			throw logger.throwing(new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< public static void setUpBeforeClass()");
		}
	}

	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(enter, ">>> public static void tearDownAfterClass()");
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			if (setup != null) {
				setup.uninitialize();
				setup = null;
			}
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			logger.trace(exit, "<<< public static void tearDownAfterClass()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

	@BeforeEach
	public void setUp() {
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(enter, ">>> public void setUp()");
			
			// --- SET UP CODE BEGIN ----------------------------------------
			
			// --- SET UP CODE END ------------------------------------------
			
		} finally {
			logger.trace(exit, "<<< public void setUp()");
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
	public void test() throws TestExecutionError {
		String function = "public void test()";
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> " + function);
			
			// --- TEST CODE BEGIN --------------------------------------------
			desc = new TestCaseDescription(
					"TC-9030-01",
					"Testing inactivity timer in case of no routing activation",
					"The tester will establish a TCP connection to the gateway and will wait then 2100 ms",
					"After 1900 ms no event did occur, "
					+ "and within the next 200 ms the TCP "
					+ "connection will be closed by the DoIP server");
			desc.logHeader();
			testImpl();
			desc.logFooter(TestResult.PASSED);
			// --- TEST CODE END ----------------------------------------------
			
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (Exception e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< " + function);
		}
	}
	
	public void testImpl() throws TestExecutionError {
		try {
			TesterTcpConnection conn = setup.createTesterTcpConnection();
			conn.clearEvents();
			logger.info("Wait for 1900 ms");
			Thread.sleep(1900);
			int count = conn.getEventCount();
			if (count > 0) {
				fail("It was expected that no event did occur "
						+ "while waiting for 1900 ms, but an unexpected event did occur. "
						+ "The event is type of " + conn.getEvent(0).getClass().getSimpleName() + ".");
			}
			logger.info("No event did occurwhich is the expected behavior.");
			logger.info("Now we wait for 200 ms.Within these 200 ms the DoIP server must close the TCP connection");
			DoipEvent event = conn.waitForEvents(1, 200);
			CheckResult result = EventChecker.checkEvent(event, DoipEventConnectionClosed.class);
			assertEquals(CheckResult.NO_ERROR, result.getCode(), "It was expected that the connection will be closed by the DoIP server within 200 ms, but the connecton hasn't been closed.");
			
		} catch (IOException e) {
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} catch (InterruptedException e) {
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		}
	}
}
