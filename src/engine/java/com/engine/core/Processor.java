package com.engine.core;

import com.engine.bean.CrawlerUrl;

public interface Processor {

	public boolean innerProcessor(CrawlerUrl url);
}
