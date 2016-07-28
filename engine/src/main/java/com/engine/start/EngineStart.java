package com.engine.start;

import com.engine.logger.ExtLogger;
import com.engine.thread.CrawlerThreadPool;

public class EngineStart {

	public static void main(String[] args) {
		ExtLogger.serverDebug("engine start...");
		CrawlerThreadPool pool = new CrawlerThreadPool();
		pool.start();
		
		try {
			pool.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
