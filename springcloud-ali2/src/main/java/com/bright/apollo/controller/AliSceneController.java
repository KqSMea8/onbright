package com.bright.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.session.SceneActionThreadPool;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月17日  
 *@Version:1.1.0  
 */
@RestController
@RequestMapping("aliScene")
public class AliSceneController {
	
	@Autowired
	private SceneActionThreadPool sceneActionThreadPool;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addSceneAction", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject addSceneAction(@PathVariable(value = "sceneNumber")Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			sceneActionThreadPool.addSceneAction(sceneNumber);
			//OboxResp resp = topicServer.request(cmd, inMsgByte, deviceSerial);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
