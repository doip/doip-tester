package doip.tester.testcases;

import static doip.junit.Assertions.assertNotNull;
import static doip.junit.Assertions.assertTrue;

import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.message.DoipUdpVehicleAnnouncementMessage;
import doip.library.message.DoipUdpVehicleIdentRequestWithEid;
import doip.library.util.StringConstants;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.EventChecker;
import doip.tester.toolkit.TesterUdpCommModule;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventMessage;
import doip.tester.toolkit.event.DoipEventUdpVehicleAnnouncementMessage;

public class TC_2010_VehicleIdentificationWithEid {
	
	public static final String BASE_ID = "2010";
	
	private static Logger logger = LogManager.getLogger(TC_2010_VehicleIdentificationWithEid.class);
	private static Marker markerEnter = MarkerManager.getMarker("ENTER");
	private static Marker markerExit  = MarkerManager.getMarker("EXIT");
	
	private static TestSetup setup = null;
	private static TestConfig config = null;
	private static TesterUdpCommModule comm = null;
	private static byte[] eid = null;
	
	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(markerEnter, ">>> public static void setUpBeforeAll()");
			setup = new TestSetup();
			setup.initialize();
			config = setup.getConfig();
			comm = setup.getTesterUdpCommModule();
			
			// We first read the EID from ECU with message "Vehice Identification Request"
			comm.clearEvents();
			comm.sendDoipUdpVehicleIdentRequest(config.getTargetAddress());
			DoipEvent event = comm.waitForEvents(1, config.get_A_DoIP_Ctrl());
			
			assertNotNull(event, "Did not receive a response on DoIP vehicle identification request");
			assertTrue(event instanceof DoipEventUdpVehicleAnnouncementMessage, 
					"The received response is not of type DoIP vehicle identification response message");
			
			DoipUdpVehicleAnnouncementMessage vam = 
					(DoipUdpVehicleAnnouncementMessage)((DoipEventMessage) event).getDoipMessage();
			
			eid = vam.getEid();
		} catch (InitializationError e) {
			throw e;
		} catch (IOException | InterruptedException e) {
			throw logger.throwing(Level.FATAL, new InitializationError(e));
		} finally {
			logger.trace(markerEnter, "<<< public static void setUpBeforeAll()");
		}
	}
	
	@Test
	@Disabled
	@DisplayName("TC-" + BASE_ID + "-01")
	public void test() throws TestExecutionError {
		TestCaseDescription desc = null;
		try {
			logger.trace(markerEnter, ">>> public void testGoodCase()");
			desc = new TestCaseDescription(
					"TC-" + BASE_ID + "-01",
					"Test vehicle identification with EID",
					"Send a vehicle identification with EID message",
					"A vehicle identification response message will be sent");
			desc.logHeader();
			
			comm.clearEvents();
			comm.sendDoipUdpVehicleIdentRequestWithEid(eid, config.getTargetAddress());
			DoipEvent event = comm.waitForEvents(1, config.get_A_DoIP_Ctrl());
			
			assertNotNull(event, "Did not receive a response on DoIP vehicle identification request with EID");
			assertTrue(event instanceof DoipEventUdpVehicleAnnouncementMessage, 
					"The received response is not of type DoIP vehicle identification response message");
			
			desc.logFooter(TestResult.PASSED);
		} catch (AssertionFailedError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (IOException | InterruptedException e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(Level.FATAL, new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(markerExit, "<<< public void testGoodCase()");
		}
	}
}
