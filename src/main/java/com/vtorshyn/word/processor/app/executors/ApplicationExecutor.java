package com.vtorshyn.word.processor.app.executors;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.vtorshyn.WordMapBuilder;
import com.vtorshyn.utils.LogLevels;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.PropertiesMap;
import com.vtorshyn.word.processor.CharFileReader;
import com.vtorshyn.word.processor.app.Application;

/**!
 * 
 * @author vtorshyn
 * 
 * Async application executor (Producer).
 * It owns a blocking queue of tasks.
 * 
 * Also it spawns BlockingWorkers (Consumers) on initialization.
 * The number of threads defined in 
 */
public class ApplicationExecutor extends Application {
	
	private BlockingQueue<Context> tasksQueue;
	private ExecutorService service;
	private CharFileReader rd;
	private WordMapBuilder mapBuilder;
	
	public ApplicationExecutor(Logger logger, PropertiesMap propeties) throws Exception {
		propeties.bind(this);
		init(logger, propeties);
		tasksQueue = new LinkedBlockingQueue<Context>(tasks());
		service = Executors.newFixedThreadPool(threads());
		wordsMap = new ConcurrentHashMap<String, Integer>(128);
		rd = new CharFileReader(logger());
		mapBuilder = new WordMapBuilder(logger());
		// Initializing objects properties supplied in PropertiesMap
		properties().bind(rd);
		properties().bind(mapBuilder);
		// Initializing char stream reader
		// It should be called after binding to PropertiesMap
		rd.init();
		startWorkers();
	}
	
	private void startWorkers() {
		for (int tid = 0; tid < threads(); ++tid) {
			service.execute(new BlockingWorker(tasksQueue));
		}
	}
	
	private void stopWorkers() throws InterruptedException {
		for (int tid = 0; tid < threads(); ++tid) {
			tasksQueue.put(new Context());
		}
		service.shutdown();
		service.awaitTermination(100, TimeUnit.MILLISECONDS);
	}

	/***
	 * Main loop for data producer.
	 * 
	 * It reads chunk of data, creates new Task context,
	 * adds it into the queue. If queue is full, 
	 * the method will wait for free slot in queue.
	 * 
	 * This method returns when input file read fully.
	 */
	public Map<String, Integer> start() throws Exception {
		int bufferSize = rd.bufferSize();
		int maxFragmentsScan = rd.bufferReserve();
		char[] buffer = new char[bufferSize + maxFragmentsScan];

		// Read next chunk of data from file buffer
		while (rd.nextChunk(buffer, 0, bufferSize, maxFragmentsScan) > 0) {
			tasksQueue.put(new Context(wordsMap, buffer, mapBuilder));
		}
		
		stopWorkers();
		
		if (isDebugRawOn())
			logger().log(LogLevels.LOG_DEBUG, wordsMap.toString());
		return wordsMap;
	}
}
