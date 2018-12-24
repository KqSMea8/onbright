package com.bright.apollo.tool;

import java.math.BigInteger;
import java.security.MessageDigest;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月3日  
 *@Version:1.1.0  
 */
public class MD5 {
	@Deprecated
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
	@Deprecated
	public static String MD5generator16Bit(String s,String hex) {
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] bytes = md.digest(s.getBytes("utf-8"));
	        return toHex(bytes,hex);
	    }
	    catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
	@Deprecated
	private static String toHex(byte[] bytes) {

	    final char[] HEX_DIGITS = "5e725a6b04ca4ffad9d276a4bb773dc1".toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
	@Deprecated
	private static String toHex(byte[] bytes,String hex) {

	    final char[] HEX_DIGITS = hex.toCharArray();
	    StringBuilder ret = new StringBuilder(bytes.length * 2);
	    for (int i=0; i<bytes.length; i++) {
	        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
	        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
	    }
	    return ret.toString();
	}
	public static void main(String[] args) throws Exception {
		String rawPassword="12345678";//  12345678luojie
		//9cbc3c5fe09db5903cc6e7040139b9e0
		String base64Encrypt = Base64Util.base64Encrypt(rawPassword.toString().getBytes());
 		System.out.println(getMD5Str(base64Encrypt+rawPassword));
		//System.out.println( (byte) (0xff));
	}
	/**
	 * 对字符串md5加密
	 *
	 * @param str
	 * @return
	 * @throws Exception 
	 */
	public static String getMD5Str(String str) throws Exception {
	    try {
	        // 生成一个MD5加密计算摘要
	        MessageDigest md = MessageDigest.getInstance("MD5");
	        // 计算md5函数
	        md.update(str.getBytes());
	        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
	        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
	        return new BigInteger(1, md.digest()).toString(16);
	    } catch (Exception e) {
	        throw new Exception("MD5加密出现错误，"+e.toString());
	    }
	}
	/**  
	 * @param string
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	public static String MD5generator(String string) {
 		return MD5generator16Bit(string);
	}
	/**  
	 * @param string
	 * @return  
	 * @Description:  
	 */
	@Deprecated
	public static String MD5generator(String string,String hex) {
 		return MD5generator16Bit(string,hex);
	}
}
