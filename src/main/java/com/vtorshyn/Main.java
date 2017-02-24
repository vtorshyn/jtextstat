package com.vtorshyn;

import com.vtorshyn.utils.Logger;
import com.vtorshyn.word.processor.WordProcessor;
import com.vtorshyn.word.processor.WordProcessorBuilder;

/**!
 * 
 * @author vtorshyn
 * Main class of application.
 */
public class Main {
	public static void main(String[] args) throws Exception {
		WordProcessor p = new WordProcessorBuilder(args).construct();
		Logger logger = Logger.get();
		p.start();
		logger.log("\n*** Completed succesfully ***");
		logger.log("Total word count: " + p.getTotalNumberOfProcessedItems());
		logger.log("Total number of unique words: " + p.getTotalMapSize());
		logger.log("Result: " + p.getSortedMap());
	}
}
