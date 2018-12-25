package com.bright.apollo.feign;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.hrstrix.HystrixFeignAli2Fallback;
import com.bright.apollo.request.CmdInfo;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年10月31日
 * @Version:1.1.0
 */
@FeignClient(name = "springcloud-ali2", fallback = HystrixFeignAli2Fallback.class, configuration = FeignConfig.class)
public interface FeignAliClient {

	/**
	 * @param obox
	 * @param setGroup
	 * @param setBytes
	 * @Description:
	 */
	@RequestMapping(value = "/aliService/sendCmd", method = RequestMethod.POST)
	ResponseObject<OboxResp> sendCmd(@RequestBody(required = true) Map<String, Object> map);

}
