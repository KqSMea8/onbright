package com.bright.apollo.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月3日  
 *@Version:1.1.0  
 */
@JsonSerialize(using = CustomOauthExceptionSerializer.class)
public class CustomOauthException extends OAuth2Exception {
    /**  
	 *   
	 */
	private static final long serialVersionUID = -3935906205559100070L;

	public CustomOauthException(String msg) {
        super(msg);
    }
}