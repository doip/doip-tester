package doip.tester.testcases;

import static doip.junit.Assertions.assertNotNull;
import static doip.junit.Assertions.assertTrue;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.util.Helper;
import doip.library.util.StringConstants;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterUdpCommModule;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventUdpVehicleAnnouncementMessage;

public class TC_1000_VehicleIdentification {
	
	private static Logger logger = LogManager.getLogger(TC_1000_VehicleIdentification.class);
	
	private static TestSetup testSetup = null;

	@BeforeAll
	public static void setUpBeforeClass() throws InitializationError {
		try {
			logger.trace(">>> public static void setUpBeforeClass()");
			logger.info(StringConstants.SINGLE_LINE);
			
			testSetup = new TestSetup();
			testSetup.initialize();
			
		} finally {
			logger.trace("<<< public static void setUpBeforeClass()");
		}
	}

	@AfterAll
	public static void tearDownAfterClass() {
		try {
			logger.trace(">>> public static void tearDownAfterClass()");
			
			if (testSetup != null) {
				testSetup.uninitialize();
				testSetup = null;
			}


		} finally {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace("<<< public static void tearDownAfterClass()");
		}
	}

	/**
	 * Action: Send a vehicle identification request
	 * Expected result: Gateway sends vehicle identification response
	 * @throws TestExecutionError 
	 * @throws IOException
	 */
	@Test
	@DisplayName("TC-1000-01")
	public void testUnicast() throws TestExecutionError {
		String function = "public void testUnicast()";
		TestCaseDescription desc = null;
		try {
			logger.trace(">>> " + function);
		
			desc = new TestCaseDescription(
					"TC-1000-01", 
					"Testing vehicle identification with unicast address.",
					"Send a vehicle identification request message to the "
					+ "unicast address of the DoIP server.",
					"The ECU sends a vehicle identification response message.");
				desc.logHeader();
			
			testValidVir(testSetup.getConfig().getTargetAddress());
			desc.logFooter(TestResult.PASSED);
			
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (Exception e) {
			logger.fatal("Unexpected " + e.getClass().getName() + ": " + e.getMessage());
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(new TestExecutionError(e));
		} finally {
			logger.trace("<<< " + function);
		}
	}
	
	
	@Test
	@DisplayName("TC-1000-02")
	public void testBroadcast() {
		String function = "public void testBroadcast()";
		TestCaseDescription desc = null;
		try {
			logger.info(StringConstants.DOUBLE_LINE);
			logger.info(">>> " + function);
			desc = new TestCaseDescription(
					"TC-1000-02", 
					"Testing vehicle identification with broadcast address.",
					"Send a vehicle identification request message to the "
					+ "unicast address of the DoIP server.",
					"The ECU sends a vehicle identification response message.");
			desc.logHeader();
			
			testValidVir(testSetup.getConfig().getBroadcastAddress());
			
		} finally {
			logger.info("<<< " + function);
			logger.info(StringConstants.DOUBLE_LINE);
		}
	}
	
	public void testValidVir(InetAddress address) {
		String method = "public void testValidVir(InetAddress address)";
		try {
			logger.info(StringConstants.DOUBLE_LINE);
			logger.info(">>> " + method);
			TesterUdpCommModule testerUdpCommModule = testSetup.getTesterUdpCommModule();
			testerUdpCommModule.clearEvents();
			TestConfig config = testSetup.getConfig();
			
			logger.info("Send valid vehicle identification request");
			testerUdpCommModule.sendDoipUdpVehicleIdentRequest(address);
			
			logger.info("Wait for incoming response");
			DoipEvent event = testerUdpCommModule.waitForEvents(1, config.get_A_DoIP_Ctrl());
			
			assertNotNull(event, "Did not receive a response on DoIP vehicle identification request");
			assertTrue(event instanceof DoipEventUdpVehicleAnnouncementMessage,"The received response is not of type DoIP vehicle identification response message");
			logger.info("Received response as expected");
			logger.info("TEST PASSED");
		} catch (IOException e) {
			logger.error("Unexpected " + e.getClass().getName() + " in testValidVir()");
			logger.error(Helper.getExceptionAsString(e));
			throw new AssertionFailedError("Test failed due to previous " + e.getClass().getName());
		} catch (InterruptedException e) {
			logger.error("Unexpected " + e.getClass().getName() + " in testValidVir()");
			logger.error(Helper.getExceptionAsString(e));
			throw new AssertionFailedError("Test failed due to previous " + e.getClass().getName());
		} finally {
			logger.trace("<<< " + method);
			logger.info(StringConstants.DOUBLE_LINE);
		}
	}
}
