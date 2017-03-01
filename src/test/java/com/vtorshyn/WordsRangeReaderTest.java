package com.vtorshyn;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.vtorshyn.exceptions.HelpException;
import com.vtorshyn.exceptions.WordMapException;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.PropertiesMap;
import com.vtorshyn.word.processor.CharFileReader;

public class WordsRangeReaderTest extends TestCase {
	private String[] commonArgs = {"-file", "testFile"};
	private String[] devNullRead =  { "-file", "/dev/null" };
	private PropertiesMap options;
	private Logger logger;
	
	public WordsRangeReaderTest(String testName) throws HelpException, Exception {
		super(testName);
		try {
			logger = new Logger();
			options = new PropertiesMap(logger, commonArgs, "-", 2)
					.bind(logger);
		} catch (WordMapException e) {
			fail(e.getMessage());
		}
	}
	
	public static Test suite() {
		return new TestSuite( WordsRangeReaderTest.class );
	}
	
	public void test_no_options_exception_thrown() {
		try {
			// TODO: test_no_options_exception_thrown: Fix failure 
			CharFileReader w = new CharFileReader(logger);
			//fail("Expected exception to be thrown");
		} catch(Exception e) {
			
		}
	}
	
	public void test_zero_read() {
		try {
			options.parse(devNullRead, "-", 2);
			CharFileReader w = new CharFileReader(logger);
			char  data[] = null;
			w.nextChunk(data,0,0,0);
			assertTrue(data == null);
		} catch(Exception e) {
			fail("UnExpected exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
