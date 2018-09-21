package com.bright.apollo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.http.HttpWithBasicAuth;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年9月12日
 * @Version:1.1.0
 */
@RestController
@RefreshScope
@RequestMapping("refreshconfig")
public class RefreshController {
	private static final Logger logger = LoggerFactory.getLogger(RefreshController.class);
 
	@Value("${Refresh.url}")
	private String url;
	//@Value("${hahah.value}")
	//private String value;
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseObject refreshConfig() {
		// 执行 curl -X POST http://localhost:8800/bus/refresh
		ResponseObject res = new ResponseObject();
		try {
		//	logger.info("===============value:"+value);
			HttpWithBasicAuth.http(url);
 			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
