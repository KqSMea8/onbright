package com.bright.apollo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月10日
 * @Version:1.1.0
 */
@Api("user Controller")
@RequestMapping("user")
@RestController
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private FeignUserClient feignUserClient;

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "register user", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/register/{mobile}/{code}/{pwd}", method = RequestMethod.POST)
	public ResponseObject register(@PathVariable String mobile,
			@PathVariable Integer code,@PathVariable String pwd) {
		ResponseObject res = null;
		try {
			res = feignUserClient.register(mobile,code,pwd);
			return res;
		} catch (Exception e) {
			logger.error(e.getMessage());
			res = new ResponseObject();
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
			return res;
		}
	}
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "send", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@GetMapping("/sendCodeToMobile/{mobile}")
	public ResponseObject register(@PathVariable String mobile) {
		ResponseObject res = null;
		try {
			res = feignUserClient.sendCodeToMobile(mobile);
			return res;
		} catch (Exception e) {
			logger.error(e.getMessage());
			res = new ResponseObject();
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
			return res;
		}
	}
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "forget password", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@GetMapping("/forget/{mobile}")
	public ResponseObject forget(@PathVariable String mobile) {
		ResponseObject res = null;
		try {
			res = feignUserClient.forget(mobile);
			return res;
		} catch (Exception e) {
			logger.error(e.getMessage());
			res = new ResponseObject();
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
			return res;
		}
	}

	   

}
