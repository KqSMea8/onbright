package com.bright.apollo.tool;

import java.security.MessageDigest;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月3日  
 *@Version:1.1.0  
 */
public class MD5 {

	public static String MD5generator16Bit(String s) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] bytes = md.digest(s.getBytes("utf-8"));
	        return toHex(bytes);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	private static String toHex(byte[] bytes) {

	    final char[] HEX_DIGITS = "5e725a6b04ca4ffad9d276a4bb773dc1".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
	public static void main(String[] args) {
		System.out.println(MD5generator16Bit("123wdddddddddddd"));
	}
	/**  
	 * @param string
	 * @return  
	 * @Description:  
	 */
	public static String MD5generator(String string) {
 		return MD5generator16Bit(string);
	}
}
