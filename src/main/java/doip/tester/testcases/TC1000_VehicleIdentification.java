package doip.tester.testcases;

import static doip.junit.Assertions.*;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.junit.TestCaseDescription;
import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.util.Helper;
import doip.library.util.StringConstants;
import doip.logging.LogManager;
import doip.logging.Logger;
import doip.tester.toolkit.DoipUdpMessageHandlerWithEventCollection;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterUdpCommModule;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventUdpVehicleAnnouncementMessage;

public class TC1000_VehicleIdentification {
	
	private static Logger logger = LogManager.getLogger(TC1000_VehicleIdentification.class);
	
	private static TestSetup testSetup = null;

	@BeforeAll
	public static void setUpBeforeClass() throws IOException, MissingProperty, EmptyPropertyValue {
		try {
			if (logger.isInfoEnabled()) {
				logger.info(StringConstants.LINE);
				logger.info(">>> public static void setUpBeforeClass()");
			}
			
			testSetup = new TestSetup();
			testSetup.initialize("src/test/resources/tester.properties");
			
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
			
			if (testSetup != null) {
				testSetup.uninitialize();
				testSetup = null;
			}


		} finally {
			if (logger.isInfoEnabled()) {
				logger.info("<<< public static void tearDownAfterClass()");
				logger.info(StringConstants.LINE);
			}
		}
	}

	/**
	 * Action: Send a vehicle identification request
	 * Expected result: Gateway sends vehicle identification response
	 * @throws IOException
	 */
	@Test
	@DisplayName("TC-1000-01")
	public void testUnicast() {
		String function = "public void testUnicast()";
		try {
			logger.info(StringConstants.WALL);
			logger.info(">>> " + function);
		
			new TestCaseDescription(
					"TC-1000-01", 
					"Testing vehicle identification with unicast address.",
					"Send a vehicle identification request message to the "
					+ "unicast address of the DoIP server.",
					"The ECU sends a vehicle identification response message.")
				.log();
			
			testValidVir(testSetup.getConfig().getTargetAddress());
			
		} finally {
			logger.info("<<< " + function);
			logger.info(StringConstants.WALL);
		}
	}
	
	
	@Test
	@DisplayName("TC-1000-02")
	public void testBroadcast() {
		String function = "public void testBroadcast()";
		try {
			logger.info(StringConstants.WALL);
			logger.info(">>> " + function);
			new TestCaseDescription(
					"TC-1000-02", 
					"Testing vehicle identification with broadcast address.",
					"Send a vehicle identification request message to the "
					+ "unicast address of the DoIP server.",
					"The ECU sends a vehicle identification response message.")
				.log();
			
			testValidVir(testSetup.getConfig().getBroadcastAddress());
			
		} finally {
			logger.info("<<< " + function);
			logger.info(StringConstants.WALL);
		}
	}
	
	public void testValidVir(InetAddress address) {
		String method = "public void testValidVir(InetAddress address)";
		try {
			logger.info(StringConstants.WALL);
			logger.info(">>> " + method);
			TesterUdpCommModule testerUdpCommModule = testSetup.getTesterUdpCommModule();
			testerUdpCommModule.clearEvents();
			TestConfig config = testSetup.getConfig();
			
			logger.info("Send valid vehicle identification request");
			testerUdpCommModule.sendDoipUdpVehicleIdentRequest(address);
			
			logger.info("Wait for incoming response");
			boolean ret = testerUdpCommModule.waitForEvents(1, config.get_A_DoIP_Ctrl());
			
			assertTrue(ret, "Did not receive a response on DoIP vehicle identification request");
			DoipEvent event = testerUdpCommModule.getEvent(0);
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
			logger.info(StringConstants.WALL);
		}
	}
}
