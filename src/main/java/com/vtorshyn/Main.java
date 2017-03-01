package com.vtorshyn;

import java.util.Map;

import com.vtorshyn.utils.BoundedMapSorter;
import com.vtorshyn.utils.LogLevels;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.word.processor.ExecutorBuilder;
import com.vtorshyn.word.processor.app.Application;

/**!
 * 
 * @author vtorshyn
 * Main class of application.
 */
public class Main {
	public static void main(String[] args) {
		try {
			ExecutorBuilder factory = new ExecutorBuilder(args); 
			Logger logger = factory.logger();
			logger.log(LogLevels.LOG_INFO, "Creating application...");
			Application app = factory.construct();
			logger.log(LogLevels.LOG_MESSAGE, "Starting application...");
			Map<String, Integer> result = app.start();
			Map<String, Integer> sorted = new BoundedMapSorter(result).sort(app.limit, app.frequency);
			
			logger.log("\n*** Completed succesfully ***");
			logger.log("Total word count: " + app.totalCount());
			logger.log("Total number of unique words: " + app.uniqueCount());
			logger.log("Result: ");
			sorted.entrySet().stream().forEach(action -> logger.log(""+action));
		} catch(Exception e) {
			System.out.println("Critical problem: " + e.getMessage());
			System.exit(-1);
		}
	}
}
