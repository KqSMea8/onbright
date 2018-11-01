package com.bright.apollo.hrstrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.feign.FeignAliClient;
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
	 * @see com.bright.apollo.feign.FeignAliClient#sendCmd(com.bright.apollo.common.entity.TObox, com.bright.apollo.enums.CMDEnum, byte[])  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> sendCmd(TObox obox, CMDEnum cmd, byte[] setBytes) {
		logger.warn("===ali server is break===");
		return serverError();
	}

}
