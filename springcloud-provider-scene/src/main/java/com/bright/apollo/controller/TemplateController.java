package com.bright.apollo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TUserSceneTemplate;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.UserSceneTemplateService;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年12月19日
 * @Version:1.1.0
 */
@Controller
@RequestMapping("template")
@RestController
public class TemplateController {
	private static final Logger logger = LoggerFactory.getLogger(TemplateController.class);
	
	@Autowired
	private UserSceneTemplateService userSceneTemplateService;
	/**
	 * @param modelName
	 * @param sceneModel
	 * @param deviceModel
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addTemplate/{userId}/{modelName}", method = RequestMethod.POST)
	ResponseObject addTemplate(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "modelName") String modelName, @RequestBody Map<String, Object> map) {
		ResponseObject res = new ResponseObject();
		try {
			if (map != null && !StringUtils.isEmpty(map.get("sceneModel").toString())
					&& !StringUtils.isEmpty(map.get("deviceModel").toString())) {
				String sceneModel = map.get("sceneModel").toString();
				String deviceModel = map.get("deviceModel").toString();
				TUserSceneTemplate tUserSceneTemplate=new TUserSceneTemplate(sceneModel,modelName,deviceModel,userId);
				userSceneTemplateService.addTemplate(tUserSceneTemplate);
				res.setStatus(ResponseEnum.AddSuccess.getStatus());
				res.setMessage(ResponseEnum.AddSuccess.getMsg());
			}else{
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());			
			}
		} catch (Exception e) {
			logger.info("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param id
	 * @param modelName
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/queryUserSceneTemplateByUserIdAndModelName/{userId}/{modelName}", method = RequestMethod.GET)
	ResponseObject<List<TUserSceneTemplate>> queryUserSceneTemplateByUserIdAndModelName(
			@PathVariable(value = "userId") Integer userId, @PathVariable(value = "modelName") String modelName){
		ResponseObject<List<TUserSceneTemplate>> res = new ResponseObject<List<TUserSceneTemplate>>();
		try {
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(userSceneTemplateService.queryUserSceneTemplateByUserIdAndModelName(userId,modelName));
		} catch (Exception e) {
			logger.info("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param id
	 * @param modelName
	 * @param map
	 * @return  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateTemplate/{userId}/{modelName}", method = RequestMethod.PUT)
	ResponseObject updateTemplate(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "modelName") String modelName, @RequestBody Map<String, Object> map){
		ResponseObject res = new ResponseObject();
		try {
			if (map != null && !StringUtils.isEmpty(map.get("sceneModel").toString())
					&& !StringUtils.isEmpty(map.get("deviceModel").toString())) {
				String sceneModel = map.get("sceneModel").toString();
				String deviceModel = map.get("deviceModel").toString();
 				userSceneTemplateService.updateTemplate(userId,modelName,sceneModel,deviceModel);
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			}else{
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());			
			}
		} catch (Exception e) {
			logger.info("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	
	}
	/**  
	 * @param id
	 * @param modelName
	 * @return  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteTemplate/{userId}/{modelName}", method = RequestMethod.DELETE)
	ResponseObject deleteTemplate(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "modelName") String modelName){
		ResponseObject res = new ResponseObject();
		try {
			userSceneTemplateService.deleteTemplate(userId,modelName);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.info("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
		}
	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/queryTemplateByUserId/{userId}", method = RequestMethod.GET)
	ResponseObject<Map<String, Object>> queryTemplateByUserId(@PathVariable(value = "userId") Integer userId){
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			List<TUserSceneTemplate> list=userSceneTemplateService.queryTemplateByUserId(userId);
			map.put("templates", list);
			res.setData(map);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.info("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
		
	}
}
