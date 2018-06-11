package com.bright.apollo.hrstrix;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	private Logger logger = Logger.getLogger(getClass());

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
	@Override
	public ResponseObject<List<TObox>> getOboxByUserAndPage(Integer userId, Integer pageIndex, Integer pageSize) {
		logger.warn("===obox server is break===");
		return serverError();
	}

}
