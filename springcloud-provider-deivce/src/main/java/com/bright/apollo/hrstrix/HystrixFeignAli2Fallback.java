package com.bright.apollo.hrstrix;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.request.CmdInfo;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月31日  
 *@Version:1.1.0  
 */
@Component
public class HystrixFeignAli2Fallback extends BasicHystrixFeignFallback implements FeignAliClient {
	private static final Logger logger = LoggerFactory.getLogger(HystrixFeignAli2Fallback.class);

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#sendCmd(java.util.Map)  
	 */
	@Override
	public ResponseObject<OboxResp> sendCmd(@RequestBody(required = true) Map<String, Object> map) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	 

}
