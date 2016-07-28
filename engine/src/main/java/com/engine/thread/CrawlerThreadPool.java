package com.engine.thread;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

import com.engine.context.SpringApplicationContext;
import com.engine.core.CrawlerManager;
import com.engine.init.ConfigArgs;
import com.engine.init.InitTodoUrl;
import com.engine.logger.ExtLogger;
import com.engine.queue.Todo;
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
		
		SpringApplicationContext.getApplicationContext();
		ExtendsConfigration.getInstance();

		if (!ConfigArgs.START_CRAWLER.equals("yes")
				&& !ConfigArgs.START_CRAWLER.equals("YES")) {
			return;
		}

		THREAD_NUM = ConfigArgs.THREAD_NUM;
		ExtLogger.serverDebug("init todo and crawler manager");

		InitTodoUrl initUrl = InitTodoUrl.getInstance();
		Todo todo = initUrl.getTodo();

		Clock clock = Clock.systemUTC();
		long startTime = clock.millis();

		List<Thread> threadList = new ArrayList<Thread>(THREAD_NUM);
		CrawlerManager crawlerManager = CrawlerManager.getInstance();
		crawlerManager.initCrawlerManager(todo, visitedMap);

		ExtLogger.serverDebug("init crawler threads");

		for (int i = 0; i < THREAD_NUM; i++) {
			CrawlerThread thread = new CrawlerThread(crawlerManager, "thread-"
					+ i);
			thread.start();
			threadList.add(thread);
		}

		try {
			while (threadList.size() > 0) {
				Thread thread = threadList.remove(0);
				thread.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		long spendTime = clock.millis() - startTime;
		ExtLogger.serverDebug(String.format(
				"%s way, spend time: %s minute %s second",
				ConfigArgs.CRAWLER_STRATEGY, spendTime / 60000,
				(spendTime % 60000) / 1000));
	}
}
