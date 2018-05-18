package com.bright.apollo.service.impl;

import java.net.URI;

import org.json.JSONObject;
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

}
