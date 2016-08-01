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

public class CrawlerThreadPool extends Thread {

	private static final String VERSION = "2016-07-29. v1";

	private VisitedMap visitedMap = new VisitedMap();
	public static int THREAD_NUM;

	@Override
	public void run() {
		ExtLogger.info(VERSION);
		ExtLogger.line();

		ExtLogger.info("<ConfigArgs> start parse initConfiguration parameters");
		ConfigArgs config = new ConfigArgs();
		config.init();
		ExtLogger.info("<ConfigArgs> end parse initConfiguration parameters");
		ExtLogger.line();

		ExtLogger
				.info("<SpringApplicationContext> start init applicationContext");
		SpringApplicationContext.getApplicationContext();
		ExtLogger
				.info("<SpringApplicationContext> end init applicationContext");

		// ExtendsConfigration.getInstance();

		if (!ConfigArgs.START_CRAWLER.equals("yes")
				&& !ConfigArgs.START_CRAWLER.equals("YES")) {
			return;
		}

		THREAD_NUM = ConfigArgs.THREAD_NUM;

		InitTodoUrl initUrl = InitTodoUrl.getInstance();
		Todo todo = initUrl.getTodo();

		Clock clock = Clock.systemUTC();
		long startTime = clock.millis();

		List<Thread> threadList = new ArrayList<Thread>(THREAD_NUM);
		CrawlerManager crawlerManager = CrawlerManager.getInstance();
		crawlerManager.initCrawlerManager(todo, visitedMap);

		ExtLogger.info(String.format(
				"init threadNum=%s, start create crawler threads", THREAD_NUM));

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
		ExtLogger.info(String.format("%s way, spend time: %s minute %s second",
				ConfigArgs.CRAWLER_STRATEGY, spendTime / 60000,
				(spendTime % 60000) / 1000));
	}
}
