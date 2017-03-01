package com.vtorshyn.word.processor.app.executors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.vtorshyn.WordMapBuilder;

public class Context {
	public Map<String, Integer> wordsMap;
	public Map<String, Integer> temporaryMap;
	public char[] buffer;
	public WordMapBuilder builder;
	
	public Context(Map<String, Integer> map, char[] buffer, WordMapBuilder builder) {
		this.wordsMap = map;
		this.buffer = Arrays.copyOf(buffer, buffer.length);
		this.builder = builder;
		this.temporaryMap = new HashMap<>();
	}
	
	public Context() {
		this.buffer = null;
	}
}