package com.bright.apollo.http;

import java.util.Vector;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月23日  
 *@Version:1.1.0  
 */
public class HttpRespons {

	String urlString; 
	
	int defaultPort;
	
	String file;
	
	String host; 
	
	String path;
	
	int port;
	
	String protocol;
	
	String query; 
	
	String ref;
	
	String userInfo;
	
	String contentEncoding; 
	
	String content;
	
	String contentType;
	
	int code;
	
	String message;
	
	String method;
	 
	int connectTimeout;
	
	int readTimeout;
	
	Vector<String> contentCollection;
	
	public int getCode() {
		return code;
	}
	
	public void setCode(int code) {
		this.code = code;
	}
	
	public int getConnectTimeout() {
		return connectTimeout;
	}
	
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Vector<String> getContentCollection() {
		return contentCollection;
	}
	
	public void setContentCollection(Vector<String> contentCollection) {
		this.contentCollection = contentCollection;
	}
	
	public String getContentEncoding() {
		return contentEncoding;
	}
	
	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public int getDefaultPort() {
		return defaultPort;
	}
	
	public void setDefaultPort(int defaultPort) {
		this.defaultPort = defaultPort;
	}
	
	public String getFile() {
		return file;
	}
	
	public void setFile(String file) {
		this.file = file;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMethod() {
		return method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getProtocol() {
		return protocol;
	}
	
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public int getReadTimeout() {
		return readTimeout;
	}
	
	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
	public String getRef() {
		return ref;
	}
	
	public void setRef(String ref) {
		this.ref = ref;
	}
	
	public String getUrlString() {
		return urlString;
	}
	
	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}
	
	public String getUserInfo() {
		return userInfo;
	}
	
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

}
