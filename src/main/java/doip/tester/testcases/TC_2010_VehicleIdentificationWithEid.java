package doip.tester.testcases;

import static doip.junit.Assertions.*;

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
import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.properties.MissingSystemProperty;
import doip.library.util.StringConstants;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.CheckResult;
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
			
			// --- SET UP BEFORE ALL BEGIN --------------------------------
			setup = new TestSetup();
			setup.initialize();
			config = setup.getConfig();
			comm = setup.getTesterUdpCommModule();
			
			// We first read the EID from ECU with message "Vehice Identification Request"
			comm.clearEvents();
			comm.sendDoipUdpVehicleIdentRequest(config.getTargetAddress());
			DoipEvent event = comm.waitForEvents(1, config.get_A_DoIP_Ctrl());
			CheckResult result = EventChecker.checkEvent(event, DoipEventUdpVehicleAnnouncementMessage.class);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}
			
			DoipUdpVehicleAnnouncementMessage vam = (DoipUdpVehicleAnnouncementMessage) ((DoipEventUdpVehicleAnnouncementMessage)  event).getDoipMessage();
			eid = vam.getEid();
			
			// --- SET UP BEFORE ALL END ----------------------------------
			
		} catch (IOException | InterruptedException | EmptyPropertyValue | MissingProperty | MissingSystemProperty e) {
			throw logger.throwing(Level.FATAL, new InitializationError(e));
		} finally {
			logger.trace(markerEnter, "<<< public static void setUpBeforeAll()");
		}
	}
	
	@Test
	@Disabled
	@DisplayName("TC-" + BASE_ID + "-01")
	public void test_01_VehicleIdentWithEid() throws TestExecutionError {
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
			CheckResult result = EventChecker.checkEvent(event, DoipEventUdpVehicleAnnouncementMessage.class);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}
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
