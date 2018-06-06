package com.bright.apollo.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.hrstrix.HystrixFeignAli2Fallback;
import com.bright.apollo.response.ResponseObject;


/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月2日  
 *@Version:1.1.0  
 */
@FeignClient(name = "springcloud-ali2",fallback = HystrixFeignAli2Fallback.class, configuration = FeignConfig.class)
public interface FeignAliClient {

    @RequestMapping(value = "/aliService/toAli", method = RequestMethod.POST)
    ResponseObject<OboxResp> toAliService(@PathVariable(value="cmd") CMDEnum cmd, @PathVariable(value="inMsg") String inMsg, @PathVariable(value="deviceSerial") String deviceSerial);

    @RequestMapping(value = "/aliService/release/{oboxSerialId}", method = RequestMethod.GET)
	ResponseObject<OboxResp> releaseObox(@PathVariable(required=true,value="oboxSerialId") String oboxSerialId) ;

	/**  
	 * @param oboxSerialId
	 * @return  
	 * @Description:  
	 */
    @RequestMapping(value = "/aliService/stopScan/{oboxSerialId}", method = RequestMethod.DELETE)
	ResponseObject<OboxResp> stopScan(@PathVariable(required=true,value="oboxSerialId") String oboxSerialId);
    
    
    
}
