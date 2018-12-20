package com.bright.apollo.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.common.entity.TDeviceStatus;
import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerAuth;
import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerUser;
import com.bright.apollo.common.entity.TLocation;
import com.bright.apollo.common.entity.TNvr;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TServerGroup;
import com.bright.apollo.common.entity.TUserLocation;
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
	@RequestMapping(value = "/intelligentFinger/getTIntelligentFingerAbandonRemoteUsersBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<List<TIntelligentFingerAbandonRemoteUser>> getTIntelligentFingerAbandonRemoteUsersBySerialId(
			@PathVariable(value = "serialId") String serialId);

	/**
	 * @param id
	 * @Description:
	 */
	@Deprecated
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
	ResponseObject updateTIntelligentFingerPushEnableBySerialIdAndValue(@PathVariable(value = "enable") Integer enable,
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "value") Integer value);

	/**
	 * @param serialid
	 * @param userSerialid
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/intelligentFinger/delIntelligentFingerAbandonRemoteUserBySerialIdAndPin/{serialId}/{pin}", method = RequestMethod.PUT)
	ResponseObject delIntelligentFingerAbandonRemoteUserBySerialIdAndPin(
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "pin") Integer pin);

	/**
	 * @param serialId
	 * @param start
	 * @param count
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/device/getDeviceStatusByCount/{serialId}/{start}/{count}", method = RequestMethod.GET)
	ResponseObject<List<TDeviceStatus>> getDeviceStatusByCount(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "start") int start, @PathVariable(value = "count") int count);

	/**
	 * @param serialId
	 * @param from
	 * @param to
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/device/getDeviceStatusByData/{serialId}/{from}/{to}", method = RequestMethod.GET)
	ResponseObject<List<TDeviceStatus>> getDeviceStatusByData(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "from") long from, @PathVariable(value = "to") long to);

	/**
	 * @param serialId
	 * @param from
	 * @param to
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/device/getDeviceStatusByDataNoGroup/{serialId}/{from}/{to}", method = RequestMethod.GET)
	ResponseObject<List<TDeviceStatus>> getDeviceStatusByDataNoGroup(@PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "from") long from, @PathVariable(value = "to") long to);

	/**
	 * @param pushList
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/intelligentFinger/batchTIntelligentFingerPush", method = RequestMethod.POST)
	ResponseObject batchTIntelligentFingerPush(@RequestBody List<TIntelligentFingerPush> pushList);

	/**
	 * @param deviceId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/ali/queryAliDevConfigBySerial/{deviceId}", method = RequestMethod.GET)
	ResponseObject<TAliDeviceConfig> queryAliDevConfigBySerial(@PathVariable(value = "deviceId") String deviceId);

	/**
	 * @param tAliDeviceConfig
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/ali/addAliDevConfig", method = RequestMethod.POST)
	ResponseObject addAliDevConfig(@RequestBody TAliDeviceConfig tAliDeviceConfig);

	/**
	 * @param tAliDeviceConfig
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/ali/updateAliDevConfig", method = RequestMethod.PUT)
	ResponseObject updateAliDevConfig(@RequestBody TAliDeviceConfig tAliDeviceConfig);

	/**
	 * @param userId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/group/queryGroup/{userId}", method = RequestMethod.GET)
	ResponseObject<Map<String, Object>> queryGroup(@PathVariable(value = "userId") Integer userId);

	/**
	 * @param groupName
	 * @param mList
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/group/addServerGroup/{groupName}/{userId}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> addServerGroup(@PathVariable(value = "groupName") String groupName,
			@PathVariable(value = "userId") Integer userId,
			@RequestParam(value = "mList", required = false) List<String> mList);

	/**
	 * @param groupId
	 * @param userId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/group/deleteServerGroup/{groupId}/{userId}", method = RequestMethod.DELETE)
	ResponseObject<Map<String, Object>> deleteServerGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "userId") Integer userId);

	/**
	 * @param groupId
	 * @param userId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/group/coverChildGroup/{groupId}/{userId}", method = RequestMethod.PUT)
	ResponseObject<Map<String, Object>> coverChildGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "userId") Integer userId,
			@RequestParam(name = "mList", required = false) List<String> mList);

	/**
	 * @param groupId
	 * @param userId
	 * @param mList
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/group/removeChildGroup/{groupId}/{userId}", method = RequestMethod.DELETE)
	ResponseObject<Map<String, Object>> removeChildGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "userId") Integer userId,
			@RequestParam(name = "mList", required = false) List<String> mList);

	/**
	 * @param groupId
	 * @param userId
	 * @param mList
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/group/addChildGroup/{groupId}/{userId}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> addChildGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "userId") Integer userId,
			@RequestParam(name = "mList", required = false) List<String> mList);

	/**
	 * @param groupId
	 * @param groupName
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/group/reNameGroup/{groupId}/{groupName}", method = RequestMethod.PUT)
	ResponseObject<Map<String, Object>> reNameGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "groupName") String groupName);

	/**
	 * @param groupId
	 * @param groupState
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/group/actionGroup/{groupId}/{groupState}", method = RequestMethod.PUT)
	ResponseObject<Map<String, Object>> actionGroup(@PathVariable(value = "groupId") Integer groupId,
			@PathVariable(value = "groupState") String groupState);

	/**
	 * @param userId
	 * @param groupId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/group/queryGroupByUserAndGroup/{userId}/{groupId}", method = RequestMethod.GET)
	ResponseObject<TServerGroup> queryGroupByUserAndGroup(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "groupId") Integer groupId);

	/**
	 * @param userId
	 * @param building
	 * @param room
	 * @param mList
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/location/createLocation/{userId}/{building}/{room}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> createLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "building") String building, @PathVariable(value = "room") String room,
			@RequestParam(required = false, name = "mList") List<String> mList);

	/**
	 * @param location
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/location/deleteLocation/{location}/{userId}", method = RequestMethod.DELETE)
	ResponseObject<Map<String, Object>> deleteLocation(@PathVariable(value = "location") Integer location,
			@PathVariable(value = "userId") Integer userId);

	/**
	 * @param serialId
	 * @param location
	 * @param xAxis
	 * @param yAxis
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/location/addDeviceLocation/{userId}/{serialId}/{location}/{xAxis}/{yAxis}/{deviceType}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> addDeviceLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "location") Integer location,
			@PathVariable(value = "xAxis") Integer xAxis, @PathVariable(value = "yAxis") Integer yAxis,
			@PathVariable(value = "deviceType") String deviceType);

	@RequestMapping(value = "/location/updateLocation/{location}/{userId}", method = RequestMethod.PUT)
	ResponseObject<Map<String, Object>> updateLocation(@PathVariable(value = "location") Integer location,
			@PathVariable(value = "userId") Integer userId,
			@RequestParam(value = "building", required = false) String building,
			@RequestParam(name = "room", required = false) String room,
			@RequestBody(required = false) List<String> mList);

	/**
	 * @param id
	 * @param serialId
	 * @param location
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/location/deleteDeviceLocation/{userId}/{serialId}/{location}", method = RequestMethod.DELETE)
	ResponseObject deleteDeviceLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "location") Integer location);

	/**
	 * @param userId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/location/queryLocation/{userId}", method = RequestMethod.GET)
	ResponseObject<Map<String, Object>> queryLocation(@PathVariable(value = "userId") Integer userId);

	/**
	 * @param userId
	 * @param location
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/location/queryDeviceLocation/{userId}/{locationId}", method = RequestMethod.GET)
	ResponseObject<Map<String, Object>> queryDeviceLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "locationId") Integer locationId);

	/**
	 * @param userId
	 * @param location
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/location/querySceneLocation/{userId}/{location}", method = RequestMethod.GET)
	ResponseObject<Map<String, Object>> querySceneLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "location") Integer location);

	/**
	 * @param userId
	 * @param location
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/location/setSceneLocation/{userId}/{location}/{sceneNumber}", method = RequestMethod.POST)
	ResponseObject setSceneLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "location") Integer location,
			@PathVariable(value = "sceneNumber") Integer sceneNumber);

	/**
	 * @param userId
	 * @param location
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/location/deleteSceneLocation/{userId}/{location}/{sceneNumber}", method = RequestMethod.DELETE)
	ResponseObject deleteSceneLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "location") Integer location,
			@PathVariable(value = "sceneNumber") Integer sceneNumber);

	/**
	 * @param tLocation
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/location/updateLocationByObj", method = RequestMethod.PUT)
	ResponseObject updateLocationByObj(@RequestBody TLocation tLocation);

	/**
	 * @param tLocation
	 * @Description:
	 */
	@RequestMapping(value = "/location/addLocation", method = RequestMethod.POST)
	ResponseObject<TLocation> addLocation(@RequestBody TLocation tLocation);

	/**
	 * @param tUserLocation
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/location/addUserLocation", method = RequestMethod.POST)
	ResponseObject addUserLocation(@RequestBody TUserLocation tUserLocation);

	/**
	 * @param userId
	 * @param location
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/location/queryLocationByUserAndLocation/{userId}/{location}", method = RequestMethod.GET)
	ResponseObject<TLocation> queryLocationByUserAndLocation(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "location") Integer location);

	/**
	 * @param serialId
	 * @param names
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/remoteLed/setRemoteLedName/{serialId}", method = RequestMethod.POST)
	ResponseObject setRemoteLedName(@PathVariable(value = "serialId", required = true) String serialId,
			@RequestParam(value = "names", required = true) String names);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/remoteLed/queryRemoteLedName/{serialId}", method = RequestMethod.POST)
	ResponseObject<Map<String, Object>> queryRemoteLedName(@PathVariable(value = "serialId", required = true) String serialId);

	/**  
	 * @param oboxSerialId  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/remoteLed/addRemoteLed/{oboxSerialId}", method = RequestMethod.POST)
	ResponseObject addRemoteLed(@PathVariable(value = "oboxSerialId", required = true)String oboxSerialId);

	/**  
	 * @param oboxSerialId  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/remoteLed/delRemoteLed/{oboxSerialId}", method = RequestMethod.DELETE)
	ResponseObject delRemoteLed(@PathVariable(value = "oboxSerialId", required = true)String oboxSerialId);

	/**  
	 * @param oboxSerialId
	 * @param status  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/remoteLed/controlRemoteLed/{oboxSerialId}/{status}", method = RequestMethod.PUT)
	ResponseObject controlRemoteLed(@PathVariable(value = "oboxSerialId", required = true)String oboxSerialId, 
			@PathVariable(value = "status", required = true)String status);

 
}
