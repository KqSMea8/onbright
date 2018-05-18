package com.bright.apollo.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.feign.FeignOboxClient;
import com.bright.apollo.feign.FeignSceneClient;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.response.SceneInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * @Title:
 * @Description:the write operation use over two Microservice ,we cound't ensure
 *                  data consistency.
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Api("facade Controller")
@RestController
@RequestMapping("facade")
public class FacadeController {
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private FeignOboxClient feignOboxClient;
	@Autowired
	private FeignUserClient feignUserClient;
	@Autowired
	private FeignDeviceClient feignDeviceClient;
	@Autowired
	private FeignSceneClient feignSceneClient;

	// delete obox
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "delete  obox", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/obox/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject delObox(@PathVariable(required = true) String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject<TObox> oboxRes = feignOboxClient.deleteObox(serialId);
			if (oboxRes.getCode() == ResponseEnum.Success.getCode()) {
				// cound't ensure this operations all success
				feignUserClient.deleteUserOboxByOboxSerialId(serialId);
				feignDeviceClient.deleleDeviceByOboxSerialId(serialId);
			}
			return oboxRes;
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// delete device
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "delete  obox", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/device/{serialId}", method = RequestMethod.DELETE)
	public ResponseObject delDevice(@PathVariable(required = true) String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject resDevice = feignDeviceClient.delDevice(serialId);
			if (resDevice.getCode() == ResponseEnum.Success.getCode()) {
				feignUserClient.deleteUserDeviceBySerialId(serialId);
			}
			return resDevice;

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// delete scene
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "delete  scene", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/scene/{sceneNumber}", method = RequestMethod.DELETE)
	public ResponseObject delScene(@PathVariable(required = true) Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			ResponseObject resScene = feignSceneClient.deleteScene(sceneNumber);
			if (resScene.getCode() == ResponseEnum.Success.getCode()) {
				feignUserClient.deleteUserSceneBySceneNumber(sceneNumber);
			}
			return resScene;
		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// query obox by page
	@SuppressWarnings({ "rawtypes" })
	@ApiOperation(value = "get obox by user and page,the pageIndex default value is 0,the pageSize defalt value is 10", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/obox/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<TObox>> getOboxByUserAndPage(@PathVariable Integer pageIndex, @PathVariable Integer pageSize) {
		ResponseObject<List<TObox>> res = new ResponseObject<List<TObox>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setCode(ResponseEnum.RequestParamError.getCode());
				res.setMsg(ResponseEnum.RequestParamError.getMsg());
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getCode() == ResponseEnum.Success.getCode() && resUser.getData() != null) {
				TUser tUser = resUser.getData();
				if (pageIndex == null)
					pageIndex = 0;
				if (pageSize == null || pageSize <= 0)
					pageSize = 10;
				res = feignOboxClient.getOboxByUserAndPage(tUser.getId(), pageIndex, pageSize);
			} else {
				res.setCode(ResponseEnum.UnKonwUser.getCode());
				res.setMsg(ResponseEnum.UnKonwUser.getMsg());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	// query device by page
	@ApiOperation(value = "get device by user and page,the pageIndex default value is 0,the pageSize defalt value is 10", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/device/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByUserAndPage(@PathVariable Integer pageIndex,
			@PathVariable Integer pageSize) {
		ResponseObject<List<TOboxDeviceConfig>> res = new ResponseObject<List<TOboxDeviceConfig>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setCode(ResponseEnum.RequestParamError.getCode());
				res.setMsg(ResponseEnum.RequestParamError.getMsg());
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getCode() == ResponseEnum.Success.getCode() && resUser.getData() != null) {
				TUser tUser = resUser.getData();
				if (pageIndex == null)
					pageIndex = 0;
				if (pageSize == null || pageSize <= 0)
					pageSize = 10;
				res = feignDeviceClient.getDeviceByUserAndPage(tUser.getId(), pageIndex, pageSize);
			} else {
				res.setCode(ResponseEnum.UnKonwUser.getCode());
				res.setMsg(ResponseEnum.UnKonwUser.getMsg());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}

	@ApiOperation(value = "get scene by user and page,the pageIndex default value is 0,the pageSize defalt value is 10", httpMethod = "GET", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/scene/{pageIndex}/{pageSize}", method = RequestMethod.GET)
	public ResponseObject<List<SceneInfo>> getSceneByUserAndPage( 
			@PathVariable Integer pageIndex, @PathVariable Integer pageSize) {
		ResponseObject<List<SceneInfo>> res = new ResponseObject<List<SceneInfo>>();
		try {
			UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (StringUtils.isEmpty(principal.getUsername())) {
				res.setCode(ResponseEnum.RequestParamError.getCode());
				res.setMsg(ResponseEnum.RequestParamError.getMsg());
			}
			ResponseObject<TUser> resUser = feignUserClient.getUser(principal.getUsername());
			if (resUser.getCode() == ResponseEnum.Success.getCode() && resUser.getData() != null) {
				TUser tUser = resUser.getData();
				if (pageIndex == null)
					pageIndex = 0;
				if (pageSize == null || pageSize <= 0)
					pageSize = 10;
				res = feignSceneClient.getSceneByUserAndPage(tUser.getId(), pageIndex, pageSize);
			} else {
				res.setCode(ResponseEnum.UnKonwUser.getCode());
				res.setMsg(ResponseEnum.UnKonwUser.getMsg());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			res.setCode(ResponseEnum.RequestTimeout.getCode());
			res.setMsg(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
}
