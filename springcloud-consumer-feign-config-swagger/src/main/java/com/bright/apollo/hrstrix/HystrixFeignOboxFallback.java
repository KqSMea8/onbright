package com.bright.apollo.hrstrix;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.feign.FeignOboxClient;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Component
public class HystrixFeignOboxFallback extends BasicHystrixFeignFallback implements FeignOboxClient {
	private static final Logger logger = LoggerFactory.getLogger(HystrixFeignOboxFallback.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.feign.FeignOboxClient#getObox(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TObox> getObox(String serialId) {
		logger.warn("===obox server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.feign.FeignOboxClient#updateObox(java.lang.String,
	 * com.bright.apollo.common.entity.TObox)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseObject<TObox> updateObox(String serialId, TObox obox) {
		logger.warn("===obox server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.feign.FeignOboxClient#deleteObox(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteObox(String serialId) {
		logger.warn("===obox server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignOboxClient#addObox(com.bright.apollo.common.
	 * entity.TObox)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public ResponseObject<TObox> addObox(String serialId, TObox obox) {
		logger.warn("===obox server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignOboxClient#getOboxByUserAndPage(java.lang.
	 * Integer, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TObox>> getOboxByUserAndPage(Integer userId, Integer pageIndex, Integer pageSize) {
		logger.warn("===obox server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignOboxClient#getOboxByUser(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TObox>> getOboxByUser(Integer userId) {
		logger.warn("===obox server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignOboxClient#getOboxByUserAndoboxSerialId(java.lang.Integer, java.lang.String)  
	 */
	@Override
	public ResponseObject<TObox> getOboxByUserAndoboxSerialId(Integer userId, String oboxSerialId) {
		logger.warn("===obox server is break===");
		return serverError();
	}

}
