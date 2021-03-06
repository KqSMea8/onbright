package com.bright.apollo.hrstrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.feign.FeignQuartzClient;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Component
public class HystrixFeignQuartzFallback extends BasicHystrixFeignFallback implements FeignQuartzClient {
	private static final Logger logger = LoggerFactory.getLogger(HystrixFeignQuartzFallback.class);

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignQuartzClient#addJob(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addJob(Integer sceneNumber, String sceneName, Integer group, String cronString) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignQuartzClient#deleteJob(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteJob(String jobName) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

	@Override
	public ResponseObject deleteJobTimer(String jobName) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignQuartzClient#pauseJob(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject pauseJob(String jobName) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

	@Override
	public ResponseObject pauseJobTimer(String jobName) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignQuartzClient#resumeJob(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject resumeJob(String jobName) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

	@Override
	public ResponseObject resumeJobTimer(String jobName) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignQuartzClient#addRemoteOpenTaskSchedule(int, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addRemoteOpenTaskSchedule(int fingerRemoteUserId, String endTime, String serialId) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject startTimerSchedule(Integer timerId, String cronString) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignQuartzClient#checkIn(java.lang.Integer, java.lang.String, java.lang.Long)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject checkIn(Integer locationId, String mobile, Long endTime) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignQuartzClient#continueLocation(java.lang.Integer, java.lang.String, java.lang.Long)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject continueLocation(Integer locationId, String mobile, Long endTime) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

}