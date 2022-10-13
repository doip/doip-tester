package doip.tester.selftests;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import doip.junit.InitializationError;
import doip.junit.TestExecutionError;
import doip.tester.testcases.TC_9020_LongTcpMessage;
import doip.tester.toolkit.TextBuilder;
import doip.tester.toolkit.server4unittest.DoipServer4UnitTest;

public class ST_9020_LongTcpMessage {

	public static Logger logger = LogManager.getLogger(ST_9020_LongTcpMessage.class);
	public static Marker enter = MarkerManager.getMarker("ENTER");
	public static Marker exit = MarkerManager.getMarker("EXIT");
	
	public static DoipServer4UnitTest server = null;
	public TC_9020_LongTcpMessage testcase = null;
	
	@BeforeAll
	public static void setUpBeforeAll() throws InitializationError {
		try {
			logger.trace(enter, ">>> public static void setUpBeforeAll()");
			server = new DoipServer4UnitTest() {
				
			};
			server.start();
			TC_9020_LongTcpMessage.setUpBeforeAll();
		} catch (IOException e) {
			throw logger.throwing(new InitializationError(TextBuilder.unexpectedException(e), e));
		} finally {
			logger.trace(exit, "<<< public static void setUpBeforeAll()");
		}
	}
	
	@AfterAll
	public static void tearDownAfterAll() {
		try {
			logger.trace(enter, ">>> public static void tearDownAfterAll()");
			TC_9020_LongTcpMessage.tearDownAfterAll();
			server.stop();
		} finally {
			logger.trace(exit, "<<< public static void tearDownAfterAll()");
		}
	}
	
	@BeforeEach
	public void setUp() throws InitializationError {
		try {
			logger.trace(enter, ">>> public void setUp()");
			testcase = new TC_9020_LongTcpMessage();
			testcase.setUp();
		} finally {
			logger.trace(exit, "<<< public void setUp");
		}
	}
	
	@AfterEach
	public void tearDown() {
		try {
			logger.trace(enter, ">>> public void tearDown()");
			if (testcase != null) {
				testcase.tearDown();
				testcase = null;
			}
		} finally {
			logger.trace(exit, "<<< public void tearDown()");
		}
	}
	
	@Test
	public void testGoodCase() throws TestExecutionError {
		try {
			logger.trace(enter, ">>> public void testGoodCase()");
			
			testcase.test();
		
		} finally {
			logger.trace(exit, "<<< public void testGoodCase()");
		}
	}
}
