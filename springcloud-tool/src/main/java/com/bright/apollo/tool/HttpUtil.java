package com.bright.apollo.tool;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月13日
 * @Version:1.1.0
 */
public class HttpUtil {

	public static JSONObject requestByBasic(String url, String user, String pass) throws Exception {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String encoding = new String(Base64.encodeBase64(StringUtils.getBytesUtf8(user + ":" + pass)));
		try {
			HttpGet httpget = new HttpGet(url);
			httpget.addHeader("Authorization", "Basic " + encoding);
			CloseableHttpResponse response = httpclient.execute(httpget);
			int status = response.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == status) {
				HttpEntity entity = response.getEntity();
				if (null == entity) {
					return null;  
				}
				BufferedReader isr = new BufferedReader(new InputStreamReader(entity.getContent()));
				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line = isr.readLine()) != null) {
					sb.append(line);
				}
				return new JSONObject(sb.toString());
			}
		} finally {
			httpclient.close();
		}
		return null;
	}

	public static JSONObject request(URI uri) {
		@SuppressWarnings({ "resource", "deprecation" })
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(uri);
		HttpResponse response;
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();

				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
				StringBuilder sb = new StringBuilder();

				for (String temp = reader.readLine(); temp != null; temp = reader.readLine()) {
					sb.append(temp);
				}
				JSONObject object = new JSONObject(sb.toString().trim());

				return object;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return null;
	}

}
