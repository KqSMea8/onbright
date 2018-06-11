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
	@ApiOperation(value = "get deivcie by device serialId", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/{serialId}", method = RequestMethod.GET)
	public ResponseObject<TOboxDeviceConfig> getDevice(@PathVariable(required = true) String serialId) {
		ResponseObject<TOboxDeviceConfig> res = null;
		try {
 			return feignDeviceClient.getDevice(serialId);
 		} catch (Exception e) {
			e.printStackTrace();
			res=new ResponseObject<TOboxDeviceConfig>();
 			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
 	@ApiOperation(value = "update deivcie by device serialId", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/{serialId}", method = RequestMethod.PUT)
	public ResponseObject<TOboxDeviceConfig> updateDevice(@PathVariable(required = true) String serialId,
			@RequestBody(required = true) TOboxDeviceConfig device) {
		ResponseObject<TOboxDeviceConfig> res = new ResponseObject<TOboxDeviceConfig>();
		try {
			res = feignDeviceClient.updateDevice(serialId,device);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@ApiOperation(value = "add device", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/{serialId}", method = RequestMethod.POST)
	public ResponseObject<TOboxDeviceConfig> addDevice(@PathVariable(required = true) String serialId,
			@RequestBody(required = true) TOboxDeviceConfig device) {
		ResponseObject<TOboxDeviceConfig> res = null;
		try { 
			return feignDeviceClient.addDevice(serialId,device);
		} catch (Exception e) {
			e.printStackTrace();
			res=new ResponseObject<TOboxDeviceConfig>();
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
		ResponseObject res = null;
		try {
			return feignDeviceClient.delDevice(serialId);
		} catch (Exception e) {
			e.printStackTrace();
			res=new ResponseObject();
 			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
