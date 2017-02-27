package com.vtorshyn;

import com.vtorshyn.utils.OptionsMap;
import com.vtorshyn.word.processor.SingleThreadWordProcessor;
import com.vtorshyn.word.processor.WordProcessor;
import com.vtorshyn.word.processor.WordProcessorBuilder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WordProcessorBuilderTest extends TestCase {
	
	private String[] singleThreadOptions = {"-file", "/dev/null", "-parallelFactor", "1"};
	private OptionsMap options;
	
	public WordProcessorBuilderTest(String testName) {
		super(testName);
	}
	
	public static Test suite() {
		return new TestSuite( WordProcessorBuilderTest.class );
	}
	
	public void test_singleThread_Processor_Created() {
		try {
			WordProcessor processor = new WordProcessorBuilder(singleThreadOptions).construct();
			assertTrue(processor instanceof SingleThreadWordProcessor);
		} catch(Exception e) {
			fail("Unexpected exception");
		}
	}
}
