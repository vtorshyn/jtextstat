package com.vtorshyn;

import com.vtorshyn.utils.OptionsMap;
import com.vtorshyn.word.processor.MultiThreadWordProcessor;
import com.vtorshyn.word.processor.WordProcessor;
import com.vtorshyn.word.processor.WordProcessorBuilder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WordProcessorMTBuilderTest extends TestCase {
	
	private String[] multiThreadOptions = {"-file", "/dev/null", "-parallelFactor", "2"};
	private OptionsMap options;
	
	public WordProcessorMTBuilderTest(String testName) {
		super(testName);
	}
	
	public static Test suite() {
		return new TestSuite( WordProcessorBuilderTest.class );
	}
	
	public void test_multiThread_Processor_Created() {
		try {
			options = OptionsMap.get();
			options.loadFromStringArray(multiThreadOptions, "-");
			WordProcessor processor = new WordProcessorBuilder(multiThreadOptions).construct();
			assertTrue(processor instanceof MultiThreadWordProcessor);
		} catch(Exception e) {
			fail("Unexpected exception:" + e);
			e.printStackTrace();
		}
	}
}
