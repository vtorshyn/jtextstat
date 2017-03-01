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
	private static long start_time = 0, end_time = 0;
	
	private static void startTimer() {
		start_time = System.nanoTime();
	}
	
	private static void stopTimer() {
		end_time = System.nanoTime();
	}
	
	private static void printApplicationStats(Logger logger, Application app) {
		long total = end_time - start_time;
		long totalMs = total/1000/1000;
		float totalSeconds = (float) ((totalMs)/1000.0); 
		long tps = (long)(app.totalCount()/totalSeconds);
		
		logger.log(LogLevels.LOG_MESSAGE, "\nTotal time (ms): " + totalMs);
		logger.log(LogLevels.LOG_MESSAGE, "Total TPS: " + tps);
	}
	
	public static void main(String[] args) {
		startTimer();
		try {
			ExecutorBuilder factory = new ExecutorBuilder(args); 
			Logger logger = factory.logger();
			logger.log(LogLevels.LOG_INFO, "Creating application...");
			Application app = factory.construct();
			logger.log(LogLevels.LOG_MESSAGE, "Starting application...");
			Map<String, Integer> result = app.start();
			Map<String, Integer> sorted = new BoundedMapSorter(result).sort(app.limit, app.frequency);
			stopTimer();
			
			logger.log(LogLevels.LOG_MESSAGE, "\n*** Completed succesfully ***");
			logger.log(LogLevels.LOG_INFO, "Total word count: " + app.totalCount());
			logger.log(LogLevels.LOG_DEBUG, "Total number of unique words: " + app.uniqueCount());
			logger.log(LogLevels.LOG_MESSAGE, "Result: ");
			sorted.entrySet().stream().forEach(action -> logger.log(LogLevels.LOG_MESSAGE, "\t"+action));
			printApplicationStats(logger, app);
		} catch(Exception e) {
			System.out.println("Critical problem: " + e.getMessage());
			System.exit(-1);
		}
	}
}
