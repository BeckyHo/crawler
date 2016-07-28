package com.engine.filter;

import java.util.HashMap;
import java.util.Map;

import org.htmlparser.filters.LinkStringFilter;

public class ParseLinkFilter {

	private static Map<String, LinkStringFilter> linkFilterMap = new HashMap<String, LinkStringFilter>();

	public synchronized void add(String urlField) {
		linkFilterMap.put(urlField, new LinkStringFilter("http://" + urlField));
	}

	public static LinkStringFilter getLinkFilter(String urlField) {
		return linkFilterMap.get(urlField);
	}
}
