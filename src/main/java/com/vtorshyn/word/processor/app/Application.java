package com.vtorshyn.word.processor.app;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.vtorshyn.utils.IntegerCommandLineOption;
import com.vtorshyn.utils.LogLevels;
import com.vtorshyn.utils.StringCommandLineOption;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.PropertiesMap;

public abstract class Application {
	@IntegerCommandLineOption(defaultValue = 5, help = "Do not print words with counters less then frequency.")
	public Integer frequency = 5;

	@IntegerCommandLineOption(defaultValue = 5, help = "Limit output to this number of entries.")
	public Integer maxEntries = 5;

	@StringCommandLineOption(defaultValue="off", help = "Dump raw map before sorting to logger.")
	public String debugRaw;
	
	protected Logger logger_;
	
	protected Map<String, Integer> wordsMap;
	private PropertiesMap propertiesMap_;
	
	public abstract Map<String, Integer> start() throws Exception;
	
	protected void init(Logger logger, PropertiesMap propertiesMap) throws Exception {
		this.logger_ = logger;
		this.propertiesMap_ = propertiesMap;
		logger.log(LogLevels.LOG_DEBUG, "Application::init()");
		propertiesMap.bind( (Application)this);
		logger.log(LogLevels.LOG_DEBUG, "Application::done()");
	}
	
	protected  Logger logger() {
		return logger_;
	}
	
	protected PropertiesMap properties() {
		return propertiesMap_;
	}
	
	protected boolean isDebugRawOn() {
		return (null != debugRaw && ("on".equals(debugRaw))); 
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
