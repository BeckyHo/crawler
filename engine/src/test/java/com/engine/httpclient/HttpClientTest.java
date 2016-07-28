package com.engine.httpclient;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientTest {

	public static void main(String[] args) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// 设置get请求参数
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		try {
			CloseableHttpResponse response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
