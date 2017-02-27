package com.vtorshyn;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.vtorshyn.utils.CharUtils;

public class CharUtilsTest extends TestCase {

	private char alphaNum[] = {'a', 'A', 'z', 'Z', '0','9'};
	private char nonAlphaNum[] =  {'!', '?', '~', '.', ',', '-', '\n'};
	
	public CharUtilsTest( String testName ){
		super( testName );
	}

	public static Test suite() {
		return new TestSuite( CharUtilsTest.class );
	}
	
	public void test_check_alpha_nums() {
		int idx = 0;
		for (; idx < alphaNum.length; ++idx) {
			char inspected = alphaNum[idx];
			assertTrue("Checking CharUtils.isazAZ09('" + inspected +"')...", CharUtils.isazAZ09(inspected) == true);
		}
	}
	
	public void test_check_non_alpha_nums() {
		int idx = 0;
		for (; idx < nonAlphaNum.length; ++idx) {
			char inspected = nonAlphaNum[idx];
			assertTrue("Checking CharUtils.isazAZ09('" + inspected +"')...", CharUtils.isazAZ09(inspected) == false);
		}
	}
}
