package com.bright.apollo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.feign.FeignOboxClient;
import com.bright.apollo.feign.FeignSceneClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.response.SceneInfo;
import com.google.gson.JsonObject;

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
	public ResponseObject<TScene> getSceneBySceneNumber(@PathVariable Integer sceneNumber) {
		ResponseObject<TScene> res = null;
		try {
			return feignSceneClient.getSceneBySceneNumber(sceneNumber);
		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject<TScene>();
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
	/**  
	 * @param sceneNumber
	 * @param sceneStatus
	 * @return  
	 * @Description:  
	 */
	@ApiOperation(value = "add servr scene ", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/updateSceneSendSetting/{sceneNumber}/{sceneStatus}", method = RequestMethod.PUT)
	public ResponseObject<Map<String, Object>> updateSceneSendSetting(
			@PathVariable(value="sceneNumber")Integer sceneNumber,@PathVariable(value="sceneStatus") String sceneStatus) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String,Object>>();
		try {
			Map<String, Object>map=new HashMap<String, Object>();
			ResponseObject<TScene> sceneRes = feignSceneClient.getSceneBySceneNumber(sceneNumber);
			if(sceneRes==null||sceneRes.getStatus()!=ResponseEnum.SelectSuccess.getStatus()
					||sceneRes.getData()==null
					){
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TScene scene = sceneRes.getData();
			scene.setMessageAlter((byte)Integer.parseInt(sceneStatus.substring(1)));
			feignSceneClient.updateScene(scene);
			map.put("scene_number", scene.getSceneNumber());
			map.put("scene_type", scene.getSceneType());
			String oboxSerialId = scene.getOboxSerialId();
			if(StringUtils.isEmpty(oboxSerialId))
				oboxSerialId=null;
			map.put("obox_scene_number", scene.getOboxSceneNumber());
			map.put("obox_serial_id",oboxSerialId);
			map.put("scene_status", sceneStatus);
			res.setData(map);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
 		} catch (Exception e) {
			e.printStackTrace();
 			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	
}
