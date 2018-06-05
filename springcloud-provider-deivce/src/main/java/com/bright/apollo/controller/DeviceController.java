package com.bright.apollo.controller;

import java.util.List;

import com.bright.apollo.service.OboxDeviceConfigService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.DeviceService;





/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@RequestMapping("device")
@RestController
public class DeviceController {
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private DeviceService deviceService;

	@Autowired
	private OboxDeviceConfigService oboxDeviceConfigService;

	// find deivce by serial_id
	@RequestMapping(value = "/{serialId}", method = RequestMethod.GET)
	public ResponseObject<TOboxDeviceConfig> getDevice(
			@PathVariable(required = true, value = "serialId") String serialId) {
		ResponseObject<TOboxDeviceConfig> res = new ResponseObject<TOboxDeviceConfig>();
		try {
			TOboxDeviceConfig device = oboxDeviceConfigService.getTOboxDeviceConfigByDeviceSerialId(serialId);
			if (device == null) {
				res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
				res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				res.setCode(ResponseEnum.Success.getCode());
				res.setMsg(ResponseEnum.Success.getMsg());
				res.setData(device);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	// list page of device

	// update device
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{serialId}", method = RequestMethod.PUT)
	public ResponseObject updateDevice(@PathVariable(required = true, value = "serialId") String serialId,
			@RequestBody(required = true) TOboxDeviceConfig device) {
		ResponseObject res = new ResponseObject();
		try {
			if (deviceService.queryDeviceBySerialId(serialId) == null) {
				res.setCode(ResponseEnum.RequestObjectNotExist.getCode());
				res.setMsg(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				deviceService.updateDeviceBySerialId(device);
				res.setCode(ResponseEnum.Success.getCode());
				res.setMsg(ResponseEnum.Success.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// add device
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{serialId}", method = RequestMethod.POST)
	public ResponseObject addDevice(@PathVariable(required = true, value = "serialId") String serialId,
			@RequestBody(required = true) TOboxDeviceConfig device) {
		ResponseObject res = new ResponseObject();
		try {
			if (deviceService.queryDeviceBySerialId(serialId) != null) {
				res.setCode(ResponseEnum.ObjExist.getCode());
				res.setMsg(ResponseEnum.ObjExist.getMsg());
			} else {
				deviceService.addDevice(device);
				res.setCode(ResponseEnum.Success.getCode());
				res.setMsg(ResponseEnum.Success.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// delete device
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject delDevice(@PathVariable(required = true, value = "serialId") String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			if (deviceService.queryDeviceBySerialId(serialId) == null) {
				res.setCode(ResponseEnum.ObjExist.getCode());
				res.setMsg(ResponseEnum.ObjExist.getMsg());
			} else {
				deviceService.deleteDeviceBySerialId(serialId);
				res.setCode(ResponseEnum.Success.getCode());
				res.setMsg(ResponseEnum.Success.getMsg());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// list device
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByUserAndPage(
			@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(value = "pageIndex") Integer pageIndex, @PathVariable(value = "pageSize") Integer pageSize) {
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
		try {
			if (userId == 0) {
				res.setCode(ResponseEnum.RequestParamError.getCode());
				res.setMsg(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (pageIndex == null)
				pageIndex = 0;
			if (pageSize == null || pageSize <= 0)
				pageSize = 10;
			List<TOboxDeviceConfig> list = deviceService.queryDeviceByUserId(userId, pageIndex, pageSize);
			if (list == null || list.size() <= 0) {
				res.setCode(ResponseEnum.SearchIsEmpty.getCode());
				res.setMsg(ResponseEnum.SearchIsEmpty.getMsg());
			} else {
				int count = deviceService.queryCountDeviceByUserId(userId);
				res.setCode(ResponseEnum.Success.getCode());
				res.setMsg(ResponseEnum.Success.getMsg());
				res.setData(list);
				res.setPageSize(pageSize);
				res.setPageIndex(pageIndex);
				res.setPageCount((count / pageSize + (count % pageSize == 0 ? 0 : 1)));
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getOboxDeviceConfigByUserId/{userId}", method = RequestMethod.GET)
	public ResponseObject<TOboxDeviceConfig> getOboxDeviceConfigByUserId(@PathVariable(required = true, value = "userId") Integer userId) {
		logger.info(" ====== getOboxDeviceConfigByUserId ====== ");
		ResponseObject res = new ResponseObject();
		try {
			List<TOboxDeviceConfig> oboxDeviceConfigList = oboxDeviceConfigService.getOboxDeviceConfigByUserId(userId);
			if ( oboxDeviceConfigList == null) {
				res.setCode(ResponseEnum.NoExistCode.getCode());
				res.setMsg(ResponseEnum.NoExistCode.getMsg());
			} else {
				res.setCode(ResponseEnum.Success.getCode());
				res.setMsg(ResponseEnum.Success.getMsg());
				res.setData(oboxDeviceConfigList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
