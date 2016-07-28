package com.engine.init;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

import com.engine.filter.ParseLinkFilter;
import com.engine.logger.ExtLogger;
import com.engine.queue.Todo;
import com.engine.queue.TodoQueue;
import com.engine.queue.TodoStack;

public class InitTodoUrl {

	private Todo todo;
	private static InitTodoUrl instance = new InitTodoUrl();

	private InitTodoUrl() {
		InputStream is = null;

		try {
			Properties properties = new Properties();
			is = this.getClass().getClassLoader()
					.getResourceAsStream("initUrl.properties");
			properties.load(is);
			ParseLinkFilter parseLinkFilter = new ParseLinkFilter();
			if (todo == null) {
				initTodo();
			}

			for (Iterator<Object> iter = properties.keySet().iterator(); iter
					.hasNext();) {
				String key = (String) iter.next();
				ExtLogger.serverDebug(String.format(
						"<InitTodoUrl>. test url=%s", key));
				File file = new File(key);
				if (file.exists()) {
					File[] files = file.listFiles();
					for (File f : files) {
						f.delete();
					}

					file.delete();
				}

				file.mkdir();
				parseLinkFilter.add(key);

				// 初始化爬取队列
				todo.add(properties.getProperty(key));
			}
		} catch (IOException e) {
			ExtLogger
					.serverDebug("<InitTodoUrl>. load initUrl.properties fail.");
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static InitTodoUrl getInstance() {
		return instance;
	}

	public Todo getTodo() {
		return todo;
	}

	private void initTodo() {
		if (ConfigArgs.CRAWLER_STRATEGY.equals("DFS")) {
			todo = new TodoStack();
		} else {
			todo = new TodoQueue();
		}
	}

}
