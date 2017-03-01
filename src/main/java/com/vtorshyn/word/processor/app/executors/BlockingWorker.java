package com.vtorshyn.word.processor.app.executors;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class BlockingWorker implements Runnable {

	private BlockingQueue<Context> queue_;
	
	public BlockingWorker(BlockingQueue<Context> queue) {
		this.queue_ = queue;
	}
	@Override
	public void run() {
		Context ctx;
		
		try {
			while((ctx = this.queue_.take()) != null) {
				if (ctx.buffer == null)
					break;
				Map<String, Integer> m = ctx.builder.buildFromCharArray(ctx.temporaryMap, ctx.buffer);
				for(Map.Entry<String, Integer> e : m.entrySet()) {
					// Merge processed map into global map
					ctx.wordsMap.merge(e.getKey(), e.getValue(), Integer::sum);
				}
			}
		} catch (InterruptedException e) {
		}
	}
}