package com.engine.thread;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import com.engine.logger.ExtLogger;

public class ClockThread implements Runnable {

	@Override
	public void run() {
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:8080/crawler/main");
		httpGet.setConfig(RequestConfig.custom().setConnectTimeout(10000)
				.build());
		httpGet.setConfig(RequestConfig.custom().setSocketTimeout(5000).build());

		int statusCode = 0;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			statusCode = response.getStatusLine().getStatusCode();
			while (statusCode != HttpStatus.SC_OK) {
				Thread.sleep(1000);
				response = httpClient.execute(httpGet);
				statusCode = response.getStatusLine().getStatusCode();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		ExtLogger.serverDebug("multi-thread start");
		
	}

}
