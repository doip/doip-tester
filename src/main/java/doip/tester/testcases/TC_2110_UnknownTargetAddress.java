package doip.tester.testcases;

import static doip.junit.Assertions.*;

import java.io.IOException;

import org.apache.logging.log4j.Level;
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
import org.opentest4j.MultipleFailuresError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.properties.MissingSystemProperty;
import doip.library.util.StringConstants;
import doip.tester.toolkit.TestConfig;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TesterTcpConnection;
import doip.tester.toolkit.TextBuilder;

public class TC_2110_UnknownTargetAddress {
	
	public static final String BASE_ID = "2110";
	
	private static Logger logger = LogManager.getLogger(TC_2110_UnknownTargetAddress.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT)");
	
	private static TestSetup setup = null;

	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		
		try {
			logger.info(StringConstants.SINGLE_LINE);
			logger.trace(enter, ">>> public static void setUpBeforeAll()");

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			setup = new TestSetup();
			setup.initialize();
			
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} catch (IOException | EmptyPropertyValue | MissingProperty | MissingSystemProperty e) {
			throw logger.throwing(new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< public static void setUpBeforeAll()");
		}
	}

	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(enter, ">>> public static void tearDownAfterAll()");
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			if (setup != null) {
				setup.uninitialize();
				setup = null;
			}
			
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			logger.trace(exit, "<<< public static void tearDownAfterAll()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

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
			logger.info(enter, ">>> public void tearDown()");
			
			// --- TEAR DOWN CODE BEGIN --------------------------------------
			
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			logger.info(exit, "<<< public void tearDown()");
			logger.info(StringConstants.SINGLE_LINE);
		}
	}

	@Test
	@DisplayName("TC-" + BASE_ID + "-01")
	public void test_01_UnknownTargetAddress() throws TestExecutionError {
		String function = "public void test()";
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> " + function);

			// --- TEST CODE BEGIN --------------------------------------------
			desc = new TestCaseDescription(
					"TC-" + BASE_ID + "-01",
					"",
					"",
					"");
			desc.logHeader();
			testImpl_01_UnknownTargetAddress();
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
	
	public void testImpl_01_UnknownTargetAddress() throws TestExecutionError {
		TesterTcpConnection conn = null;
		try {
			TestConfig config = setup.getConfig();
			conn = setup.createTesterTcpConnection();
			TestFunctions.performRoutingActivation(conn, config, 0, -1);
			TestFunctions.executeDiagnosticServiceAndCheckforNegAck(
					conn,
					config.getTesterAddress(),
					0x0000,
					new byte[] {0x10, 0x03},
					config.get_A_DoIP_Diagnostic_Message()
					);
		} catch (IOException e) {
			throw logger.throwing(Level.FATAL, new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			
		}
	}
}
