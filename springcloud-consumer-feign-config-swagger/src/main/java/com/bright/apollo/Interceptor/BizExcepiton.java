package com.bright.apollo.Interceptor;

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

	@SuppressWarnings("rawtypes")
	@ExceptionHandler({ Exception.class })
	@ResponseBody
	public ResponseObject processException(Exception exception) {
		ResponseObject res = new ResponseObject();
		res.setCode(ResponseEnum.Error.getCode());
		res.setMsg(ResponseEnum.Error.getMsg());
		return res;
	}
}
