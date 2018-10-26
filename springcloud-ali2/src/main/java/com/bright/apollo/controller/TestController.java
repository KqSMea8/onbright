package com.bright.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.bean.PushExceptionMsg;
import com.bright.apollo.bean.PushSystemMsg;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.ExceptionEnum;
import com.bright.apollo.enums.SystemEnum;
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
		PushSystemMsg systemMsg = new PushSystemMsg(
				SystemEnum.system
						.getValue(),
				SystemEnum.scene
						.getValue(),
						506, null,
						"会所嫩模已经触发,请注意！【昂宝电子】");
 		pushObserverManager
				.sendMessage(null,
						systemMsg);
	}
}
