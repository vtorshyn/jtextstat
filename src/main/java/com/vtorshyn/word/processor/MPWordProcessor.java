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

import com.vtorshyn.WordMapBuilder;
import com.vtorshyn.utils.OptionsMap;
import com.vtorshyn.utils.IntegerCommandLineOption;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.StringCommandLineOption;

public class MPWordProcessor implements WordProcessor, Runnable {

	@IntegerCommandLineOption(defaultValue = 5, help = "Simple filter for words counters.")
	public Integer minimum;

	@IntegerCommandLineOption(defaultValue = 5, help = "Limit output to this number of entries.")
	public Integer maxEntries;

	@StringCommandLineOption(help = "dump raw map before sorting")
	public String debugRaw;

	private Map<String, Integer> wordsMap;
	private Logger logger;
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
	private void init() throws Exception {
		try {
			OptionsMap o = OptionsMap.get();
			logger = Logger.get();
			o.bind(this.getClass(), this);
			logger.log(this.getClass().getName() + ".maxEntries=" + maxEntries.toString());
			logger.log(this.getClass().getName() + ".minimum=" + minimum.toString());
		} catch (Exception e) {
			logger.log("Error: " + e.getMessage());
			throw e;
		}
	}
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
		Map<String, Integer> m = wordsMap.entrySet().stream().filter(s -> s.getValue() >= minimum.intValue())
				.sorted(Map.Entry.<String, Integer> comparingByValue(Comparator.reverseOrder())
						.thenComparing(Map.Entry.comparingByKey()) // and the by
																	// key
				).limit(max_output).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		return m;
	}

	@Override
	public void run() {
		logger = Logger.get();
		Map<String, Integer> m = this.ctx.builder.buildFromCharArray(this.ctx.buffer);
		for (Map.Entry<String, Integer> e : m.entrySet()) {
			//if (this.ctx.wordsMap.containsKey(e.getKey())) {
				this.ctx.wordsMap.merge(e.getKey(), e.getValue(), Integer::sum);
			//} else {
				//this.ctx.wordsMap.put(e.getKey(), e.getValue());
			//}
		}
	}
}
