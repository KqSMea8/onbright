package com.bright.apollo.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.OauthService;

/**  
 *@Title:  
 *@Description: the springcloud-oauth2's work is checkout token 
 *@Author:JettyLiu
 *@Since:2018年6月13日  
 *@Version:1.1.0  
 */
@RestController
@RequestMapping("oauthclient")
public class OauthClientController {

	Logger logger = Logger.getLogger(OauthClientController.class);
	
	@Autowired
	private OauthService oauthService;
	
	@RequestMapping(value = "/addClient",method = RequestMethod.POST)
	public ResponseObject<OauthClientDetails> addClient(@RequestBody(required = true) OauthClientDetails oauthClientDetails) {
		ResponseObject<OauthClientDetails> res=new ResponseObject<OauthClientDetails>();
		try {
			oauthService.addOauthClientDetails(oauthClientDetails);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
			res.setData(oauthClientDetails);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
