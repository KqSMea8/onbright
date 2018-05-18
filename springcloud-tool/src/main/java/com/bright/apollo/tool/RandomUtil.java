package com.bright.apollo.tool;  
  
  
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月9日  
 *@Version:1.1.0  
 */
public class RandomUtil {
	 
	public static int makeCode() {
 		return (int)((Math.random()*9+1)*100000);
	}
}
