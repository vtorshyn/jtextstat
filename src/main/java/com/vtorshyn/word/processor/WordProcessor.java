package com.vtorshyn.word.processor;

import java.util.Map;

public interface WordProcessor {
	
	public void start() throws Exception;
	public Map<String, Integer> getSortedMap();
	public int getTotalMapSize();
	public int getTotalNumberOfProcessedItems();
}