package com.bright.apollo.tool;

import org.apache.commons.codec.binary.Base64;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月14日  
 *@Version:1.1.0  
 */
public class Base64Util {
	/**
	 * base64算法加密
	 * @param data
	 * @return
	 */
	public static String base64Encrypt(byte[] data){
		@SuppressWarnings("static-access")
		String result = new Base64().encodeBase64String(data);
		return result;
	}
	
	/**
	 * base64算法解密
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static String base64Decrypt(String data) {
		@SuppressWarnings("static-access")
		byte[] resultBytes = new Base64().decodeBase64(data);
		return new String(resultBytes);
	}
}
