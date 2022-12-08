package doip.tester.selftests;

import static doip.junit.Assertions.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import doip.junit.InitializationError;
import doip.junit.TestCaseDescription;
import doip.junit.TestExecutionError;
import doip.junit.TestResult;
import doip.library.comm.DoipUdpMessageHandler;
import doip.library.message.DoipUdpHeaderNegAck;
import doip.library.util.Helper;
import doip.library.util.LookupTable;
import doip.library.util.StringConstants;
import doip.tester.testcases.TC_2100_DoubleUdpMessage;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.server4unittest.DoipServer4UnitTest;

public class ST_2100_DoubleMessages {
	
	public static final String BASE_ID = "2100";
	
	private static Logger logger = LogManager.getLogger(ST_2100_DoubleMessages.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT)");
	
	private TC_2100_DoubleUdpMessage testcase = null;
	
	public static DoipServer4UnitTest createSpecialServer(int nackCode) {
		return new DoipServer4UnitTest() {
			@Override
			public DoipUdpMessageHandler createDoipUdpMessageHandler(String udpReceiverThreadName, LookupTable lookupTable) {
				return new DoipUdpMessageHandler(udpReceiverThreadName, lookupTable) {
					@Override
					public boolean processDatagramByFunction(DatagramPacket packet) {
						DoipUdpHeaderNegAck doipResponse = new DoipUdpHeaderNegAck(nackCode);
						byte[] response = doipResponse.getMessage();
						InetAddress targetAddress = packet.getAddress();
						int targetPort = packet.getPort();
						try {
							this.sendDatagramPacket(response, response.length, targetAddress, targetPort);
						} catch (IOException e) {
							logger.error(Helper.getExceptionAsString(e));
						}
						return true;
					}
				};
			}
		};
	}
	
	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		
		try {
			logger.info(StringConstants.HASH_LINE);
			logger.trace(enter, ">>> public static void setUpBeforeAll()");

			// --- SET UP BEFORE CLASS BEGIN --------------------------------
			TC_2100_DoubleUdpMessage.setUpBeforeClass();
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
			
			TC_2100_DoubleUdpMessage.tearDownAfterAll();
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
			//logger.info("Create instance of test case '" + TC_2100_DoubleUdpMessage.class.getSimpleName() + "' and call function 'setUp()'.");
			logger.info("Create instance of test case '" + TC_2100_DoubleUdpMessage.class.getSimpleName() + "'.");
			testcase = new TC_2100_DoubleUdpMessage();
			//testcase.setUp();
			// --- SET UP CODE END ------------------------------------------
			
		} finally {
			logger.trace(exit, "<<< public void setUp()");
		}
	}


	@AfterEach
	public void tearDown() {
		try {
			logger.trace(enter, ">>> public void tearDown()");
			
			// --- TEAR DOWN CODE BEGIN --------------------------------------
			/*
			if (testcase != null) {
				logger.info("Call function 'tearDown()' in test case class '" + TC_2100_DoubleUdpMessage.class.getSimpleName() + "'.");
				testcase.tearDown();
				testcase = null;
			}*/
			//logger.info("wait for 100 ms before next test case will be started.");
			//Thread.sleep(100);
			// --- TEAR DOWN CODE END ----------------------------------------
			
		//} catch (InterruptedException e) {
		//	logger.fatal("Thread.sleep(...) has been interrupted.");
		} finally {
			logger.trace(exit, "<<< public void tearDown()");
			logger.info(StringConstants.HASH_LINE);
		}
	}
	@Test
	@DisplayName("ST-" + BASE_ID + "-01-01")
	public void test_01_VehicleIdentRequest_01_GoodCase() throws TestExecutionError {
		String function = "public void test_01_VehicleIdentRequest_01_GoodCase()";
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> " + function);

			// --- TEST CODE BEGIN --------------------------------------------
			desc = new TestCaseDescription(
					"ST-" + BASE_ID + "-01-01",
					"Test test case TC-2100-01",
					"Execute test case TC-2100-01",
					"The test runs successful");
			desc.emphasize().logHeader();
			testImpl_01_VehicleIdentRequest_01_GoodCase();
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
	
	public void testImpl_01_VehicleIdentRequest_01_GoodCase() throws TestExecutionError {
		DoipServer4UnitTest server = null;
		try {
			server = new DoipServer4UnitTest();
			server.start();
			testcase.test_01_VehicleIdentRequest();
		} catch (IOException e) {
			throw new TestExecutionError(TextBuilder.unexpectedException(e), e);
		} finally {
			if (server != null) {
				server.stop();
				server = null;
			}
		}
	}

	@Test
	@DisplayName("ST-" + BASE_ID + "-01-02")
	public void test_01_VehicleIdentRequest_02_WrongNackCode() throws TestExecutionError {
		String function = "public void test_01_VehicleIdentRequest_02_WrongNackCode()";
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> " + function);

			// --- TEST CODE BEGIN --------------------------------------------
			desc = new TestCaseDescription(
					"ST-" + BASE_ID + "-01-02",
					"Test test case TC-" + BASE_ID + "-01 with wrong NACK code.",
					"Execute test case TC-" + BASE_ID + "-01, but DoIP server response with a wrong NACK code.",
					"The test will fail and report that the NACK code is wrong.");
			desc.emphasize().logHeader();
			testImpl_01_VehicleIdentRequest_02_WrongNackCode();
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

	private void testImpl_01_VehicleIdentRequest_02_WrongNackCode() throws TestExecutionError {
		DoipServer4UnitTest server = null;
		try {
			server = createSpecialServer(DoipUdpHeaderNegAck.NACK_UNKNOWN_PAYLOAD_TYPE);
			server.start();
			assertThrows(AssertionFailedError.class, () -> testcase.test_01_VehicleIdentRequest());
		} catch (IOException e) {
			throw logger.throwing(new TestExecutionError(TextBuilder.unexpectedException(e), e));
		} finally {
			if (server != null) {
				server.stop();
				server = null;
			}
		}
	}
	
	@Test
	@DisplayName("ST-" + BASE_ID + "-01-03")
	public void test_01_VehicleIdentRequest_03_RunTesCaseTwice() throws TestExecutionError {
		String function = "public void test_01_VehicleIdentRequest_03_RunTestCaseTwice()";
		TestCaseDescription desc = null;
		try {
			logger.trace(enter, ">>> " + function);

			// --- TEST CODE BEGIN --------------------------------------------
			desc = new TestCaseDescription(
					"ST-" + BASE_ID + "-01-03",
					"Test test case TC-" + BASE_ID + "-01 twice.",
					"Execute test case TC-" + BASE_ID + "-01 twice.",
					"The test runs successful");
			desc.emphasize().logHeader();
			testImpl_01_VehicleIdentRequest_03_RunTestCaseTwice();
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

	public void testImpl_01_VehicleIdentRequest_03_RunTestCaseTwice() throws TestExecutionError {
		DoipServer4UnitTest server = null;
		try {
			server = new DoipServer4UnitTest();
			server.start();
			testcase.test_01_VehicleIdentRequest();
			//testcase.tearDown();
			testcase = new TC_2100_DoubleUdpMessage();
			//testcase.setUp();
			testcase.test_01_VehicleIdentRequest();
		} catch (IOException e) {
			throw new TestExecutionError(TextBuilder.unexpectedException(e), e);
		} finally {
			if (server != null) {
				server.stop();
				server = null;
			}
		}
	}
}
