package com.vtorshyn.word.processor.app;

import java.util.Map;

import com.vtorshyn.utils.IntegerCommandLineOption;
import com.vtorshyn.utils.LogLevels;
import com.vtorshyn.utils.StringCommandLineOption;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.PropertiesMap;

public abstract class Application {
	@IntegerCommandLineOption(defaultValue=1, mandatory=false, help="Specifies number of threads used for processing.")
	public Integer threads;
	
	@IntegerCommandLineOption(defaultValue=2, mandatory=false, help="Specifies maximum queue size.")
	public Integer queue;
	
	@IntegerCommandLineOption(defaultValue = 2, help = "Do not print words with counters less then frequency.")
	public Integer frequency;

	@IntegerCommandLineOption(defaultValue = 5, help = "Limit output to this number of entries.")
	public Integer limit;

	@StringCommandLineOption(defaultValue="false", help = "When \"true\", will print raw map before sorting.")
	public String debugRaw;
	
	protected Logger logger_;
	
	protected Map<String, Integer> wordsMap;
	private PropertiesMap propertiesMap_;
	
	public abstract Map<String, Integer> start() throws Exception;
	
	protected void init(Logger logger, PropertiesMap propertiesMap) throws Exception {
		logger.log(LogLevels.LOG_DEBUG2, "Application::init()");
		this.logger_ = logger;
		this.propertiesMap_ = propertiesMap;
		propertiesMap.bind( (Application)this);
		logger.log(LogLevels.LOG_DEBUG2, "Application::done()");
	}
	
	protected  Logger logger() {
		return logger_;
	}
	
	protected PropertiesMap properties() {
		return propertiesMap_;
	}
	
	protected boolean isDebugRawOn() {
		return (null != debugRaw && ("true".equals(debugRaw))); 
	}
	
	protected int tasks() {
		return this.queue;
	}
	
	protected int threads() {
		return this.threads;
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
