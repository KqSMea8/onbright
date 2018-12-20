package com.bright.apollo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月20日  
 *@Version:1.1.0  
 */
@Api("hotel Controller")
@RestController
@RequestMapping("hotel")
public class HotelController {
	private static final Logger logger = LoggerFactory.getLogger(HotelController.class);
	//check in
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "checkIn", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/checkIn/{endTime}/{mobile}/{locationId}", method = RequestMethod.POST)
	public ResponseObject checkIn(@PathVariable(required = true, value = "endTime") Long endTime,
			@PathVariable(required = true, value = "mobile") String mobile,
			@PathVariable(required = true, value = "mobile") Integer locationId) {
		ResponseObject res = new ResponseObject();
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	//check out 
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "checkOut", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/checkOut/{locationId}", method = RequestMethod.PUT)
	public ResponseObject checkOut(
			@PathVariable(required = true, value = "mobile") Integer locationId) {
		ResponseObject res = new ResponseObject();
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	//continue 
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "continueLocation", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/continueLocation/{endTime}/{locationId}", method = RequestMethod.PUT)
	public ResponseObject continueLocation(@PathVariable(required = true, value = "endTime") Long endTime,
			@PathVariable(required = true, value = "mobile") Integer locationId) {
		ResponseObject res = new ResponseObject();
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	//query device 
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "queryDevice", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/queryDevice", method = RequestMethod.GET)
	public ResponseObject queryDevice() {
		ResponseObject res = new ResponseObject();
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	//control device
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "controlDevice", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/controlDevice/{serialId}/{status}", method = RequestMethod.PUT)
	public ResponseObject controlDevice(@PathVariable(required = true, value = "serialId") String serialId,
			@PathVariable(required = true, value = "status") String status) {
		ResponseObject res = new ResponseObject();
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	//control scene
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "controlScene", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/controlScene/{sceneNumber}", method = RequestMethod.PUT)
	public ResponseObject controlScene(@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
}
