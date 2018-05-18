package com.bright.apollo.service;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月8日
 * @Version:1.1.0
 */
public interface AuthorizeConfigProvider {
	boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);
}
