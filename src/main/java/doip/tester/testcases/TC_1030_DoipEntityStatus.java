package doip.tester.testcases;

import static doip.junit.Assertions.*;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.SystemPropertiesLookup;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.message.DoipMessage;
import doip.library.message.DoipUdpEntityStatusRequest;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterUdpCommModule;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventMessage;
import doip.tester.toolkit.event.DoipEventUdpEntityStatusResponse;

public class TC_1030_DoipEntityStatus {
	
	private static Logger logger = LogManager.getLogger(TC_1030_DoipEntityStatus.class);
	
	private static TestSetup setup = null;
	
	private static TesterUdpCommModule comm = null;
	
	private static TestConfig config = null;
	
	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		logger.trace(">>> public static void setUpBeforeAll()");
		try {
			setup = new TestSetup();
			setup.initialize();
			config = setup.getConfig();
			comm = setup.getTesterUdpCommModule();
		} finally {
			logger.trace("<<< public static void setUpBeforeAll()");
		}
	}
	
	@AfterAll
	public static void tearDownAfterAll() {
		logger.trace(">>> public static void tearDownAfterAll()");
		try {
			if (setup != null) {
				setup.uninitialize();
				setup = null;
			}
		} finally {
			logger.trace("<<< public static void tearDownAfterAll()");
		}
	}
	
	@Test
	public void test() throws TestExecutionError {
		logger.trace(">>> public void test()");
		TestCaseDescription desc = null;
		try {
			desc = new TestCaseDescription(
					"TC-1030-01",
					"Send DoIP entity status request and check response",
					"---", "---");
			desc.logHeader();
			
			comm.clearEvents();
			comm.send(new DoipUdpEntityStatusRequest(), config.getTargetAddress(), config.getTargetPort());
			DoipEvent event = comm.waitForEvents(1, config.get_A_DoIP_Ctrl());
			assertNotNull(event, "A DoIP entity status request had been sent but no response " +
					"has been received within A_DoIP_Ctrl.");
			if (!(event instanceof DoipEventUdpEntityStatusResponse)) {
				if (event instanceof DoipEventMessage) {
					DoipEventMessage messageEvent = (DoipEventMessage) event;
					fail("A DoIP entity status request had been sent but a wrong response has been received. " +
							"A \"" + DoipMessage.getPayloadTypeAsString(DoipMessage.TYPE_UDP_ENTITY_STATUS_RES) +
							"\" was expected, but a \"" + messageEvent.getDoipMessage().getName()
							+ "\" has been received.");
				} else {
					fail("A DoIP entity status request had been sent but a wrong event did occur."
							+ " A \"" + DoipMessage.getPayloadTypeAsString(DoipMessage.TYPE_UDP_ENTITY_STATUS_RES) +
							"\" was expected, but a \"" + event.getClass().getName()
							+ "\" did occur.");
				}
			}
			
			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (IOException | InterruptedException e) {
			logger.fatal("Unexpected "+ e.getClass().getName() +": " + e.getMessage());
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(new TestExecutionError(e));
		} finally {
			logger.trace("<<< public void test()");
		}
	}

}
