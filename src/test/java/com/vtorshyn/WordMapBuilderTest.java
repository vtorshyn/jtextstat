package com.vtorshyn;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Map;

import com.vtorshyn.WordMapBuilder;
import com.vtorshyn.utils.OptionsMap;

public class WordMapBuilderTest extends TestCase {
	private OptionsMap options;
	private WordMapBuilder builder;
	private String[] commonArgs = {"-file", "testFile"};

	private char[] words_buffer = {'w','o','r', 'd', ' ', 'w', 'o', 'r', 'd', ' ', 'w', 'o', 'r', 'd'};
	private char[] words_non_alpha_buffer = {'w','o','r', 'd', '!', 'w', 'o', 'r', 'd', '#', 'w', 'o', 'r', 'd'};
	private char[] words_non_alpha_buffer_space_in_end = {'w','o','r', 'd', '!', 'w', 'o', 'r', 'd', '#', 'w', 'o', 'r', 'd', ' '};
	private char[] words_non_alpha_buffer_many_spaces_in_begin_and_end = {' ', ' ', ' ', ' ', 'w','o','r', 'd', '!', ' ', ' ', 'w', 'o', 'r', 'd', ' ', '#', ' ', 'w', 'o', 'r', 'd', ' ', ' ', ' ', ' '};
	
	@SuppressWarnings("static-access")
	public WordMapBuilderTest( String testName ){
		super( testName );
		try {
			options = options.init(commonArgs);
			builder = new WordMapBuilder();
		} catch(Exception e) {
			fail();
		}
	}

	public static Test suite() {
		return new TestSuite( WordMapBuilderTest.class );
	}

	public void test_WordMapBuilder_no_non_alpha() {
		Map<String,Integer>m = builder.buildFromCharArray(words_buffer);
		assertTrue("Checking total words count in the map", m.size() == 1);
		assertTrue("Checking word \"word\" frequency", m.get("word") == 3);
	}

	public void test_WordMapBuilder_with_non_alpha() {
		Map<String,Integer>m = builder.buildFromCharArray(words_non_alpha_buffer);
		assertTrue("Checking total words count in the map", m.size() == 1);
		assertTrue("Checking word \"word\" frequency", m.get("word") == 3);
	}
	
	public void test_WordMapBuilder_with_non_alpha_space_in_end() {
		Map<String,Integer>m = builder.buildFromCharArray(words_non_alpha_buffer_space_in_end);
		assertTrue("Checking total words count in the map", m.size() == 1);
		assertTrue("Checking word \"word\" frequency", m.get("word") == 3);
	}
	
	public void test_WordMapBuilder_with_non_alpha_many_spaces() {
		Map<String,Integer>m = builder.buildFromCharArray(words_non_alpha_buffer_many_spaces_in_begin_and_end);
		assertTrue("Checking total words count in the map", m.size() == 1);
		assertTrue("Checking word \"word\" frequency", m.get("word") == 3);
	}
}
