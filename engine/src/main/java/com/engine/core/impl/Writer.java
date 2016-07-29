package com.engine.core.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.engine.bean.CrawlerUrl;
import com.engine.core.Processor;
import com.engine.init.ConfigArgs;
import com.engine.logger.ExtLogger;

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
		String strUrl = url.getUrl();
		strUrl = strUrl.substring(strUrl.lastIndexOf("/") + 1);

		// 用于鉴定网站主页, 如http://google.com/; 此时name = ""
		if (strUrl == null || strUrl.length() == 0) {
			return false;
		} else if (!strUrl.contains("html") && !strUrl.contains("htm")) {
			return false;
		} else if (!strUrl.endsWith("html") && !strUrl.endsWith("htm")) {
			strUrl = strUrl.replace("id=", "");
			if (strUrl.contains(".html")) {
				strUrl = strUrl.replace(".html", "");
				strUrl = strUrl + ".html";
			} else if (strUrl.contains(".html")) {
				strUrl = strUrl.replace(".htm", "");
				strUrl = strUrl + ".htm";
			}
		}

		strUrl = strUrl.replace("?", "");
		// 文件保存路径
		String parent = ConfigArgs.DOWNLOAD_PATH;
		String filePath = parent + File.separator + strUrl;
		ExtLogger
				.info(String.format(
						"page download path, parent=%s, filePath=%s", parent,
						filePath));

		try {
			File parentFile = new File(parent);
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}

			file = new File(filePath);
			file.createNewFile();
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filePath), "UTF-8"));
			writer.write(url.getContent());
			// fileProcessor(file, url.getUrl());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		url.setContent(filePath);
		return true;
	}

	// private boolean fileProcessor(File file, String strUrl) {
	// if (file != null && file.exists()) {
	// ExtendsConfigration config = ExtendsConfigration.getInstance();
	// WriteChainAnalyzer analyzer = config.getWriteChainAnalyzer();
	// analyzer.analysisFile(file, strUrl);
	// }
	//
	// return true;
	// }

	// private String getPathDir(String strUrl) {
	// int index = strUrl.indexOf("://");
	// if (index != -1) {
	// strUrl = strUrl.substring(index + 3);
	// }
	//
	// index = strUrl.lastIndexOf("/");
	// if (index != -1) {
	// strUrl = strUrl.substring(0, index);
	// }
	//
	// ExtLogger.info(String.format("<Writer>. save url=%s", strUrl));
	// return strUrl;
	// }
}
