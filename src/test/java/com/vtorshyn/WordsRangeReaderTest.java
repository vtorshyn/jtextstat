package com.vtorshyn;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.vtorshyn.exceptions.WordMapException;
import com.vtorshyn.utils.OptionsMap;
import com.vtorshyn.word.processor.WordsRangeReader;

public class WordsRangeReaderTest extends TestCase {
	private String[] commonArgs = {"-file", "testFile"};
	private String[] devNullRead =  { "-file", "/dev/null" };
	private OptionsMap options;
	
	public WordsRangeReaderTest(String testName) {
		super(testName);
		try {
			options = OptionsMap.init(commonArgs);
		} catch (WordMapException e) {
			fail(e.getMessage());
		}
	}
	
	public static Test suite() {
		return new TestSuite( WordsRangeReaderTest.class );
	}
	
	public void test_no_options_exception_thrown() {
		try {
			WordsRangeReader w = new WordsRangeReader();
			fail("Excpected exception to be thrown");
		} catch(Exception e) {
			
		}
	}
	
	public void test_zero_read() {
		try {
			options.loadFromStringArray(devNullRead, "-");
			WordsRangeReader w = new WordsRangeReader();
			char  data[] = w.nextChunk();
			assertTrue(data == null);
		} catch(Exception e) {
			fail("UnExpected exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
