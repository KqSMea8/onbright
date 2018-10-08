package com.bright.apollo.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.bright.apollo.token.LoginAuthenticationToken;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月12日  
 *@Version:1.1.0  
 */
public class LoginAuthenticationProvider implements AuthenticationProvider {


    private static final Logger logger = LoggerFactory.getLogger(LoginAuthenticationProvider.class.getName());
    public LoginAuthenticationProvider() {
        logger.info("===LoginAuthenticationProvider loading===");
    }
    
    private UserDetailsService userDetailsService;
	/* (non-Javadoc)  
	 * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(org.springframework.security.core.Authentication)  
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //获取过滤器封装的token信息
        LoginAuthenticationToken authenticationToken = (LoginAuthenticationToken) authentication;
        //获取用户信息（数据库认证）
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
        //不通过
        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("Unable to obtain user information");
        }
        //通过
        LoginAuthenticationToken authenticationResult = new LoginAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }
	/**
     * 根据token类型，来判断使用哪个Provider
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
    


}
