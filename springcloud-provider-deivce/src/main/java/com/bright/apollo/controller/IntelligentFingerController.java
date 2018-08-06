package com.bright.apollo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.request.IntelligentOpenRecordDTO;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.IntelligentFingerService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月6日  
 *@Version:1.1.0  
 */
@RequestMapping("intelligentFinger")
@RestController
public class IntelligentFingerController {
	private static final Logger logger = LoggerFactory.getLogger(IntelligentFingerController.class);
	@Autowired
	private IntelligentFingerService intelligentFingerService;
	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/countFingerAuth/{serialId}", method = RequestMethod.GET)
	ResponseObject<Integer> countFingerAuth(@PathVariable(value = "serialId") String serialId){
		ResponseObject<Integer> res = new ResponseObject<Integer>();
		try {
			res.setData(intelligentFingerService.countFingerAuth(serialId));
 			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
 		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@RequestMapping(value = "/queryIntelligentOpenRecordByDate/{serialId}/{end}/{start}", method = RequestMethod.GET)
	ResponseObject<List<IntelligentOpenRecordDTO>> queryIntelligentOpenRecordByDate(
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "end") String end,
			@PathVariable(value = "start") String start){
		ResponseObject<List<IntelligentOpenRecordDTO>> res = new ResponseObject<List<IntelligentOpenRecordDTO>>();
		try {
			res.setData(intelligentFingerService.queryIntelligentOpenRecordByDate(serialId,end,start));
 			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
 		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	
	}
}
