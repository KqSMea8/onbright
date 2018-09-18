package com.bright.apollo.pwdEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bright.apollo.config.WebSecurityConfig;
import com.bright.apollo.tool.Base64Util;
import com.bright.apollo.tool.MD5;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月18日  
 *@Version:1.1.0  
 */
public class CusromPasswordEncoder  implements PasswordEncoder {
	private static final Logger logger = LoggerFactory.getLogger(CusromPasswordEncoder.class);
	/* (non-Javadoc)  
	 * @see org.springframework.security.crypto.password.PasswordEncoder#encode(java.lang.CharSequence)  
	 */
	@Override
	public String encode(CharSequence rawPassword) {
		if(rawPassword==null||rawPassword.length()<=0){
			logger.warn("Empty raw password");
			return null;
		}
		String base64Encrypt = Base64Util.base64Encrypt(rawPassword.toString().getBytes());
		return MD5.MD5generator(base64Encrypt+rawPassword.toString());
	}

	/* (non-Javadoc)  
	 * @see org.springframework.security.crypto.password.PasswordEncoder#matches(java.lang.CharSequence, java.lang.String)  
	 */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword == null || encodedPassword.length() == 0) {
			logger.warn("Empty encoded password");
			return false;
		}
		if (rawPassword == null || rawPassword.length() == 0) {
			logger.warn("Empty raw password");
			return false;
		}
		return false;
	}

}
