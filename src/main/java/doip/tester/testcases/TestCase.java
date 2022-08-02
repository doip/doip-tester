package doip.tester.testcases;

import doip.library.util.StringConstants;
import doip.logging.LogManager;
import doip.logging.Logger;

/**
 * Base class for all test cases
 *
 */
public class TestCase {
	
	private static Logger logger = LogManager.getLogger(TestCase.class);
/*
	public static void printTestCaseDescription(String text) {
			logger.info("\n" + StringConstants.WALL + "\n" + text + "\n" + StringConstants.WALL);
	}
	*/
}
