package com.bright.apollo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月8日  
 *@Version:1.1.0  
 */
public class Test {
	
	public static void main(String[] args) {
		PasswordEncoder encoder=new BCryptPasswordEncoder();
		CharSequence charSequence="123456";
		System.out.println(encoder.encode(charSequence));
		//$2a$10$e/MIQD62FBlgntJAuYmF6eK/sxRCxtBMXbHZIYJRIxyqzGDB9MuXe
		//$2a$10$cKRbR9IJktfmKmf/wShyo.5.J8IxO/7YVn8twuWFtvPgruAF8gtKq
	}
}
