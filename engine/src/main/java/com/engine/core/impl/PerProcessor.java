package com.engine.core.impl;

import com.engine.bean.CrawlerUrl;
import com.engine.core.Processor;

public class PerProcessor implements Processor {

	private static final String[] unexpectFile = { ".doc", ".zip", ".rar",
			".exe", ".pdf", ".xls", ".gif", ".png", ".jpg", ".js", ".css",
			".xls" };

	// 预处理模块, 判断目标连接是否合法
	@Override
	public boolean innerProcessor(CrawlerUrl url) {
		String urlName = url.getUrl();
		for (String end : unexpectFile) {
			if (urlName.endsWith(end)) {
				return false;
			}
		}

		return true;
	}
}
