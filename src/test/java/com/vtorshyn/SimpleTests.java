package com.vtorshyn;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SimpleTests extends TestCase {
	public SimpleTests( String testName ){
		super( testName );
	}

	public static Test suite() {
		return new TestSuite( SimpleTests.class );
	}
	/**!
	* Simple test for Reader when input data is larger than app level read buffer
	*/
	public void test_Check_Reader_With_Chunks() {
	}

	/**!
	* Simple test for Reader when app level read buffer is larger than input data
	*/
	public void test_Check_Reader_With_No_Chunks() {
	}
	/**!
	* Simple test for ApplicationOptions class logic (all-in-one, not good)
	*/
	public void test_Check_ApplicationOptions() {
	}
}
