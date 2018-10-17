package com.bright.apollo.feign;

import java.util.List;
import java.util.Map;

import com.bright.apollo.common.entity.TAliDevTimer;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.hrstrix.HystrixFeignAli2Fallback;
import com.bright.apollo.request.SceneActionDTO;
import com.bright.apollo.request.SceneConditionDTO;
import com.bright.apollo.response.AliDevInfo;
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
	ResponseObject<OboxResp> scanByRestart(@PathVariable(value = "oboxSerialId", required = true) String oboxSerialId,
			@RequestParam(required = false, value = "deviceType") String deviceType,
			@RequestParam(required = false, value = "deviceChildType") String deviceChildType,
			@RequestParam(required = false, value = "deviceChildType") String serialId);

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
	ResponseObject controlServerScene(@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber);

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
	 * @param string
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/aliService/setDeviceStatus/{oboxSerialId}", method = RequestMethod.PUT)
	ResponseObject<OboxResp> setDeviceStatus(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId,
			@RequestParam(required = true, value = "status") String status,
			@RequestParam(required = true, value = "rfAddr") String rfAddr);

	/**
	 * @param sceneName
	 * @param sceneGroup
	 * @param string
	 * @Description:
	 */
	@RequestMapping(value = "/aliService/addLocalScene", method = RequestMethod.POST)
	ResponseObject<OboxResp> addLocalScene(@RequestParam(required = true, value = "sceneName") String sceneName,
			@RequestParam(required = true, value = "oboxSerialId") String oboxSerialId,
			@RequestParam(required = false, value = "sceneGroup") String sceneGroup);

	/**
	 * @param sceneNumber
	 * @param oboxSerialId
	 * @param sceneConditionDTOs
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/aliService/addLocalSceneCondition/{sceneNumber}/{oboxSerialId}", method = RequestMethod.POST)
	ResponseObject<OboxResp> addLocalSceneCondition(
			@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber,
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId,
			@RequestBody(required = true) List<List<SceneConditionDTO>> sceneConditionDTOs);

	/**
	 * @param type
	 * @param zone
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/aliDevice/registAliDev/{type}", method = RequestMethod.GET)
	ResponseObject<AliDevInfo> registAliDev(@PathVariable(required = true, value = "type") String type,
			@RequestParam(required = false, value = "zone") String zone);

	/**
	 * @param nodeActionDTOs
	 * @param sceneNumber
	 * @param oboxSerialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/aliService/addLocalSceneAction/{sceneNumber}/{oboxSerialId}", method = RequestMethod.POST)
	ResponseObject addLocalSceneAction(@RequestBody(required = true) List<SceneActionDTO> nodeActionDTOs,
			@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber,
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId);

	/**
	 * @param data
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/aliDevice/getSearchNewDevice", method = RequestMethod.PUT)
	ResponseObject<List<Map<String, String>>> getSearchNewDevice(@RequestBody(required = true) TObox obox);

	/**
	 * 
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/aliScene/setLocalScene/{sceneStatus}/{oboxSceneNumber}/{sceneName}/{oboxSerialId}", method = RequestMethod.PUT)
	ResponseObject setLocalScene(@PathVariable(value = "sceneStatus") Byte sceneStatus,
			@PathVariable(value = "oboxSceneNumber") Integer oboxSceneNumber,
			@PathVariable(value = "sceneName") String sceneName,
			@PathVariable(value = "oboxSerialId") String oboxSerialId,
			@RequestParam(required = false, value = "groupAddr") String groupAddr);

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/aliScene/modifyLocalSceneCondition/{oboxSceneNumber}/{oboxSerialId}/{userId}", method = RequestMethod.PUT)
	ResponseObject modifyLocalSceneCondition(@PathVariable(value = "oboxSceneNumber") Integer oboxSceneNumber,
			@PathVariable(value = "oboxSerialId") String oboxSerialId, @PathVariable(value = "userId") Integer userId,
			@RequestBody List<List<SceneConditionDTO>> sceneConditionDTOs);

	/**
	 * @param oboxSceneNumber
	 * @param sceneName
	 * @param oboxSerialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/aliScene/deleteLocalScene/{oboxSceneNumber}/{sceneName}/{oboxSerialId}", method = RequestMethod.DELETE)
	ResponseObject deleteLocalScene(@PathVariable(value = "oboxSceneNumber") Integer oboxSceneNumber,
			@PathVariable(value = "sceneName") String sceneName,
			@PathVariable(value = "oboxSerialId") String oboxSerialId);

	/**
	 * @param oboxSceneNumber
	 * @param sceneName
	 * @param oboxSerialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/aliScene/excuteLocalScene/{oboxSceneNumber}/{sceneName}/{oboxSerialId}", method = RequestMethod.PUT)
	ResponseObject excuteLocalScene(@PathVariable(value = "oboxSceneNumber") Integer oboxSceneNumber,
			@PathVariable(value = "sceneName") String sceneName,
			@PathVariable(value = "oboxSerialId") String oboxSerialId);

	/**
	 * @param sceneNumber
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/aliScene/addSceneAction/{sceneNumber}", method = RequestMethod.POST)
	ResponseObject addSceneAction(@PathVariable(value = "sceneNumber") Integer sceneNumber);

	/**
	 * @param oboxSceneNumber
	 * @param sceneName
	 * @param oboxSerialId
	 * @param sceneStatus
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/aliScene/enableLocalScene/{oboxSceneNumber}/{sceneName}/{oboxSerialId}/{sceneStatus}", method = RequestMethod.PUT)
	ResponseObject excuteLocalScene(@PathVariable(value = "oboxSceneNumber") Integer oboxSceneNumber,
			@PathVariable(value = "sceneName") String sceneName,
			@PathVariable(value = "oboxSerialId") String oboxSerialId,
			@PathVariable(value = "sceneStatus") String sceneStatus);

	/**
	 * @param oboxSerialId
	 * @param name
	 * @param address
	 * @Description:
	 */
	@RequestMapping(value = "/aliService/modifyDeviceName/{oboxSerialId}/{name}/{address}", method = RequestMethod.PUT)
	ResponseObject<OboxResp> modifyDeviceName(@PathVariable(value = "oboxSerialId") String oboxSerialId,
			@PathVariable(value = "name") String name, @PathVariable(value = "address") String address);

	/**
	 * @param oboxSerialId
	 * @param deviceRfAddr
	 * @Description:
	 */
	@RequestMapping(value = "/aliService/deleteDevice/{oboxSerialId}/{address}/{deviceName}", method = RequestMethod.PUT)
	ResponseObject<OboxResp> deleteDevice(@PathVariable(value = "oboxSerialId") String oboxSerialId,
			@PathVariable(value = "address") String address, @PathVariable(value = "deviceName") String deviceName);

	/**
	 * 红外进入学习码
	 * 
	 * @param object
	 * @return
	 */
	@RequestMapping(value = "/aliDevice/sendlearn", method = RequestMethod.POST)
	ResponseObject<List<Map<String, String>>> sendLearn2IR(@RequestBody(required = true) Object object);

	/**
	 * @param operation
	 * @param data
	 * @param deviceConfig
	 * @param startTime
	 * @param endTime
	 * @param times
	 * @param userSerialId
	 * @param randomNum
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/aliService/sendMessageToFinger/{operation}/{startTime}/{endTime}/{times}/{userSerialId}/{randomNum}/{serialId}/{address}", method = RequestMethod.POST)
	ResponseObject<OboxResp> sendMessageToFinger(@PathVariable(value = "operation") String operation, 
			@PathVariable(value = "startTime") String startTime,
			@PathVariable(value = "endTime") String endTime, @PathVariable(value = "times") String times,
			@PathVariable(value = "userSerialId") Integer userSerialId,
			@PathVariable(value = "randomNum") String randomNum,
			@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "address") String address
			);

	/**  
	 * @param deviceConfig  
	 * @Description:  
	 */
	@RequestMapping(value = "/aliService/getRealNodeStatus", method = RequestMethod.PUT)
	ResponseObject<OboxResp> getRealNodeStatus(@RequestBody TOboxDeviceConfig deviceConfig);

	/**  
	 * @param serialId  
	 * @Description:  
	 */
	@RequestMapping(value = "/aliService/deleteObox/{serialId}", method = RequestMethod.DELETE)
	ResponseObject<OboxResp>  deleteObox(@PathVariable(value = "serialId") String serialId);

	/**  
	 * @param serialId  
	 * @Description:  
	 */
	@RequestMapping(value = "/aliService/addRemoteLed/{serialId}", method = RequestMethod.POST)
	ResponseObject<OboxResp> addRemoteLed(@PathVariable(value = "serialId")String serialId);

	/**  
	 * @param oboxSerialId  
	 * @Description:  
	 */
	@RequestMapping(value = "/aliService/delRemoteLed/{serialId}", method = RequestMethod.DELETE)
	ResponseObject<OboxResp> delRemoteLed(@PathVariable(value = "serialId")String serialId);

	/**  
	 * @param oboxSerialId
	 * @param status
	 * @Description:
	 */
	@RequestMapping(value = "/aliService/controlRemoteLed/{serialId}/{status}", method = RequestMethod.PUT)
	ResponseObject<OboxResp> controlRemoteLed(@PathVariable(value = "serialId")String serialId, 
			@PathVariable(value = "status")String status);

	/**
	 * @Description:阿里WiFi设备查询
	 */
	@RequestMapping(value = "/aliService/queryAliDevice/{userId}", method = RequestMethod.GET)
	ResponseObject queryAliDevice(@PathVariable(required = true, value = "userId") Integer userId);

	/**
	 * @Description:设置阿里WiFi
	 */
	@RequestMapping(value = "/aliService/setAliDevice", method = RequestMethod.POST)
	ResponseObject setAliDevice(@RequestParam(required = true, value = "value") Object value,@RequestParam(required = true, value = "deviceId") String deviceId);

	/**
	 * @Description:读取阿里WiFi
	 */
	@RequestMapping(value = "/aliService/readAliDevice", method = RequestMethod.POST)
	ResponseObject readAliDevice(@RequestParam(required = true, value = "functionId") String functionId,@RequestParam(required = true, value = "deviceId") String deviceId,@RequestParam(required = true, value = "value") String value);

	/**
	 * @Description:查询阿里WiFi Timer
	 */
	@RequestMapping(value = "/aliService/queryAliDeviceTimer/{deviceId}", method = RequestMethod.GET)
	ResponseObject queryAliDeviceTimer(@PathVariable(required = true, value = "deviceId") String deviceId);

	/**
	 * @Description:倒计时阿里WiFi Timer
	 */
	@RequestMapping(value = "/aliService/setAliCountdown/{deviceId}/{command}/{timer}/{timerValue}", method = RequestMethod.GET)
	ResponseObject setAliCountdown(@PathVariable(value = "deviceId") String deviceId,
								   @PathVariable(value = "command") String command,
								   @PathVariable(value = "timer") String timer,
								   @PathVariable(value = "timerValue") String timerValue);

	/**
	 * @Description:阿里WiFi 计时器Timer
	 */
	@RequestMapping(value = "/aliService/setAliTimer/{deviceId}/{command}/{timer}/{timerValue}", method = RequestMethod.GET)
	ResponseObject setAliTimer(@PathVariable(value = "deviceId") String deviceId,
								   @PathVariable(value = "command") String command,
								   @PathVariable(value = "timer") String timer,
								   @PathVariable(value = "timerValue") String timerValue);

	/**
	 * @Description:上传阿里WiFi
	 */
	@RequestMapping(value = "/aliService/uploadAliDevice", method = RequestMethod.POST)
	ResponseObject uploadAliDevice(@RequestParam(required = true,value = "deviceName") String deviceName,
							   @RequestParam(required = true,value = "productKey") String productKey,@RequestParam(required = true, value = "config") Object config,@RequestParam(required = true,value = "userId") Integer userId);


	/**
	 * @Description:通过序列号查询TImer
	 */
	@RequestMapping(value = "/aliService/getAliDevTimerByDeviceSerialIdAndCountDown/{deviceId}", method = RequestMethod.GET)
	ResponseObject getAliDevTimerByDeviceSerialIdAndCountDown(@PathVariable(value = "deviceId") String deviceId);

	/**
	 * @Description:通过序列号查询TImer
	 */
	@RequestMapping(value = "/aliService/delAliDevTimerByDeviceSerialId/{id}", method = RequestMethod.GET)
	ResponseObject delAliDevTimerByDeviceSerialId(@PathVariable(value = "id") Integer id);

	/**
	 * @Description:新增TImer
	 */
	@RequestMapping(value = "/aliService/addAliDevTimer", method = RequestMethod.POST)
	ResponseObject addAliDevTimer(@RequestParam(required = true, value = "aliDevTimer") TAliDevTimer aliDevTimer);

	/**
	 * @Description:新增TImer
	 */
	@RequestMapping(value = "/aliService/updateAliDevTimer", method = RequestMethod.POST)
	ResponseObject updateAliDevTimer(@RequestParam(required = true, value = "aliDevTimer") TAliDevTimer aliDevTimer);

	/**
	 * @Description:通过序列号查询TImer
	 */
	@RequestMapping(value = "/aliService/delAliDevice", method = RequestMethod.POST)
	ResponseObject delAliDevice(@RequestParam(required = true, value = "value") Object value,@RequestParam(required = true, value = "deviceId") String deviceId);
}
