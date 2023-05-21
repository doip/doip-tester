package doip.tester.testcases;

import static doip.junit.Assertions.*;

import java.io.IOException;
import java.io.ObjectInputFilter.Config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.message.DoipUdpHeaderNegAck;
import doip.library.message.DoipUdpVehicleIdentRequest;
import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.properties.MissingSystemProperty;
import doip.library.util.Helper;
import doip.library.util.StringConstants;
import doip.tester.toolkit.CheckResult;
import doip.tester.toolkit.EventChecker;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.event.DoipEvent;
import doip.tester.toolkit.event.DoipEventUdpHeaderNegAck;
import doip.tester.toolkit.event.DoipEventUdpVehicleAnnouncementMessage;

public class TC_2100_DoubleUdpMessage {
	
	public static final String BASE_ID = "2100";
	
	private static Logger logger = LogManager.getLogger(TC_2100_DoubleUdpMessage.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT)");
	
	private static TestSetup setup = null;

	@BeforeAll
	public static void setUpBeforeClass() throws InitializationError {
		
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(enter, ">>> public static void setUpBeforeClass()");

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			setup = new TestSetup();
			setup.initialize();
			
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} catch (IOException | EmptyPropertyValue | MissingProperty | MissingSystemProperty e) {
			throw logger.throwing(new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< public static void setUpBeforeClass()");
		}
	}

	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(enter, ">>> public static void tearDownAfterClass()");
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			if (setup != null) {
				setup.uninitialize();
				setup = null;
			}
			
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			logger.trace(exit, "<<< public static void tearDownAfterClass()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

/*	
	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(enter, ">>> public void setUp()");
			
			// --- SET UP CODE BEGIN ----------------------------------------
			
			// --- SET UP CODE END ------------------------------------------
			
		} finally {
			logger.info(exit, "<<< public void setUp()");
		}
	}

	@AfterEach
	public void tearDown() {
		try {
			logger.trace(enter, ">>> public void tearDown()");
			
			// --- TEAR DOWN CODE BEGIN --------------------------------------
			
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			logger.trace(exit, "<<< public void tearDown()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}
*/
	@Test
	@DisplayName("TC-" + BASE_ID + "-01")
	public void test_01_VehicleIdentRequest() throws TestExecutionError {
		String function = "public void test()";
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> " + function);

			
			// --- TEST CODE BEGIN --------------------------------------------
			desc = new TestCaseDescription(
					"TC-" + BASE_ID + "-01",
					"Send UDP packet which contains a '" + DoipUdpVehicleIdentRequest.getMessageNameOfClass() + "' twice.",
					"",
					"");
			desc.logHeader();
			testImpl_01_VehicleIdentRequest();
			desc.logFooter(TestResult.PASSED);
			// --- TEST CODE END ----------------------------------------------
			
		} catch (AssertionError e) {
			desc.logFooter(TestResult.FAILED);
			throw e;
		} catch (TestExecutionError e) {
			desc.logFooter(TestResult.ERROR);
			throw e;
		} catch (Exception e) {
			desc.logFooter(TestResult.ERROR);
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< " + function);
		}
	}
	
	public void testImpl_01_VehicleIdentRequest() throws TestExecutionError {
		String function = "public void testImpl_01_VehicleIdentRequest()";
		try {
			logger.trace(enter, ">>> " + function);
			DoipUdpVehicleIdentRequest doipUdpVehicleIdentRequest = new DoipUdpVehicleIdentRequest();
			byte[] virMessage = doipUdpVehicleIdentRequest.getMessage();
			byte[] doubleMessage = Helper.concat(virMessage, virMessage);
			setup.getTesterUdpCommModule().clearEvents();
			setup.getTesterUdpCommModule().send(doubleMessage, setup.getConfig().getTargetAddress());
			DoipEvent event = setup.getTesterUdpCommModule().waitForEvents(1, setup.getConfig().get_A_DoIP_Ctrl());
			CheckResult result = EventChecker.checkEvent(event, DoipEventUdpHeaderNegAck.class);
			if (result.getCode() != CheckResult.NO_ERROR) {
				fail(result.getText());
			}
			DoipEventUdpHeaderNegAck doipEventUdpHeaderNegAck = (DoipEventUdpHeaderNegAck) event;
			DoipUdpHeaderNegAck doipUdpHeaderNegAck = (DoipUdpHeaderNegAck) doipEventUdpHeaderNegAck.getDoipMessage();
			assertEquals(DoipUdpHeaderNegAck.NACK_INVALID_PAYLOAD_LENGTH, doipUdpHeaderNegAck.getCode(), 
					"The NACK Code is wrong");
			
		} catch (IOException | InterruptedException e) {
			throw new TestExecutionError(TextBuilder.unexpectedException(e), e);
		} finally {
			logger.trace(exit, "<<< " + function);
		}
	}
}
