package doip.tester.testcases;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import doip.junit.Assertions;
import doip.library.util.Helper;
import doip.library.util.StringConstants;

public class TestCaseForEvaluation  {
	
	private static Logger logger = LogManager.getLogger(TestCaseForEvaluation .class);
	public static Marker markerEnter = MarkerManager.getMarker("ENTER");
	public static Marker markerExit  = MarkerManager.getMarker("EXIT");

	@BeforeAll
	public static void setUpBeforeAll() {
		
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(markerEnter, ">>> public static void setUpBeforeClass()");

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} finally {
			logger.trace("<<< public static void setUpBeforeClass()");
		}
	}

	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(markerEnter, ">>> public static void tearDownAfterClass()");
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			logger.trace(markerExit, "<<< public static void tearDownAfterClass()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

	@BeforeEach
	public void setUp() throws Exception {
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(markerEnter, ">>> public void setUp()");

			// --- SET UP CODE BEGIN ----------------------------------------

			throw new Exception("Hello Exception");

			// --- SET UP CODE END ------------------------------------------
			
		} catch (Exception e){
			logger.error(Helper.getExceptionAsString(e));
			throw e;
		} finally {
			logger.trace(markerExit, "<<< public void setUp()");
		}
	}

	@AfterEach
	public void tearDown() {
		try {
			logger.trace(markerEnter, ">>> public void tearDown()");
			
			// --- TEAR DOWN CODE BEGIN --------------------------------------
			
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			logger.trace(markerExit, "<<< public void tearDown()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

	// TODO: remove back slashes in next line 
	@Test
	public void test() {
		String function = "public void test()";
		try {
			logger.info(StringConstants.DOUBLE_LINE);
			logger.trace(markerEnter, ">>> " + function);
			
			// --- TEST CODE BEGIN --------------------------------------------
			
			// --- TEST CODE END ----------------------------------------------
			
		} finally {
			logger.info(markerExit, "<<< " + function);
			logger.info(StringConstants.DOUBLE_LINE);
		}
	}
}
