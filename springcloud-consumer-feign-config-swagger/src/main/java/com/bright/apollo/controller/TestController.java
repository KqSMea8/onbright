package com.bright.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.response.AliDevInfo;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月22日  
 *@Version:1.1.0  
 */
 
@RestController
@RequestMapping("test")
public class TestController {
	@Autowired
	private FeignDeviceClient feignDeviceClient;
  	@RequestMapping(value = "/getOboxDeviceConfigByUserId", method = RequestMethod.GET)
	public ResponseObject registAliDev() {
		ResponseObject res = new ResponseObject();
		try {
			
			return feignDeviceClient.getOboxDeviceConfigByUserId(439);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
