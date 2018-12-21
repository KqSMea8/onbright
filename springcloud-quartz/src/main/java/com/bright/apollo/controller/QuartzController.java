package com.bright.apollo.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.QuartzService;
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月16日  
 *@Version:1.1.0  
 */
@RestController
@RequestMapping("quartz")
public class QuartzController {
	private static final Logger log = LoggerFactory.getLogger(QuartzController.class);
	@Autowired
    private QuartzService quartzService;
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addJob/{sceneNumber}/{sceneName}/{group}", method = RequestMethod.POST, produces = "application/json")
    public ResponseObject addJob(
    		@PathVariable(value="sceneNumber",required=true) Integer sceneNumber,
    		@PathVariable(value="sceneName",required=true) String sceneName,
    		@PathVariable(value="group",required=true) Integer group,
    		@RequestParam(value="cronString",required=true) String cronString
    		) {
		ResponseObject res=new ResponseObject();
		try {
			quartzService.startSchedule(sceneNumber, sceneName, cronString, group);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			log.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteJob/{jobName}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseObject deleteJob(@PathVariable(value="jobName",required=true) String jobName) {
		ResponseObject res=new ResponseObject();
		try {
			quartzService.deleteJob(jobName);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			log.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleteJobTimer/{jobName}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseObject deleteJobTimer(@PathVariable(value="jobName",required=true) String jobName) {
		ResponseObject res=new ResponseObject();
		try {
			quartzService.deleteJobTimer(jobName);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			log.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pauseJob/{jobName}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseObject pauseJob(@PathVariable(value="jobName",required=true) String jobName) {
		ResponseObject res=new ResponseObject();
		try {
			quartzService.pauseJob(jobName);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			log.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/pauseJobTimer/{jobName}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseObject pauseJobTimer(@PathVariable(value="jobName",required=true) String jobName) {
		ResponseObject res=new ResponseObject();
		try {
			quartzService.pauseJobTimer(jobName);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			log.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/resumeJob/{jobName}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseObject resumeJob(@PathVariable(value="jobName",required=true) String jobName) {
		ResponseObject res=new ResponseObject();
		try {
			quartzService.resumeJob(jobName);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			log.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/resumeJobTimer/{jobName}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseObject resumeJobTimer(@PathVariable(value="jobName",required=true) String jobName) {
		ResponseObject res=new ResponseObject();
		try {
			quartzService.resumeJobTimer(jobName);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			log.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	/**
	 * @param fingerRemoteUserId
	 * @param endTime
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addRemoteOpenTaskSchedule/{fingerRemoteUserId}/{endTime}/{serialId}", method = RequestMethod.POST, produces = "application/json")
	public ResponseObject addRemoteOpenTaskSchedule(
			@PathVariable(value = "fingerRemoteUserId", required = true) int fingerRemoteUserId,
			@PathVariable(value = "endTime", required = true) String endTime,
			@PathVariable(value = "serialId", required = true) String serialId){
		ResponseObject res=new ResponseObject();
		try {
			quartzService.startRemoteOpenTaskSchedule(fingerRemoteUserId, endTime, serialId);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			log.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/startTimerSchedule/{timerId}/{cronString}", method = RequestMethod.POST, produces = "application/json")
	public ResponseObject startTimerSchedule(
			@PathVariable(value="timerId",required=true) Integer timerId,
			@PathVariable(value="cronString",required=true) String cronString
	) {
		ResponseObject res=new ResponseObject();
		try {
			quartzService.startTimerSchedule(timerId,cronString);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			log.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	/**  
	 * @param locationId
	 * @param mobile
	 * @param endTime  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/checkIn/{locationId}/{mobile}/{endTime}", method = RequestMethod.POST, produces = "application/json")
	public ResponseObject checkIn(@PathVariable(value="locationId",required=true) Integer locationId, 
			@PathVariable(value="mobile",required=true)String mobile,
			@PathVariable(value="endTime",required=true)Long endTime){
		ResponseObject res=new ResponseObject();
		try {
			quartzService.checkIn(locationId,mobile,endTime);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			log.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	/**  
	 * @param locationId
	 * @param userName
	 * @param endTime  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/continueLocation/{locationId}/{mobile}/{endTime}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseObject continueLocation(@PathVariable(value="locationId",required=true) Integer locationId, 
			@PathVariable(value="mobile",required=true)String mobile,
			@PathVariable(value="endTime",required=true)Long endTime){
		ResponseObject res=new ResponseObject();
		try {
			quartzService.deleteHotelJob(locationId,mobile);
			quartzService.checkIn(locationId,mobile,endTime);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			log.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	
	}
}
