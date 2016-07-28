package test.engine.httpclient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

public class HttpClientTest {

	@Test
	public void testHttpClient() {

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://jy.hbut.edu.cn/");
		httpGet.setConfig(RequestConfig.custom().setSocketTimeout(1000).build());

		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();

				reader = new BufferedReader(new InputStreamReader(
						httpEntity.getContent()));
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("F:/hbut.html")));
				String line = null;
				while ((line = reader.readLine()) != null) {
					line = new String(line.getBytes(), "UTF-8");
					writer.write(line);
				}
			}
			System.out.println("success");
		} catch (ClientProtocolException e) {
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

			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
