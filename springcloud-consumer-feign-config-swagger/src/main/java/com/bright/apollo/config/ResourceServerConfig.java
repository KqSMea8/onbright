package com.bright.apollo.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.bright.apollo.exception.AuthExceptionEntryPoint;
import com.bright.apollo.service.AuthorizeConfigManager;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月8日  
 *@Version:1.1.0  
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	private static final String SERVER_RESOURCE_ID = "oauth2";
	@Autowired
    private RedisConnectionFactory redisConnectionFactory;
	@Resource
    private AuthorizeConfigManager authorizeConfigManager;
	@Autowired
	private LoginAuthenticationSecurityConfig smsLoginAuthenticationSecurityConfig;
    @Bean
    RedisTokenStore redisTokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }
    @Autowired
    private AccessDeniedHandler CustomAccessDeniedHandler;
    @Override
    public void configure(HttpSecurity http) throws Exception {
    	 http. 
         csrf().disable()
         .exceptionHandling()
         .authenticationEntryPoint(new Http401AuthenticationEntryPoint("Bearer realm=\"webrealm\""))
         .and()
         .httpBasic();
    	 authorizeConfigManager.config(http.authorizeRequests());
    	 http.apply(smsLoginAuthenticationSecurityConfig);
    	 
    }
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(redisTokenStore()).resourceId(SERVER_RESOURCE_ID)
        .accessDeniedHandler(CustomAccessDeniedHandler).authenticationEntryPoint(new AuthExceptionEntryPoint());
    }
}