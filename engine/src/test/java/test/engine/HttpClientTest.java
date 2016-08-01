package test.engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.junit.Test;

public class HttpClientTest {

	@Test
	public void testHttpClient() {

		final String encode = "utf-8";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://www.baidu.com");
		// 设置连接超时时间, 单位毫秒
		httpGet.setConfig(RequestConfig.custom().setConnectTimeout(5000)
				.build());
		// 设置从connectManager获取Connection超时时间, 单位毫秒
		httpGet.setConfig(RequestConfig.custom()
				.setConnectionRequestTimeout(1000).build());
		// 设置请求获取响应的超时时间, 单位毫秒
		httpGet.setConfig(RequestConfig.custom().setSocketTimeout(5000).build());

		BufferedReader reader = null;
		BufferedWriter writer = null;
		File tempFile = new File("F:/hbut.html");
		if (tempFile.exists()) {
			tempFile.delete();
		}
		try {
			StringBuffer sb = new StringBuffer();
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();

				reader = new BufferedReader(new InputStreamReader(
						httpEntity.getContent(), encode));
				writer = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream(tempFile), encode));
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}

				String html = sb.toString();
				writer.write(html);
				if (html != null || !"".equals(html)) {
					Parser parser = Parser.createParser(html, encode);
					NodeFilter filter = new NodeClassFilter(LinkTag.class);
					NodeList nodes = parser.extractAllNodesThatMatch(filter);

					for (int i = 0; i < nodes.size(); i++) {
						Node node = nodes.elementAt(i);
						if (node instanceof LinkTag) {
							LinkTag link = (LinkTag) node;
							System.out.println(link.getLinkText() + ": "
									+ link.getLink());
						}
					}
				}
			}
			System.out.println(statusCode);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
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
