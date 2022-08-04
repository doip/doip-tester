package doip.tester.testcases;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import doip.logging.LogManager;
import doip.logging.Logger;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.exception.DiagnosticServiceExecutionFailed;
import doip.tester.toolkit.exception.RoutingActivationFailed;
import doip.junit.Assertions;
import doip.junit.SetUpBeforeClassFailed;
import doip.junit.TestCaseDescription;
import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.util.Helper;

public class TC_1070_DiagnosticMessage {

	private static Logger logger = LogManager.getLogger(TC_1000_VehicleIdentification.class);
	
	private static TestSetup testSetup = null;


	@BeforeAll
	static void setUpBeforeClass() throws SetUpBeforeClassFailed {
		String function = "static void setUpBeforeClass()"; 
		try {
			logger.info(">>> " + function);
			testSetup = new TestSetup();
			testSetup.initialize("src/test/resources/tester.properties");
		} catch (IOException e) {
			throw new SetUpBeforeClassFailed("Unexpected IOException had been thrown.", e);
		} catch (MissingProperty e) {
			throw new SetUpBeforeClassFailed("Unexpected MissingProperty had been thrown.", e); 
		} catch (EmptyPropertyValue e) {
			throw new SetUpBeforeClassFailed("Unexpected EmptyPropertyValue had been thrown.", e); 
		} finally {
			logger.info("<<< " + function);
		}
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		String function = "static void tearDownAfterClass()";
		try {
			logger.info(">>> " + function);
			if (testSetup != null) {
				testSetup.uninitialize();
				testSetup = null;
			}
		} finally {
			logger.info("<<< " + function);
		}
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("TC-1070-01")
	void testDiagnosticMessage() throws IOException, DiagnosticServiceExecutionFailed, InterruptedException, RoutingActivationFailed {
		String function = "void testDiagnosticMessage()";
		try {
			logger.info(">>> " + function);
			
			new TestCaseDescription(
					"TC-1070-01", 
					"Send diagnostic mesage after successful routing activation", 
					"1. Send routing activation 2. Send diagnostic message 0x10 0x03", 
					"1. Routing activation was successful 2. Response from DoIP server is 0x50 0x03 ...")
				.log();
			
			TesterTcpConnection conn = testSetup.createTesterTcpConnection();
			conn.performRoutingActivation(0);
			conn.executeDiagnosticService(new byte[] {0x10, 0x03}, true);
			logger.info("TEST PASSED");
		} catch (IOException | DiagnosticServiceExecutionFailed e) {
			logger.error("Unexpected " + e.getClass().getName());
			logger.error(Helper.getExceptionAsString(e));
			logger.error("TEST FAILED");
			throw e;
		} finally { 
			logger.info("<<< " + function);
		}
	}
	
	@Test
	@Disabled
	 void testDiagnosticessageWithoutRoutingActivation() throws IOException, InterruptedException{
		String function  = "void testDiagnosticessageWithoutRoutingActivation()";
		try {
			logger.info(">>> " + function);
			TesterTcpConnection conn = testSetup.createTesterTcpConnection();
			
			Assertions.assertThrows(DiagnosticServiceExecutionFailed.class, () -> {
				conn.executeDiagnosticService(new byte[] {0x10, 0x03}, true);
			});
			
			
			logger.info("TEST PASSED");
		} catch (IOException e) {
			logger.error("Unexpected " + e.getClass().getName());
			logger.error(Helper.getExceptionAsString(e));
			logger.error("TEST FAILED");
			throw e;
		} finally { 
			logger.info("<<< " + function);
		}
	}

}
