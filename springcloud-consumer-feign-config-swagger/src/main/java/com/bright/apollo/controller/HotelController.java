package com.bright.apollo.controller;

import java.util.Date;
import java.util.Map;

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
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.enums.LocationStatusEnum;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.feign.FeignQuartzClient;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.tool.MobileUtil;

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
			if(endTime<new Date().getTime()){
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			// 判断locationId与user关系
			ResponseObject<TLocation> resLocation = feignDeviceClient
					.queryLocationByUserAndLocation(resUser.getData().getId(), locationId);
			if (resLocation == null || resLocation.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TLocation tLocation = resLocation.getData();
			if (StringUtils.isEmpty(tLocation.getUserName())
					&& tLocation.getStatus() == LocationStatusEnum.TREE.getStatus()
					) {
				if(MobileUtil.checkMobile(mobile)){
					ResponseObject<TUser> userRes = feignUserClient.getUser(mobile);
					if(userRes==null||userRes.getData()==null){
						feignUserClient.addUser(mobile);
					}
					tLocation.setStatus(LocationStatusEnum.CHECK.getStatus());
					tLocation.setUserName(mobile);
					feignDeviceClient.updateLocationByObj(tLocation);
					feignQuartzClient.checkIn(locationId,mobile,endTime);
					res.setStatus(ResponseEnum.AddSuccess.getStatus());
					res.setMessage(ResponseEnum.AddSuccess.getMsg());
					return res;
				} 
				res.setStatus(ResponseEnum.ErrorMobile.getStatus());
				res.setMessage(ResponseEnum.ErrorMobile.getMsg());
				return res;
			} 
			res.setStatus(ResponseEnum.LocationNoExist.getStatus());
			res.setMessage(ResponseEnum.LocationNoExist.getMsg());
			return res;
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
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
		try {} catch (Exception e) {
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
			if(endTime<new Date().getTime()){
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			// 判断locationId与user关系
			ResponseObject<TLocation> resLocation = feignDeviceClient
					.queryLocationByUserAndLocation(resUser.getData().getId(), locationId);
			if (resLocation == null || resLocation.getData() == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TLocation tLocation = resLocation.getData();
			if(StringUtils.isEmpty(tLocation.getUserName())){
				logger.warn("===the userName is null===");
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			tLocation.setStatus(LocationStatusEnum.CHECK.getStatus());
			feignDeviceClient.updateLocationByObj(tLocation);
			//feignQuartzClient.checkIn(locationId,mobile,endTime);
			feignQuartzClient.continueLocation(locationId,tLocation.getUserName(),endTime);
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
	public ResponseObject queryDeviceByadmin() {
		ResponseObject res = new ResponseObject();
		try {

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
	public ResponseObject queryDeviceByGust() {
		ResponseObject res = new ResponseObject();
		try {

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

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
}
