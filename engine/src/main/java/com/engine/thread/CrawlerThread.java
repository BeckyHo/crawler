package com.engine.thread;

import java.text.DecimalFormat;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import com.engine.bean.CrawlerUrl;
import com.engine.core.CrawlerManager;
import com.engine.core.Processor;
import com.engine.core.ProcessorChain;
import com.engine.logger.ExtLogger;
import com.engine.queue.Todo;
import com.engine.queue.VisitedMap;

public class CrawlerThread extends Thread {

	private Todo todo = null;
	private VisitedMap visitedMap = null;
	private HttpClient httpClient = null;
	private CrawlerManager crawlerManager = null;
	private ProcessorChain processorChain = null;

	public CrawlerThread(CrawlerManager crawlerManager, String name) {
		super(name);
		this.crawlerManager = crawlerManager;
		init();
	}

	private void init() {
		this.todo = crawlerManager.getTodo();
		this.visitedMap = crawlerManager.getVisitedMap();
		processorChain = ProcessorChain.getInstance();
		httpClient = HttpClients.createDefault();
		// http的超时时间在HttpUriRequest中设置, 比如:
		/**
		 * HttpGet httpGet = new HttpGet("http://www.google.com");
		 * httpGet.setConfig
		 * (RequestConfig.custom().setSocketTimeout(1000).build());
		 */
	}

	@Override
	public void run() {
		while (true) {
			CrawlerUrl crawlerUrl = crawlerManager.getCrawlerUrl();
			if (crawlerUrl == null) {
				// 线程结束, 通知所有等待线程依次结束
				crawlerManager.notifyManager();
				break;
			}

			ExtLogger.info(String.format(
					"<CrawlerThread> threadName=%s get url=%s", getName(),
					crawlerUrl.getUrl()));
			crawlerUrl.setHttpClient(httpClient);
			for (Processor processor : processorChain.getProcessorChain()) {
				try {
					if (!processor.innerProcessor(crawlerUrl)) {
						break;
					}
				} catch (Exception e) {
					ExtLogger
							.info(String.format(
									"procesorChain %s process %s error",
									processor.getClass().getName(),
									crawlerUrl.getUrl()));
					e.printStackTrace();
				}
			}

			crawlerUrl.setContent(null);
			show();
		}

		ExtLogger.info(String.format("%s thread stop, remain %s to crawler",
				getName(), todo.getSize()));
	}

	private void show() {
		DecimalFormat df = new DecimalFormat("#.000");
		String value = df.format(((double) visitedMap.getSize() / (todo
				.getSize() + visitedMap.getSize())) * 100);

		ExtLogger.info(String.format("threadName=%s 以爬取: %s \t 还差%s条url",
				getName(), value, todo.getSize()));
	}
}
