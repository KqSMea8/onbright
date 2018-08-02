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

import com.bright.apollo.common.entity.TNvr;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TYSCamera;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.CameraService;
import com.bright.apollo.service.DeviceService;
import com.bright.apollo.service.NvrService;
import com.bright.apollo.service.OboxDeviceConfigService;



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
	private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);
	@Autowired
	private DeviceService deviceService;

	@Autowired
	private OboxDeviceConfigService oboxDeviceConfigService;

	@Autowired
	private CameraService cameraService;

	@Autowired
	private NvrService nvrService;

	// find deivce by serial_id
	@RequestMapping(value = "/{serialId}", method = RequestMethod.GET)
	public ResponseObject<TOboxDeviceConfig> getDevice(
			@PathVariable(required = true, value = "serialId") String serialId) {
		ResponseObject<TOboxDeviceConfig> res = new ResponseObject<TOboxDeviceConfig>();
		try {
			TOboxDeviceConfig device = oboxDeviceConfigService.getTOboxDeviceConfigByDeviceSerialId(serialId);
			if (device == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				res.setStatus(ResponseEnum.SelectSuccess.getStatus());
				res.setMessage(ResponseEnum.SelectSuccess.getMsg());
				res.setData(device);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	// list page of device

	// update device
	@RequestMapping(value = "/{serialId}", method = RequestMethod.PUT)
	public ResponseObject<TOboxDeviceConfig> updateDevice(
			@PathVariable(required = true, value = "serialId") String serialId,
			@RequestBody(required = true) TOboxDeviceConfig device) {
		ResponseObject<TOboxDeviceConfig> res = new ResponseObject<TOboxDeviceConfig>();
		try {
			if (deviceService.queryDeviceBySerialId(serialId) == null) {
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
			} else {
				deviceService.updateDeviceBySerialId(device);
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
				res.setData(deviceService.queryDeviceBySerialId(serialId));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// add device
	@RequestMapping(value = "/{serialId}", method = RequestMethod.POST)
	public ResponseObject<TOboxDeviceConfig> addDevice(
			@PathVariable(required = true, value = "serialId") String serialId,
			@RequestBody(required = true) TOboxDeviceConfig device) {
		ResponseObject<TOboxDeviceConfig> res = new ResponseObject<TOboxDeviceConfig>();
		try {
 			if (oboxDeviceConfigService.queryDeviceConfigBySerialID(serialId) != null) {
				res.setStatus(ResponseEnum.ObjExist.getStatus());
				res.setMessage(ResponseEnum.ObjExist.getMsg());
			} else {
				oboxDeviceConfigService.addTOboxDeviceConfig(device);
				res.setStatus(ResponseEnum.AddSuccess.getStatus());
				res.setMessage(ResponseEnum.AddSuccess.getMsg());
				res.setData(oboxDeviceConfigService.queryDeviceConfigBySerialID(serialId));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("===getScene error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
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
				res.setStatus(ResponseEnum.ObjExist.getStatus());
				res.setMessage(ResponseEnum.ObjExist.getMsg());
			} else {
				deviceService.deleteDeviceBySerialId(serialId);
				res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
				res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// list device
	@RequestMapping(value = "/{userId}/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByUserAndPage(
			@PathVariable(required = true, value = "userId") Integer userId,
			@PathVariable(value = "pageIndex") Integer pageIndex, @PathVariable(value = "pageSize") Integer pageSize) {
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
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
			List<TOboxDeviceConfig> list = deviceService.queryDeviceByUserId(userId, pageIndex, pageSize);
			if (list == null || list.size() <= 0) {
				res.setStatus(ResponseEnum.SearchIsEmpty.getStatus());
				res.setMessage(ResponseEnum.SearchIsEmpty.getMsg());
			} else {
				int count = deviceService.queryCountDeviceByUserId(userId);
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

	@RequestMapping(value = "/getOboxDeviceConfigByUserId/{userId}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getOboxDeviceConfigByUserId(
			@PathVariable(required = true, value = "userId") Integer userId) {
		logger.info(" ====== getOboxDeviceConfigByUserId ====== ");
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
		try {
			List<TOboxDeviceConfig> oboxDeviceConfigList = oboxDeviceConfigService.getOboxDeviceConfigByUserId(userId);
			if (oboxDeviceConfigList == null) {
				res.setStatus(ResponseEnum.NoExistCode.getStatus());
				res.setMessage(ResponseEnum.NoExistCode.getMsg());
			} else {
				res.setStatus(ResponseEnum.SelectSuccess.getStatus());
				res.setMessage(ResponseEnum.SelectSuccess.getMsg());
				res.setData(oboxDeviceConfigList);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getDevicesByOboxSerialId/{oboxSerialId}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getDevicesByOboxSerialId(
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId) {
		logger.info(" ====== getDevicesByOboxSerialId ====== ");
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
		try {
			List<TOboxDeviceConfig> oboxDeviceConfigList = oboxDeviceConfigService
					.getOboxDeviceConfigByOboxSerialId(oboxSerialId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(oboxDeviceConfigList);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getDeviceByUser/{userId}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByUser(@PathVariable(value = "userId") Integer userId) {
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
		try {
			List<TOboxDeviceConfig> oboxDeviceConfigList = oboxDeviceConfigService.getOboxDeviceConfigByUserId(userId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(oboxDeviceConfigList);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getYSCameraBySerialId/{deviceSerialId}", method = RequestMethod.GET)
	public ResponseObject<TYSCamera> getYSCameraBySerialId(
			@PathVariable(value = "deviceSerialId") String deviceSerialId) {
		ResponseObject<TYSCamera> res = new ResponseObject<TYSCamera>();
		try {
			TYSCamera tysCamera = cameraService.getYSCameraBySerialId(deviceSerialId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(tysCamera);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getNvrByIP/{deviceSerialId}", method = RequestMethod.GET)
	public ResponseObject<TNvr> getNvrByIP(@PathVariable(value = "ip") String ip) {
		ResponseObject<TNvr> res = new ResponseObject<TNvr>();
		try {
			TNvr nvr = nvrService.getNvrByIP(ip);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(nvr);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getDeviceTypeByUser/{userId}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceTypeByUser(@PathVariable(value = "userId") Integer userId) {
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
		try {
			List<TOboxDeviceConfig> list = oboxDeviceConfigService.getDeviceTypeByUser(userId);
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

	@RequestMapping(value = "/getDevciesByUserIdAndType/{userId}/{deviceType}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getDevciesByUserIdAndType(
			@PathVariable(value = "userId") Integer userId, @PathVariable(value = "deviceType") String deviceType) {
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
		try {
			List<TOboxDeviceConfig> list = oboxDeviceConfigService.getDevciesByUserIdAndType(userId, deviceType);
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

	@RequestMapping(value = "/getYSCameraByUserId/{userId}", method = RequestMethod.GET)
	public ResponseObject<List<TYSCamera>> getYSCameraByUserId(@PathVariable(value = "userId") Integer userId) {
		ResponseObject<List<TYSCamera>> res = new ResponseObject<List<TYSCamera>>();
		try {
			List<TYSCamera> list = cameraService.getYSCameraByUserId(userId);
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
	@RequestMapping(value = "/getDeviceByUserAndSerialId/{userId}/{serialID}", method = RequestMethod.GET)
	ResponseObject<TOboxDeviceConfig> getDeviceByUserAndSerialId(@PathVariable(value = "userId") Integer userId,
			@PathVariable(value = "serialID") String serialID) {
		ResponseObject<TOboxDeviceConfig> res = new ResponseObject<TOboxDeviceConfig>();
		try {
			TOboxDeviceConfig device=oboxDeviceConfigService.getDeviceByUserAndSerialId(userId,serialID);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(device);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/deleleDeviceByOboxSerialId/{serialId}", method = RequestMethod.DELETE)
	ResponseObject deleleDeviceByOboxSerialId(
			@PathVariable(value = "serialId") String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			oboxDeviceConfigService.deleteTOboxDeviceConfigByOboxSerialId(serialId);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
 		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
