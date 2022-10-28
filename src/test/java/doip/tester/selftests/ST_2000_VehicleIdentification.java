package doip.tester.selftests;

import static doip.junit.Assertions.assertThrows;

import org.apache.logging.log4j.Level;
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
import doip.library.message.DoipUdpEntityStatusResponse;
import doip.library.util.StringConstants;
import doip.tester.testcases.TC_2000_VehicleIdentification;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TestUtils;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.server4unittest.DoipServer4UnitTest;

public class ST_2000_VehicleIdentification {
	
	public static final String BASE_ID = "2000";

	public static Logger logger = LogManager.getLogger(ST_2000_VehicleIdentification.class);
	public static Marker markerEnter = MarkerManager.getMarker("ENTER");
	public static Marker markerExit  = MarkerManager.getMarker("EXIT");
	
	private static DoipServer4UnitTest server = null;
	private TC_2000_VehicleIdentification testcase = null;
	
	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		try {
			logger.info(StringConstants.HASH_LINE);
			logger.trace(markerEnter, ">>> public void setUpBeforeAll()");
			server = new DoipServer4UnitTest();
			
			TC_2000_VehicleIdentification.setUpBeforeClass();
			
		} catch (InitializationError e) {
			throw e;
		} catch (Throwable e) {
			logger.fatal(TextBuilder.unexpectedException(e));
			throw logger.throwing(new InitializationError(e));
		} finally {
			logger.trace(markerExit, "<<< public void setUpBeforeAll()");
		}
	}
	
	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(markerEnter, ">>> public void tearDownAfterAll()");
			
			TC_2000_VehicleIdentification.tearDownAfterClass();
			server = null;
			
		} finally {
			logger.trace(markerExit, "<<< public void tearDownAfterAll()");
			logger.info(StringConstants.HASH_LINE);
		}
	}
	
	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.info(StringConstants.HASH_LINE);
			logger.trace(markerEnter, ">>> public void setUp()");
			
			server.start();
			testcase = new TC_2000_VehicleIdentification();
			
		} catch (Throwable e) {
			logger.fatal(TextBuilder.unexpectedException(e));
			throw logger.throwing(new InitializationError(e));
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
			
			logger.info(StringConstants.HASH_LINE);
		}
	}
	
	@Test
	// @Disabled
	@DisplayName("ST-"+BASE_ID+"-01-01")
	public void testUnicastGoodCase() throws TestExecutionError {
		TestCaseDescription desc = null;
		try {
			logger.trace(markerEnter, ">>> public void testGoodCase()");
			desc = new TestCaseDescription(
					"ST-"+BASE_ID+"-01-01",
					"Execute test case TC-" + BASE_ID + "-01 in case a valid vehicle "
					+ "identification response is received.",
					"Execute test case TC-"+BASE_ID+"-01.",
					"Test case TC-"+BASE_ID+"-01 will pass.");
			desc.emphasize().logHeader();
			testcase.testUnicast();
			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (TestExecutionError e) {
			desc.logFooter(TestResult.ERROR);
			throw e;
		} finally {
			logger.trace(markerExit, "<<< public void testGoodCase()");
		}
	}
	
	@Test
	// @Disabled
	@DisplayName("ST-" + BASE_ID + "-01-02")
	public void testUnicastNoResponse() throws TestExecutionError {
		TestCaseDescription desc = null;
		try {
			logger.trace(markerEnter, ">>> public void testUnicastNoResponse()");
			server.setSilent(true);
			desc = new TestCaseDescription(
					"ST-" + BASE_ID + "-01-01",
					"Execute test case TC-" + BASE_ID + "-01 in case "
					+ "no response has been received.",
					"Execute test case TC-" + BASE_ID + "-01.",
					"Test case TC-" + BASE_ID + "-01 will fail.");
			desc.emphasize().logHeader();
			assertThrows(AssertionFailedError.class, () -> testcase.testUnicast());
			desc.logFooter(TestResult.PASSED);
		} catch (Exception e) {
			String error = TextBuilder.unexpectedException(e);
			logger.fatal(error);
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(new TestExecutionError(error, e));
		} finally {
			logger.trace(markerExit, "public void testUnicastNoResponse()");
		}
	}
	

	@Test
	// @Disabled
	@DisplayName("ST-" + BASE_ID + "-01-03")
	public void testUnicastWrongResponse() throws TestExecutionError {
		TestCaseDescription desc = null;
		try {
			logger.trace(markerEnter, ">>> public void testUnicastWrongResponse()");
			server.setNextUdpResponse(new DoipUdpEntityStatusResponse(0, 1, 0, 0x10000 ).getMessage());
			
			desc = new TestCaseDescription(
					"ST-" + BASE_ID + "-01-03",
					"Run test case TC-" + BASE_ID + "-01 in case of valid but wrong response.",
					"Run test case TC-" + BASE_ID + "-01 and let server send a wrong but valid response.",
					"Test case will fail");
			desc.emphasize().logHeader();
			assertThrows(AssertionFailedError.class, () -> testcase.testUnicast());
			logger.info("Test case TC-" + BASE_ID + "-01 failed which is the correct and expected result.");
			TestCaseDescription.logFooter(TestResult.PASSED, true);
		} catch (AssertionFailedError e) {
			TestCaseDescription.logFooter(TestResult.FAILED, true);
			throw e;
		} catch (Exception e) {
			TestCaseDescription.logFooter(TestResult.ERROR, true);
			throw logger.throwing(Level.FATAL,
					new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(markerExit,  "<<< public void testUnicastWrongResponse()");
		}
	}
	
	@Test
	@DisplayName("ST-1000-01-04")
	public void testUnicastInvalidResponse() {
		server.setNextUdpResponse(new byte[] {0x03, (byte) 0xFC, 0x0, 0x0});
		assertThrows(AssertionFailedError.class, () -> testcase.testUnicast());
	}
}
