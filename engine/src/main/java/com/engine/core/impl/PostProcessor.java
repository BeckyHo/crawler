package com.engine.core.impl;

import com.engine.bean.CrawlerUrl;
import com.engine.core.Processor;

public class PostProcessor implements Processor {

	@Override
	public boolean innerProcessor(CrawlerUrl url) {

		return false;
	}
}
