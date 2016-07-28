package com.engine.core;

import java.util.ArrayList;
import java.util.List;

import com.engine.core.impl.Extractor;
import com.engine.core.impl.Fetcher;
import com.engine.core.impl.PerProcessor;
import com.engine.core.impl.Writer;

public class ProcessorChain {

	// Ԥ������, �򵥹��˵�������Ȥ������
	public static final String perProcessorClassName = "com.engine.core.impl.PerProcessor";
	// ��ȡ��, ������ҳ
	public static final String fetcherClassName = "com.engine.core.impl.Fetcher";
	// ��ȡ��, ������ǰ��ҳ�и���Ȥ��url
	public static final String extractorClassName = "com.engine.core.impl.Extractor";
	// д��, ����ҳ�־û������ش���
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
