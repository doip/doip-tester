package doip.junit;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestCaseDescription {

	// Strings are 78 characters long
	public static final String SINGLE_LINE = "------------------------------------------------------------------------------";
	public static final String DOUBLE_LINE = "==============================================================================";
	public static final String HASH_LINE   = "##############################################################################";
	
	private String id;
	private String description;
	private String action;
	private String expectedResult;
	private String additionalInfo;
	private int maxLength = 78;
	private String separator = DOUBLE_LINE;

	private static Logger logger = LogManager.getLogger(TestCaseDescription.class);
	
	public TestCaseDescription(String id, String description, String action, String expectedResult) {
		this.id = id;
		this.description = description;
		this.action = action;
		this.expectedResult = expectedResult;
		this.additionalInfo = null;
	}

	public TestCaseDescription(String id, String description, String action, String expectedResult, String additionalInfo) {
		this.id = id;
		this.description = description;
		this.action = action;
		this.expectedResult = expectedResult;
		this.additionalInfo = additionalInfo;
	}
	
	public void setAdditionalInfo(String info) {
		this.additionalInfo = info;
	}

	public String getSeparator() {
		return this.separator;
	}
	
	/**
	 * Returns the test case description 
	 */
	public String toString() {
		String string;
		string =     separator +
				   "\nTest case ID:    " + id +
				   "\n" + separator +
				   "\nDescription:\n    " + formatSection(description, maxLength - 4, "    ") +
				   "\nAction:\n    " + formatSection(action, maxLength - 4, "    ") +
				   "\nExpected result:\n    " + formatSection(expectedResult, maxLength -4 , "    ") +
				   "\n" + separator;
		
		if (this.additionalInfo != null) {
			string += "\n" + formatSection(this.additionalInfo, maxLength, "")
			 + "\n" + separator;
		}
		return string;
	}
	
	/**
	 * Logs the test case as level "info" 
	 */
	public void logHeader() {
		String[] lines = this.toString().split("\n");
		for (String line: lines) {
			logger.info(line);
		}
	}
	
	public void logFooter(TestResult result) {
		logger.info(getSeparator());
		switch (result) {
		case PASSED:
			logger.info("TEST " + result);
			break;
		case FAILED:
		case ERROR:
			logger.error("TEST " + result);
			break;
		default:
			logger.fatal("TEST " + result);
			break;
		}
		logger.info(getSeparator());
	}
	
	
	public TestCaseDescription emphasize() {
		this.separator = HASH_LINE;
		return this;
	}
	
	/**
	 * This function will take a string and wraps the text where
	 * the line would exceed a given length.
	 * @param text The text which shall get wrapped
	 * @param limit The maximum number of characters in one line
	 * @param indent Gives an indent which will be added at the beginning
	 * 		of the next lines
	 * @return The wrapped text
	 */
	public String formatParagraph(String paragraph, int limit, String indent) {
		StringBuilder returnString = new StringBuilder(4096);
		String line = "";
		String[] words = paragraph.split("\\s+");
		
		Iterator<String> iter = Arrays.asList(words).iterator();
		while (iter.hasNext()) {
			String word = iter.next();
			if (line.length() + word.length() > limit) {
				// Not enough space
				returnString.append(line);
				returnString.append("\n");
				returnString.append(indent);
				line = word;
			} else {
				// Enough space
				if (line.length() > 0 && !line.matches("\\s+")) line +=  " ";
				line += word;
			}
		}
		returnString.append(line);
		return returnString.toString();
	}
	
	/**
	 * It will take a String as input which constist of multiple
	 * paragraphs. 
	 * @param text
	 * @param limit Maximum number of characters in a line
	 * @param indent Indentation if required
	 * @return
	 */
	public String formatSection(String text, int limit, String indent) {
		StringBuilder returnString = new StringBuilder(4096);
		
		String[] paragraphs = text.split("\n");
		
		Iterator<String> iter = Arrays.asList(paragraphs).iterator();
		while (iter.hasNext()) {
			String paragraph = iter.next();
			String formattedParagraph = formatParagraph(paragraph, limit, indent);
			returnString.append(formattedParagraph);
			if (iter.hasNext()) {
				returnString.append('\n');
				returnString.append(indent);
			}
		}
		
		return returnString.toString();
	}
}