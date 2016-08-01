package com.engine.core.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

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

		String strUrl = url.getUrl();

		if (strUrl.contains("javascript") || strUrl.contains("window.")) {
			ExtLogger.info(String.format("<Fetcher>. visite unexpected url=%s",
					url.getUrl()));
			return false;
		}

		HttpClient httpClient = url.getHttpClient();

		HttpGet httpGet = new HttpGet(strUrl);
		// �������ӳ�ʱʱ��, ��λ����
		httpGet.setConfig(RequestConfig.custom().setConnectTimeout(5000)
				.build());
		// ���ô�connectManager��ȡConnection��ʱʱ��, ��λ����
		httpGet.setConfig(RequestConfig.custom()
				.setConnectionRequestTimeout(5000).build());
		// ���������ȡ��Ӧ�ĳ�ʱʱ��, ��λ����
		httpGet.setConfig(RequestConfig.custom().setSocketTimeout(5000).build());

		BufferedReader reader = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		try {
			response = (CloseableHttpResponse) httpClient.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();
			if (status != HttpStatus.SC_OK) {
				ExtLogger.info(String.format(
						"<Fetcher>, visit fail url=%s , code=%s", strUrl,
						status));
				return false;
			}
			entity = response.getEntity();
			InputStream inputStream = entity.getContent();

			reader = new BufferedReader(new InputStreamReader(inputStream,
					"UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String content = null;
			while ((content = reader.readLine()) != null) {
				buffer.append(content);
				if (buffer.length() > MAX_CONTENT_LENGTH) {
					ExtLogger.info(String.format(
							"<Fetcher>.visite() content exceed %s, url=%s",
							MAX_CONTENT_LENGTH, url.getUrl()));
					return false;
				}
			}

			url.setContent(buffer.toString());
			ExtLogger.info(String.format(
					"<Fetcher>.visite() fetcher successful, url=%s",
					url.getUrl()));
		} catch (ClientProtocolException e) {
			ExtLogger
					.info(String.format("<Fetcher>. url=%s fail", url.getUrl()));
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (entity != null) {
					EntityUtils.consume(entity);
				}

				if (response != null) {
					response.close();
				}

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
