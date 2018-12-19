package com.bright.apollo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserSceneTemplate;
import com.bright.apollo.feign.FeignOboxClient;
import com.bright.apollo.feign.FeignSceneClient;
import com.bright.apollo.feign.FeignUserClient;
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
	private static final Logger logger = LoggerFactory.getLogger(SceneController.class);
	@Autowired
	private FeignSceneClient feignSceneClient;
	@Autowired
	private FeignOboxClient feignOboxClient;
	@Autowired
	private FeignUserClient feignUserClient;
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
	@Deprecated
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
			map.put("scene_number", scene.getSceneNumber().intValue());
			map.put("scene_type", scene.getSceneType());
			String oboxSerialId = scene.getOboxSerialId();
			if(!StringUtils.isEmpty(oboxSerialId)){
				map.put("obox_serial_id",oboxSerialId);
			}
			if(scene.getOboxSceneNumber()!=null){
				map.put("obox_scene_number", scene.getOboxSceneNumber().intValue());
			}
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
	@ApiOperation(value = "querySceneByOboxSerialId", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/querySceneByOboxSerialId/{oboxSerialId}", method = RequestMethod.GET)
	public ResponseObject<Map<String, Object>> querySceneByOboxSerialId(
			@PathVariable(value="oboxSerialId") String oboxSerialId) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String,Object>>();
		try {
			logger.info("===querySceneByObxSerialId agrs:"+oboxSerialId);
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				logger.warn("===this user can't find===");
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			logger.info("==userId:"+resUser.getData().getId()+"===userName:"+resUser.getData().getUserName());
			ResponseObject<TObox> oboxRes = feignOboxClient.getOboxByUserAndoboxSerialId(resUser.getData().getId(), oboxSerialId);
			if(oboxRes==null||oboxRes.getData()==null){
				logger.warn("===this obox can't find===");
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			return feignSceneClient.querySceneByOboxSerialId( oboxSerialId);
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
 			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "addTemplate,the map's key is the sceneModel and deviceModel(required),the respone is add success", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/addTemplate/{modelName}", method = RequestMethod.POST)
	public ResponseObject addTemplate(
			@PathVariable(value="modelName") String modelName,
			@RequestBody(required=true) Map<String, Object> map) {
		ResponseObject res = new ResponseObject();
		try {
			logger.info("===addTemplate args:"+modelName+"===map:"+map);
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			if(map!=null&&map.get("sceneModel")!=null&&map.get("deviceModel")!=null){
				return feignSceneClient.addTemplate(resUser.getData().getId(),modelName,map);
			}else{
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
			}
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
 			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "updateTemplate,the map's key is the sceneModel and deviceModel(required),the respone is update success", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/updateTemplate/{modelName}", method = RequestMethod.PUT)
	public ResponseObject updateTemplate(
			@PathVariable(value="modelName") String modelName,
			@RequestBody(required=true) Map<String, Object> map) {
		ResponseObject res = new ResponseObject();
		try {
			logger.info("===updateTemplate args:"+modelName+"===map:"+map);
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			if(map==null||map.get("sceneModel")==null||map.get("deviceModel")==null){
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<List<TUserSceneTemplate>>templatesRes= feignSceneClient.queryUserSceneTemplateByUserIdAndModelName(resUser.getData().getId(),modelName);
			if(templatesRes==null||templatesRes.getStatus()!=ResponseEnum.SelectSuccess.getStatus()||
					templatesRes==null
					){
				logger.warn("===the template is null or select error===");
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			List<TUserSceneTemplate> list = templatesRes.getData();
			if(list.size()<=0){
				res.setStatus(ResponseEnum.SearchIsEmpty.getStatus());
				res.setMessage(ResponseEnum.SearchIsEmpty.getMsg());
				return res;
			}else if(list.size()>1){
				res.setStatus(ResponseEnum.MultipleObjExist.getStatus());
				res.setMessage(ResponseEnum.MultipleObjExist.getMsg());
				return res;
			}
			return feignSceneClient.updateTemplate(resUser.getData().getId(),modelName,map);
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
 			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "deleteTemplate,the respone is delete success", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/deleteTemplate/{modelName}", method = RequestMethod.DELETE)
	public ResponseObject  deleteTemplate(@PathVariable(value="modelName") String modelName) {
		ResponseObject  res = new ResponseObject ();
		try {
			logger.info("===deleteTemplate args:"+modelName );
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			return feignSceneClient.deleteTemplate(resUser.getData().getId(),modelName);
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
 			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@ApiOperation(value = "queryTemplate,the respone is a map ,the map's key is sceneModels(list) and deviceModels(list),", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/queryTemplate", method = RequestMethod.GET)
	public ResponseObject<Map<String, Object>> queryTemplate() {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String,Object>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			return feignSceneClient.queryTemplateByUserId(resUser.getData().getId());
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
 			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
