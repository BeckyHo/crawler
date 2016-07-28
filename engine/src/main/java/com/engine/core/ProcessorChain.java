package com.engine.core;

import java.util.ArrayList;
import java.util.List;

import com.engine.core.impl.Extractor;
import com.engine.core.impl.Fetcher;
import com.engine.core.impl.PerProcessor;
import com.engine.core.impl.Writer;

public class ProcessorChain {

	// 预处理链, 简单过滤掉不感兴趣的连接
	public static final String perProcessorClassName = "com.engine.core.impl.PerProcessor";
	// 提取链, 下载网页
	public static final String fetcherClassName = "com.engine.core.impl.Fetcher";
	// 抽取链, 分析当前网页中感兴趣的url
	public static final String extractorClassName = "com.engine.core.impl.Extractor";
	// 写链, 将网页持久化到本地磁盘
	public static final String writerClassName = "com.engine.core.impl.Writer";

	private List<Processor> processorChain;

	public ProcessorChain() {
		init();
	}

	private synchronized void init() {
		processorChain = new ArrayList<Processor>();

		try {
			processorChain.add((PerProcessor) Class.forName(
					perProcessorClassName).newInstance());
			processorChain.add((Fetcher) Class.forName(fetcherClassName)
					.newInstance());
			processorChain.add((Extractor) Class.forName(extractorClassName)
					.newInstance());
			processorChain.add((Writer) Class.forName(writerClassName)
					.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public List<Processor> getProcessorChain() {
		return processorChain;
	}

	public void setProcessorChain(List<Processor> processorChain) {
		this.processorChain = processorChain;
	}
}
