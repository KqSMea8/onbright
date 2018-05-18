package com.bright.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月16日  
 *@Version:1.1.0  
 */
@Api("device Controller")
@RequestMapping("device")
@RestController
public class DeviceController {
 	@Autowired
	private FeignDeviceClient feignDeviceClient;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "get deivcie by device serialId", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/{serialId}", method = RequestMethod.GET)
	public ResponseObject<TOboxDeviceConfig> getDevice(@PathVariable(required = true) String serialId) {
		ResponseObject res = new ResponseObject();
		try {
 			res = feignDeviceClient.getDevice(serialId);
 		} catch (Exception e) {
			e.printStackTrace();
 			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "update deivcie by device serialId", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/{serialId}", method = RequestMethod.PUT)
	public ResponseObject updateDevice(@PathVariable(required = true) String serialId,
			@RequestBody(required = true) TOboxDeviceConfig device) {
		ResponseObject res = new ResponseObject();
		try {
			res = feignDeviceClient.updateDevice(serialId,device);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "add device", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/{serialId}", method = RequestMethod.POST)
	public ResponseObject addDevice(@PathVariable(required = true) String serialId,
			@RequestBody(required = true) TOboxDeviceConfig device) {
		ResponseObject res = new ResponseObject();
		try { 
			res = feignDeviceClient.addDevice(serialId,device);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "del device", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject delDevice(@PathVariable(required = true) String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			res = feignDeviceClient.delDevice(serialId);
		} catch (Exception e) {
			e.printStackTrace();
 			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
