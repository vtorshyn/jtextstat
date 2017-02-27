package com.vtorshyn.word.processor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import com.vtorshyn.utils.Logger;

import com.vtorshyn.WordMapBuilder;


public class MPWordProcessor extends WordProcessor implements Runnable {

	private Map<String, Integer> wordsMap;
	
	private int threadPoolSize = 1;
	
	private class Context {
		public char[] buffer;
		public WordMapBuilder builder;
		public Map<String, Integer> wordsMap;
		
		public Context(Map<String, Integer> wordsMap, char[] buffer, WordMapBuilder builder) {
			this.wordsMap = wordsMap;
			this.buffer = buffer;
			this.builder = builder;
		}
	}
	
	private Context ctx;
	
	public MPWordProcessor(int threadPoolSize) throws Exception {
		init();
		this.threadPoolSize = threadPoolSize;
	}
	
	private MPWordProcessor(Context ctx) throws Exception {
		//init();
		this.ctx = ctx;
	}

	public void start() throws Exception {
		ExecutorService s = Executors.newFixedThreadPool(this.threadPoolSize);
		WordsRangeReader rd = new WordsRangeReader();
		WordMapBuilder mapBuilder = new WordMapBuilder();
		wordsMap = new ConcurrentHashMap<String, Integer>(512);
		char[] buffer;

		while ((buffer = rd.nextChunk()) != null) {
			MPWordProcessor r = new MPWordProcessor(new Context(wordsMap, buffer, mapBuilder));
			s.execute(r);
		}
		
		s.shutdown();
		s.awaitTermination(10, TimeUnit.SECONDS);
		if (debugRaw.length() > 0)
			logger.log(wordsMap.toString());
	}

	public int getTotalMapSize() {
		return wordsMap.size();
	}

	public int getTotalNumberOfProcessedItems() {
		int total = 0;
		for (Map.Entry<String, Integer> e : wordsMap.entrySet()) {
			total += e.getValue().intValue();
		}
		return total;
	}

	public Map<String, Integer> getSortedMap() {
		// return wordsMap;
		int max_output = maxEntries.intValue();
		Map<String, Integer> m = wordsMap.entrySet().stream().filter(s -> s.getValue() >= frequency.intValue())
				.sorted(Map.Entry.<String, Integer> comparingByValue(Comparator.reverseOrder()) // by value
						.thenComparing(Map.Entry.comparingByKey()) // and then by key
				).limit(max_output).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return m;
	}

	@Override
	public void run() {
		logger = Logger.get();
		Map<String, Integer> m = this.ctx.builder.buildFromCharArray(this.ctx.buffer);
		for (Map.Entry<String, Integer> e : m.entrySet()) {
			this.ctx.wordsMap.merge(e.getKey(), e.getValue(), Integer::sum);
		}
	}
}
