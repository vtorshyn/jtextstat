package com.vtorshyn.word.processor;

import com.vtorshyn.utils.OptionsMap;
import com.vtorshyn.utils.IntegerCommandLineOption;
import com.vtorshyn.utils.Logger;

public class WordProcessorBuilder {
	
	@IntegerCommandLineOption(defaultValue=1, help="Use single or parallel processors.")
	public Integer parallelFactor;
	private Logger logger = null;
	
	private void init() throws Exception {
		try {
			OptionsMap o = OptionsMap.get();
			o.bind(this.getClass(), this);
			logger.log(this.getClass().getName()+".parallelFactor="+parallelFactor.toString());
		} catch (Exception e) {
			logger.log("Error: " + e.getMessage());
			throw e;
		}
	}
	
	public WordProcessorBuilder(String[] args) throws Exception {
		OptionsMap options = OptionsMap.init(args);
		logger = Logger.init(options);
		init();
	}
	
	public WordProcessor construct() throws Exception {
		if (parallelFactor > 1) {
			return new MPWordProcessor(parallelFactor);
		} 
		return new SingleThreadWordProcessor();
	}
	
}