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
	private String name = null;
	private ProcessorChain processorChain = null;

	public CrawlerThread(CrawlerManager crawlerManager, String name) {
		this.crawlerManager = crawlerManager;
		this.name = name;
		init();
	}

	private void init() {
		this.todo = crawlerManager.getTodo();
		this.visitedMap = crawlerManager.getVisitedMap();
		processorChain = new ProcessorChain();
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
					break;
				}
			}

			crawlerUrl.setContent(null);
			crawlerManager.notifyManager();
			show();

			ExtLogger.info(String.format("%s thread end", name));
		}
	}

	private void show() {
		DecimalFormat df = new DecimalFormat("#.000");
		String value = df.format(((double) visitedMap.getSize() / (todo
				.getSize() + visitedMap.getSize())) * 100);

		System.out
				.println("已爬取：" + value + "%    还差" + todo.getSize() + "条url");
	}
}
