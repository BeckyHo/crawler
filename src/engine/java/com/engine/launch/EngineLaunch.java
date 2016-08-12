package com.engine.launch;

import com.engine.logger.ExtLogger;
import com.engine.thread.CrawlerThreadPool;

public class EngineLaunch {

	public static void main(String[] args) {

		ExtLogger.info("engine launch...");
		CrawlerThreadPool pool = new CrawlerThreadPool();
		pool.start();

		try {
			pool.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
