package com.engine.writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.engine.bean.Resource;
import com.engine.utils.ExtendsUtils;

@SuppressWarnings("unchecked")
public class ExtendsConfigration {

	private static ExtendsConfigration configration;

	private WriteChainAnalyzer writeChainAnalyzer;

	private Map<String, List<Resource>> extendsConfig;

	private ExtendsConfigration() {
		HashMap<String, List<Resource>> map = new HashMap<String, List<Resource>>();
		extendsConfig = Collections.synchronizedMap(map);
		writeChainAnalyzer = new XMLWriteChainAnalyzer();
		initializeConfigration();
		writeChainAnalyzer.initialize(this);
	}

	private void initializeConfigration() {
		SAXReader saxReader = new SAXReader();
		ClassLoader clsLoader = ExtendsConfigration.class.getClassLoader();
		try {
			Document document = saxReader.read(clsLoader
					.getResourceAsStream("extractor-cfg.xml"));
			Element extractorConf = document.getRootElement();
			List<Element> confs = extractorConf.elements();
			for (Element e : confs) {
				List<Element> els = e.elements();

				List<Resource> resources = new ArrayList<Resource>();
				String host = null;
				for (int i = 0; i < els.size(); i++) {
					Element ex = els.get(i);
					String tagName = ex.getName();
					if ("resource".equals(tagName)
							&& !ExtendsUtils.isEmptyString(tagName)) {
						Resource resource = new Resource();
						List<Element> attrs = ex.elements();
						for (int j = 0; j < attrs.size(); j++) {
							Element attr = attrs.get(j);
							if ("name".equals(attr.getName())) {
								resource.setName(attr.getText());
							} else if ("processor".equals(attr.getName())) {
								resource.setProcessorName(attr.getText());
							}
						}
						// add resource
						resources.add(resource);
					} else if ("host".equals(tagName)
							&& !ExtendsUtils.isEmptyString(tagName)) {
						host = ex.getText();

					}
				}
				for (int i = 0; i < resources.size(); i++) {
					resources.get(i).setHost(host);
				}
				if (!ExtendsUtils.isEmptyString(host)) {
					extendsConfig.put(host, resources);
				}
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public static ExtendsConfigration getInstance() {
		if (null == configration) {
			synchronized (ExtendsConfigration.class) {
				// 多线程安全
				if (null == configration) {
					configration = new ExtendsConfigration();
				}
			}
		}
		return configration;
	}

	public boolean containHost(String uri) {
		String host = ExtendsUtils.getHostName(uri);
		if (null == host)
			return false;
		return extendsConfig.containsKey(host);
	}

	public List<Resource> getResourcesByURI(String uri) {
		String host = ExtendsUtils.getHostName(uri);
		if (null == host)
			return new ArrayList<Resource>();
		return extendsConfig.get(host);
	}

	public List<Resource> getResourcesByHostName(String host) {
		return extendsConfig.get(host);
	}

	public WriteChainAnalyzer getWriteChainAnalyzer() {
		return writeChainAnalyzer;
	}

	public List<String> getFileProcessorNames() {
		Set<Map.Entry<String, List<Resource>>> set = extendsConfig.entrySet();
		Iterator<Map.Entry<String, List<Resource>>> iterator = set.iterator();
		List<String> processorNames = new ArrayList<String>();
		while (iterator.hasNext()) {
			Map.Entry<String, List<Resource>> e = iterator.next();
			List<Resource> resources = e.getValue();
			int size = resources.size();
			for (int i = 0; i < size; i++) {
				String name = resources.get(i).getProcessorName();
				if (!ExtendsUtils.isEmptyString(name)) {
					processorNames.add(name);
				}
			}
		}
		return processorNames;
	}

	public String getSaveDirPath() {
		return "C:/Users/Administrator/Desktop/spider/mirror";
	}
}
