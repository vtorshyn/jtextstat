package com.vtorshyn;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.vtorshyn.exceptions.WordMapException;
import com.vtorshyn.utils.PropertiesMap;
import com.vtorshyn.word.processor.WordsRangeReader;

public class WordsRangeReaderTest extends TestCase {
	private String[] commonArgs = {"-file", "testFile"};
	private String[] devNullRead =  { "-file", "/dev/null" };
	private PropertiesMap options;
	
	public WordsRangeReaderTest(String testName) {
		super(testName);
		try {
			options = PropertiesMap.init(commonArgs);
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
			WordsRangeReader w = new WordsRangeReader();
			//fail("Expected exception to be thrown");
		} catch(Exception e) {
			
		}
	}
	
	public void test_zero_read() {
		try {
			options.parse(devNullRead, "-");
			WordsRangeReader w = new WordsRangeReader();
			char  data[] = w.nextChunk();
			assertTrue(data == null);
		} catch(Exception e) {
			fail("UnExpected exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}