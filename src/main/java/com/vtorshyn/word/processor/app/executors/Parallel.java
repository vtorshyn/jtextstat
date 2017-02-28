package com.vtorshyn.word.processor.app.executors;

import java.nio.CharBuffer;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.vtorshyn.WordMapBuilder;
import com.vtorshyn.utils.IntegerCommandLineOption;
import com.vtorshyn.utils.LogLevels;
import com.vtorshyn.utils.Logger;
import com.vtorshyn.utils.PropertiesMap;
import com.vtorshyn.word.processor.WordsRangeReader;
import com.vtorshyn.word.processor.app.Application;


public class Parallel extends Application implements Runnable {

	ExecutorService service;
	int threadPoolSize;

	private BlockingQueue<Runnable> queue;

	public Parallel(Logger logger, PropertiesMap properties, int threadPoolSize) throws Exception {
		super();
		this.threadPoolSize = threadPoolSize;
		logger.log(LogLevels.LOG_INFO, " threadPoolSize=" + threadPoolSize());
		service = Executors.newFixedThreadPool(threadPoolSize());
		init(logger, properties);
		
	}
	
	private Context ctx;
	
	private Parallel(Context ctx) throws Exception {
		this.ctx = ctx;
	}
	
	private int threadPoolSize() {
		return this.threadPoolSize;
	}	
	
	private void initWorkers(int amount) {
		queue = new ArrayBlockingQueue<Runnable>(amount, true);
		int id = 0;
		logger().log(LogLevels.LOG_DEBUG, " spawning workers");
		for(; id < amount; ++id) {
			logger().log(LogLevels.LOG_DEBUG, " spawning worker id=" + id);
			service.execute(new ParallelTask(logger(), queue, amount));
		}
		logger().log(LogLevels.LOG_DEBUG, " workers started");
	}

	public Map<String, Integer> start() throws Exception {
		WordsRangeReader rd = new WordsRangeReader(logger() );
		WordMapBuilder mapBuilder = new WordMapBuilder(logger());
		properties().bind(rd);
		properties().bind(mapBuilder);
		rd.init();
		wordsMap = new ConcurrentHashMap<String, Integer>(512);
		initWorkers(threadPoolSize);
		int bufferSize = rd.bufferSize();
		int maxFragmentsScan = rd.bufferReserve();
		int readSegmentSize= bufferSize + maxFragmentsScan;
		int shareBufferSize = threadPoolSize * readSegmentSize;		
		
		char[] buffer = new char[shareBufferSize];
		CharBuffer sharedBuffer = CharBuffer.wrap(buffer);
		//int runningThreads = 0;
		
		Integer totalReadSize = 0;
		logger().log(LogLevels.LOG_INFO, " entering parsing loop");
		int active = 0;
		int flag = 0;
		while (rd.hasData()) {
				
		}
		
		logger().log(LogLevels.LOG_INFO, " parsing loop done. Total: " + totalReadSize);
		
		service.shutdown();
		service.awaitTermination(1, TimeUnit.SECONDS);
		if (debugRaw.length() > 0)
			logger().log(wordsMap.toString());
		return wordsMap;
	}

	@Override
	public void run() {
		this.ctx.logger.logSync(LogLevels.LOG_DEBUG2, "worker picked a job. Parsed size: " + this.ctx.buffer.length);
		Map<String, Integer> m = this.ctx.builder.buildFromCharArray(this.ctx.buffer);
		
		for (Map.Entry<String, Integer> e : m.entrySet()) {
			this.ctx.wordsMap.merge(e.getKey(), e.getValue(), Integer::sum);
		}
		this.ctx.logger.logSync(LogLevels.LOG_DEBUG2, "map=" + this.ctx.wordsMap.toString());
	}
}
