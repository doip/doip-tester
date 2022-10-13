package doip.tester.toolkit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventMessage;

public class TestUtils {

	private static Logger logger = LogManager.getLogger(TestUtils.class);
	/*
	public void checkEvent(DoipEvent event, Class<? extends DoipEvent> clazz) {
		
		if (event == null) {
			// Check if it a DoipEventMessage was expected
			if (DoipEventMessage.class.isAssignableFrom(clazz)) {
	
			}
		}
	
	}*/
	

}
