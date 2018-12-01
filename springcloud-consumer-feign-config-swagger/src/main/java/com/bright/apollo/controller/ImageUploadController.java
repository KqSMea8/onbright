package com.bright.apollo.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bright.apollo.common.entity.TLocation;
import com.bright.apollo.common.entity.TUser;
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

	@ApiOperation(value = "uploadPic", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "Success", response = ResponseObject.class)
	@RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
	public ResponseObject<Map<String, Object>> uploadPic(
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(required = true, value = "building") String building,
			@RequestParam(required = true, value = "room") String room,
			@RequestParam(required = false, value = "location") Integer location,
			@RequestParam(required = false, value = "action", defaultValue = "00") String action) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
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
			if (file != null) {
				String saveToTemp = ftpService.saveToTemp(file.getOriginalFilename(), file.getInputStream(), picPathVo);
				String[] uploadFile = ftpService.uploadFile(file.getOriginalFilename(), saveToTemp, picPathVo);
				if (uploadFile != null) {
					logger.info("===uploadFile:" + uploadFile);
					if (action.equals("00")) {
						// add
					} else {
						if (location == null || location == 0) {
							res.setStatus(ResponseEnum.RequestParamError.getStatus());
							res.setMessage(ResponseEnum.RequestParamError.getMsg());
							return res;
						}
						ResponseObject<Map<String, Object>> locationRes = feignDeviceClient
								.queryLocation(resUser.getData().getId(), location);
						if (locationRes == null || locationRes.getStatus() != ResponseEnum.SelectSuccess.getStatus()
								|| locationRes.getData() == null||
								locationRes.getData().get("locations")==null
								) {
							res.setStatus(ResponseEnum.RequestParamError.getStatus());
							res.setMessage(ResponseEnum.RequestParamError.getMsg());
							return res;
						}
						Map<String, Object> data = locationRes.getData();
						List<TLocation> list=(List<TLocation>) data.get("locations");
						if(list==null||list.size()<=0){
							res.setStatus(ResponseEnum.RequestParamError.getStatus());
							res.setMessage(ResponseEnum.RequestParamError.getMsg());
							return res;
						}
						
					}
					res.setStatus(ResponseEnum.AddSuccess.getStatus());
					res.setMessage(ResponseEnum.AddSuccess.getMsg());
					return res;
				}
				logger.warn("===upload pic fature===");
			}
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	@ApiOperation(value = "deletePic", httpMethod = "DELETE", produces = "application/json")
	@ApiResponse(code = 200, message = "Success", response = ResponseObject.class)
	@RequestMapping(value = "/deletePic/{path}", method = RequestMethod.DELETE)
	public ResponseObject<Map<String, Object>> deletePic(
			@PathVariable(required=true,name="path")String path) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
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
			ftpService.deleteFtpFile(path, picPathVo);
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
}