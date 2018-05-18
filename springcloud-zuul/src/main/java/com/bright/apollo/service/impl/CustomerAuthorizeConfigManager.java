/*package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import com.bright.apollo.service.AuthorizeConfigManager;
import com.bright.apollo.service.AuthorizeConfigProvider;

*//**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月8日  
 *@Version:1.1.0  
 *//*
@Component
public class CustomerAuthorizeConfigManager implements AuthorizeConfigManager {
	private final List<AuthorizeConfigProvider> authorizeConfigProviders;


	@Autowired
	public CustomerAuthorizeConfigManager(List<AuthorizeConfigProvider> authorizeConfigProviders) {
		this.authorizeConfigProviders = authorizeConfigProviders;
	}
	 (non-Javadoc)  
	 * @see com.bright.apollo.service.AuthorizeConfigManager#config(org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry)  
	 
	@Override
	public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {

		boolean existAnyRequestConfig = false;
		String existAnyRequestConfigName = null;

		for (AuthorizeConfigProvider authorizeConfigProvider : authorizeConfigProviders) {
			boolean currentIsAnyRequestConfig = authorizeConfigProvider.config(config);
			if (existAnyRequestConfig && currentIsAnyRequestConfig) {
				throw new RuntimeException("重复的anyRequest配置:" + existAnyRequestConfigName + ","
						+ authorizeConfigProvider.getClass().getSimpleName());
			} else if (currentIsAnyRequestConfig) {
				existAnyRequestConfig = true;
				existAnyRequestConfigName = authorizeConfigProvider.getClass().getSimpleName();
			}
		}

		if (!existAnyRequestConfig) {
			config.anyRequest().authenticated();
			//config.anyRequest().permitAll();
		}

	}

}
*/