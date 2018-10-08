package com.bright.apollo.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
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

import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.http.MobClient;
import com.bright.apollo.service.UserService;
import com.bright.apollo.service.WxService;
import com.bright.apollo.tool.Base64Util;
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

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("===validateCodeFilter before===");
		String url = smsLoginVo.getUrl();
		if (pathMatcher.match(url, request.getRequestURI())) {
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
			// wx login
			try {
				String wxToken = request.getParameter(wxLoginParamVo.getWxToken());
				String openId = request.getParameter(wxLoginParamVo.getOpenId());
				logger.info("===wxToken:" + wxToken + "===openId:" + openId);
				if (StringUtils.isEmpty(wxToken) || StringUtils.isEmpty(openId)) {
					throw new InternalAuthenticationServiceException("request param error");
				}
				JSONObject wxUserInfo = wxService.getWxUserInfo(wxToken, openId);
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
