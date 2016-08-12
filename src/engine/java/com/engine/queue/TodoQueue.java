package com.engine.queue;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class TodoQueue implements Todo {

	private Queue<String> queue = new LinkedBlockingQueue<String>();
	private int addNumber = 0;

	@Override
	public boolean add(String url) {
		if (queue.offer(url)) {
			++addNumber;
			return true;
		}

		return false;
	}

	@Override
	public String remove() {

		return queue.poll();
	}

	@Override
	public String element() {

		return queue.element();
	}

	@Override
	public boolean contains(String url) {

		return queue.contains(url);
	}

	@Override
	public int getSize() {

		return queue.size();
	}

	@Override
	public int getAddNumber() {

		return addNumber;
	}
}
