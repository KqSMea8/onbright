package com.bright.apollo.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
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
	@GetMapping("/user/register/{mobile}")
	public ResponseObject register(@PathVariable(value = "mobile") String mobile);

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/forget/{mobile}", method = RequestMethod.GET)
	public ResponseObject forget(@PathVariable(value = "mobile") String mobile);

	/**
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/deleteUserObox/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserOboxByOboxSerialId(String serialId);

	/**
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/deleteUserDevice/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserDeviceBySerialId(String serialId);

	/**
	 * @param sceneNumber
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/deleteUserScene/{sceneNumber}", method = RequestMethod.DELETE)
	public ResponseObject deleteUserSceneBySceneNumber(Integer sceneNumber);

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
	@RequestMapping(value = "/user/getUserDevcieByUserIdAndSerialId/{userId}/{oboxSerialId}", method = RequestMethod.GET)
	public ResponseObject<TUserDevice> getUserDevcieByUserIdAndSerialId(@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(required = true, value = "deviceSerialId") String deviceSerialId);

}
