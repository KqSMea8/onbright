package com.bright.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.feign.FeignOboxClient;
import com.bright.apollo.feign.FeignSceneClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.response.SceneInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月2日
 * @Version:1.1.0
 */
@Api("scene Controller")
@RequestMapping("scene")
@RestController
public class SceneController {
	@Autowired
	private FeignSceneClient feignSceneClient;
	@Autowired
	private FeignOboxClient feignOboxClient;

	@RequestMapping(value = "/{sceneNumber}", method = RequestMethod.GET)
	@ApiOperation(value = "get Scene by sceneNumber", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	public ResponseObject<SceneInfo> getScene(@PathVariable Integer sceneNumber) {
		ResponseObject<SceneInfo> res = null;
		try {
			return feignSceneClient.getScene(sceneNumber);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject<SceneInfo>();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/{sceneNumber}", method = RequestMethod.PUT)
	@ApiOperation(value = "update Scene", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	public ResponseObject<SceneInfo> updateScene(@PathVariable(required = true) Integer sceneNumber,
			@RequestBody(required = true) SceneInfo info) {
		ResponseObject<SceneInfo> res = null;
		try {
			//rewrite
		//	return feignSceneClient.updateScene(sceneNumber, info);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject<SceneInfo>();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "del Scene by sceneNumber", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/{sceneNumber}", method = RequestMethod.DELETE)
	public ResponseObject deleteScene(@PathVariable Integer sceneNumber) {
		ResponseObject res = null;
		try {
			return feignSceneClient.deleteScene(sceneNumber);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "del SceneCondition by sceneNumber,if condtionId not null delete by conditionId.else delete condition by sceneNumber", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/scene/deleteSceneCondition/{sceneNumber}/{condtionId}", method = RequestMethod.DELETE)
	public ResponseObject deleteSceneCondition(@PathVariable Integer sceneNumber, @PathVariable Integer condtionId) {
		ResponseObject res = null;
		try {
			return feignSceneClient.deleteSceneCondition(sceneNumber, condtionId);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "del SceneAction by sceneNumber,if actionId not null delete by actionId.else delete action by sceneNumber", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/scene/deleteSceneAction/{sceneNumber}/{actionId}", method = RequestMethod.DELETE)
	public ResponseObject deleteSceneAction(@PathVariable Integer sceneNumber, @PathVariable Integer actionId) {
		ResponseObject res = null;
		try {
			return feignSceneClient.deleteSceneAction(sceneNumber, actionId);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@ApiOperation(value = "add servr scene ", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/addScene", method = RequestMethod.POST)
	public ResponseObject<SceneInfo> addScene(@RequestBody(required = true) SceneInfo info) {
		ResponseObject<SceneInfo> res = null;
		try {
			return feignSceneClient.addSceneInfo(info);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject<SceneInfo>();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	
}
