package com.bright.apollo.hrstrix;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.feign.FeignOauthClient;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月13日  
 *@Version:1.1.0  
 */
@Component
public class HystrixFeignOauthFallback extends BasicHystrixFeignFallback implements FeignOauthClient{
	private Logger logger = Logger.getLogger(getClass());
	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignOauthClient#addOauthClientDetails(com.bright.apollo.common.entity.OauthClientDetails)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OauthClientDetails> addOauthClientDetails(OauthClientDetails oauthClientDetails) {
		logger.warn("===oauth server is break===");
		return serverError();
	}

}
