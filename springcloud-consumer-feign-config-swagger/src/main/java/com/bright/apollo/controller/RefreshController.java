package com.bright.apollo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月12日  
 *@Version:1.1.0  
 */
@RestController("refresh")
public class RefreshController {
 
	@RequestMapping(value = "/all", method = RequestMethod.POST)
	public void getObox(HttpServletRequest request,HttpServletResponse response) {
		//执行 curl -X POST http://localhost:8800/bus/refresh
	}
}
