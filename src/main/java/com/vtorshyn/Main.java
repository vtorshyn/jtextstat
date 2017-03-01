package com.vtorshyn;

import java.util.Map;

import com.vtorshyn.utils.BoundedMapSorter;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.word.processor.ApplicationFactory;
import com.vtorshyn.word.processor.app.Application;

/**!
 * 
 * @author vtorshyn
 * Main class of application.
 */
public class Main {
	public static void main(String[] args) {
		try {
			ApplicationFactory factory = new ApplicationFactory(args); 
			Logger logger = factory.logger();
			Application app = factory.construct();
			Map<String, Integer> result = app.start();
			Map<String, Integer> sorted = new BoundedMapSorter(result).sort(app.limit, app.frequency);
			
			logger.log("\n*** Completed succesfully ***");
			logger.log("Total word count: " + app.getTotalNumberOfProcessedItems());
			logger.log("Total number of unique words: " + app.getTotalMapSize());
			logger.log("Result: ");
			sorted.entrySet().stream().forEach(action -> logger.log(""+action));
		} catch(Exception e) {
			System.out.println("Critical problem: " + e.getMessage());
			System.exit(-1);
		}
	}
}
