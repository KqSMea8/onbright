package com.bright.apollo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerAuth;
import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerUser;
import com.bright.apollo.request.IntelligentFingerWarnDTO;
import com.bright.apollo.request.IntelligentOpenRecordDTO;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.IntelligentFingerService;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年8月6日
 * @Version:1.1.0
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
	ResponseObject<Integer> countFingerAuth(@PathVariable(value = "serialId") String serialId) {
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
			@PathVariable(value = "start") String start) {
		ResponseObject<List<IntelligentOpenRecordDTO>> res = new ResponseObject<List<IntelligentOpenRecordDTO>>();
		try {
			res.setData(intelligentFingerService.queryIntelligentOpenRecordByDate(serialId, end, start));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param serialId
	 * @Description:
	 */
	@RequestMapping(value = "/getCountIntelligentWarnBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<Integer> getCountIntelligentWarnBySerialId(@PathVariable(value = "serialId") String serialId) {
		ResponseObject<Integer> res = new ResponseObject<Integer>();
		try {
			res.setData(intelligentFingerService.queryCountIntelligentWarnBySerialId(serialId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**
	 * @param serialId
	 * @param formatDate
	 * @param formatDate2
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/getIntelligentWarnByDate/{serialId}/{end}/{start}", method = RequestMethod.GET)
	ResponseObject<List<IntelligentFingerWarnDTO>> getIntelligentWarnByDate(
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "end") String end,
			@PathVariable(value = "start") String start){
		ResponseObject<List<IntelligentFingerWarnDTO>> res = new ResponseObject<List<IntelligentFingerWarnDTO>>();
		try {
			res.setData(intelligentFingerService.queryIntelligentWarnByDate(serialId, end, start));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/getCountIntelligentUserBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<Integer> getCountIntelligentUserBySerialId(@PathVariable(value = "serialId") String serialId){
		ResponseObject<Integer> res = new ResponseObject<Integer>();
		try {
			res.setData(intelligentFingerService.queryCountIntelligentUserBySerialId(serialId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/getIntelligentUserBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<List<TIntelligentFingerUser>> getIntelligentUserBySerialId(@PathVariable(value = "serialId")String serialId){
		ResponseObject<List<TIntelligentFingerUser>> res = new ResponseObject<List<TIntelligentFingerUser>>();
		try {
			res.setData(intelligentFingerService.queryIntelligentUserBySerialId(serialId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**
	 * @param serialId
	 * @param pin
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/getIntelligentFingerUserBySerialIdAndPin/{serialId}/{pin}", method = RequestMethod.GET)
	ResponseObject<TIntelligentFingerUser> getIntelligentFingerUserBySerialIdAndPin(
			@PathVariable(value = "serialId") String serialId, @PathVariable(value = "pin") String pin){
		ResponseObject<TIntelligentFingerUser> res = new ResponseObject<TIntelligentFingerUser>();
		try {
			res.setData(intelligentFingerService.queryIntelligentFingerUserBySerialIdAndPin(serialId,pin));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param fingerUser  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updatentelligentFingerUser", method = RequestMethod.PUT)
	ResponseObject updatentelligentFingerUser(@RequestBody TIntelligentFingerUser fingerUser){
		ResponseObject res = new ResponseObject();
		try {
			intelligentFingerService.updatentelligentFingerUser(fingerUser);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/getIntelligentAuthBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<TIntelligentFingerAuth> getIntelligentAuthBySerialId(@PathVariable(value = "serialId")String serialId){
		ResponseObject<TIntelligentFingerAuth> res = new ResponseObject<TIntelligentFingerAuth>();
		try {
			res.setData(intelligentFingerService.queryIntelligentAuthBySerialId(serialId));
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param auth  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addIntelligentFingerAuth", method = RequestMethod.POST)
	ResponseObject addIntelligentFingerAuth(@RequestBody TIntelligentFingerAuth auth){
		ResponseObject res = new ResponseObject();
		try {
			intelligentFingerService.addIntelligentFingerAuth(auth);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/getIntelligentFingerRemoteUsersBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<List<TIntelligentFingerRemoteUser>> getIntelligentFingerRemoteUsersBySerialId(@PathVariable(value = "serialId")String serialId){
		ResponseObject<List<TIntelligentFingerRemoteUser>> res = new ResponseObject<List<TIntelligentFingerRemoteUser>>();
		try {
			res.setData(intelligentFingerService.queryIntelligentFingerRemoteUsersBySerialId(serialId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/getTIntelligentFingerAbandonRemoteUsersBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<List<TIntelligentFingerAbandonRemoteUser>> getTIntelligentFingerAbandonRemoteUsersBySerialId(
			@PathVariable(value = "serialId")String serialId){
		ResponseObject<List<TIntelligentFingerAbandonRemoteUser>> res = new ResponseObject<List<TIntelligentFingerAbandonRemoteUser>>();
		try {
			res.setData(intelligentFingerService.queryTIntelligentFingerAbandonRemoteUsersBySerialId(serialId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param id  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/delIntelligentFingerAbandonRemoteUserById/{id}", method = RequestMethod.DELETE)
	ResponseObject delIntelligentFingerAbandonRemoteUserById(@PathVariable(value = "id")Integer id){
		ResponseObject res = new ResponseObject();
		try {
			intelligentFingerService.delIntelligentFingerAbandonRemoteUserById(id);
			//res.setData(intelligentFingerService.queryTIntelligentFingerAbandonRemoteUsersBySerialId(serialId));
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/getTIntelligentFingerRemoteUsersBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<List<TIntelligentFingerRemoteUser>> getTIntelligentFingerRemoteUsersBySerialId(@PathVariable(value = "serialId")String serialId){
		ResponseObject<List<TIntelligentFingerRemoteUser>> res = new ResponseObject<List<TIntelligentFingerRemoteUser>>();
		try {
 			res.setData(intelligentFingerService.queryTIntelligentFingerRemoteUsersBySerialId(serialId));
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param fingerRemoteUser
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/addTIntelligentFingerRemoteUser", method = RequestMethod.POST)
	ResponseObject<Integer> addTIntelligentFingerRemoteUser(@RequestBody(required=true) TIntelligentFingerRemoteUser fingerRemoteUser){
		ResponseObject<Integer> res = new ResponseObject<Integer>();
		try {
 			res.setData(intelligentFingerService.addTIntelligentFingerRemoteUser(fingerRemoteUser));
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param fingerRemoteUserId
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/getIntelligentFingerRemoteUserById/{fingerRemoteUserId}", method = RequestMethod.GET)
	ResponseObject<TIntelligentFingerRemoteUser> getIntelligentFingerRemoteUserById(@PathVariable(value = "fingerRemoteUserId")int fingerRemoteUserId){
		ResponseObject<TIntelligentFingerRemoteUser> res = new ResponseObject<TIntelligentFingerRemoteUser>();
		try {
 			res.setData(intelligentFingerService.queryTintelligentFingerRemoteUserById(fingerRemoteUserId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param id  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/delTIntelligentFingerRemoteUserById/{id}", method = RequestMethod.DELETE)
	ResponseObject delTIntelligentFingerRemoteUserById(@PathVariable(value = "id")int id){
		ResponseObject res = new ResponseObject();
		try {
			intelligentFingerService.delTIntelligentFingerRemoteUserById(id);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**  
	 * @param fingerAuth  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateTintelligentFingerAuth", method = RequestMethod.PUT)
	ResponseObject updateTintelligentFingerAuth(@RequestBody TIntelligentFingerAuth fingerAuth){
		ResponseObject res = new ResponseObject();
		try {
			intelligentFingerService.updateTintelligentFingerAuth(fingerAuth);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/getTIntelligentFingerPushsBySerialId/{serialId}", method = RequestMethod.GET)
	ResponseObject<List<TIntelligentFingerPush>> getTIntelligentFingerPushsBySerialId(
			@PathVariable(value = "serialId") String serialId){
		ResponseObject<List<TIntelligentFingerPush>> res = new ResponseObject<List<TIntelligentFingerPush>>();
		try {
			res.setData(intelligentFingerService.queryTIntelligentFingerPushsBySerialId(serialId));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	
	}
	/**  
	 * @param fingerRemoteUser  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateTIntelligentFingerRemoteUser", method = RequestMethod.PUT)
	ResponseObject updateTIntelligentFingerRemoteUser(@RequestBody TIntelligentFingerRemoteUser fingerRemoteUser){
		ResponseObject res = new ResponseObject();
		try {
			intelligentFingerService.updateTintelligentFingerRemoteUser(fingerRemoteUser);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	}
	/**  
	 * @param serialId
	 * @param pin
	 * @return  
	 * @Description:  
	 */
	@RequestMapping(value = "/getTIntelligentFingerRemoteUserBySerialIdAndPin/{serialId}/{pin}", method = RequestMethod.GET)
	ResponseObject<TIntelligentFingerRemoteUser> getTIntelligentFingerRemoteUserBySerialIdAndPin(
			@PathVariable(value="serialId")String serialId,
			@PathVariable(value="pin")int pin){
		ResponseObject<TIntelligentFingerRemoteUser> res = new ResponseObject<TIntelligentFingerRemoteUser>();
		try {
			res.setData(intelligentFingerService.queryTIntelligentFingerRemoteUserBySerialIdAndPin(serialId,pin));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	
	}
	/**
	 * @param mobile
	 * @param serialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateTIntelligentFingerPushMobileBySerialId/{mobile}/{serialId}", method = RequestMethod.PUT)
	ResponseObject updateTIntelligentFingerPushMobileBySerialId(@PathVariable(value = "mobile") String mobile,
			@PathVariable(value = "serialId") String serialId){
		ResponseObject res = new ResponseObject();
		try {
			intelligentFingerService.updateTIntelligentFingerPushMobileBySerialId(mobile,serialId);
			//res.setData(intelligentFingerService.queryTIntelligentFingerRemoteUserBySerialIdAndPin(serialId,pin));
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	}
	/**  
	 * @param enable
	 * @param serialId
	 * @param value  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/updateTIntelligentFingerPushEnableBySerialIdAndValue/{enable}/{serialId}/{value}", method = RequestMethod.PUT)
	ResponseObject updateTIntelligentFingerPushEnableBySerialIdAndValue(
			@PathVariable(value = "enable")Integer enable, 
			@PathVariable(value = "serialId")String serialId,
			@PathVariable(value = "value")Integer value){
		ResponseObject res = new ResponseObject();
		try {
			intelligentFingerService.updateTIntelligentFingerPushEnableBySerialIdAndValue(enable,serialId,value);
			//res.setData(intelligentFingerService.queryTIntelligentFingerRemoteUserBySerialIdAndPin(serialId,pin));
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/delIntelligentFingerAbandonRemoteUserBySerialIdAndPin/{serialId}/{pin}", method = RequestMethod.PUT)
	ResponseObject delIntelligentFingerAbandonRemoteUserBySerialIdAndPin(
			@PathVariable(value = "serialId")String serialId,
			@PathVariable(value = "pin")Integer pin){
		ResponseObject res = new ResponseObject();
		try {
			intelligentFingerService.delIntelligentFingerAbandonRemoteUserBySerialIdAndPin(serialId,pin);
			//res.setData(intelligentFingerService.queryTIntelligentFingerRemoteUserBySerialIdAndPin(serialId,pin));
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	
	}
}

