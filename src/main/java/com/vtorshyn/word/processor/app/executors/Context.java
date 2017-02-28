package com.vtorshyn.word.processor.app.executors;

import java.util.Map;

import com.vtorshyn.WordMapBuilder;
import com.vtorshyn.utils.Logger;

public class Context {
	public char[] buffer;
	public WordMapBuilder builder;
	public Map<String, Integer> wordsMap;
	public Logger logger;
	
	public Context(Logger logger, Map<String, Integer> wordsMap, char[] buffer, WordMapBuilder builder) {
		this.logger = logger;
		this.wordsMap = wordsMap;
		this.buffer = buffer;
		this.builder = builder;
	}
}