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
	public ResponseObject addJob(@PathVariable(value = "sceneNumber", required = true) Integer sceneNumber,
			@PathVariable(value = "sceneName", required = true) String sceneName,
			@PathVariable(value = "group", required = true) Integer group,
			@RequestParam(value = "cronString", required = true) String cronString);

	/**
	 * @param jobName
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/deleteJob/{jobName}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseObject deleteJob(@PathVariable(value = "jobName", required = true) String jobName);

	/**
	 * @param jobName
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/deleteJobTimer/{jobName}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseObject deleteJobTimer(@PathVariable(value = "jobName", required = true) String jobName);

	/**
	 * @param jobName
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/pauseJob/{jobName}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseObject pauseJob(@PathVariable(value = "jobName", required = true) String jobName);

	/**
	 * @param jobName
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/pauseJobTimer/{jobName}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseObject pauseJobTimer(@PathVariable(value = "jobName", required = true) String jobName);
	/**
	 * @param jobName
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/resumeJob/{jobName}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseObject resumeJob(@PathVariable(value = "jobName", required = true) String jobName);

	/**
	 * @param jobName
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/resumeJobTimer/{jobName}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseObject resumeJobTimer(@PathVariable(value = "jobName", required = true) String jobName);

	/**
	 * @param fingerRemoteUserId
	 * @param endTime
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/addRemoteOpenTaskSchedule/{fingerRemoteUserId}/{endTime}/{serialId}", method = RequestMethod.POST, produces = "application/json")
	public ResponseObject addRemoteOpenTaskSchedule(
			@PathVariable(value = "fingerRemoteUserId", required = true) int fingerRemoteUserId,
			@PathVariable(value = "endTime", required = true) String endTime,
			@PathVariable(value = "serialId", required = true) String serialId);

	/**
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/startTimerSchedule/{timerId}/{cronString}", method = RequestMethod.POST, produces = "application/json")
	ResponseObject startTimerSchedule(
			@PathVariable(value="timerId",required=true) Integer timerId,
			@PathVariable(value="cronString",required=true) String cronString);

	/**  
	 * @param locationId
	 * @param mobile
	 * @param endTime  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/checkIn/{locationId}/{mobile}/{endTime}", method = RequestMethod.POST, produces = "application/json")
	public ResponseObject checkIn(@PathVariable(value="locationId",required=true) Integer locationId, 
			@PathVariable(value="mobile",required=true)String mobile,
			@PathVariable(value="endTime",required=true)Long endTime);

	/**  
	 * @param locationId
	 * @param userName
	 * @param endTime  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/quartz/continueLocation/{locationId}/{mobile}/{endTime}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseObject continueLocation(@PathVariable(value="locationId",required=true) Integer locationId, 
			@PathVariable(value="mobile",required=true)String mobile,
			@PathVariable(value="endTime",required=true)Long endTime);

}
