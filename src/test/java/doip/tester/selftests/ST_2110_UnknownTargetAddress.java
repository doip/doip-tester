package doip.tester.selftests;

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

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.comm.DoipTcpConnection;
import doip.library.message.DoipTcpDiagnosticMessage;
import doip.library.message.DoipTcpDiagnosticMessageNegAck;
import doip.library.util.StringConstants;
import doip.tester.testcases.TC_2110_UnknownTargetAddress;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.server4unittest.DoipServer4UnitTest;

public class ST_2110_UnknownTargetAddress {
	
	public static final String BASE_ID = TC_2110_UnknownTargetAddress.BASE_ID;
	
	private static Logger logger = LogManager.getLogger(ST_2110_UnknownTargetAddress.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT)");
	
	private TC_2110_UnknownTargetAddress testcase = null;

	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		
		try {
			logger.info(StringConstants.HASH_LINE);
			logger.trace(enter, ">>> public static void setUpBeforeAll()");

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			TC_2110_UnknownTargetAddress.setUpBeforeAll();
			// --- SET UP BEFORE CLASS END ----------------------------------
			
		} finally {
			logger.trace(exit, "<<< public static void setUpBeforeAll()");
		}
	}

	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(enter, ">>> public static void tearDownAfterAll()");
			
			// --- TEAR DOWN AFTER CLASS BEGIN ------------------------------
			TC_2110_UnknownTargetAddress.tearDownAfterAll();
			// --- TEAR DOWN AFTER CLASS END --------------------------------
			
		} finally {
			logger.trace(exit, "<<< public static void tearDownAfterAll()");
			logger.info(StringConstants.HASH_LINE);
		}
	}

	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.info(StringConstants.HASH_LINE);
			logger.trace(enter, ">>> public void setUp()");
			
			// --- SET UP CODE BEGIN ----------------------------------------
			testcase = new TC_2110_UnknownTargetAddress();
			testcase.setUp();
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
			if (testcase != null) {
				testcase.tearDown();
				testcase = null;
			}
			// --- TEAR DOWN CODE END ----------------------------------------
			
		} finally {
			logger.info(exit, "<<< public void tearDown()");
			logger.info(StringConstants.HASH_LINE);
		}
	}

	@Test
	@DisplayName("ST-" + BASE_ID + "-01-01")
	public void test_01_UnknownTargetAddress_01_GoodCase() throws TestExecutionError {
		String function = "public void test()";
		TestCaseDescription desc = null;
		
		try {
			logger.trace(enter, ">>> " + function);

			// --- TEST CODE BEGIN --------------------------------------------
			desc = new TestCaseDescription(
					"ST-" + BASE_ID + "-01-01",
					"Test test case TC-" + BASE_ID + "-01, good case",
					"Execute test case TC-" + BASE_ID + "-01 where DoIP server responds as expected.",
					"Test case TC-" + BASE_ID +"-01 runs successful.");
			desc.emphasize().logHeader();
			testImpl_01_UnknownTargetAddress_01_GoodCase();
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
	
	public void testImpl_01_UnknownTargetAddress_01_GoodCase() throws TestExecutionError {
		DoipServer4UnitTest server = null;
		try  {
			server = createSpecialServer();
			server.start();
			testcase.test_01_UnknownTargetAddress();
		} catch (IOException e) {
			throw logger.throwing(Level.FATAL, new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			if (server != null) {
				server.stop();
				server = null;
			}
		}
	}

	@Test
	@DisplayName("ST-" + BASE_ID + "-01-02")
	public void test_01_UnknownTargetAddress_02_PosAck() throws TestExecutionError {
		String function = "public void test_01_UnknownTargetAddress_02_PosAck";
		TestCaseDescription desc = null;
		
		try {
			logger.trace(enter, ">>> " + function);

			// --- TEST CODE BEGIN --------------------------------------------
			desc = new TestCaseDescription(
					"ST-" + BASE_ID + "-01-02",
					"Test test case TC-" + BASE_ID + "-01, bad case",
					"Execute test case TC-" + BASE_ID + "-01 where DoIP server will send pos. ack. even if it is a invalid target address.",
					"Test case TC-" + BASE_ID +"-01 will fail.");
			desc.emphasize().logHeader();
			testImpl_01_UnknownTargetAddress_02_PosAck();
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

	public void testImpl_01_UnknownTargetAddress_02_PosAck() throws TestExecutionError {
		DoipServer4UnitTest server = null;
		try  {
			server = new DoipServer4UnitTest();
			server.start();
			assertThrows(AssertionFailedError.class, () -> testcase.test_01_UnknownTargetAddress());
		} catch (IOException e) {
			throw logger.throwing(Level.FATAL, new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			if (server != null) {
				server.stop();
				server = null;
			}
		}
	}
	
	public DoipServer4UnitTest createSpecialServer() {
		return new DoipServer4UnitTest() {
			
			private static Marker enter = MarkerManager.getMarker("ENTER");
			private static Marker exit = MarkerManager.getMarker("EXIT");
			
			@Override
			public void onDoipTcpDiagnosticMessage(DoipTcpConnection doipTcpConnection, DoipTcpDiagnosticMessage doipMessage) {
				try {
					logger.trace(enter, ">>> public void onDoipTcpDiagnosticMessage(DoipTcpConnection doipTcpConnection, DoipTcpDiagnosticMessage doipMessage)");
					if (isSilent()) {
						logger.debug("Gateway has been set to silent, therefore no response will be send.");
						return;
					}
					int sourceAddress = doipMessage.getSourceAddress();
					int targetAddress = doipMessage.getTargetAddress();
					
					DoipTcpDiagnosticMessageNegAck negAck =
							new DoipTcpDiagnosticMessageNegAck(targetAddress, sourceAddress, DoipTcpDiagnosticMessageNegAck.NACK_CODE_TARGET_UNREACHABLE, null);
					doipTcpConnection.send(negAck);
					
				} finally {
					logger.trace(exit, "<<< public void onDoipTcpDiagnosticMessage(DoipTcpConnection doipTcpConnection, DoipTcpDiagnosticMessage doipMessage)");
				}
			}

		};
	}
}
