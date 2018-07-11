package com.bright.apollo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月3日
 * @Version:1.1.0
 */
@Component("customWebResponseExceptionTranslator")
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseEntity translate(Exception e) throws Exception {
		OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
		return ResponseEntity.status(oAuth2Exception.getHttpErrorCode())
				.body(new CustomOauthException(oAuth2Exception.getMessage()));

	}
}
