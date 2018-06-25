package com.bright.apollo.hrstrix;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Component
public class HystrixFeignDeviceFallback extends BasicHystrixFeignFallback implements FeignDeviceClient {
	private Logger logger = Logger.getLogger(getClass());

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
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByUserAndPage(Integer userId, Integer pageIndex,
			Integer pageSize) {
		logger.warn("===device server is break===");
		return serverError();
	}

	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getOboxDeviceConfigByUserId(Integer userId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDevicesByOboxSerialId(String oboxSerialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

}
