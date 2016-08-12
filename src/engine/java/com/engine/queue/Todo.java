package com.engine.queue;

public interface Todo {

	public boolean add(String url);

	public String remove();

	public String element();

	public boolean contains(String url);

	public int getSize();

	public int getAddNumber();
}
