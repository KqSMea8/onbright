package com.bright.apollo.token;

import java.io.Serializable;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年9月12日
 * @Version:1.1.0
 */
public class LoginAuthenticationToken extends AbstractAuthenticationToken implements Serializable {

	/**  
	 *   
	 */
	private static final long serialVersionUID = 5683685570539179815L;
	// private static final long serialVersionUID =
	// SpringSecurityCoreVersion.SERIAL_VERSION_UID;
	private static final Logger logger = LoggerFactory.getLogger(LoginAuthenticationToken.class.getName());
	private final Object principal;

	public LoginAuthenticationToken(String principal) {
		super(null);
		this.principal = principal;
		this.setAuthenticated(false);
		logger.info("LoginAuthenticationToken setAuthenticated ->false loading ...");
	}

	public LoginAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		// must use super, as we override
		super.setAuthenticated(true);
		logger.info("LoginAuthenticationToken setAuthenticated ->true loading ...");
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		if (authenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
		}
		super.setAuthenticated(false);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
	}

}
