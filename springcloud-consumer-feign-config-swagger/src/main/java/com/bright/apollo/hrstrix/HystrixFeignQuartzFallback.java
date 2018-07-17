package com.bright.apollo.hrstrix;

import org.apache.log4j.Logger;
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
	private Logger logger = Logger.getLogger(getClass());

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignQuartzClient#addJob(java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addJob(Integer sceneNumber, String sceneName, Integer group, String cronString) {
		logger.warn("===quartz server is break===");
		return serverError();
	}

}