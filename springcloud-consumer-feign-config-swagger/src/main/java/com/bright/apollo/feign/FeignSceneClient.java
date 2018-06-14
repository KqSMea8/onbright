package com.bright.apollo.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	@RequestMapping(value = "/scene/{sceneNumber}", method = RequestMethod.PUT)
	@SuppressWarnings("rawtypes")
	ResponseObject<SceneInfo> updateScene(@PathVariable(value = "sceneNumber") Integer sceneNumber, @RequestBody SceneInfo info);

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
	@RequestMapping(value = "/scene/addScene", method = RequestMethod.POST)
	ResponseObject<SceneInfo> addScene(@RequestBody SceneInfo info);

	/**
	 * @param sceneNumber
	 * @param condtionId
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/deleteSceneCondition/{sceneNumber}/{condtionId}", method = RequestMethod.DELETE)
	public ResponseObject deleteSceneCondition(@PathVariable(value = "sceneNumber") Integer sceneNumber, @PathVariable(value = "condtionId") Integer condtionId);

	/**
	 * @param sceneNumber
	 * @param actionId
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/scene/deleteSceneAction/{sceneNumber}/{actionId}", method = RequestMethod.DELETE)
	public ResponseObject deleteSceneAction(@PathVariable(value = "sceneNumber") Integer sceneNumber, @PathVariable(value = "actionId") Integer actionId);

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
}
