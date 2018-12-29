package com.bright.apollo.filter;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bright.apollo.cache.CacheHelper;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.http.MobClient;
import com.bright.apollo.service.UserService;
import com.bright.apollo.service.WxService;
import com.bright.apollo.tool.Base64Util;
import com.bright.apollo.tool.HttpUtil;
import com.bright.apollo.tool.MD5;
import com.bright.apollo.vo.SmsLoginParamVo;
import com.bright.apollo.vo.SmsLoginVo;
import com.bright.apollo.vo.WxLoginParamVo;
import com.bright.apollo.vo.WxLoginVo;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年9月10日
 * @Version:1.1.0
 */
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(ValidateCodeFilter.class);

	public ValidateCodeFilter() {
		logger.info("===Loading ValidateCodeFilter===");
	}

	private AntPathMatcher pathMatcher = new AntPathMatcher();
	@Autowired
	private UserService userService;
	@Autowired
	private SmsLoginVo smsLoginVo;
	@Autowired
	private SmsLoginParamVo smsLoginParamVo;
	@Autowired
	private WxLoginVo wxLoginVo;
	@Autowired
	private WxLoginParamVo wxLoginParamVo;
	@Autowired
	private WxService wxService;
	@Autowired
	private CacheHelper cacheHelper;
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("===remoteAddress:"+request.getRemoteAddr()+"===validateCodeFilter before===");
		//String url = smsLoginVo.getUrl();
		if (pathMatcher.match(smsLoginVo.getUrl(), request.getRequestURI())) {
			// mobile login
			try {
				String shareSdkAppkey = request.getParameter(smsLoginParamVo.getAppkey());
				String zone = request.getParameter(smsLoginParamVo.getZone());
				String mobile = request.getParameter(smsLoginParamVo.getMobile());
				String code = request.getParameter(smsLoginParamVo.getCode());
				logger.info("===shareSdkAppkey:" + smsLoginParamVo.getAppkey() + "===zone:" + smsLoginParamVo.getZone()
						+ "===code:" + smsLoginParamVo.getCode() + "===mobile:" + smsLoginParamVo.getMobile());
				if (StringUtils.isEmpty(shareSdkAppkey) || StringUtils.isEmpty(zone) || StringUtils.isEmpty(mobile)
						|| StringUtils.isEmpty(code)) {
					throw new InternalAuthenticationServiceException("request param error");
				}
				MobClient mobClient = new MobClient();
				mobClient.addParam("appkey", shareSdkAppkey).addParam("phone", mobile).addParam("zone", zone)
						.addParam("code", code);
				mobClient.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				mobClient.addRequestProperty("Accept", "application/json");
				String result = mobClient.post();
				logger.info("===result:" + result);
				//request.remo
				JSONObject object = new JSONObject(result);
				if (object.getInt("status") == 200) {
					if (userService.queryUserByName(mobile) == null) {
						String pwd = mobile.substring(mobile.length() - 8);
						try {
							userService.addUser(mobile, encrypt(encrypt(pwd)));
						} catch (Exception e) {
							logger.error("===error msg:" + e.getMessage());
						}
					}
					filterChain.doFilter(request, response);
				} else {
					throw new InternalAuthenticationServiceException("Validation code is incorrect");
				}

				// filterChain.doFilter(request, response);
				logger.info("====result:" + result);
			} catch (Exception e) {
				logger.error("====error msg:" + e.getMessage());
				throw new InternalAuthenticationServiceException(e.getMessage());
			} 

		} else if (pathMatcher.match(wxLoginVo.getUrl(), request.getRequestURI())) {
			// wx login  for phone use wx login
			try {
				String wxToken = request.getParameter(wxLoginParamVo.getWxToken());
				String openId = request.getParameter(wxLoginParamVo.getOpenId());
				logger.info("===wxToken:" + wxToken + "===openId:" + openId);
				if (StringUtils.isEmpty(wxToken) || StringUtils.isEmpty(openId)) {
					throw new InternalAuthenticationServiceException("request param error");
				}
				JSONObject wxUserInfo = wxService.getWxUserInfo(wxLoginParamVo.getWxUrl(),wxToken, openId);
				if (wxUserInfo == null || !wxUserInfo.has("headimgurl") || !wxUserInfo.has("nickname")
						|| !wxUserInfo.has("sex")) {
					throw new InternalAuthenticationServiceException("request param error");
				} else {
					TUser tuser = userService.queryUserByOpenId(openId);
					if (tuser == null) {
						logger.info("===headimgurl:" + wxUserInfo.getString("headimgurl") + "===nickname:"
								+ wxUserInfo.getString("nickname"));
						userService.saveUserByWeiXinInfo(openId, wxUserInfo.getString("headimgurl"),
								wxUserInfo.getString("nickname"));
					}
					filterChain.doFilter(request, response);
				}
			} catch (Exception e) {
				logger.error("====error msg:" + e.getMessage());
				throw new InternalAuthenticationServiceException(e.getMessage());
			}
		} else if (pathMatcher.match(wxLoginVo.getCodeUrl(), request.getRequestURI())) {
			// login/wx/code   for wx mini program
			try {
				String code = request.getParameter(wxLoginParamVo.getCode());
				logger.info("===code:" + code);
				if (StringUtils.isEmpty(code)) {
					throw new InternalAuthenticationServiceException("code is null");
				}
				long startTime = new Date().getTime();
				JSONObject wxToken = wxService.getWxToken(code, wxLoginParamVo.getAppId(), wxLoginParamVo.getSecret(),
						wxLoginParamVo.getGrantType(), wxLoginParamVo.getWxLoginUrl());
				long endTime = new Date().getTime();
				logger.info("===time"+(endTime-startTime)+"===wxToken:"+wxToken);
				if (wxToken == null || !wxToken.has("session_key") || !wxToken.has("openid")) {
					throw new InternalAuthenticationServiceException("wx code verify request error ");
				} else {
 					String openId = wxToken.getString("openid");
					TUser tuser = userService.queryUserByOpenId(openId);
					if(tuser==null){
						userService.saveUserByWeiXinInfo(openId);
					}
					cacheHelper.addOpenId(code, openId);
					filterChain.doFilter(request, response);
				}
		//		URI uri = URI.create("www.baidu.com");
			//	HttpUtil.request(uri);
//				cacheHelper.addOpenId(code, "o40G45XVBWdf8HKQCkN-9W74vNBk");
				//filterChain.doFilter(request, response);
			} catch (Exception e) {
				logger.error("====error msg:" + e.getMessage());
				throw new InternalAuthenticationServiceException(e.getMessage());
			}

		} else {
			// let it go
			filterChain.doFilter(request, response);
		}
	}

	private static String encrypt(String rawPassword) throws Exception {
		String base64Encrypt = Base64Util.base64Encrypt(rawPassword.toString().getBytes());
		return MD5.getMD5Str(base64Encrypt + rawPassword);
	}
}