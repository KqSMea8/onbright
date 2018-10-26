package com.bright.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.bean.PushExceptionMsg;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.ExceptionEnum;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.session.PushObserverManager;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月26日  
 *@Version:1.1.0  
 */
@RestController
@RequestMapping("test")
public class TestController {
	@Autowired
	private PushObserverManager pushObserverManager;
	@RequestMapping(value = "/toAli", method = RequestMethod.POST)
	@ResponseBody
	public void toAliService() {
		PushExceptionMsg exceptionMsg=new PushExceptionMsg
				(ExceptionEnum.alldevice.getValue(),
						ExceptionEnum.pic.getValue(),506, 559, null,null);
		//JPushService.sendAlter(tScene.getSceneName(), tUser.getUserName(), urlString);
		pushObserverManager
		.sendMessage(exceptionMsg,
				null);
	}
}
