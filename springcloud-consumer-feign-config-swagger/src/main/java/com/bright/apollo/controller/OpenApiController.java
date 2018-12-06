package com.bright.apollo.controller;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月4日  
 *@Version:1.1.0  
 */
@Api("openAPI Controller")
@RestController
@RequestMapping("open")
public class OpenApiController {

	private static final Logger logger = LoggerFactory.getLogger(OpenApiController.class);

	/**
	 * @param 手机号码
	 * @param 酒店唯一标识
	 * @param 房间标识
	 * @param 开房时间
	 * @param 退房时间
	 * @param 临时开锁密码标识
	 * @return success 
	 * @Description:
	 * 1.判断手机号码是否存在，不存在怎么处理
	 * 2.判断酒店是否与用户存在关联关系
	 * 3.判断房间是否存在，以及是否属于空置状态
	 * 4.开房时间和退房时间是否符合逻辑
	 * 5.房间是否包含门锁
	 * 6.分配房间给mobile用户
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "checkInHotel", httpMethod = "POST", produces = "application/json")
	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/checkInHotel", method = RequestMethod.POST)
	public ResponseObject checkInHotel( @RequestBody(required=true) Map<String, Object>map) {
		ResponseObject res=new  ResponseObject();
		try {
			//校验数据
		} catch (Exception e) {
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
			logger.error("===error msg:"+e.getMessage());
		}
		return res;
	}
	/**
	 * @param 手机号码
	 * @param 酒店唯一标识
	 * @param 房间标识
	 * @param 退房时间
	 * @return success 
	 * @Description:
	 * 1.判断手机号码是否存在，不存在怎么处理
	 * 2.判断酒店是否与用户存在关联关系
	 * 3.判断房间是否存在，以及是否处在用户
	 * 4.退房时间是否符合逻辑
	 * 5.房间是否包含门锁
	 * 6.重新判断用户退房时间，更改远程密码
	 */
	
 
}
