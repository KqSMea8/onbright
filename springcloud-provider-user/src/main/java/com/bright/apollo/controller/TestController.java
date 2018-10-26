package com.bright.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.cache.UserCacheService;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.dto.OboxResp.Type;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.SmsService;
 

/**  
 *@Title:  
 *@Description:  Test redis
 *@Author:JettyLiu
 *@Since:2018年6月21日  
 *@Version:1.1.0  
 */
@RestController
@RequestMapping("test")
public class TestController {
	@Autowired
	private SmsService smsService;
	@Autowired
	private UserCacheService userCacheService;
	@GetMapping("/register/{mobile}")
	public String register(@PathVariable String mobile) {
		userCacheService.saveBindCode(mobile, 123456);
		return "success";
	}
	@GetMapping("/get/{mobile}")
	public String get(@PathVariable String mobile) {
		String bindCode = userCacheService.getBindCode(mobile);
		if(StringUtils.isEmpty(bindCode))
			return "error";
		return bindCode;
	}
	@SuppressWarnings({ "rawtypes" })
 	@RequestMapping(value = "/testpost/{word}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String  searchDevicesByOldStyle(@PathVariable(required=true,value="word") String word) {
		  
		 return word;
	}
	@SuppressWarnings({ "rawtypes" })
 	@RequestMapping(value = "/testpost/{word}/uostop", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String  testpost(@PathVariable(required=true,value="word") String word) {
	 
		 return "uostop"+word;
	}
	@SuppressWarnings({ "rawtypes" })
 	@RequestMapping(value = "/test/sms", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String  testsms() {
		smsService.sendAuthCode(123456, "15879618946");
		 return "uostop";
	}
}
