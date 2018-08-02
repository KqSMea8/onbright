package com.bright.apollo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(OauthClientController.class);
	
	@Autowired
	private OauthService oauthService;
	
	@RequestMapping(value = "/addClient",method = RequestMethod.POST)
	public ResponseObject<OauthClientDetails> addClient(@RequestBody(required = true) OauthClientDetails oauthClientDetails) {
		ResponseObject<OauthClientDetails> res=new ResponseObject<OauthClientDetails>();
		try {
			oauthService.addOauthClientDetails(oauthClientDetails);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
			res.setData(oauthClientDetails);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
