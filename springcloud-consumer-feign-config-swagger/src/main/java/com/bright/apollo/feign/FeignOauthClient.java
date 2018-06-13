package com.bright.apollo.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.hrstrix.HystrixFeignOauthFallback;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月13日  
 *@Version:1.1.0  
 */
@FeignClient(name = "srpingcloud-provider-user",fallback = HystrixFeignOauthFallback.class, configuration = FeignConfig.class)
public interface FeignOauthClient {

	/**  
	 * @param oauthClientDetails
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(method = RequestMethod.POST)
	ResponseObject<OauthClientDetails> addOauthClientDetails(OauthClientDetails oauthClientDetails);

}
