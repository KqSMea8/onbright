package com.bright.apollo.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.common.entity.TCreateTableLog;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserAliDevice;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.common.entity.TUserOperation;
import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.hrstrix.HystrixFeignUserFallback;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月2日
 * @Version:1.1.0
 */
@FeignClient(name = "springcloud-provider-user", fallback = HystrixFeignUserFallback.class, configuration = FeignConfig.class)
public interface FeignUserClient {

	@SuppressWarnings("rawtypes")
	@GetMapping("/user/register/{mobile}/{code}/{pwd}")
	public ResponseObject register(@PathVariable(value = "mobile") String mobile,
			@PathVariable(value = "code") Integer code, @PathVariable(value = "pwd") String pwd);

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/forget/{mobile}", method = RequestMethod.GET)
	public ResponseObject forget(@PathVariable(value = "mobile") String mobile);

	/**
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/deleteUserObox/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserOboxByOboxSerialId(
			@PathVariable(required = true, value = "serialId") String serialId);

	/**
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/deleteUserDevice/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserDeviceBySerialId(
			@PathVariable(required = true, value = "serialId") String serialId);

	/**
	 * @param sceneNumber
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/deleteUserScene/{sceneNumber}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserSceneBySceneNumber(
			@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber);

	/**
	 * @param username
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/getUser/{userName}", method = RequestMethod.GET)
	public ResponseObject<TUser> getUser(@PathVariable(required = true, value = "userName") String userName);

	@RequestMapping(value = "/user/getUserById/{id}", method = RequestMethod.GET)
	public ResponseObject<TUser> getUserById(@PathVariable(required = true, value = "id") Integer id);

	/**
	 * @param oauthClientDetails
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/oauthclient/addClient", method = RequestMethod.POST)
	ResponseObject<OauthClientDetails> addOauthClientDetails(
			@RequestBody(required = true) OauthClientDetails oauthClientDetails);

	/**
	 * @param tUserObox
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/addUserObox", method = RequestMethod.POST)
	ResponseObject addUserObox(@RequestBody(required = true) TUserObox tUserObox);

	/**
	 * @param tUserDevice
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/addUserDevice", method = RequestMethod.POST)
	ResponseObject addUserDevice(@RequestBody(required = true) TUserDevice tUserDevice);

	/**
	 * @param tUserScene
	 * @Description:
	 */
	@RequestMapping(value = "/user/addUserScene", method = RequestMethod.POST)
	ResponseObject<TUserScene> addUserScene(@RequestBody(required = true) TUserScene tUserScene);

	/**
	 * @param id
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/getUserDevcieByUser/{userId}", method = RequestMethod.GET)
	ResponseObject<List<TUserDevice>> getUserDevcieByUser(
			@PathVariable(required = true, value = "userId") Integer userId);

	/**
	 * @param userId
	 * @param oboxSerialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/getUserObox/{userId}/{oboxSerialId}", method = RequestMethod.GET)
	public ResponseObject<TUserObox> getUserObox(@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId);

	/**
	 * @param userId
	 * @param deviceSerialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/getUserDevcieByUserIdAndSerialId/{userId}/{deviceSerialId}", method = RequestMethod.GET)
	public ResponseObject<TUserDevice> getUserDevcieByUserIdAndSerialId(
			@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(required = true, value = "deviceSerialId") String deviceSerialId);

	/**
	 * @param fromDate
	 * @param toDate
	 * @param serialId
	 * @param startIndex
	 * @param countIndex
	 * @Description:
	 */
	@RequestMapping(value = "/user/getUserOperation/{fromDate}/{toDate}/{serialId}/{startIndex}/{countIndex}", method = RequestMethod.GET)
	public ResponseObject<List<TUserOperation>> getUserOperation(
			@PathVariable(required = true, value = "fromDate") Long fromDate,
			@PathVariable(required = true, value = "toDate") Long toDate,
			@PathVariable(required = true, value = "serialId") String serialId,
			@PathVariable(required = true, value = "startIndex") Integer startIndex,
			@PathVariable(required = true, value = "countIndex") Integer countIndex);

	/**
	 * @param from
	 * @param to
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/queryUserOperationByDate/{from}/{to}/{serialId}", method = RequestMethod.GET)
	public ResponseObject<List<TUserOperation>> queryUserOperationByDate(
			@PathVariable(required = true, value = "from") long from,
			@PathVariable(required = true, value = "to") long to,
			@PathVariable(required = true, value = "serialId") String serialId);

	/**
	 * @param tableName
	 * @param serialId
	 * @param day
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/queryUserOperationByMonth/{tableName}/{serialId}/{day}", method = RequestMethod.GET)
	public ResponseObject<List<TUserOperation>> queryUserOperationByMonth(
			@PathVariable(required = true, value = "tableName") String tableName,
			@PathVariable(required = true, value = "serialId") String serialId,
			@PathVariable(required = true, value = "day") String day);

	/**
	 * @param tableName
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/queryUserOperationByMonthDayList/{tableName}", method = RequestMethod.GET)
	public ResponseObject<List<TUserOperation>> queryUserOperationByMonthDayList(
			@PathVariable(required = true, value = "tableName") String tableName);

	/**
	 * @param tUserOperationSuffix
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/listCreateTableLogByNameWithLike/{tUserOperationSuffix}", method = RequestMethod.GET)
	public ResponseObject<List<TCreateTableLog>> listCreateTableLogByNameWithLike(
			@PathVariable(required = true, value = "tUserOperationSuffix") String tUserOperationSuffix);

	/**
	 * @param name
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/queryUserOperation/{name}/{serialId}", method = RequestMethod.GET)
	public ResponseObject<List<TUserOperation>> queryUserOperation(
			@PathVariable(required = true, value = "name") String name,
			@PathVariable(required = true, value = "serialId") String serialId);

	/**
	 * @param mobile
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/sendCodeToMobile/{mobile}", method = RequestMethod.GET)
	public ResponseObject sendCodeToMobile(@PathVariable(required = true, value = "mobile") String mobile);

	/**
	 * @param code
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/wxLogin/{code}", method = RequestMethod.GET)
	public ResponseObject<Map<String, Object>> wxLogin(@PathVariable(required = true, value = "code") Integer code);

	/**
	 * @param serialId
	 * @Description:
	 */
	@RequestMapping(value = "/user/getUserDeviceExceptRoot/{serialId}", method = RequestMethod.GET)
	public ResponseObject<List<TUserDevice>> getUserDeviceExceptRoot(
			@PathVariable(required = true, value = "serialId") String serialId);

	/**
	 * @param appkey
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/sendCodeToApp/{appkey}", method = RequestMethod.GET)
	public ResponseObject<Map<String, Object>> sendCodeToApp(
			@PathVariable(required = true, value = "appkey") String appkey);

	/**
	 * @param mobile
	 * @param code
	 * @param pwd
	 * @param appkey
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/register/{mobile}/{code}/{pwd}/{appkey}", method = RequestMethod.POST)
	public ResponseObject register(@PathVariable(required = true, value = "mobile") String mobile,
			@PathVariable(required = true, value = "code") String code,
			@PathVariable(required = true, value = "pwd") String pwd,
			@PathVariable(required = true, value = "appkey") String appkey);

	/**
	 * @param string
	 * @Description:
	 */
	@RequestMapping(value = "/oauthclient/getClients/{grantType}", method = RequestMethod.GET)
	public ResponseObject<List<OauthClientDetails>> getClients(
			@PathVariable(required = true, value = "grantType") String grantType);

	/**
	 * @param user
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/updateUserPwd", method = RequestMethod.PUT)
	public ResponseObject updateUserPwd(@RequestBody TUser user);

	/**
	 * @param clientId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/oauthclient/getClientByClientId/{clientId}", method = RequestMethod.GET)
	public ResponseObject<OauthClientDetails> getClientByClientId(
			@PathVariable(required = true, value = "clientId") String clientId);

	/**  
	 * @param deviceSerialId  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/deleteUserAliDev/{deviceSerialId}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserAliDev(@PathVariable(required = true, value = "deviceSerialId")  String deviceSerialId);

	/**  
	 * @param tUserAliDev  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/addUserAliDev", method = RequestMethod.POST)
	public ResponseObject addUserAliDev(@RequestBody TUserAliDevice tUserAliDev);
}
