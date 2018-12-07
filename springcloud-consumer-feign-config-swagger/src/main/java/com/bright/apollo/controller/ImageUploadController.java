package com.bright.apollo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bright.apollo.common.entity.TLocation;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserLocation;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.FtpService;
import com.bright.apollo.vo.PicPathVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年11月26日
 * @Version:1.1.0
 */
@Api("image Controller")
@RestController
@RequestMapping("image")
public class ImageUploadController {
	private static final Logger logger = LoggerFactory.getLogger(ImageUploadController.class);
	@Autowired
	private PicPathVo picPathVo;
	@Autowired
	private FtpService ftpService;
	@Autowired
	private FeignUserClient feignUserClient;
	@Autowired
	private FeignDeviceClient feignDeviceClient;
	@Deprecated
	@ApiOperation(value = "createLocation", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "Success", response = ResponseObject.class)
	@RequestMapping(value = "/createLocation", method = RequestMethod.POST)
	public ResponseObject<Map<String, Object>> createLocation(
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(required = true, value = "building") String building,
			@RequestParam(required = true, value = "room") String room) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
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
			String[] uploadFile =null;
			if (file != null) {
				String saveToTemp = ftpService.saveToTemp(file.getOriginalFilename(), file.getInputStream(), picPathVo);
				uploadFile = ftpService.uploadFile(file.getOriginalFilename(), saveToTemp, picPathVo);
			}
			TLocation tLocation = new TLocation();
			
			tLocation.setBuilding(building);
			tLocation.setRoom(room);
				if (uploadFile != null && uploadFile.length == 2) {
				//	TLocation tLocation = null;
					logger.info("===uploadFile:" + uploadFile);
				//	if (action.equals("00")) {
						tLocation.setDownloadUrl(uploadFile[0]);
						tLocation.setThumUrl(uploadFile[1]);
						map.put("thum_url", tLocation.getThumUrl());
						map.put("url", tLocation.getDownloadUrl());
				}
				ResponseObject<TLocation> locationRes = feignDeviceClient.addLocation(tLocation);
				if (locationRes == null || locationRes.getData() == null) {
					res.setStatus(ResponseEnum.RequestParamError.getStatus());
					res.setMessage(ResponseEnum.RequestParamError.getMsg());
					return res;
				}
				tLocation = locationRes.getData();
				TUserLocation tUserLocation = new TUserLocation();
				tUserLocation.setLocationId(tLocation.getId());
				tUserLocation.setUserId(resUser.getData().getId());
				feignDeviceClient.addUserLocation(tUserLocation);
				res.setStatus(ResponseEnum.AddSuccess.getStatus());
				res.setMessage(ResponseEnum.AddSuccess.getMsg());
						/*} else {
						if (location == null || location == 0) {
							res.setStatus(ResponseEnum.RequestParamError.getStatus());
							res.setMessage(ResponseEnum.RequestParamError.getMsg());
							return res;
						}
						ResponseObject<TLocation> locationRes = feignDeviceClient
								.queryLocationByUserAndLocation(resUser.getData().getId(), location);
						if (locationRes == null 
								|| locationRes.getData() == null  ) {
							res.setStatus(ResponseEnum.RequestParamError.getStatus());
							res.setMessage(ResponseEnum.RequestParamError.getMsg());
							return res;
						}
						Map<String, Object> data = locationRes.getData();
						List<TLocation> list = (List<TLocation>) data.get("locations");
						if (list == null || list.size() <= 0) {
							res.setStatus(ResponseEnum.RequestParamError.getStatus());
							res.setMessage(ResponseEnum.RequestParamError.getMsg());
							return res;
						}
						tLocation = locationRes.getData();
						ftpService.deleteFtpFile(picPathVo, tLocation.getDownloadUrl(), tLocation.getThumUrl());
						tLocation.setDownloadUrl(uploadFile[0]);
						tLocation.setThumUrl(uploadFile[1]);
						if (!StringUtils.isEmpty(building))
							tLocation.setBuilding(building);
						if (!StringUtils.isEmpty(room))
							tLocation.setRoom(room);
						feignDeviceClient.updateLocationByObj(tLocation);
						res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
						res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
					}*/
				 	
					map.put("location", tLocation.getId());
					map.put("room", tLocation.getRoom());
					map.put("building", tLocation.getBuilding());
					
 					res.setData(map);

					return res;
				//}
		 
			//}
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	@ApiOperation(value = "updateLocation", httpMethod = "PUT", produces = "application/json")
	@ApiResponse(code = 200, message = "Success", response = ResponseObject.class)
	@RequestMapping(value = "/updateLocation", method = RequestMethod.PUT)
	public ResponseObject<Map<String, Object>> updateLocation(
			@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(required = true, value = "building") String building,
			@RequestParam(required = true, value = "room") String room,
			@RequestParam(required = true, value = "location") Integer location) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
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
			ResponseObject<TLocation> locationRes = feignDeviceClient
					.queryLocationByUserAndLocation(resUser.getData().getId(), location);
			if (locationRes == null 
					|| locationRes.getData() == null  ) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			TLocation tLocation = locationRes.getData();
			tLocation.setBuilding(building);
			tLocation.setRoom(room);
			String[] uploadFile =null;
			if (file != null) {
				String saveToTemp = ftpService.saveToTemp(file.getOriginalFilename(), file.getInputStream(), picPathVo);
				uploadFile = ftpService.uploadFile(file.getOriginalFilename(), saveToTemp, picPathVo);
				ftpService.deleteFtpFile(picPathVo, tLocation.getDownloadUrl(), tLocation.getThumUrl());
				tLocation.setDownloadUrl(uploadFile[0]);
				tLocation.setThumUrl(uploadFile[1]);
			}
			feignDeviceClient.updateLocationByObj(tLocation);
			map.put("location", tLocation.getId());
			map.put("room", tLocation.getRoom());
			map.put("building", tLocation.getBuilding());
			if(!StringUtils.isEmpty(tLocation.getDownloadUrl())){
				map.put("url", tLocation.getDownloadUrl());
			}
			if(!StringUtils.isEmpty(tLocation.getThumUrl())){
				map.put("thum_url", tLocation.getThumUrl());
			}
			res.setData(map);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
}
