package com.bright.apollo.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.hrstrix.HystrixFeignUserFallback;
import com.bright.apollo.response.ResponseEnum;
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
	public ResponseObject<TUser> getUser(String username);

	@RequestMapping(value = "/user/getUserById/{id}", method = RequestMethod.GET)
	public ResponseObject<TUser> getUserById(Integer id);
	
	
	/**  
	 * @param oauthClientDetails
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/oauthclient/addClient", method = RequestMethod.POST)
	ResponseObject<OauthClientDetails> addOauthClientDetails(@RequestBody(required = true) OauthClientDetails oauthClientDetails);

}
