package com.engine.queue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TodoStack implements Todo {

	private List<String> stack = new LinkedList<String>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	private int addNumber = 0;

	@Override
	public boolean add(String url) {
		boolean flag = false;

		try {
			lock.writeLock().lock();
			stack.add(0, url);
			++addNumber;
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.writeLock().unlock();
		}

		return flag;
	}

	@Override
	public String remove() {
		String top = null;

		try {
			lock.writeLock().lock();
			top = stack.remove(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.writeLock().unlock();
		}
		return top;
	}

	@Override
	public String element() {
		String top = null;

		try {
			lock.readLock().lock();
			top = stack.get(0);
		} catch (Exception e) {
		} finally {
			lock.readLock().unlock();
		}
		return top;
	}

	@Override
	public boolean contains(String url) {
		boolean flag = false;

		try {
			lock.readLock().lock();
			flag = stack.contains(url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.readLock().unlock();
		}
		return flag;
	}

	@Override
	public int getSize() {
		return stack.size();
	}

	@Override
	public int getAddNumber() {
		return addNumber;
	}
}
