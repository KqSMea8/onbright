package com.bright.apollo.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerAuth;
import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerUser;
import com.bright.apollo.common.entity.TNvr;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TYSCamera;
import com.bright.apollo.hrstrix.HystrixFeignDeviceFallback;
import com.bright.apollo.request.IntelligentFingerWarnDTO;
import com.bright.apollo.request.IntelligentOpenRecordDTO;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月2日
 * @Version:1.1.0
 */
@FeignClient(value = "springcloud-provider-device", fallback = HystrixFeignDeviceFallback.class, configuration = FeignConfig.class)
public interface FeignDeviceClient {
	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.GET)
	ResponseObject<TOboxDeviceConfig> getDevice(@PathVariable(value = "serialId") String serialId);

	/**
	 * @param serialId
	 * @param device
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.PUT)
	ResponseObject<TOboxDeviceConfig> updateDevice(@PathVariable(value = "serialId") String serialId,
			@RequestBody TOboxDeviceConfig device);

	/**
	 * @param serialId
	 * @param device
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.POST)
	ResponseObject<TOboxDeviceConfig> addDevice(@PathVariable(value = "serialId") String serialId,
			@RequestBody TOboxDeviceConfig device);

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.DELETE)
	ResponseObject delDevice(@PathVariable(value = "serialId") String serialId);

	/**
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/device/deleleDeviceByOboxSerialId/{serialId}", method = RequestMethod.DELETE)
	ResponseObject deleleDeviceByOboxSerialId(@PathVariable(value = "serialId") String serialId);

	/**
	 * @param id
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/device/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getDeviceByUserAndPage(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "pageIndex") Integer pageIndex, @PathVariable(value = "pageSize") Integer pageSize);

	@RequestMapping(value = "/device/getOboxDeviceConfigByUserId/{userId}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getOboxDeviceConfigByUserId(@PathVariable(value = "userId") Integer userId);

	/**
	 * @param oboxSerialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/device/getDevicesByOboxSerialId/{oboxSerialId}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getDevicesByOboxSerialId(
			@PathVariable(value = "oboxSerialId") String oboxSerialId);

	/**
	 * @param id
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/device/getDeviceByUser/{userId}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getDeviceByUser(@PathVariable(value = "userId") Integer userId);

	/**
	 * @param deviceSerialId
	 * @Description:
	 */
	@RequestMapping(value = "/device/getYSCameraBySerialId/{deviceSerialId}", method = RequestMethod.GET)
	ResponseObject<TYSCamera> getYSCameraBySerialId(@PathVariable(value = "deviceSerialId") String deviceSerialId);

	/**
	 * @param deviceSerialId
	 * @Description:
	 */
	@RequestMapping(value = "/device/getNvrByIP/{ip}", method = RequestMethod.GET)
	ResponseObject<TNvr> getNvrByIP(@PathVariable(value = "ip") String ip);

	/**
	 * @param id
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/device/getDeviceTypeByUser/{userId}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getDeviceTypeByUser(@PathVariable(value = "userId") Integer userId);

	/**
	 * @param id
	 * @param deviceType
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/device/getDevciesByUserIdAndType/{userId}/{deviceType}", method = RequestMethod.GET)
	ResponseObject<List<TOboxDeviceConfig>> getDevciesByUserIdAndType(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "deviceType") String deviceType);

	/**
	 * @param id
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/device/getYSCameraByUserId/{userId}", method = RequestMethod.GET)
	ResponseObject<List<TYSCamera>> getYSCameraByUserId(@PathVariable(value = "userId") Integer userId);

	/**
	 * @param userId
	 * @param serialID
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/device/getDeviceByUserAndSerialId/{userId}/{serialID}", method = RequestMethod.GET)
	ResponseObject<TOboxDeviceConfig> getDeviceByUserAndSerialId(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "serialID") String serialID);

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/countFingerAuth/{serialId}", method = RequestMethod.GET)
	ResponseObject<Integer> countFingerAuth(@PathVariable(value = "serialId") String serialId);

	/**
	 * @param serialId
	 * @param end
	 * @param start
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/queryIntelligentOpenRecordByDate/{serialId}/{end}/{start}", method = RequestMethod.GET)
	ResponseObject<List<IntelligentOpenRecordDTO>> queryIntelligentOpenRecordByDate(
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "end") String end,
			@PathVariable(value = "start") String start);

	/**
	 * @param serialId
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getCountIntelligentWarnBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<Integer> getCountIntelligentWarnBySerialId(@PathVariable(value = "serialId") String serialId);

	/**
	 * @param serialId
	 * @param end
	 * @param start
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getIntelligentWarnByDate/{serialId}/{end}/{start}", method = RequestMethod.GET)
	ResponseObject<List<IntelligentFingerWarnDTO>> getIntelligentWarnByDate(
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "end") String end,
			@PathVariable(value = "start") String start);

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getCountIntelligentUserBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<Integer> getCountIntelligentUserBySerialId(@PathVariable(value = "serialId") String serialId);

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getIntelligentUserBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<List<TIntelligentFingerUser>> getIntelligentUserBySerialId(
			@PathVariable(value = "serialId") String serialId);

	/**
	 * @param serialId
	 * @param pin
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getIntelligentFingerUserBySerialIdAndPin/{serialId}/{pin}", method = RequestMethod.GET)
	ResponseObject<TIntelligentFingerUser> getIntelligentFingerUserBySerialIdAndPin(
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "pin") String pin);

	/**
	 * @param fingerUser
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/intelligentFinger/updatentelligentFingerUser", method = RequestMethod.PUT)
	ResponseObject updatentelligentFingerUser(@RequestBody TIntelligentFingerUser fingerUser);

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getIntelligentAuthBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<TIntelligentFingerAuth> getIntelligentAuthBySerialId(
			@PathVariable(value = "serialId") String serialId);

	/**
	 * @param auth
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/intelligentFinger/addIntelligentFingerAuth", method = RequestMethod.POST)
	ResponseObject addIntelligentFingerAuth(@RequestBody TIntelligentFingerAuth auth);

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getIntelligentFingerRemoteUsersBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<List<TIntelligentFingerRemoteUser>> getIntelligentFingerRemoteUsersBySerialId(
			@PathVariable(value = "serialId") String serialId);

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getIntelligentFingerRemoteUsersBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<List<TIntelligentFingerAbandonRemoteUser>> getTIntelligentFingerAbandonRemoteUsersBySerialId(
			@PathVariable(value = "serialId") String serialId);

	/**
	 * @param id
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/intelligentFinger/delIntelligentFingerAbandonRemoteUserById/{id}", method = RequestMethod.DELETE)
	ResponseObject delIntelligentFingerAbandonRemoteUserById(@PathVariable(value = "id") Integer id);

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getTIntelligentFingerRemoteUsersBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<List<TIntelligentFingerRemoteUser>> getTIntelligentFingerRemoteUsersBySerialId(
			@PathVariable(value = "serialId") String serialId);

	/**
	 * @param fingerRemoteUser
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/addTIntelligentFingerRemoteUser", method = RequestMethod.POST)
	ResponseObject<Integer> addTIntelligentFingerRemoteUser(
			@RequestBody(required = true) TIntelligentFingerRemoteUser fingerRemoteUser);

	/**
	 * @param fingerRemoteUserId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getIntelligentFingerRemoteUserById/{fingerRemoteUserId}", method = RequestMethod.GET)
	ResponseObject<TIntelligentFingerRemoteUser> getIntelligentFingerRemoteUserById(
			@PathVariable(value = "fingerRemoteUserId") int fingerRemoteUserId);

	/**
	 * @param id
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/intelligentFinger/delTIntelligentFingerRemoteUserById/{id}", method = RequestMethod.DELETE)
	ResponseObject delTIntelligentFingerRemoteUserById(@PathVariable(value = "id") int id);

	/**
	 * @param fingerAuth
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/intelligentFinger/updateTintelligentFingerAuth", method = RequestMethod.PUT)
	ResponseObject updateTintelligentFingerAuth(@RequestBody TIntelligentFingerAuth fingerAuth);

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getTIntelligentFingerPushsBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<List<TIntelligentFingerPush>> getTIntelligentFingerPushsBySerialId(
			@PathVariable(value = "serialId") String serialId);

	/**
	 * @param fingerRemoteUser
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/intelligentFinger/updateTIntelligentFingerRemoteUser", method = RequestMethod.PUT)
	ResponseObject updateTIntelligentFingerRemoteUser(@RequestBody TIntelligentFingerRemoteUser fingerRemoteUser);

	/**
	 * @param serialId
	 * @param pin
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/intelligentFinger/getTIntelligentFingerRemoteUserBySerialIdAndPin/{serialId}/{pin}", method = RequestMethod.GET)
	ResponseObject<TIntelligentFingerRemoteUser> getTIntelligentFingerRemoteUserBySerialIdAndPin(
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "pin") int pin);

	
	/**
	 * @param mobile
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/intelligentFinger/updateTIntelligentFingerPushMobileBySerialId/{mobile}/{serialId}", method = RequestMethod.PUT)
	ResponseObject updateTIntelligentFingerPushMobileBySerialId(@PathVariable(value = "mobile") String mobile,
			@PathVariable(value = "serialId") String serialId);

	/**  
	 * @param enable
	 * @param serialId
	 * @param value  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/intelligentFinger/updateTIntelligentFingerPushEnableBySerialIdAndValue/{enable}/{serialId}/{value}", method = RequestMethod.PUT)
	ResponseObject updateTIntelligentFingerPushEnableBySerialIdAndValue(
			@PathVariable(value = "enable")Integer enable, 
			@PathVariable(value = "serialId")String serialId,
			@PathVariable(value = "value")Integer value);

}
