package com.bright.apollo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.response.MsgExceptionDTO;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.TMsgService;
import com.bright.apollo.tool.RandomUtil;
import com.bright.apollo.tool.Verify;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月18日  
 *@Version:1.1.0  
 */
@RequestMapping("msg")
@RestController
public class MsgController {
	private static final Logger logger = LoggerFactory.getLogger(MsgController.class);
	@Autowired
	private TMsgService tMsgService;
	/**
	 * @param userId
	 * @Description:
	 */
	@RequestMapping(value = "/countMsgList/{userId}", method = RequestMethod.GET)
	public ResponseObject<Integer> countMsgList(@PathVariable(required = true, value = "userId") Integer userId){
		ResponseObject<Integer> res = new ResponseObject<Integer>();
		try {
			res.setData(tMsgService.countMsgList(userId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	
	}
	@RequestMapping(value = "/queryMsgExceList/{userId}/{type}/{start}/{count}", method = RequestMethod.GET)
	public ResponseObject<List<MsgExceptionDTO>> queryMsgExceList(@PathVariable(required = true, value = "userId")Integer userId,
			@PathVariable(required = true, value = "type")Integer type, 
			@PathVariable(required = true, value = "start")int start, 
			@PathVariable(required = true, value = "count")int count){
		ResponseObject<List<MsgExceptionDTO>> res = new ResponseObject<List<MsgExceptionDTO>>();
		try {
			res.setData(tMsgService.queryMsgExceList(userId, type,start,count));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**
	 * @param id
	 * @param parseInt
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/countMsgExceList/{userId}/{type}", method = RequestMethod.GET)
	public ResponseObject<Integer> countMsgExceList(@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(required = true, value = "type") Integer type){
		ResponseObject<Integer> res = new ResponseObject<Integer>();
		try {
			res.setData(tMsgService.countMsgExceList(userId, type));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	
	}
	/**  
	 * @param id
	 * @param statue  
	 * @Description:  
	 */
	@RequestMapping(value = "/msg/updateMsgState/{id}/{statue}", method = RequestMethod.PUT)
	public ResponseObject updateMsgState(@PathVariable(required = true, value = "id") Integer id,
			@PathVariable(required = true, value = "statue") int statue){

		ResponseObject<Integer> res = new ResponseObject<Integer>();
		try {
			tMsgService.updateMsgState(id,statue);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	
	
	}
}
