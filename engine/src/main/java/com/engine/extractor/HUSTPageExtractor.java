package com.engine.extractor;

import com.engine.bean.CrawlerUrl;
import com.engine.core.CrawlerManager;
import com.engine.core.Processor;

public class HUSTPageExtractor implements Processor {

	private String indexPage = "http://job.hust.edu.cn/show/recruitnews/jobnewslist.htm?page=";

	@Override
	public boolean innerProcessor(CrawlerUrl url) {
		CrawlerManager manager = CrawlerManager.getInstance();
		// Ŀǰ������Ƹ��վ��ֻ��ʾ1��59ҳ����Ƹ��Ϣ
		for (int i = 2; i < 60; i++) {
			String pageUrl = indexPage + i;

			manager.addUrl(pageUrl);
		}

		return true;
	}
}
