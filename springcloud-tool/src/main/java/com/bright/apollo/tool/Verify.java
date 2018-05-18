package com.bright.apollo.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月9日
 * @Version:1.1.0
 */
public class Verify {
	/**
	 * @param cellphone
	 * @return
	 * @Description:
	 */
	public static boolean checkCellphone(String cellphone) {
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
		return check(cellphone, regex);
	}

	/**
	 * @param str
	 * @param regex
	 * @return
	 * @Description:
	 */
	private static boolean check(String str, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
