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

import static doip.junit.Assertions.*;
import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.util.StringConstants;
import doip.tester.testcases.TC_2010_VehicleIdentificationWithEid;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.server4unittest.DoipServer4UnitTest;

public class ST_2010_VehicleIdentificationWithEid {
	
	public static final String BASE_ID = "2010";
	
	private static Logger logger = LogManager.getLogger(ST_2010_VehicleIdentificationWithEid.class);
	private static Marker markerEnter = MarkerManager.getMarker("ENTER");
	private static Marker markerExit = MarkerManager.getMarker("EXIT");
	
	private TC_2010_VehicleIdentificationWithEid testcase = null;
	
	private static DoipServer4UnitTest server = null;

	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		try {
			logger.info(StringConstants.HASH_LINE);
			logger.trace(markerEnter, ">>> public static void setUpBeforeAll()");
			
			server = new DoipServer4UnitTest();
			server.start();
			
			TC_2010_VehicleIdentificationWithEid.setUpBeforeAll();
			
		} catch (IOException e) {
			throw logger.throwing(Level.FATAL, new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(markerExit, "<<< public static void setUpBeforeAll()");
		}
	}
	
	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(markerEnter, ">>> public static void tearDownAfterAll()");
			if (server != null) {
				server .stop();
				server = null;
			}
		} finally {
			logger.trace(markerExit, "<<< public static void tearDownAfterAll()");
			logger.info(StringConstants.HASH_LINE);
		}
	}
	
	@BeforeEach
	public void setUp() {
		try {
			logger.info(StringConstants.HASH_LINE);
			logger.trace(markerEnter, ">>> public void setUp()");
			
			testcase = new TC_2010_VehicleIdentificationWithEid();
			
		} finally {
			logger.trace(markerExit, "<<< public void setUp()");
		}
	}
	
	@AfterEach
	public void tearDown() {
		try {
			logger.trace(markerEnter, ">>> public void tearDown()");
			
			testcase = null;
			
		} finally {
			logger.trace(markerExit, "<<< public void tearDown()");
			logger.info(StringConstants.HASH_LINE);
		}
	}
	
	@Test
	@DisplayName("ST-" + BASE_ID + "-01-01")
	public void test_01_VehicleIdentWithEid_01_GoodCase() throws TestExecutionError {
		TestCaseDescription desc = null;
		try {
			logger.trace(markerEnter, ">>> public void test_01_VehicleIdentWithEid_01_GoodCase()");
			
			desc = new TestCaseDescription("ST-" + BASE_ID + "-01-01",
					"Test test case TC-" + BASE_ID + "-01-01",
					"Run test case TC-" + BASE_ID + "-01",
					"Test reports that it passed.");
			desc.emphasize().logHeader();
			
			server.setSilent(false);
			testcase.test_01_VehicleIdentWithEid();
			desc.logFooter(TestResult.PASSED); 
		} catch (TestExecutionError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} finally {
			logger.trace(markerExit, "<<< public void test_01_VehicleIdentWithEid_01_GoodCase()");
		}
	}
	
	@Test
	@DisplayName("ST-" + BASE_ID + "-01-02")
	public void test_01_VehicleIdentWithEid_02_NoResponse() {
		TestCaseDescription desc = null;
		String function = "public void test_01_VehicleIdentWithEid_02_NoResponse()";
		try {
			logger.trace(markerEnter, ">>> " + function);
			desc = new TestCaseDescription("ST-" + BASE_ID + "-01-01",
					"Test test case TC-" + BASE_ID + "-01-01",
					"Run test case TC-" + BASE_ID + "-01 where server does not answer",
					"Test reports that it failed.");
			desc.emphasize().logHeader();
			
			server.setSilent(true);
			assertThrows(AssertionFailedError.class, () -> testcase.test_01_VehicleIdentWithEid());
			
			desc.logFooter(TestResult.PASSED);
		} finally {
			logger.trace(markerExit, "<<< " + function);
		}
	}
}
