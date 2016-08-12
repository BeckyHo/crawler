package com.engine.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.engine.bean.CrawlerUrl;
import com.engine.utils.MD5Tool;

public class VisitedMap {

	private Map<String, CrawlerUrl> visited = new HashMap<String, CrawlerUrl>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	public void add(CrawlerUrl url) {
		try {
			lock.writeLock().lock();
			visited.put(url.getKey(), url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.writeLock().unlock();
		}
	}

	public CrawlerUrl remove(String key) {
		CrawlerUrl url = null;
		try {
			lock.writeLock().lock();
			url = visited.remove(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.writeLock().unlock();
		}

		return url;
	}

	public boolean isExist(String url) {
		boolean flag = false;
		String key = MD5Tool.getMD5String(url);
		try {
			lock.readLock().lock();
			flag = visited.containsKey(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.readLock().unlock();
		}

		return flag;
	}

	public int getSize() {
		return visited.size();
	}
}
