package doip.tester.unittests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import doip.junit.InitializationError;
import doip.library.properties.EmptyPropertyValue;
import doip.library.properties.MissingProperty;
import doip.library.properties.MissingSystemProperty;
import doip.library.util.StringConstants;
import doip.tester.toolkit.TestSetup;
import doip.tester.toolkit.TextBuilder;

public class UT_4003_ParameterizedTest {
	
	private static Logger logger = LogManager.getLogger(UT_4003_ParameterizedTest.class);
	private static Marker enter = MarkerManager.getMarker("ENTER");
	private static Marker exit = MarkerManager.getMarker("EXIT");
	
	
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

	
	public static Stream<Arguments> provideParams() {
		try {
			logger.trace(enter, ">>> public static Stream<Arguments> provideParams()");
			return Stream.of(
					Arguments.of((byte) 0xFF, (byte) 0x00),
					Arguments.of((byte) 0xFD, (byte) 0x01)
					);
		} finally {
			logger.trace(enter, "<<< public static Stream<Arguments> provideParams()");
		}
	}
	
	@ParameterizedTest(name = "UT-4003-01[{index}]")
	@MethodSource("provideParams")
	public void test1(byte protocolVersion, byte inverseProtocolVersion) {
		logger.trace(enter, ">>> public void test1()");
		logger.trace(exit, "<<< public void test1()");
	}

	@ParameterizedTest(name = "UT-4003-02[{index}]")
	@MethodSource("provideParams")
	@Tag("slow")
	public void test2(byte x, byte y) {
		logger.trace(enter, ">>> public void test2()");
		logger.trace(exit, "<<< public void test2()");
	}
}
