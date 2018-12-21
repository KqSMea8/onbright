package com.bright.apollo.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TLocation;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.feign.FeignOboxClient;
import com.bright.apollo.feign.FeignQuartzClient;
import com.bright.apollo.feign.FeignSceneClient;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.response.DeviceDTO;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年12月20日
 * @Version:1.1.0
 */
@Controller
@Api("hotel Controller")
@RestController
@RequestMapping("hotel")
public class HotelController {
	private static final Logger logger = LoggerFactory.getLogger(HotelController.class);
	@Autowired
	private FeignUserClient feignUserClient;
	@Autowired
	private FeignDeviceClient feignDeviceClient;
	@Autowired
	private FeignQuartzClient feignQuartzClient;
	@Autowired
	private FeignAliClient feignAliClient;
	@Autowired
	private FeignSceneClient feignSceneClient;
	@Autowired
	private FeignOboxClient feignOboxClient;
	// check in
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "checkIn", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/checkIn/{endTime}/{mobile}/{locationId}", method = RequestMethod.POST)
	public ResponseObject checkIn(@PathVariable(required = true, value = "endTime") Long endTime,
			@PathVariable(required = true, value = "mobile") String mobile,
			@PathVariable(required = true, value = "locationId") Integer locationId) {
		ResponseObject res = new ResponseObject();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			if (endTime < new Date().getTime()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject checkRes = feignDeviceClient.checkIn(resUser.getData().getId(), locationId, mobile);

			if (checkRes == null || checkRes.getStatus() != ResponseEnum.AddSuccess.getStatus()) {
				return checkRes;
			}
			feignQuartzClient.checkIn(locationId, mobile, endTime);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// check out
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "checkOut", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/checkOut/{locationId}", method = RequestMethod.PUT)
	public ResponseObject checkOut(@PathVariable(required = true, value = "mobile") Integer locationId) {
		ResponseObject res = new ResponseObject();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			return feignDeviceClient.checkOut(resUser.getData().getId(), locationId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// continue
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "continueLocation", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/continueLocation/{endTime}/{locationId}", method = RequestMethod.PUT)
	public ResponseObject continueLocation(@PathVariable(required = true, value = "endTime") Long endTime,
			@PathVariable(required = true, value = "locationId") Integer locationId) {
		ResponseObject res = new ResponseObject();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			if (endTime < new Date().getTime()) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TLocation> resLocation = feignDeviceClient.continueLocation(resUser.getData().getId(),
					locationId);
			if (resLocation == null || resLocation.getData() == null) {
				return resLocation;
			}
			feignQuartzClient.continueLocation(locationId, resLocation.getData().getUserName(), endTime);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// query device
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "queryDeviceByadmin", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/queryDeviceByadmin", method = RequestMethod.GET)
	public ResponseObject<List<DeviceDTO>> queryDeviceByadmin() {
		ResponseObject<List<DeviceDTO>> res = new ResponseObject<List<DeviceDTO>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			return feignDeviceClient.queryDeviceByadmin(resUser.getData().getId());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "queryDeviceByGust", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/queryDeviceByGust", method = RequestMethod.GET)
	public ResponseObject<List<DeviceDTO>> queryDeviceByGust() {
		ResponseObject<List<DeviceDTO>> res = new ResponseObject<List<DeviceDTO>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			return feignDeviceClient.queryDeviceByGust(resUser.getData().getUserName());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// control device
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "controlDevice", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/controlDevice/{serialId}/{status}", method = RequestMethod.PUT)
	public ResponseObject controlDevice(@PathVariable(required = true, value = "serialId") String serialId,
			@PathVariable(required = true, value = "status") String status) {
		ResponseObject res = new ResponseObject();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			// 判断 serialId是否合法
			ResponseObject<TOboxDeviceConfig> deviceRes = feignDeviceClient.queryLocationDeviceBySerialIdAndUserName(serialId, resUser.getData().getUserName());
			if(deviceRes==null||deviceRes.getData()==null){
				return deviceRes;
			}
			TOboxDeviceConfig tOboxDeviceConfig = deviceRes.getData();
			feignAliClient.setDeviceStatus(tOboxDeviceConfig.getOboxSerialId(),
					status, tOboxDeviceConfig.getDeviceRfAddr());
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// control scene
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "controlScene", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/controlScene/{sceneNumber}", method = RequestMethod.PUT)
	public ResponseObject controlScene(@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser == null || resUser.getStatus() != ResponseEnum.SelectSuccess.getStatus()
					|| resUser.getData() == null) {
				res.setStatus(ResponseEnum.UnKonwUser.getStatus());
				res.setMessage(ResponseEnum.UnKonwUser.getMsg());
				return res;
			}
			ResponseObject<TScene> sceneRes= feignDeviceClient.queryLocationSceneBySceneNumberAndUserName(sceneNumber,resUser.getData().getUserName());
			if(sceneRes==null||sceneRes.getData()==null){
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
				return res;
			}
			TScene scene = sceneRes.getData();
			if (scene.getSceneRun() == 0) {
				scene.setSceneRun((byte) 1);
				scene.setAlterNeed((byte) 1);
				feignSceneClient.updateScene(scene);
				feignAliClient.addSceneAction(sceneNumber);
			}else{
				ResponseObject<TObox> oboxRes = feignOboxClient.getObox(scene.getOboxSerialId());
				if (oboxRes != null && oboxRes.getData() != null
						&& oboxRes.getStatus() == ResponseEnum.SelectSuccess.getStatus()) {
					feignAliClient.excuteLocalScene(scene.getOboxSceneNumber(), scene.getSceneName(),
							oboxRes.getData().getOboxSerialId());
				}else{
					res.setStatus(ResponseEnum.SendOboxFail.getStatus());
					res.setMessage(ResponseEnum.SendOboxFail.getMsg());
					return res;
				}
			}
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
}
