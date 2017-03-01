package com.vtorshyn;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Map;

import com.vtorshyn.WordMapBuilder;
import com.vtorshyn.exceptions.HelpException;
import com.vtorshyn.exceptions.WordMapException;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.PropertiesMap;;

public class WordMapBuilderTest extends TestCase {
	private PropertiesMap options;
	private WordMapBuilder builder;
	private Logger logger;
	private String[] commonArgs = {"-file", "testFile"};

	private String words_buffer = "word word word\0";
	private String words_non_alpha_buffer = "word&word?word\0";
	private char[] words_non_alpha_buffer_space_in_end = {'w','o','r', 'd', '!', 'w', 'o', 'r', 'd', '#', 'w', 'o', 'r', 'd', ' '};
	private char[] words_non_alpha_buffer_many_spaces_in_begin_and_end = {' ', ' ', ' ', ' ', 'w','o','r', 'd', '!', ' ', ' ', 'w', 'o', 'r', 'd', ' ', '#', ' ', 'w', 'o', 'r', 'd', ' ', ' ', ' ', '\0'};
	
	@SuppressWarnings("static-access")
	public WordMapBuilderTest( String testName ){
		super( testName );
		try {
			logger = new Logger();
			options = new PropertiesMap(logger, commonArgs, "-", 2)
					.bind(logger);
			builder = new WordMapBuilder(logger);
		} catch(Exception e) {
			fail();
		}
	}

	public static Test suite() {
		return new TestSuite( WordMapBuilderTest.class );
	}

	public void test_WordMapBuilder_no_non_alpha_ignore_case() {
		String[] args = { "-ignoreCase", "true", "-file","file"};
		try {
			new PropertiesMap(logger, args, "-", 2).bind(builder);
		} catch (WordMapException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HelpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,Integer>m = builder.buildFromCharArray(words_buffer.toCharArray());
		System.out.println(m.toString());
		assertTrue("Checking total words count in the map", m.size() == 1);
		assertTrue("Checking word \"word\" frequency", m.get("word") == 3);
	}

	public void test_WordMapBuilder_with_non_alpha() {
		Map<String,Integer>m = builder.buildFromCharArray(words_non_alpha_buffer.toCharArray());
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
