package com.vtorshyn.word.processor;

import java.util.Map;
import com.vtorshyn.utils.IntegerCommandLineOption;
import com.vtorshyn.utils.StringCommandLineOption;
import com.vtorshyn.utils.OptionsMap;
import com.vtorshyn.utils.Logger;

public abstract class WordProcessor {
	@IntegerCommandLineOption(defaultValue = 5, help = "Simple filter for words counters.")
	public Integer minimum;

	@IntegerCommandLineOption(defaultValue = 5, help = "Limit output to this number of entries.")
	public Integer maxEntries;

	@StringCommandLineOption(help = "dump raw map before sorting")
	public String debugRaw;
	
	protected Logger logger;
	
	public abstract void start() throws Exception;
	public abstract Map<String, Integer> getSortedMap();
	public abstract int getTotalMapSize();
	public abstract int getTotalNumberOfProcessedItems();
	
	protected void init() throws Exception {
		try {
			OptionsMap o = OptionsMap.get();
			logger = Logger.get();
			o.bind(WordProcessor.class, this);
			logger.log(WordProcessor.class.getName() + ".maxEntries=" + maxEntries.toString());
			logger.log(WordProcessor.class.getName() + ".minimum=" + minimum.toString());
		} catch (Exception e) {
			logger.log("Error: " + e.getMessage());
			throw e;
		}
	}
}
