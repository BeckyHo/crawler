package com.engine.core.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import com.engine.bean.CrawlerUrl;
import com.engine.core.Processor;
import com.engine.init.ConfigArgs;
import com.engine.logger.ExtLogger;
import com.engine.writer.ExtendsConfigration;
import com.engine.writer.WriteChainAnalyzer;

public class Writer implements Processor {

	@Override
	public boolean innerProcessor(CrawlerUrl url) {
		File file = null;
		return save(url, file);
	}

	private boolean save(CrawlerUrl url, File file) {
		if (url.getContent() == null) {
			return false;
		}

		BufferedWriter writer = null;
		String name = url.getUrl();
		name = name.substring(name.lastIndexOf("/") + 1);

		// 用于鉴定网站主页, 如http://google.com/; 此时name = ""
		if (name == null || name.length() == 0) {
			return false;
		} else if (!name.contains("html") && !name.contains("htm")) {
			return false;
		} else if (!name.endsWith("html") && !name.endsWith("htm")) {
			name = name.replace("id=", "");
			if (name.contains(".html")) {
				name = name.replace(".html", "");
				name = name + ".html";
			} else if (name.contains(".html")) {
				name = name.replace(".htm", "");
				name = name + ".htm";
			}
		}

		name = name.replace("?", "");
		// 文件保存路径
		String parent = ConfigArgs.DOWNLOAD_PATH
				+ this.getPathDir(url.getUrl());
		String fileName = parent + File.separator + name;

		try {
			File parentFile = new File(parent);
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}

			file = new File(fileName);
			file.createNewFile();
			writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(url.getContent());
			fileProcessor(file, url.getUrl());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		url.setContent(fileName);
		return true;
	}

	private boolean fileProcessor(File file, String strUrl) {
		if (file != null && file.exists()) {
			ExtendsConfigration config = ExtendsConfigration.getInstance();
			WriteChainAnalyzer analyzer = config.getWriteChainAnalyzer();
			analyzer.analysisFile(file, strUrl);
		}

		return true;
	}

	private String getPathDir(String strUrl) {
		int index = strUrl.indexOf("://");
		if (index != -1) {
			strUrl = strUrl.substring(index + 3);
		}

		index = strUrl.lastIndexOf("/");
		if (index != -1) {
			strUrl = strUrl.substring(0, index);
		}

		ExtLogger.info(String.format("<Writer>. save url=%s", strUrl));
		return strUrl;
	}
}
