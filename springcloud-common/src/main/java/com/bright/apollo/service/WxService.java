package com.bright.apollo.service;

import org.json.JSONObject;

import com.bright.apollo.constant.Constant;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月12日
 * @Version:1.1.0
 */
public interface WxService {
	// https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
	static final String WX_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Constant.WX_APPID
			+ "&secret=" + Constant.WX_SECRET + "CODE&grant_type=authorization_code&code=";
	static final String WX_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=";

	public JSONObject getWxToken(String code);

	public JSONObject getWxUserInfo(String token, String openId);

	public JSONObject getWxUserInfo(String url, String token, String openId);

	/**
	 * @param code
	 * @param appId
	 * @param secret
	 * @param grantType
	 * @param wxLoginUrl
	 * @return
	 * @Description:
	 */
	public JSONObject getWxToken(String code, String appId, String secret, String grantType, String wxLoginUrl);
	
}
