package com.engine.core.impl;

import com.engine.bean.CrawlerUrl;
import com.engine.core.Processor;
import com.engine.logger.ExtLogger;

public class PerProcessor implements Processor {

	private static final String[] unexpectSuffix = { ".doc", ".zip", ".rar",
			".exe", ".pdf", ".xls", ".gif", ".png", ".jpg", ".js", ".css",
			".xls" };

	// 预处理模块, 判断目标连接是否合法
	@Override
	public boolean innerProcessor(CrawlerUrl url) {
		String strUrl = url.getUrl();
		for (String suffix : unexpectSuffix) {
			if (strUrl.endsWith(suffix)) {
				ExtLogger.info(String.format(
						"<PerProcessor>. innerProcessor unexpected url=%s",
						strUrl));
				return false;
			}
		}

		return true;
	}
}
