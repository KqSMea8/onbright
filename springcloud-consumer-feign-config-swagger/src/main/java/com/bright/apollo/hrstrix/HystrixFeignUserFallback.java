package com.bright.apollo.hrstrix;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月10日  
 *@Version:1.1.0  
 */
@Component
public class HystrixFeignUserFallback extends BasicHystrixFeignFallback implements FeignUserClient  {
	private Logger logger = Logger.getLogger(getClass());
	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#register(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject register(String mobile) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#forget(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject forget(String mobile) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#deleteUserOboxByOboxSerialId(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteUserOboxByOboxSerialId(String serialId) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#deleteUserDeviceBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteUserDeviceBySerialId(String serialId) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#deleteUserSceneBySceneNumber(java.lang.Integer)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteUserSceneBySceneNumber(Integer sceneNumber) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}
 
	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#getUser(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject<TUser> getUser(String username) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	@Override
	public ResponseObject<TUser> getUserById(Integer id) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}
}
