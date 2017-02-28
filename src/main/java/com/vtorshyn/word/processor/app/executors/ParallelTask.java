package com.vtorshyn.word.processor.app.executors;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.vtorshyn.utils.LogLevels;
import com.vtorshyn.utils.Logger;

public class ParallelTask implements Runnable {
	public int threadPoolSize;
	public BlockingQueue<Runnable> queue;
	//public Context ctx;
	private Logger logger;
	Integer processedJobs;

	public ParallelTask(Logger logger, BlockingQueue<Runnable> queue, int threadPoolSize) {
		processedJobs = 0;
		this.logger = logger;
		this.threadPoolSize = threadPoolSize;
		//this.ctx = ctx;
		this.queue = queue;
	}

	@Override
	public void run() {
		logger.logSync(LogLevels.LOG_INFO, "Serving queue: " + queue.isEmpty());
		Runnable r = null;
		try 
		{
			while ( (r = queue.poll(100, TimeUnit.MILLISECONDS))  != null ) {
				logger.logSync(LogLevels.LOG_DEBUG2, "processed:" + processedJobs);
				r.run();
				++processedJobs;
			}
		} 
		catch (Exception e) {
			logger.logSync(LogLevels.LOG_ERROR, e.getMessage());
			//e.printStackTrace();
		}
		logger.logSync(LogLevels.LOG_INFO, "Serving queue done: " + queue.isEmpty());
	}
}