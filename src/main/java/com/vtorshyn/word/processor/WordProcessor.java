package com.vtorshyn.word.processor;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.vtorshyn.utils.IntegerCommandLineOption;
import com.vtorshyn.utils.StringCommandLineOption;
import com.vtorshyn.utils.OptionsMap;
import com.vtorshyn.utils.Logger;

public abstract class WordProcessor {
	@IntegerCommandLineOption(defaultValue = 5, help = "Simple filter for words counters.")
	public Integer frequency;

	@IntegerCommandLineOption(defaultValue = 5, help = "Limit output to this number of entries.")
	public Integer maxEntries;

	@StringCommandLineOption(help = "dump raw map before sorting")
	public String debugRaw;
	
	protected Logger logger;
	
	protected Map<String, Integer> wordsMap;
	
	public abstract void start() throws Exception;
	
	protected void init() throws Exception {
		try {
			OptionsMap o = OptionsMap.get();
			logger = Logger.get();
			o.bind(WordProcessor.class, this);
			logger.log(WordProcessor.class.getName() + ".maxEntries=" + maxEntries.toString());
			logger.log(WordProcessor.class.getName() + ".frequency=" + frequency.toString());
		} catch (Exception e) {
			logger.log("Error: " + e.getMessage());
			throw e;
		}
	}
	
	public Map<String, Integer> getSortedMap () {
		//return wordsMap;
		int max_output = maxEntries.intValue(); 
		Map<String, Integer> m = wordsMap.entrySet().stream()
				.filter(s -> s.getValue() >= frequency.intValue())
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
}
