package com.vtorshyn.word.processor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.vtorshyn.WordMapBuilder;


public class SingleThreadWordProcessor extends WordProcessor {

	public void start() throws Exception {
		init();
		WordsRangeReader rd = new WordsRangeReader();
		WordMapBuilder mapBuilder = new WordMapBuilder();
		wordsMap = new HashMap<String, Integer>(512);
		char[] buffer;

		// Read next chunk of data from file buffer
		while ((buffer = rd.nextChunk()) != null) {
			Map<String, Integer> m = mapBuilder.buildFromCharArray(buffer);
			for(Map.Entry<String, Integer> e : m.entrySet()) {
				// Merge processed map into global map
				wordsMap.merge(e.getKey(), e.getValue(), Integer::sum);
			}
		}
		if (debugRaw.length() > 0)
			logger.log(wordsMap.toString());
	}
}
