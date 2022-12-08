package doip.junit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.function.Executable;
import org.opentest4j.AssertionFailedError;
import org.opentest4j.MultipleFailuresError;

public class Assertions {

	private static Logger logger = LogManager.getLogger(Assertions.class);
	public static Level assertionFailed = Level.getLevel("ASSERTION_FAILED");

	public static void assertAll(Executable... executables) {
		try {
			org.junit.jupiter.api.Assertions.assertAll(executables);
		} catch (AssertionError e) {
			throw logger.throwing(e);
			//throw e;
		}
	}
	
	public static void assertTrue(boolean condition) {
		try {
			org.junit.jupiter.api.Assertions.assertTrue(condition);
		}  catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertTrue(boolean condition, String message) {
		try {
			org.junit.jupiter.api.Assertions.assertTrue(condition, message);
		}  catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertFalse(boolean condition) {
		try {
			org.junit.jupiter.api.Assertions.assertFalse(condition);
		}  catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertFalse(boolean condition, String message) {
		try {
			org.junit.jupiter.api.Assertions.assertFalse(condition, message);
		}  catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}
	
	public static void assertEquals(int expected, int actual) {
		try {
			org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
		}  catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertEquals(int expected, int actual, String message) {
		try {
			org.junit.jupiter.api.Assertions.assertEquals(expected, actual, message);
		} catch (AssertionFailedError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertEquals(String expected, String actual) {
		try {
			org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertEquals(String expected, String actual, String message) {
		try {
			org.junit.jupiter.api.Assertions.assertEquals(expected, actual, message);
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertArrayEquals(byte[] expecteds, byte[] actuals) {
		try {
			org.junit.jupiter.api.Assertions.assertArrayEquals(expecteds, actuals);
		} catch (AssertionFailedError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertArrayEquals(byte[] expecteds, byte[] actuals, String message) {
		try {
			org.junit.jupiter.api.Assertions.assertArrayEquals(expecteds, actuals, message);
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertNotNull(Object obj) {
		try {
			org.junit.jupiter.api.Assertions.assertNotNull(obj);
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertNotNull(Object obj, String message) {
		try {
			org.junit.jupiter.api.Assertions.assertNotNull(obj, message);
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertNull(Object obj) {
		try {
			org.junit.jupiter.api.Assertions.assertNull(obj);
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertNull(Object obj, String message) {
		try {
			org.junit.jupiter.api.Assertions.assertNull(obj, message);
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertEquals(boolean expected, boolean actual) {
		try {
			org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static void assertEquals(boolean expected, boolean actual, String message) {
		try {
			org.junit.jupiter.api.Assertions.assertEquals(expected, actual, message);
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}
	
	public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable) {
		try {
			return org.junit.jupiter.api.Assertions.assertThrows(expectedType, executable);
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}
	
	public static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable, String message) {
		try {
			return org.junit.jupiter.api.Assertions.assertThrows(expectedType, executable, message);
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static <V> V fail() {
		try {
			return org.junit.jupiter.api.Assertions.fail();
		} catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	public static <V> V fail(String message) {
		try {
			return org.junit.jupiter.api.Assertions.fail(message);
		}
		 catch (AssertionError e) {
			throw logger.throwing(assertionFailed, e);
		}
	}

	/**
	 * Returns an exception including its stack trace as string.
	 * @param e The exception
	 * @return The exception as string
	 */
	/*
	private static String getExceptionAsString(Throwable e) {
		StringBuilder s = new StringBuilder(4096);
		String message = e.getMessage();
		if (message != null) {
			s.append(message);
		}
		s.append("\n");
		s.append(e.getClass().getName());
		s.append("\n");
		StackTraceElement[] elements = e.getStackTrace();

		for (StackTraceElement element : elements) {
			s.append("    ");
			s.append(element);
			s.append("\n");
		}

		return s.toString();
	}*/
}
