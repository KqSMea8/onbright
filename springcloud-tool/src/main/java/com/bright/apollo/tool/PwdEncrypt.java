package com.bright.apollo.tool;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月20日  
 *@Version:1.1.0  
 */
public class PwdEncrypt {
	public static String encrypt(String rawPassword) throws Exception {
		String base64Encrypt = Base64Util.base64Encrypt(rawPassword.toString().getBytes());
		return MD5.getMD5Str(base64Encrypt + rawPassword);
	}
}
