package com.bright.apollo.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月8日  
 *@Version:1.1.0  
 */
@Component  
@ConfigurationProperties(prefix="WxLogin")
public class WxLoginVo {

	private String url;
	
	private String codeUrl;
	
	private String smsUrl;
	
	private String httpMethod;
	
	private String antUrl;
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

	public String getCodeUrl() {
		return codeUrl;
	}

	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}

	public String getAntUrl() {
		return antUrl;
	}

	public void setAntUrl(String antUrl) {
		this.antUrl = antUrl;
	}

	public String getSmsUrl() {
		return smsUrl;
	}

	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}
	
}
