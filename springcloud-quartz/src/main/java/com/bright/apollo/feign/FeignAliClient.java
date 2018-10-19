package com.bright.apollo.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.config.FeignConfig;
import com.bright.apollo.hystrix.HystrixFeignAli2Fallback;
import com.bright.apollo.response.ResponseObject;
 
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月17日  
 *@Version:1.1.0  
 */
@FeignClient(name = "springcloud-ali2", fallback = HystrixFeignAli2Fallback.class, configuration = FeignConfig.class)
public interface FeignAliClient {

	/**  
	 * @param sceneNumber  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/aliScene/addSceneAction/{sceneNumber}", method = RequestMethod.POST)
	ResponseObject addSceneAction(@PathVariable(value = "sceneNumber")Integer sceneNumber);

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/aliScene/addTimerAction/{timerId}", method = RequestMethod.POST)
	ResponseObject addTimerAction(@PathVariable(value = "timerId")Integer timerId);
	
}