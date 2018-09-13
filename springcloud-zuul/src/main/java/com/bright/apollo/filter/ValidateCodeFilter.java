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

import com.bright.apollo.http.MobClient;
import com.bright.apollo.vo.SmsLoginParamVo;
import com.bright.apollo.vo.SmsLoginVo;

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
	private SmsLoginVo smsLoginVo;
	@Autowired
	private SmsLoginParamVo smsLoginParamVo;

	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("===validateCodeFilter before===");
		String url = smsLoginVo.getUrl();
		if (pathMatcher.match(url, request.getRequestURI())) {
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
					// throw new CustomException(HttpStatus.BAD_REQUEST.value(),
					// "request param error");
				}
				MobClient mobClient = new MobClient();
				mobClient.addParam("appkey", shareSdkAppkey).addParam("phone", mobile).addParam("zone", zone)
						.addParam("code", code);
				mobClient.addRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				mobClient.addRequestProperty("Accept", "application/json");
				String result = mobClient.post();
				JSONObject object = new JSONObject(result);
				if (object.getInt("status") == 200) {
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

		} else {
			// let it go
			filterChain.doFilter(request, response);
		}
	}

}
