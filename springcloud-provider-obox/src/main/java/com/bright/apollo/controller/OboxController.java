package com.bright.apollo.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.OboxService;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@RequestMapping("obox")
@RestController
public class OboxController {
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private OboxService oboxService;

	// find by id
	@RequestMapping(value = "/getObox/{serialId}", method = RequestMethod.GET)
	public ResponseObject<TObox> getObox(@PathVariable(value = "serialId") String serialId) {
		ResponseObject<TObox> res = new ResponseObject<TObox>();
		try { 
			TObox obox = oboxService.queryOboxsByOboxSerialId(serialId);
			if (obox == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				res.setStatus(ResponseEnum.SelectSuccess.getStatus());
				res.setMessage(ResponseEnum.SelectSuccess.getMsg());
				res.setData(obox);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// update obox
	@RequestMapping(value = "/{serialId}", method = RequestMethod.PUT)
	public ResponseObject<TObox> updateObox(@PathVariable(required = true, value = "serialId") String serialId,
			@RequestBody(required = true) TObox obox) {
		ResponseObject<TObox> res = new ResponseObject<TObox>();
		try {
			if (oboxService.queryOboxBySerialId(serialId) == null) {
				res.setStatus(ResponseEnum.ObjExist.getStatus());
				res.setMessage(ResponseEnum.ObjExist.getMsg());
			} else {
				oboxService.update(obox);
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
				res.setData(oboxService.queryOboxBySerialId(serialId));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// delete obox
	@RequestMapping(value = "/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject<TObox> deleteObox(@PathVariable(required = true, value = "serialId") String serialId) {
		ResponseObject<TObox> res = new ResponseObject<TObox>();
		try {
			TObox obox = oboxService.queryOboxBySerialId(serialId);
			if (obox == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				oboxService.deleteOboxById(obox);
				res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
				res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
				res.setData(obox);
			}
		} catch (Exception e) {
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// add obox
	@RequestMapping(value = "/addObox/{serialId}", method = RequestMethod.POST)
	public ResponseObject<TObox> addObox(@PathVariable(required = true, value = "serialId") String serialId,
			@RequestBody(required = true) TObox obox) {
		ResponseObject<TObox> res = new ResponseObject<TObox>();
		try {
			if (oboxService.queryOboxBySerialId(serialId) != null) {
				res.setStatus(ResponseEnum.ObjExist.getStatus());
				res.setMessage(ResponseEnum.ObjExist.getMsg());
				return res;
			}
			oboxService.addObox(obox);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
			res.setData(oboxService.queryOboxBySerialId(serialId));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// -------------------------------------
	// find list of page
	// -------------------------------------
	@RequestMapping(value = "/getOboxByUserAndPage/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<TObox>> getOboxByUserAndPage(
			@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(value = "pageIndex") Integer pageIndex, @PathVariable(value = "pageSize") Integer pageSize) {
		ResponseObject<List<TObox>> res = new ResponseObject<List<TObox>>();
		try {
			if (userId == 0) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (pageIndex == null)
				pageIndex = 0;
			if (pageSize == null || pageSize <= 0)
				pageSize = 10;
			List<TObox> list = oboxService.queryOboxByUserId(userId, pageIndex, pageSize);
			if (list == null || list.size() <= 0) {
				res.setStatus(ResponseEnum.SearchIsEmpty.getStatus());
				res.setMessage(ResponseEnum.SearchIsEmpty.getMsg());
			} else {
				int count = oboxService.queryCountOboxByUserId(userId);
				res.setStatus(ResponseEnum.SelectSuccess.getStatus());
				res.setMessage(ResponseEnum.SelectSuccess.getMsg());
				res.setData(list);
				res.setPageSize(pageSize);
				res.setPageIndex(pageIndex);
				res.setPageCount((count / pageSize + (count % pageSize == 0 ? 0 : 1)));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@RequestMapping(value = "/getOboxByUser/{userId}", method = RequestMethod.GET)
	public ResponseObject<List<TObox>> getOboxByUser(
			@PathVariable(required = true, value = "userId") Integer userId){
		ResponseObject<List<TObox>> res = new ResponseObject<List<TObox>>();
		try {
			if (userId == 0) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			List<TObox> list = oboxService.queryOboxByUserId(userId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(list);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@RequestMapping(value = "/getOboxByUserAndoboxSerialId/{userId}/{oboxSerialId}", method = RequestMethod.GET)
	public ResponseObject<TObox> getOboxByUserAndoboxSerialId(
			@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId
			){
		ResponseObject<TObox> res = new ResponseObject<TObox>();
		try {
 			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(oboxService.getOboxByUserAndoboxSerialId(userId,oboxSerialId));
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
