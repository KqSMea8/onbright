package com.bright.apollo.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

 

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月23日  
 *@Version:1.1.0  
 */
public class HttpRequester {

	private String defaultContentEncoding;
	
	public  HttpRequester(){
		this.defaultContentEncoding = Charset.defaultCharset().name();  
	}
	
	public HttpRespons sendGet(String urlString) throws IOException{
		return this.send(urlString, "GET", null, null);
	}
	
	public HttpRespons sendGet(String urlString, Map<String, Object> params) throws IOException{
		return this.send(urlString, "GET", params, null);
	}
	
	public HttpRespons sendGet(String urlString, Map<String,Object> params,
			Map<String, Object> propertys) throws IOException {
		 return this.send(urlString, "GET", params, propertys);
	}
	
	public HttpRespons sendJsonPost(String urlString,JSONObject obj) throws IOException{
		URL url = new URL(urlString);
		HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setRequestMethod("POST");
		urlConnection.setUseCaches(false);
		urlConnection.setInstanceFollowRedirects(true);
		urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
		urlConnection.connect();
		
		DataOutputStream out = new DataOutputStream(urlConnection.getOutputStream());
		out.writeBytes(obj.toString());
		out.flush();
		out.close();
		
		return this.makeContent(urlString, urlConnection);
	}
	
	public HttpRespons sendPost(String urlString) throws IOException{
		return this.send(urlString, "POST", null, null);
	}
	
	public HttpRespons sendPost(String urlString, Map<String, Object> params) throws IOException{
		return this.send(urlString, "POST", params, null);
	}
	
	public HttpRespons sendPost(String urlString, Map<String, Object> params,
			Map<String, Object> propertys) throws IOException {
		 return this.send(urlString, "POST", params, propertys);
	}
	
	private HttpRespons send(String urlString, String method,
			Map<String, Object> parameters,Map<String, Object> propertys)
			throws IOException{
		HttpURLConnection urlConnection = null;
		
		if (method.equalsIgnoreCase("GET") && parameters != null) {
			StringBuffer param = new StringBuffer();
			int i = 0;
			for (String key : parameters.keySet()) {
				if (i == 0) {
					param.append("?");
				}else {
					param.append("&");
				}
				param.append(key).append("=").append(parameters.get(key));
				i++;
			}
			urlString += param;
		}
		URL url = new URL(urlString);
		urlConnection = (HttpURLConnection)url.openConnection();
		
		urlConnection.setRequestMethod(method);
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.setUseCaches(false);
		
		if (propertys != null) {
			for (String key : propertys.keySet()) {
				urlConnection.addRequestProperty(key, propertys.get(key)+"");
			}
		}
		
		if (method.equalsIgnoreCase("POST") && parameters != null) {
			StringBuffer param = new StringBuffer(); 
			for (String key : parameters.keySet()) { 
				param.append("&"); 
				param.append(key).append("=").append(parameters.get(key)); 
			}
			urlConnection.getOutputStream().write(param.toString().getBytes());
			urlConnection.getOutputStream().flush();
			urlConnection.getOutputStream().close(); 
		}
		
		return this.makeContent(urlString, urlConnection);
	}
	
	private HttpRespons makeContent(String urlString,
			HttpURLConnection urlConnection) throws IOException{
		HttpRespons httpResponser = new HttpRespons();
		
		try {
			InputStream in = urlConnection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			httpResponser.setContentCollection(new Vector<String>());
			StringBuffer temp = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				httpResponser.getContentCollection().add(line);
				temp.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			
			String ecod = urlConnection.getContentEncoding();
			if (ecod == null) {
				ecod = this.defaultContentEncoding;
			}
			
			httpResponser.setUrlString(urlString);
			
			httpResponser.setDefaultPort(urlConnection.getURL().getDefaultPort());
			httpResponser.setFile(urlConnection.getURL().getFile());
			httpResponser.setHost(urlConnection.getURL().getHost());
			httpResponser.setPath(urlConnection.getURL().getPath());
			httpResponser.setPort(urlConnection.getURL().getPort());
			httpResponser.setProtocol(urlConnection.getURL().getProtocol());
			httpResponser.setQuery(urlConnection.getURL().getQuery());
			httpResponser.setRef(urlConnection.getURL().getRef());
			httpResponser.setUserInfo(urlConnection.getURL().getUserInfo());
			
			httpResponser.setContent(new String(temp.toString().getBytes(), ecod));
			httpResponser.setContentEncoding(ecod);
			httpResponser.setCode(urlConnection.getResponseCode());
			httpResponser.setMessage(urlConnection.getResponseMessage());
			httpResponser.setContentType(urlConnection.getContentType());
			httpResponser.setMethod(urlConnection.getRequestMethod());
			httpResponser.setConnectTimeout(urlConnection.getConnectTimeout());
			httpResponser.setReadTimeout(urlConnection.getReadTimeout());
			
			return httpResponser;
		} catch (Exception e) {
			 
			e.printStackTrace();
		}finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return null;
	}
	
	public String getDefaultContentEncoding() {
		return defaultContentEncoding;
	}
	
	public void setDefaultContentEncoding(String defaultContentEncoding) {
		this.defaultContentEncoding = defaultContentEncoding;
	}

}
