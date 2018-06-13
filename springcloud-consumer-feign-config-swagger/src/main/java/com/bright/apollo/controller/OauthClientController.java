package com.bright.apollo.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.feign.FeignOauthClient;
import com.bright.apollo.feign.FeignOboxClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.OauthService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年6月13日  
 *@Version:1.1.0  
 */
@Api("OauthClient Controller")
@RestController
@RequestMapping("oauthclient")
public class OauthClientController {

	Logger logger = Logger.getLogger(OauthClientController.class);
	
	@Autowired
	private FeignOauthClient feignOauthClient;
	@ApiOperation(value = "add oauth client", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseObject<OauthClientDetails> addClient(@RequestBody(required = true) OauthClientDetails oauthClientDetails) {
		ResponseObject<OauthClientDetails> res=null;
		try {
			res=feignOauthClient.addOauthClientDetails(oauthClientDetails);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res=new ResponseObject<OauthClientDetails>();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
