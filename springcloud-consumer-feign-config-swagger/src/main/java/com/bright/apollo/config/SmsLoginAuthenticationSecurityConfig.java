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

import com.bright.apollo.filter.SmsLoginAuthenticationFilter;
import com.bright.apollo.provider.SmsLoginAuthenticationProvider;
import com.bright.apollo.vo.SmsLoginParamVo;
import com.bright.apollo.vo.SmsLoginVo;

 

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月11日  
 *@Version:1.1.0  
 */
@Configuration("mobileLoginAuthenticationSecurityConfig")
public class SmsLoginAuthenticationSecurityConfig  extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


    private static final Logger logger = LoggerFactory.getLogger(SmsLoginAuthenticationSecurityConfig.class.getName());
    
    @Value("${login.mobile.url}")
    private String defaultMobileLoginUrl;
    @Value("${login.mobile.parameter}")
    private String defaultMobileLoginParameter;
    @Value("${login.mobile.httpMethod}")
    private String defaultMobileLoginHttpMethod;
    
    @Autowired
    private UserDetailsService customUserDetailsService;
    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;
    @Autowired
	private SmsLoginVo smsLoginVo;
	@Autowired
	private SmsLoginParamVo smsLoginParamVo;

    public SmsLoginAuthenticationSecurityConfig() {
        logger.info("SmsLoginAuthenticationSecurityConfig loading ...");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
    	 
         String url = smsLoginVo.getUrl();
         String parameter = smsLoginParamVo.getMobile();
         String httpMethod = smsLoginVo.getHttpMethod();

         SmsLoginAuthenticationFilter mobileLoginAuthenticationFilter = new SmsLoginAuthenticationFilter(StringUtils.isBlank(url) ? defaultMobileLoginUrl : url,
                 StringUtils.isBlank(parameter) ? defaultMobileLoginUrl : parameter, StringUtils.isBlank(httpMethod) ? defaultMobileLoginHttpMethod : httpMethod);

        mobileLoginAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        mobileLoginAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        mobileLoginAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        SmsLoginAuthenticationProvider smsLoginAuthenticationProvider = new SmsLoginAuthenticationProvider();
        smsLoginAuthenticationProvider.setUserDetailsService(customUserDetailsService);

        http.authenticationProvider(smsLoginAuthenticationProvider)
                .addFilterAfter(mobileLoginAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }


}
