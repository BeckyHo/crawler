package com.engine.core.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

import com.engine.bean.CrawlerUrl;
import com.engine.core.Processor;
import com.engine.logger.ExtLogger;

public class Fetcher implements Processor {

	private static final int MAX_CONTENT_LENGTH = 300000; // ��ҳ�����ַ������ֵ

	/**
	 * ץȡģ��: ����Ŀ��ҳ��, �����ظ�ҳ��, ����ҳ�汣�浽���ش�����
	 */
	@Override
	public boolean innerProcessor(CrawlerUrl url) {
		return visite(url);
	}

	private boolean visite(CrawlerUrl url) {

		if (url.getUrl().contains("window.")
				|| url.getUrl().contains("window.")) {
			return false;
		}

		HttpClient httpClient = url.getHttpClient();
		HttpGet httpGet = null;

		httpGet = new HttpGet(url.getUrl());
		// ����get������ʱʱ��, ��λ����
		httpGet.setConfig(RequestConfig.custom().setConnectTimeout(10000)
				.build());
		// ����socket��ʱʱ��
		httpGet.setConfig(RequestConfig.custom().setSocketTimeout(6000).build());

		BufferedReader reader = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();
			if (status != HttpStatus.SC_OK) {
				ExtLogger.debug(String.format(
						"<Fetcher>, visit url=% fail, code=%s", url.getUrl(),
						status));
				url.setHttpClient(null);
				return false;
			}
			InputStream inputStream = response.getEntity().getContent();
			reader = new BufferedReader(new InputStreamReader(inputStream,
					"UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String content = null;
			while ((content = reader.readLine()) != null) {
				buffer.append(content);
				if (buffer.length() > MAX_CONTENT_LENGTH) {
					break;
				}
			}

			url.setContent(buffer.toString());
			ExtLogger.info(String.format("<Fetcher>. url=%s visit successful",
					url.getUrl()));
		} catch (ClientProtocolException e) {
			ExtLogger.info(String.format("<Fetcher>. url=%s visit fail",
					url.getUrl()));
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return true;
	}
}
