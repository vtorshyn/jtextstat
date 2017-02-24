package com.vtorshyn.word.processor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.vtorshyn.WordMapBuilder;
import com.vtorshyn.utils.OptionsMap;
import com.vtorshyn.utils.IntegerCommandLineOption;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.StringCommandLineOption;

public class SingleThreadWordProcessor implements WordProcessor {
	@IntegerCommandLineOption(defaultValue=5, help="Simple filter for words counters.")
	public Integer minimum;
	
	@IntegerCommandLineOption(defaultValue=5, help="Limit output to this number of entries.")
	public Integer maxEntries;
	
	@StringCommandLineOption(help="dump raw map before sorting")
	public String debugRaw;
	
	private Map<String, Integer> wordsMap;
	private Logger logger;
	
	private void init() throws Exception {
		try {
			OptionsMap o = OptionsMap.get();
			logger = Logger.get();
			o.bind(this.getClass(), this);
			logger.log(this.getClass().getName()+".maxEntries="+maxEntries.toString());
			logger.log(this.getClass().getName()+".minimum="+minimum.toString());
		} catch (Exception e) {
			logger.log("Error: " + e.getMessage());
			throw e;
		}
	}
	public void start() throws Exception {
		init();
		WordsRangeReader rd = new WordsRangeReader();
		WordMapBuilder mapBuilder = new WordMapBuilder();
		wordsMap = new HashMap<String, Integer>(512);
		char[] buffer;

		while ((buffer = rd.nextChunk()) != null) {
			Map<String, Integer> m = mapBuilder.buildFromCharArray(buffer);
			for(Map.Entry<String, Integer> e : m.entrySet()) {
				wordsMap.merge(e.getKey(), e.getValue(), Integer::sum);
			}
		}
		if (debugRaw.length() > 0)
			logger.log(wordsMap.toString());
	}
	
	public int getTotalMapSize() {
		return wordsMap.size();
	}
	
	public int getTotalNumberOfProcessedItems() {
		int total = 0;
		for(Map.Entry<String, Integer> e : wordsMap.entrySet()) {
			total += e.getValue().intValue();
		}
		return total;
	}
	
	public Map<String, Integer> getSortedMap () {
		//return wordsMap;
		int max_output = maxEntries.intValue(); 
		Map<String, Integer> m = wordsMap.entrySet().stream()
				.filter(s -> s.getValue() >= minimum.intValue())
				.sorted(Map.Entry.<String, Integer> comparingByValue(Comparator.reverseOrder()) 
						.thenComparing(Map.Entry.comparingByKey()) // and the by
																	// key
				).limit(max_output).collect(
						Collectors.toMap(
								Entry::getKey, Entry::getValue, 
								(e1, e2) -> e1, LinkedHashMap::new)
						);
		return m;
	}
}