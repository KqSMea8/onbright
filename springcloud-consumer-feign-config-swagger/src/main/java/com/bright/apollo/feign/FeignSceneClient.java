package com.bright.apollo.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.common.entity.TUserSceneTemplate;
import com.bright.apollo.hrstrix.HystrixFeignSceneFallback;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.response.SceneInfo;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月2日
 * @Version:1.1.0
 */
@FeignClient(name = "springcloud-provider-scene", fallback = HystrixFeignSceneFallback.class, configuration = FeignConfig.class)
public interface FeignSceneClient {

	/**
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/scene/{sceneNumber}")
	ResponseObject<SceneInfo> getScene(@PathVariable(value = "sceneNumber") Integer sceneNumber);

	/**
	 * @param sceneNumber
	 * @param info
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/updateScene", method = RequestMethod.PUT)
	ResponseObject<TScene> updateScene(@RequestBody(required = true) TScene scene);

	/**
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/{sceneNumber}", method = RequestMethod.DELETE)
	@SuppressWarnings("rawtypes")
	ResponseObject deleteScene(@PathVariable(value = "sceneNumber") Integer sceneNumber);

	/**
	 * @param info
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/addSceneInfo", method = RequestMethod.POST)
	ResponseObject<SceneInfo> addSceneInfo(@RequestBody SceneInfo info);

	/**
	 * @param sceneNumber
	 * @param condtionId
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/deleteSceneCondition/{sceneNumber}/{condtionId}", method = RequestMethod.DELETE)
	public ResponseObject deleteSceneCondition(@PathVariable(value = "sceneNumber") Integer sceneNumber,
			@PathVariable(value = "condtionId") Integer condtionId);

	/**
	 * @param sceneNumber
	 * @param actionId
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/deleteSceneAction/{sceneNumber}/{actionId}", method = RequestMethod.DELETE)
	public ResponseObject deleteSceneAction(@PathVariable(value = "sceneNumber") Integer sceneNumber,
			@PathVariable(value = "actionId") Integer actionId);

	/**
	 * @param userId
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	ResponseObject<List<SceneInfo>> getSceneByUserAndPage(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "pageIndex") Integer pageIndex, @PathVariable(value = "pageSize") Integer pageSize);

	/**
	 * @param info
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/addLocalScene", method = RequestMethod.POST)
	ResponseObject<SceneInfo> addLocalScene(@RequestBody(required = true) SceneInfo info);

	/**
	 * @param oboxSerialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/getScenesByOboxSerialId/{oboxSerialId}", method = RequestMethod.GET)
	ResponseObject<List<TScene>> getScenesByOboxSerialId(@PathVariable(value = "oboxSerialId") String oboxSerialId);

	/**
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/getSceneConditionsBySceneNumber/{sceneNumber}", method = RequestMethod.GET)
	ResponseObject<List<TSceneCondition>> getSceneConditionsBySceneNumber(
			@PathVariable(value = "sceneNumber") Integer sceneNumber);

	/**
	 * @param sceneNumber
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/deleteSceneConditionBySceneNumber/{sceneNumber}", method = RequestMethod.DELETE)
	ResponseObject deleteSceneConditionBySceneNumber(@PathVariable(value = "sceneNumber") Integer sceneNumber);

	/**
	 * @param sceneNumber
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/deleteSceneActionsBySceneNumber/{sceneNumber}", method = RequestMethod.DELETE)
	ResponseObject deleteSceneActionsBySceneNumber(@PathVariable(value = "sceneNumber") Integer sceneNumber);

	/**
	 * @param oboxSerialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/deleteSceneByOboxSerialId/{oboxSerialId}", method = RequestMethod.DELETE)
	ResponseObject deleteSceneByOboxSerialId(@PathVariable(value = "oboxSerialId") String oboxSerialId);

	/**
	 * @param tScene
	 * @Description:
	 */
	@RequestMapping(value = "/scene/addScene", method = RequestMethod.POST)
	ResponseObject<TScene> addScene(@RequestBody(required = true) TScene tScene);

	/**
	 * @param tSceneAction
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/addSceneAction", method = RequestMethod.POST)
	ResponseObject addSceneAction(@RequestBody(required = true) TSceneAction tSceneAction);

	/**
	 * @param tSceneCondition
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/addSceneCondition", method = RequestMethod.POST)
	ResponseObject addSceneCondition(@RequestBody(required = true) TSceneCondition tSceneCondition);

	/**
	 * @param oboxSerialId
	 * @param sceneNumber
	 * @Description:
	 */
	@RequestMapping(value = "/scene/getScenesByOboxSerialIdAndSceneNumber/{oboxSerialId}/{oboxSceneNumber}", method = RequestMethod.GET)
	ResponseObject<TScene> getScenesByOboxSerialIdAndOboxSceneNumber(
			@PathVariable(value = "oboxSerialId") String oboxSerialId,
			@PathVariable(value = "oboxSceneNumber") Integer oboxSceneNumber);

	/**
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/getSceneBySceneNumber/{sceneNumber}", method = RequestMethod.GET)
	ResponseObject<TScene> getSceneBySceneNumber(@PathVariable(value = "sceneNumber") Integer sceneNumber);

	/**
	 * @param sceneNumber
	 * @param conditionGroup
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/getSceneConditionsBySceneNumberAndConditionGroup/{sceneNumber}/{conditionGroup}", method = RequestMethod.GET)
	ResponseObject<List<TSceneCondition>> getSceneConditionsBySceneNumberAndConditionGroup(
			@PathVariable(value = "sceneNumber") Integer sceneNumber,
			@PathVariable(value = "conditionGroup") Integer conditionGroup);

	/**
	 * @param sceneNumber
	 * @Description:
	 */
	@RequestMapping(value = "/scene/getSceneActionsBySceneNumber/{sceneNumber}", method = RequestMethod.GET)
	ResponseObject<List<TSceneAction>> getSceneActionsBySceneNumber(
			@PathVariable(value = "sceneNumber") Integer sceneNumber);

	/**
	 * @param tSceneAction
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/updateSceneAction", method = RequestMethod.PUT)
	ResponseObject updateSceneAction(@RequestBody TSceneAction tSceneAction);

	/**
	 * @param sceneNumber
	 * @param actionId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/deleteSceneActionBySceneNumberAndActionId/{sceneNumber}/{actionId}", method = RequestMethod.DELETE)
	ResponseObject deleteSceneActionBySceneNumberAndActionId(@PathVariable(value = "sceneNumber") Integer sceneNumber,
			@PathVariable(value = "actionId") String actionId);

	/**
	 * @param userId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/getSceneByUserId/{userId}", method = RequestMethod.GET)
	ResponseObject<List<TScene>> getSceneByUserId(@PathVariable(value = "userId") Integer userId);

	/**
	 * @param userId
	 * @param start
	 * @param count
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/getSceneByUserIdAndPage/{userId}/{start}/{count}", method = RequestMethod.GET)
	ResponseObject<List<TScene>> getSceneByUserIdAndPage(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "start") int start, @PathVariable(value = "count") int count);

	/**
	 * @param serialId
	 * @param node
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/deleteSceneActionByActionId/{serialId}/{nodeType}", method = RequestMethod.DELETE)
	ResponseObject deleteSceneActionByActionId(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "nodeType") String nodeType);

	/**
	 * @param deviceSerialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/deleteSceneConditionBySerialId/{serialId}", method = RequestMethod.DELETE)
	ResponseObject deleteSceneConditionBySerialId(@PathVariable(value = "serialId") String serialId);

	/**
	 * @param userId
	 * @param oboxSerialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scene/querySceneByOboxSerialId/{oboxSerialId}", method = RequestMethod.GET)
	ResponseObject<Map<String, Object>> querySceneByOboxSerialId(
			@PathVariable(value = "oboxSerialId") String oboxSerialId);

	/**
	 * @param modelName
	 * @param sceneModel
	 * @param deviceModel
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/template/addTemplate/{userId}/{modelName}", method = RequestMethod.POST)
	ResponseObject addTemplate(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "modelName") String modelName, @RequestBody Map<String, Object> map);

	/**
	 * @param id
	 * @param modelName
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/template/queryUserSceneTemplateByUserIdAndModelName/{userId}/{modelName}", method = RequestMethod.GET)
	ResponseObject<List<TUserSceneTemplate>> queryUserSceneTemplateByUserIdAndModelName(
			@PathVariable(value = "userId") Integer userId, @PathVariable(value = "modelName") String modelName);

	/**  
	 * @param id
	 * @param modelName
	 * @param map
	 * @return  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/template/updateTemplate/{userId}/{modelName}", method = RequestMethod.PUT)
	ResponseObject updateTemplate(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "modelName") String modelName, @RequestBody Map<String, Object> map);


	/**  
	 * @param id
	 * @param modelName
	 * @return  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/template/deleteTemplate/{userId}/{modelName}", method = RequestMethod.DELETE)
	ResponseObject deleteTemplate(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "modelName") String modelName);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/template/queryTemplateByUserId/{userId}", method = RequestMethod.GET)
	ResponseObject<Map<String, Object>> queryTemplateByUserId(@PathVariable(value = "userId") Integer userId);

}
