package com.bright.apollo.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.request.CommonRequest;
import com.bright.apollo.request.RequestParam;
import com.google.gson.JsonObject;
import com.zz.common.exception.AppException;

/**  
 *@Title:  
 *@Description:  for old api 
 *@Author:JettyLiu
 *@Since:2018年6月29日  
 *@Version:1.1.0  
 */
@RestController
public class CommonController {
	@RequestMapping("/common")  
	public String common(HttpServletRequest request, HttpServletResponse response) throws AppException, UnsupportedEncodingException {  
		request.setCharacterEncoding("UTF-8");
		String CMD = request.getParameter("CMD");
		RequestParam requestParam = new RequestParam(
				request.getParameterMap());
		String requestData = request.getParameter("data");
		CommonRequest commonRequest = new CommonRequest(request, CMD,
				requestData, requestParam);
		//JsonObject respData = commonAction.handleRequest(commonRequest);
		JsonObject respData=new JsonObject();
		return respData.toString();  
    }  
}
