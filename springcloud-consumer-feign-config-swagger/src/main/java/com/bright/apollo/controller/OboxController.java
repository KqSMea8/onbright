package com.bright.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.feign.FeignOboxClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Api("obox Controller")
@RequestMapping("obox")
@RestController
public class OboxController {
	@Autowired
	private FeignOboxClient feignOboxClient;

	@ApiOperation(value = "get obox by serialId", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/{serialId}", method = RequestMethod.GET)
	public ResponseObject<TObox> getObox(@PathVariable(required = true) String serialId) {
		ResponseObject<TObox> res = null;
		try {
			return feignOboxClient.getObox(serialId);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject<TObox>();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@ApiOperation(value = "update obox ", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/{serialId}", method = RequestMethod.PUT)
	public ResponseObject<TObox> updateObox(@PathVariable(required = true) String serialId,
			@RequestBody(required = true) TObox obox) {
		ResponseObject<TObox> res = null;
		try {
			return feignOboxClient.updateObox(serialId, obox);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject<TObox>();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "delete obox ", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject deleteObox(@PathVariable(required = true) String serialId) {
		ResponseObject res = null;
		try {
			return feignOboxClient.deleteObox(serialId);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
/*
	@ApiOperation(value = "add obox ", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/addObox/{serialId}", method = RequestMethod.POST)
	public ResponseObject<TObox> addObox(@PathVariable(required = true, value = "serialId") String serialId,
			@RequestBody(required = true) TObox obox) {
		ResponseObject<TObox> res = null;
		try {
			return feignOboxClient.addObox(serialId, obox);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject<TObox>();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}*/

}
