package com.bright.apollo.hystrix;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TNvr;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TYSCamera;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月17日  
 *@Version:1.1.0  
 */
@Component
public class HystrixFeignDeviceFallback extends BasicHystrixFeignFallback implements FeignDeviceClient {
	private static final Logger logger = LoggerFactory.getLogger(HystrixFeignDeviceFallback.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignDeviceClient#getDevice(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TOboxDeviceConfig> getDevice(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignDeviceClient#updateDevice(java.lang.String,
	 * com.bright.apollo.common.entity.TOboxDeviceConfig)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public ResponseObject<TOboxDeviceConfig> updateDevice(String serialId, TOboxDeviceConfig device) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignDeviceClient#addDevice(java.lang.String,
	 * com.bright.apollo.common.entity.TOboxDeviceConfig)
	 */
	@SuppressWarnings({"unchecked" })
	@Override
	public ResponseObject<TOboxDeviceConfig> addDevice(String serialId, TOboxDeviceConfig device) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignDeviceClient#delDevice(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject delDevice(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#deleleDeviceByOboxSerialId(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleleDeviceByOboxSerialId(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDeviceByUserAndPage(java.lang.Integer, java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByUserAndPage(Integer userId, Integer pageIndex,
			Integer pageSize) {
		logger.warn("===device server is break===");
		return serverError();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getOboxDeviceConfigByUserId(Integer userId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDevicesByOboxSerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDevicesByOboxSerialId(String oboxSerialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDeviceByUser(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByUser(Integer userId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getYSCameraBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TYSCamera> getYSCameraBySerialId(String deviceSerialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getNvrByIP(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TNvr> getNvrByIP(String ip) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDeviceTypeByUser(java.lang.Integer)  
	 */
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceTypeByUser(Integer userId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDevciesByUserIdAndType(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDevciesByUserIdAndType(Integer userId, String deviceType) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getYSCameraByUserId(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TYSCamera>> getYSCameraByUserId(Integer userId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDeviceByUserAndSerialId(java.lang.Integer, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TOboxDeviceConfig> getDeviceByUserAndSerialId(Integer userId, String serialID) {
		logger.warn("===device server is break===");
		return serverError();
	}

 
	 

}
