package com.engine.thread;

import com.engine.init.ConfigArgs;
import com.engine.logger.ExtLogger;
import com.engine.queue.VisitedMap;
import com.engine.writer.ExtendsConfigration;

public class CrawlerThreadPool extends Thread {

	private VisitedMap visitedMap = new VisitedMap();
	
	public static int THREAD_NUM;
	
	@Override
	public void run() {
		ExtLogger.serverDebug("server start...");
		ConfigArgs config = new ConfigArgs();
		
		config.init();
		ExtendsConfigration.getInstance();
		
		
		
		
		
		
		
	}
}
