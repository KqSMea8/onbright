package com.bright.apollo.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月14日  
 *@Version:1.1.0  
 */
public class NumberHelper {

	public static boolean isNumeric(String str){ 
		   Pattern pattern = Pattern.compile("[0-9]*"); 
		   Matcher isNum = pattern.matcher(str);
		   if( !isNum.matches() ){
		       return false; 
		   } 
		   return true; 
		}

}
