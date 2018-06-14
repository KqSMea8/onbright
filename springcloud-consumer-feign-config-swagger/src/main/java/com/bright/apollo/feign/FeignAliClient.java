package com.bright.apollo.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.hrstrix.HystrixFeignAli2Fallback;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月2日
 * @Version:1.1.0
 */
@FeignClient(name = "springcloud-ali2", fallback = HystrixFeignAli2Fallback.class, configuration = FeignConfig.class)
public interface FeignAliClient {

	@RequestMapping(value = "/aliService/toAli", method = RequestMethod.POST)
	ResponseObject<OboxResp> toAliService(@RequestParam(value = "cmd") CMDEnum cmd,
			@RequestParam(value = "inMsg") String inMsg, @RequestParam(value = "deviceSerial") String deviceSerial);
	
	/**
	 * @param oboxSerialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/aliService/stopScan/{oboxSerialId}", method = RequestMethod.DELETE)
	ResponseObject<OboxResp> stopScan(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId);

	/**
	 * @param oboxSerialId
	 * @param deviceType
	 * @param deviceChildType
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/aliService/scanByRestart/{oboxSerialId}", method = RequestMethod.POST)
	ResponseObject<OboxResp> scanByRestart(
			@PathVariable(value = "oboxSerialId", required = true) String oboxSerialId,
			@RequestParam(required=false,value="deviceType") String deviceType,
			@RequestParam(required=false,value="deviceChildType") String deviceChildType,
			@RequestParam(required=false,value="deviceChildType") String serialId);

	/**
	 * @param oboxSerialId
	 * @param deviceType
	 * @param deviceChildType
	 * @param serialId
	 * @param countOfDevice
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/aliService/scanByUnRestart/{oboxSerialId}", method = RequestMethod.POST)
	ResponseObject<OboxResp> scanByUnStop(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId,
			@RequestParam(required = false, value = "deviceType") String deviceType,
			@RequestParam(required = false, value = "deviceChildType") String deviceChildType,
			@RequestParam(required = false, value = "serialId") String serialId,
			@RequestParam(required = true, value = "countOfDevice") Integer countOfDevice);

	/**
	 * @param oboxSerialId
	 * @param deviceType
	 * @param deviceChildType
	 * @param serialId
	 * @param countOfDevice
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/aliService/scanByInitiative/{oboxSerialId}", method = RequestMethod.POST)
	ResponseObject<OboxResp> scanByInitiative(
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId,
			@RequestParam(required = false, value = "deviceType") String deviceType,
			@RequestParam(required = false, value = "deviceChildType") String deviceChildType,
			@RequestParam(required = false, value = "serialId") String serialId,
			@RequestParam(required = true, value = "countOfDevice") Integer countOfDevice);

	/**
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/aliService/controlServerScene/{sceneNumber}", method = RequestMethod.PUT)
	ResponseObject controlServerScene(
			@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber);


	/**  
	 * @param oboxSerialId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/aliService/release/{oboxSerialId}", method = RequestMethod.GET)
	ResponseObject<OboxResp> releaseObox(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId);

	/**  
	 * @param oboxSerialId
	 * @param status
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/aliService/setDeviceStatus/{oboxSerialId}", method = RequestMethod.PUT)
	ResponseObject<OboxResp> setDeviceStatus(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId, 
			@RequestParam(required = true, value = "status") String status);

}
