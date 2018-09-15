package com.bright.apollo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月15日  
 *@Version:1.1.0  
 */
@RequestMapping("refresh")
@RestController
public class RefreshConfigController {

 	@RequestMapping(method = RequestMethod.GET,value="refreshConfig")
	public ResponseObject refreshConfig() {
		ResponseObject<TObox> res = null;
		try {
 		} catch (Exception e) {
			e.printStackTrace();
			res = new ResponseObject<TObox>();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
