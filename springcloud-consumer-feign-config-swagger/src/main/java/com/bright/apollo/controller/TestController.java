package com.bright.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.vo.SmsLoginVo;

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
	private SmsLoginVo SmsLoginVo;
	@Autowired
	private FeignDeviceClient feignDeviceClient;
	@Autowired
	private FacadeController facadeController;
  	@RequestMapping(value = "/getOboxDeviceConfigByUserId", method = RequestMethod.GET)
	public ResponseObject registAliDev() {
		ResponseObject res = new ResponseObject();
		try {
			
			return feignDeviceClient.getOboxDeviceConfigByUserId(439);
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
  	@RequestMapping(value = "/test", method = RequestMethod.GET)
  	public ResponseObject test() {
		ResponseObject res = new ResponseObject();
		try {
			facadeController.controlRemoteLed("aaaa", "sssss", "ssss");
			System.out.println("===url:"+SmsLoginVo.getUrl());
			System.out.println("===method:"+SmsLoginVo.getHttpMethod());
 			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
  	@RequestMapping(value = "/test2", method = RequestMethod.POST)
  	@ResponseBody
  	public ResponseObject test2() {
		ResponseObject res = new ResponseObject();
		try {
			facadeController.controlRemoteLed("aaaa", "sssss", "ssss");
			System.out.println("===url:"+SmsLoginVo.getUrl());
			System.out.println("===method:"+SmsLoginVo.getHttpMethod());
 			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
