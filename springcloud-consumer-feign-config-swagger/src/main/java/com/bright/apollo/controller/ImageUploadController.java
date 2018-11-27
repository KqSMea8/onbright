package com.bright.apollo.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.FtpService;
import com.bright.apollo.tool.MD5;
import com.bright.apollo.vo.PicPathVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月26日  
 *@Version:1.1.0  
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
	@ApiOperation(value = "uploadPic", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "Success", response = ResponseObject.class)
	@RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
	public ResponseObject<Map<String, Object>> uploadPic(
			@RequestParam(value="file",required=true) MultipartFile file//,
			/*@RequestParam(required=true,value="building") String building,
			@RequestParam(required=true,value="room") String room,
			@RequestParam(required=true,value="location") Integer location,
			@RequestParam(required=false,value="action",defaultValue="00") String action*/
			) {
		ResponseObject<Map<String, Object>> res = new ResponseObject<Map<String, Object>>();
		try {
			//file.get
		} catch (Exception e) {
			logger.error("===error msg:"+e.getMessage());
			res.setStatus(ResponseEnum.RequestTimeout.getStatus());
			res.setMessage(ResponseEnum.RequestTimeout.getMsg());
		}
		return res;
	}
	
}
