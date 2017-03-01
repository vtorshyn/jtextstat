package com.vtorshyn.word.processor;

import com.vtorshyn.utils.PropertiesMap;
import com.vtorshyn.word.processor.app.Application;
import com.vtorshyn.word.processor.app.executors.ApplicationExecutor;
import com.vtorshyn.exceptions.HelpException;
import com.vtorshyn.utils.LogLevels;
import com.vtorshyn.utils.Logger;

public class ExecutorBuilder {
	
	private Logger logger = null;
	private PropertiesMap properties;
	private String[] raw_properties;
	
	private void init() throws Exception {
		try {
			int expectedPropertiesCount = 2;
			logger = new Logger();
			properties = new PropertiesMap(logger, raw_properties, "-", expectedPropertiesCount)
					.bind(logger)
					.bind(this);
		} catch (HelpException ex) {
			logger.log("Usage:");
			logger.log("\n[Reading Settings]\n");
			logger.logSupportedProperties(CharFileReader.class);
			logger.log("\n[Executor Settings]\n");
			logger.logSupportedProperties(Application.class);
			logger.logSupportedProperties(this.getClass());
			logger.log("\n[Logging]\n");
			logger.logSupportedProperties(Logger.class);
			System.exit(1);
		} catch (Exception e) {
			logger.log(LogLevels.LOG_DEBUG1, "Failed to initializa application. Reason: " + e.getMessage());
			throw e;
		}
	}
	
	public ExecutorBuilder(String[] args) throws Exception {
		raw_properties = args;
		init();
	}
	
	public PropertiesMap properties() {
		return properties;
	}
	
	public Logger logger() {
		return logger;
	}
	
	public Application construct() throws Exception {
		Application processor = new ApplicationExecutor(logger, properties);
		properties.bind(processor);
		return processor;
	}
	
}