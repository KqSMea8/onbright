package com.bright.apollo.Interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月29日
 * @Version:1.1.0
 */
@ControllerAdvice
public class BizExcepiton {

	private static final Logger logger = LoggerFactory.getLogger(BizExcepiton.class);
	@SuppressWarnings("rawtypes")
	@ExceptionHandler({ Exception.class })
	@ResponseBody
	public ResponseObject processException(Exception exception) {
		logger.error("exception ====== "+exception.getMessage());
		ResponseObject res = new ResponseObject();
		res.setStatus(ResponseEnum.Error.getStatus());
		res.setMessage(ResponseEnum.Error.getMsg());
		return res;
	}
}
