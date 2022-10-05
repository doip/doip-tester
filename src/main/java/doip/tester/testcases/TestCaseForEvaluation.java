package doip.tester.testcases;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import doip.junit.Assertions;
import doip.library.util.Helper;
import doip.library.util.StringConstants;
import doip.logging.LogManager;
import doip.logging.Logger;

public class TestCaseForEvaluation  {
	
	private static Logger logger = LogManager.getLogger(TestCaseForEvaluation .class);

	@BeforeAll
	public static void setUpBeforeClass() {
		
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.SINGLE_LINE);
				logger.info(">>> public static void setUpBeforeClass()");
			}

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public static void setUpBeforeClass()");
				logger.info(StringConstants.SINGLE_LINE);
			}
		}
	}

	@AfterAll
	public static void tearDownAfterClass() {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.SINGLE_LINE);
				logger.info(">>> public static void tearDownAfterClass()");
			}
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public static void tearDownAfterClass()");
				logger.info(StringConstants.SINGLE_LINE);
			}
		}
	}

	@BeforeEach
	public void setUp() throws Exception {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.SINGLE_LINE);
				logger.info(">>> public void setUp()");
			}

			// --- SET UP CODE BEGIN ----------------------------------------

			throw new Exception("Hello Exception");
			// --- SET UP CODE END ------------------------------------------
			
		} catch (Exception e){
			logger.error(Helper.getExceptionAsString(e));
			throw e;
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public void setUp()");
				logger.info(StringConstants.SINGLE_LINE);
			}	
		}
	}

	@AfterEach
	public void tearDown() {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.SINGLE_LINE);
				logger.info(">>> public void tearDown()");
			}
			
			// --- TEAR DOWN CODE BEGIN --------------------------------------
			
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public void tearDown()");
				logger.info(StringConstants.SINGLE_LINE);
			}
		}
	}

	// TODO: remove back slashes in next line 
	@Test
	public void test() {
		String function = "public void test()";
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.DOUBLE_LINE);
				logger.info(">>> " + function);
			}
			
			// --- TEST CODE BEGIN --------------------------------------------
			
			// --- TEST CODE END ----------------------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< " + function);
				logger.info(StringConstants.DOUBLE_LINE);
			}
		}
	}
}
