package com.bright.apollo.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月17日  
 *@Version:1.1.0  
 */
@Component
public class HystrixFeignAli2Fallback extends BasicHystrixFeignFallback implements FeignAliClient{
	private static final Logger logger = LoggerFactory.getLogger(HystrixFeignAli2Fallback.class);
	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#addSceneAction(java.lang.Integer)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addSceneAction(Integer sceneNumber) {
		logger.warn("===ali server is break===");
		return serverError();
	}

}
