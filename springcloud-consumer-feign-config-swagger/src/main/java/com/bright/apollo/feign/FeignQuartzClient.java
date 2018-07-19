package com.bright.apollo.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bright.apollo.hrstrix.HystrixFeignQuartzFallback;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月2日
 * @Version:1.1.0
 */
@FeignClient(name = "springcloud-quartz", fallback = HystrixFeignQuartzFallback.class, configuration = FeignConfig.class)
public interface FeignQuartzClient {
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/addJob/{sceneNumber}/{sceneName}/{group}", method = RequestMethod.POST, produces = "application/json")
    public ResponseObject addJob(
    		@PathVariable(value="sceneNumber",required=true) Integer sceneNumber,
    		@PathVariable(value="sceneName",required=true) String sceneName,
    		@PathVariable(value="group",required=true) Integer group,
    		@RequestParam(value="cronString",required=true) String cronString
    		);

	/**  
	 * @param jobName  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/deleteJob/{jobName}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseObject deleteJob(@PathVariable(value="jobName",required=true) String jobName);

 
}
