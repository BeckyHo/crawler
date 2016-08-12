package com.engine.init;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import com.engine.logger.ExtLogger;

public class ConfigArgs {

	public static int THREAD_NUM = 1;
	public static String CRAWLER_STRATEGY = "BFS";
	public static String DOWNLOAD_CHARSET = "UTF-8";
	public static String DOWNLOAD_PATH = "snapshot";
	public static String START_CRAWLER = "YES";
	public static String LUCENE_PATH = "date/";
	public static String LUCENE_STRATEGY = "NO";

	public void init() {
		InputStream is = null;

		Properties properties = new Properties();
		is = this.getClass().getClassLoader()
				.getResourceAsStream("initConfiguration.properties");
		try {
			properties.load(is);
			for (Iterator<Object> iter = properties.keySet().iterator(); iter
					.hasNext();) {
				String key = (String) iter.next();

				if (key.equals("THREAD_NUM")) {
					THREAD_NUM = Integer.parseInt(properties.getProperty(key));
					if (THREAD_NUM <= 0) {
						ExtLogger.info(String.format(
								"<ConfigArgs>. init error, threadNum=%s",
								THREAD_NUM));
						THREAD_NUM = 1;
					} else if (key.equals("CRAWLER_STRATEGY")) {
						CRAWLER_STRATEGY = properties.getProperty(key);
					} else if (key.equals("DOWNLOAD_CHARSET")) {
						DOWNLOAD_CHARSET = properties.getProperty(key);
					} else if (key.equals("DOWNLOAD_PATH")) {
						DOWNLOAD_PATH = properties.getProperty(key);
					} else if (key.equals("START_CRAWLER")) {
						START_CRAWLER = properties.getProperty(key);
					} else if (key.equals("LUCENE_PATH")) {
						LUCENE_PATH = properties.getProperty(key);
					} else if (key.equals("LUCENE_STRATEGY")) {
						LUCENE_STRATEGY = properties.getProperty(key);
					}
				}
			}

			// show log
			info();
		} catch (IOException e) {
			ExtLogger.info("<ConfigArgs>. init parameter error");
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				ExtLogger.info("<ConfigArgs>. inputStream close error");
			}
		}
	}

	public void info() {
		StringBuffer sb = new StringBuffer();
		sb.append("THREAD_NUM = ").append(THREAD_NUM).append("\t")
				.append("CRAWLER_STRATEGY = ").append(CRAWLER_STRATEGY)
				.append("\t").append("DOWNLOAD_CHARSET = ")
				.append(DOWNLOAD_CHARSET).append("\t")
				.append("DOWNLOAD_PATH = ").append(DOWNLOAD_PATH).append("\t")
				.append("START_CRAWLER = ").append(START_CRAWLER).append("\t")
				.append("LUCENE_PATH = ").append(LUCENE_PATH).append("\t")
				.append("LUCENE_STRATEGY = ").append(LUCENE_STRATEGY)
				.append("\t");

		ExtLogger.info(sb.toString());
	}
}
