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
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.exception.DiagnosticServiceExecutionFailed;
import doip.tester.toolkit.exception.RoutingActivationFailed;
import doip.junit.Assertions;
import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestResult;
import doip.library.util.Helper;
import doip.library.util.StringConstants;

public class TC_1070_DiagnosticMessage {

	private static Logger logger = LogManager.getLogger(TC_1000_VehicleIdentification.class);
	
	private static TestSetup testSetup = null;
	private static TestConfig config = null;


	@BeforeAll
	static void setUpBeforeClass() throws InitializationError {
		String function = "static void setUpBeforeClass()"; 
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(">>> " + function);
			testSetup = new TestSetup();
			testSetup.initialize();
			config = testSetup.getConfig();
		} finally {
			logger.trace("<<< " + function);
		}
	}

	@AfterAll
	static void tearDownAfterClass() {
		String function = "static void tearDownAfterClass()";
		try {
			logger.trace(">>> " + function);
			if (testSetup != null) {
				testSetup.uninitialize();
				testSetup = null;
			}
		} finally {
			logger.trace("<<< " + function);
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

	@BeforeEach
	void setUp() {
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	@DisplayName("TC-1070-01")
	void testDiagnosticMessage() throws IOException, DiagnosticServiceExecutionFailed, InterruptedException, RoutingActivationFailed {
		String function = "void testDiagnosticMessage()";
		TestCaseDescription desc = null;
		try {
			logger.info(">>> " + function);
			
			desc = new TestCaseDescription(
					"TC-1070-01", 
					"Send diagnostic mesage after successful routing activation", 
					"1. Send routing activation 2. Send diagnostic message 0x10 0x03", 
					"1. Routing activation was successful 2. Response from DoIP server is 0x50 0x03 ...");
			desc.logHeader();
			
			TesterTcpConnection conn = testSetup.createTesterTcpConnection();
			conn.performRoutingActivation(config.getTesterAddress(), 0);
			conn.executeDiagnosticService(new byte[] {0x10, 0x03});
			desc.logFooter(TestResult.PASSED);
		} catch (IOException | DiagnosticServiceExecutionFailed e) {
			logger.error("Unexpected " + e.getClass().getName());
			logger.error(Helper.getExceptionAsString(e));
			desc.logFooter(TestResult.FAILED);
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
				conn.executeDiagnosticService(new byte[] {0x10, 0x03});
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
