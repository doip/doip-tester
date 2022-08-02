package doip.tester.testcases;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import doip.library.util.Helper;
import doip.library.util.StringConstants;
import doip.logging.LogManager;
import doip.logging.Logger;

public class TestTemplate {
	
	private static Logger logger = LogManager.getLogger(TestTemplate.class);

	@BeforeAll
	public static void setUpBeforeClass() {
		
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.LINE);
				logger.info(">>> public static void setUpBeforeClass()");
			}

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public static void setUpBeforeClass()");
				logger.info(StringConstants.LINE);
			}
		}
	}

	@AfterAll
	public static void tearDownAfterClass() {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.LINE);
				logger.info(">>> public static void tearDownAfterClass()");
			}
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public static void tearDownAfterClass()");
				logger.info(StringConstants.LINE);
			}
		}
	}

	@BeforeEach
	public void setUp() {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.LINE);
				logger.info(">>> public void setUp()");
			}
			
			// --- SET UP CODE BEGIN ----------------------------------------
			
			// --- SET UP CODE END ------------------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public void setUp()");
				logger.info(StringConstants.LINE);
			}	
		}
	}

	@AfterEach
	public void tearDown() {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.LINE);
				logger.info(">>> public void tearDown()");
			}
			
			// --- TEAR DOWN CODE BEGIN --------------------------------------
			
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public void tearDown()");
				logger.info(StringConstants.LINE);
			}
		}
	}

	// TODO: remove back slashes in next line 
	//@Test
	public void test() {
		String function = "public void test()";
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.WALL);
				logger.info(">>> " + function);
			}
			
			// --- TEST CODE BEGIN --------------------------------------------
			
			// --- TEST CODE END ----------------------------------------------
			
		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< " + function);
				logger.info(StringConstants.WALL);
			}
		}
	}
}
