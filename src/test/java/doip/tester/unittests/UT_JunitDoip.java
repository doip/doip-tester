package doip.tester.unittests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static doip.junit.Assertions.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class UT_JunitDoip {
	
	private static Logger logger = LogManager.getLogger(UT_JunitDoip.class);

	@Test
	@Disabled
	public void test() throws Exception {
		try {
			logger.info(">>> public void test()");
			assertAll(
				() -> fail("First assert that will fail"),
				() -> fail("Second assert that will fail")
			);
		} catch (Exception e) {
			throw logger.throwing(e);
		} finally {
			logger.info("<<< public void test()");
		}
	}
}
