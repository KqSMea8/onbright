package com.bright.apollo.service.impl;

import java.net.URI;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.bright.apollo.service.WxService;
import com.bright.apollo.tool.HttpUtil;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月12日
 * @Version:1.1.0
 */
@Service
public class WxServcieImpl implements WxService {
	private static final Logger logger = LoggerFactory.getLogger(WxServcieImpl.class);
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.WxService#getWxToken(java.lang.String)
	 */
	@Override
	public JSONObject getWxToken(String code) {
		if (StringUtils.isEmpty(code))
			return null;
		URI uri = URI.create(WX_TOKEN_URL + code);
		return HttpUtil.request(uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.WxService#getWxUserInfo(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public JSONObject getWxUserInfo(String token, String openId) {
		if (StringUtils.isEmpty(token) || StringUtils.isEmpty(openId))
			return null;
		URI uri = URI.create(WX_USERINFO_URL + token + "&openid=" + openId);
		return HttpUtil.request(uri);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.WxService#getWxUserInfo(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getWxUserInfo(String url, String token, String openId) {
		URI uri = URI.create(url + token + "&openid=" + openId);
		return HttpUtil.request(uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.service.WxService#getWxToken(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject getWxToken(String code, String appId, String secret, String grantType, String wxLoginUrl) {
		if (StringUtils.isEmpty(code))
			return null;
		StringBuffer sb = new StringBuffer(wxLoginUrl);
		// "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
		// +Constant.WX_APPID+"&secret="+Constant.WX_SECRET+"CODE&grant_type=
		// authorization_code&code=";
		sb.append("?appid=").append(appId).append("&secret=").append(secret)
		.append("&grant_type=").append(grantType)
		.append("&code=").append(code);
		logger.info("===url:"+sb.toString());
		URI uri = URI.create(sb.toString());
		return HttpUtil.request(uri);
	}

}
