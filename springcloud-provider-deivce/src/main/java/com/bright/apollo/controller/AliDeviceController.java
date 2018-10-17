package com.bright.apollo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.common.entity.TUserAliDevice;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.AliDeviceConfigService;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年10月17日
 * @Version:1.1.0
 */
@RequestMapping("ali")
@RestController
public class AliDeviceController {
	private static final Logger logger = LoggerFactory.getLogger(AliDeviceController.class);
	@Autowired
	private AliDeviceConfigService aliDeviceConfigService;

	@RequestMapping(value = "/queryAliDevConfigBySerial/{deviceId}", method = RequestMethod.GET)
	public ResponseObject<TAliDeviceConfig> getDevice(
			@PathVariable(required = true, value = "deviceId") String deviceId) {
		ResponseObject<TAliDeviceConfig> res = new ResponseObject<TAliDeviceConfig>();
		try {
			TAliDeviceConfig device = aliDeviceConfigService.getAliDeviceConfigBySerializeId(deviceId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(device);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===queryAliDevConfigBySerial error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param tAliDeviceConfig  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addAliDevConfig", method = RequestMethod.POST)
	ResponseObject addAliDevConfig(@RequestBody TAliDeviceConfig tAliDeviceConfig){
		ResponseObject res = new ResponseObject();
		try {
			aliDeviceConfigService.addAliDevConfig(tAliDeviceConfig);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===addAliDevConfig error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param tAliDeviceConfig  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateAliDevConfig", method = RequestMethod.PUT)
	ResponseObject updateAliDevConfig(@RequestBody TAliDeviceConfig tAliDeviceConfig){
		ResponseObject res = new ResponseObject();
		try {
			aliDeviceConfigService.update(tAliDeviceConfig);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===updateAliDevConfig error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	 
}
