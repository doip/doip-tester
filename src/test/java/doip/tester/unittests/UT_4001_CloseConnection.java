package doip.tester.unittests;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.util.StringConstants;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventConnectionClosed;
import doip.tester.toolkit.server4unittest.DoipServer4UnitTest;
import doip.tester.toolkit.server4unittest.DoipTcpConnection4UnitTest;

public class UT_4001_CloseConnection {
	
	private static Logger logger = LogManager.getLogger(UT_4001_CloseConnection.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT");

	private static DoipServer4UnitTest server = null;
	private static TestSetup setup = null;
	
	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(enter, ">>> public void setUpBeforeAll()");
			server = new DoipServer4UnitTest();
			setup = new TestSetup();
			setup.initialize();
		} finally {
			logger.trace(exit, "<<< public void setUpBeforeAll()");
		}
	}
	
	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(enter, ">>> public static void tearDownAfterAll()");
			if (setup != null) {
				setup.uninitialize();
				setup = null;
			}
			if (server != null) {
				server = null;
			}
		} finally {
			logger.trace(exit, "<<< public static void tearDownAfterAll()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}
	
	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(enter, ">>> public void setUp()");
			server.start();
		} catch (IOException e) {
			throw logger.throwing(new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< public void setUp>()");
		}
	}
	
	@AfterEach
	public void tearDown() {
		try {
			logger.trace(enter, ">>> public void tearDown()");
			server.stop();
		} finally {
			logger.trace(exit, "<<< public void tearDown()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}
	
	@Test
	@DisplayName("UT-4001-01")
	public void test() throws TestExecutionError {
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> public void test()");
			desc = new TestCaseDescription(
					"UT-4001-01",
					"Test closing a TCP connection",
					"Establish a TCP connection and close the connection on server side",
					"There will be a SocketClosedEvent on tester side");
			desc.logHeader();
			
			TesterTcpConnection testerconn = setup.createTesterTcpConnection();
			assertNotNull(testerconn);
			DoipTcpConnection4UnitTest serverconn = server.getConnection(0);
			assertNotNull(serverconn);
			
			serverconn.stop();
			DoipEvent event = testerconn.waitForEvents(1, 10);
			assertNotNull(event);
			assertTrue(event instanceof DoipEventConnectionClosed);

			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (Exception e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< public void test()");
		}
	}
}
