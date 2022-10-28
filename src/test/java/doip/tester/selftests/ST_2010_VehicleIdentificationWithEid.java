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
import doip.junit.TestExecutionError;
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
		}
	}
	
	@BeforeEach
	public void setUp() {
		try {
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
		}
	}
	
	@Test
	@DisplayName("ST-" + BASE_ID + "-01-01")
	public void testGoodCase() throws TestExecutionError {
		try {
			logger.trace(markerEnter, ">>> public void testGoodCase()");
			
			server.setSilent(false);
			testcase.test();
			
		} catch (TestExecutionError e) {
			throw e;
		} finally {
			logger.trace(markerExit, "<<< public void testGoodCase()");
		}
	}
	
	@Test
	@DisplayName("ST-" + BASE_ID + "-1-02")
	public void testNoResponse() {
		try {
			logger.trace(markerEnter, ">>> public void testNoResponse()");
			server.setSilent(true);
			assertThrows(AssertionFailedError.class, () -> testcase.test());
		} finally {
			logger.trace(markerExit, "<<< public void testNoResponse()");
		}
	}
}
