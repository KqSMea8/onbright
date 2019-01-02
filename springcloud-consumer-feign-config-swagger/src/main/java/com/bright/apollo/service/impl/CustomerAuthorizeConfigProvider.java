package com.bright.apollo.service.impl;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import com.bright.apollo.service.AuthorizeConfigProvider;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月8日
 * @Version:1.1.0
 */
@Component
@Order(Integer.MIN_VALUE)
public class CustomerAuthorizeConfigProvider implements AuthorizeConfigProvider {

	/*
	 * (non-Javadoc)
	 *
	 * @see com.bright.apollo.service.AuthorizeConfigProvider#config(org.
	 * springframework.security.config.annotation.web.configurers.
	 * ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry)
	 */
	@Override
	public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
		// permit through url
		System.out.println(" ===config=== ");
		config.antMatchers("/user/wxSendCodeToMobile","/swagger-ui.html", "/swagger-resources/**",
		"/webjars/springfox-swagger-ui/**","/v2/api-docs", "/user/**","/rokid/**","/oauthclient/**","/test/**","/refreshconfig/**","/tmall2/**","/tmall/**","/view/**","/PrivacyPolicyCN.html","PrivacyPolicyEN.html","/html/**","/OBHotel/**","/static/**","/fonts/**").permitAll();
		return false;
	}

}
