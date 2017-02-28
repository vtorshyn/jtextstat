package com.vtorshyn.word.processor.app.executors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.vtorshyn.WordMapBuilder;
import com.vtorshyn.utils.LogLevels;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.PropertiesMap;
import com.vtorshyn.word.processor.WordsRangeReader;
import com.vtorshyn.word.processor.app.Application;


public class Simple extends Application {
	public Simple(Logger logger, PropertiesMap propeties) throws Exception {
		propeties.bind(this);
		init(logger, propeties);
	}

	public Map<String, Integer> start() throws Exception {
		WordsRangeReader rd = new WordsRangeReader(logger());
		WordMapBuilder mapBuilder = new WordMapBuilder(logger());
		
		// Initializing objects properties supplied in PropertiesMap
		properties().bind(rd);
		properties().bind(mapBuilder);
		
		// Initializing char stream reader
		// It should be called after binding to PropertiesMap
		rd.init();
		
		wordsMap = new HashMap<String, Integer>(512);
		int bufferSize = rd.bufferSize();
		int maxFragmentsScan = rd.bufferReserve();
		char[] buffer = new char[bufferSize + maxFragmentsScan];

		// Read next chunk of data from file buffer
		while (rd.nextChunk(buffer, 0, bufferSize, maxFragmentsScan) > 0) {
			Map<String, Integer> m = mapBuilder.buildFromCharArray(buffer);
			for(Map.Entry<String, Integer> e : m.entrySet()) {
				// Merge processed map into global map
				wordsMap.merge(e.getKey(), e.getValue(), Integer::sum);
			}
		}
		if (isDebugRawOn())
			logger().log(LogLevels.LOG_DEBUG, wordsMap.toString());
		return wordsMap;
	}
}
