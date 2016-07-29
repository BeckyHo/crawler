package com.engine.core.impl;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkStringFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.engine.bean.CrawlerUrl;
import com.engine.core.CrawlerManager;
import com.engine.core.Processor;
import com.engine.extractor.HUSTPageExtractor;
import com.engine.filter.ParseLinkFilter;
import com.engine.init.ConfigArgs;
import com.engine.logger.ExtLogger;

public class Extractor implements Processor {

	private static final String HUST_LINK = "http://job.hust.edu.cn/show/recruitnews/jobnewslist.htm?page=1";
	private static final String ENCODE = ConfigArgs.DOWNLOAD_CHARSET;

	/**
	 * 分析模块, 从下载下来的目标页面中提取新的url连接.从不同方面分析页面链接
	 */
	@Override
	public boolean innerProcessor(CrawlerUrl url) {
		if (url.getUrl().equals(HUST_LINK)) {
			Processor processor = new HUSTPageExtractor();
			processor.innerProcessor(url);
		}

		return parse(url);
	}

	private boolean parse(CrawlerUrl url) {

		String content = url.getContent();
		if (content == null || "".equals(content)) {
			ExtLogger.info(String.format(
					"<Extractor>. parse null content, url=%s", url.getUrl()));
			return false;
		}

		CrawlerManager crawlerManager = CrawlerManager.getInstance();
		Parser parser = Parser.createParser(content, ENCODE);
		NodeFilter filter = new NodeClassFilter(LinkTag.class);

		ExtLogger
				.info(String.format("<Extractor>. parser url=%s", url.getUrl()));

		LinkStringFilter linkFilter = ParseLinkFilter.getLinkFilter(url
				.getStandardUrl());
		if (linkFilter == null) {
			ExtLogger.info(String.format("<Extractor>. outside url=%s",
					url.getUrl()));
			return false;
		}

		try {
			// 获取感兴趣的结点
			NodeList nodes = parser.extractAllNodesThatMatch(filter);
			for (int i = 0; i < nodes.size(); i++) {
				Node node = nodes.elementAt(i);
				if (node instanceof LinkTag) {
					LinkTag link = (LinkTag) node;
					String strUrl = link.getLink();
					if (strUrl == null || strUrl.length() <= 4) {
						continue;
					}

					boolean ok = strUrl
							.startsWith("http://jy.hbut.edu.cn/xyzp/");
					if (!ok) {
						continue;
					}

					ExtLogger.info(String.format("<Extractor>. parse url=%s",
							strUrl));
					crawlerManager.addUrl(strUrl);
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}

		return true;
	}
}
