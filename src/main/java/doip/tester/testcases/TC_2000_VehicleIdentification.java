package doip.tester.testcases;

import static doip.junit.Assertions.*;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.logging.log4j.Level;
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
import doip.library.message.DoipUdpVehicleAnnouncementMessage;
import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.properties.MissingSystemProperty;
import doip.library.util.Helper;
import doip.library.util.StringConstants;
import doip.tester.toolkit.CheckResult;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.EventChecker;
import doip.tester.toolkit.TesterUdpCommModule;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventUdpVehicleAnnouncementMessage;

public class TC_2000_VehicleIdentification {
	
	public static final String BASE_ID = "2000";
	
	private static Logger logger = LogManager.getLogger(TC_2000_VehicleIdentification.class);
	
	private static TestSetup testSetup = null;

	@BeforeAll
	public static void setUpBeforeClass() throws InitializationError {
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(">>> public static void setUpBeforeClass()");
			
			testSetup = new TestSetup();
			testSetup.initialize();
			
		} catch (IOException | EmptyPropertyValue | MissingProperty | MissingSystemProperty e) {
			throw logger.throwing(Level.FATAL, new InitializationError(TextBuilder.unexpectedException(e), e));
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
			logger.trace("<<< public static void tearDownAfterClass()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

	/**
	 * Action: Send a vehicle identification request
	 * Expected result: Gateway sends vehicle identification response
	 * @throws TestExecutionError 
	 * @throws IOException
	 */
	@Test
	@DisplayName("TC-" + BASE_ID + "-01")
	public void testUnicast() throws TestExecutionError {
		String function = "public void testUnicast()";
		TestCaseDescription desc = null;
		try {
			logger.trace(">>> " + function);
		
			desc = new TestCaseDescription(
					"TC-" + BASE_ID + "-01", 
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
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(Level.FATAL, 
					new TestExecutionError(TextBuilder.unexpectedException(e),e));
		} finally {
			logger.trace("<<< " + function);
		}
	}
	
	
	@Test
	@DisplayName("TC-" + BASE_ID + "-02")
	public void testBroadcast() throws TestExecutionError {
		String function = "public void testBroadcast()";
		TestCaseDescription desc = null;
		try {
			logger.trace(">>> " + function);
			desc = new TestCaseDescription(
					"TC-" + BASE_ID + "-02", 
					"Testing vehicle identification with broadcast address.",
					"Send a vehicle identification request message to the "
					+ "unicast address of the DoIP server.",
					"The ECU sends a vehicle identification response message.");
			desc.logHeader();
			
			testValidVir(testSetup.getConfig().getBroadcastAddress());
			desc.logFooter(TestResult.PASSED);
			
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (Exception e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(Level.FATAL, 
					new TestExecutionError(TextBuilder.unexpectedException(e),e));			
		} finally {
			logger.trace("<<< " + function);
		}
	}
	
	public void testValidVir(InetAddress address) {
		String method = "public void testValidVir(InetAddress address)";
		try {
			logger.trace(">>> " + method);
			TesterUdpCommModule testerUdpCommModule = testSetup.getTesterUdpCommModule();
			testerUdpCommModule.clearEvents();
			TestConfig config = testSetup.getConfig();
			
			logger.info("Send valid vehicle identification request");
			testerUdpCommModule.sendDoipUdpVehicleIdentRequest(address);
			
			DoipEvent event = testerUdpCommModule.waitForEvents(1, config.get_A_DoIP_Ctrl());
			CheckResult result = EventChecker.checkEvent(event, DoipEventUdpVehicleAnnouncementMessage.class);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}
			assertNotNull(event, TextBuilder.noValidDoipMessageReceived(
					DoipUdpVehicleAnnouncementMessage.getMessageNameOfClass()));
			
			String expected = DoipUdpVehicleAnnouncementMessage.getMessageNameOfClass();
			
			assertTrue(event instanceof DoipEventUdpVehicleAnnouncementMessage,
					"Did not receive a valid '" + expected + "'");
			
			
			logger.info("Received a valid DoIP vehicle identification response message as expected");
		} catch (IOException e) {
			logger.error("Unexpected " + e.getClass().getName() + " in testValidVir()");
			throw new AssertionFailedError("Test failed due to previous " + e.getClass().getName());
		} catch (InterruptedException e) {
			logger.error("Unexpected " + e.getClass().getName() + " in testValidVir()");
			logger.error(Helper.getExceptionAsString(e));
			throw new AssertionFailedError("Test failed due to previous " + e.getClass().getName());
		} finally {
			logger.trace("<<< " + method);
		}
	}
}
