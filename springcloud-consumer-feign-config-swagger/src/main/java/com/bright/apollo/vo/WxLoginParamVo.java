package com.bright.apollo.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年10月8日
 * @Version:1.1.0
 */
@Component
@ConfigurationProperties(prefix = "WxLoginParam")
public class WxLoginParamVo {

	private String wxToken;

	private String openId;
	private String appId;

	private String secret;

	private String wxUrl;

	public String getWxToken() {
		return wxToken;
	}

	public void setWxToken(String wxToken) {
		this.wxToken = wxToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getWxUrl() {
		return wxUrl;
	}

	public void setWxUrl(String wxUrl) {
		this.wxUrl = wxUrl;
	}

}
