package com.engine.core;

import com.engine.bean.CrawlerUrl;
import com.engine.init.ConfigArgs;
import com.engine.logger.ExtLogger;
import com.engine.queue.Todo;
import com.engine.queue.VisitedMap;

public class CrawlerManager {

	private Todo todo = null;
	private VisitedMap visitedMap = null;
	private volatile int waitThreadCount = 0;
	private volatile boolean stop = false;

	private static CrawlerManager instance = new CrawlerManager();

	private CrawlerManager() {

	}

	public static CrawlerManager getInstance() {
		return instance;
	}

	public void initCrawlerManager(Todo todo, VisitedMap visitedMap) {
		this.todo = todo;
		this.visitedMap = visitedMap;
	}

	public synchronized CrawlerUrl getCrawlerUrl() {
		String url = todo.remove();
		while (url == null) {
			try {
				++waitThreadCount;
				boolean flag1 = waitThreadCount == ConfigArgs.THREAD_NUM;
				boolean flag2 = url == null;
				if (flag1 && flag2) {
					stop = true;
					break;
				}

				wait();
				--waitThreadCount;

			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}

			url = todo.remove();
			if (stop) {
				break;
			}
		}

		CrawlerUrl crawlerUrl = null;
		if (url != null) {
			crawlerUrl = new CrawlerUrl(url);
			visitedMap.add(crawlerUrl);
		}

		return crawlerUrl;
	}

	public synchronized void addUrl(String url) {
		if (!visitedMap.isExist(url) && !todo.contains(url)) {
			ExtLogger.info(String.format(
					"<CrawlerManager>.addUrl() waitThreadCount=%s",
					waitThreadCount));
			todo.add(url);
			notifyAll();
		}
	}

	public synchronized void notifyManager() {
		ExtLogger.info(String.format(
				"<CrawlerManager>.notifyManager() waitThreadCount=%s",
				waitThreadCount));
		notifyAll();
	}

	public Todo getTodo() {
		return todo;
	}

	public VisitedMap getVisitedMap() {
		return visitedMap;
	}
}
