package com.bright.apollo.vo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月11日  
 *@Version:1.1.0  
 */
@Configuration  
@RefreshScope
public class SmsLoginVo {
	@Value("${SmsLogin.url}")
	private String url;
	@Value("${SmsLogin.httpMethod}")
	private String httpMethod;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	

}
