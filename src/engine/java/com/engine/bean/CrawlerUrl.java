package com.engine.bean;

import org.apache.http.client.HttpClient;

import com.engine.utils.MD5Tool;

public class CrawlerUrl {

	private String url;

	private String standardUrl;// ÓòÃû

	private String key;

	private String content; // Ò³ÃæÄÚÈİ

	private CrawlerUrl parentUrl;

	private HttpClient httpClient;

	public CrawlerUrl(String url) {
		setUrl(url);
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		setStandardUrl(url);
		setKey(url);
	}

	public String getStandardUrl() {
		return standardUrl;
	}

	public void setStandardUrl(String standardUrl) {
		int index = standardUrl.indexOf("://");
		if (index != -1) {
			standardUrl = standardUrl.substring(index + 3);
		}

		index = standardUrl.indexOf("/");

		if (index != -1) {
			standardUrl = standardUrl.substring(0, index);
		}

		this.standardUrl = standardUrl;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = MD5Tool.getMD5String(key);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public CrawlerUrl getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(CrawlerUrl parentUrl) {
		this.parentUrl = parentUrl;
	}
}
