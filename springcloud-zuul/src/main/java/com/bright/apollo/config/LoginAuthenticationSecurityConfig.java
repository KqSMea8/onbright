package com.bright.apollo.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bright.apollo.filter.LoginAuthenticationFilter;
import com.bright.apollo.provider.LoginAuthenticationProvider;
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
@Configuration("loginAuthenticationSecurityConfig")
public class LoginAuthenticationSecurityConfig
		extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private static final Logger logger = LoggerFactory.getLogger(LoginAuthenticationSecurityConfig.class.getName());

	@Value("${login.mobile.url}")
	private String defaultMobileLoginUrl;
	@Value("${login.mobile.parameter}")
	private String defaultMobileLoginParameter;
	@Value("${login.mobile.httpMethod}")
	private String defaultMobileLoginHttpMethod;
	//private static final String antUrl = "/login/**";
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthenticationSuccessHandler customAuthenticationSuccessHandler;
	@Autowired
	private AuthenticationFailureHandler customAuthenticationFailureHandler;
	@Autowired
	private SmsLoginVo smsLoginVo;
	@Autowired
	private SmsLoginParamVo smsLoginParamVo;
	@Autowired
	private WxLoginVo wxLoginVo;
	@Autowired
	private WxLoginParamVo wxLoginParamVo;

	public LoginAuthenticationSecurityConfig() {
		logger.info("LoginAuthenticationSecurityConfig loading ...");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		String url = smsLoginVo.getUrl();
		String parameter = smsLoginParamVo.getMobile();
		String httpMethod = smsLoginVo.getHttpMethod();
		LoginAuthenticationFilter loginAuthenticationFilter = new LoginAuthenticationFilter(wxLoginVo.getAntUrl(),
				StringUtils.isBlank(parameter) ? defaultMobileLoginUrl : parameter, wxLoginParamVo.getOpenId(),
				wxLoginParamVo.getCode(), StringUtils.isBlank(httpMethod) ? defaultMobileLoginHttpMethod : httpMethod);

		loginAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		loginAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
		loginAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

		LoginAuthenticationProvider loginAuthenticationProvider = new LoginAuthenticationProvider();
		loginAuthenticationProvider.setUserDetailsService(userDetailsService);

		http.authenticationProvider(loginAuthenticationProvider).addFilterAfter(loginAuthenticationFilter,
				UsernamePasswordAuthenticationFilter.class);
	}

}
