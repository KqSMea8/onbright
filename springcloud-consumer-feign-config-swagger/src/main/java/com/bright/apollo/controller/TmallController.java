package com.bright.apollo.controller;


import com.bright.apollo.redis.RedisBussines;

import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月16日  
 *@Version:1.1.0  
 */
//@Api("device Controller")
@RequestMapping("tmall")
@RestController
public class TmallController {
	Logger logger = Logger.getLogger(TmallController.class);

	@Autowired
	private RedisBussines redisBussines;

	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@ApiOperation(value = "get deivcie by device serialId", httpMethod = "GET", produces = "application/json")
//	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/tmallCmd", method = RequestMethod.POST)
	public Object tmallCmd(@RequestBody Object object, HttpServletResponse response) throws IOException {

		logger.info("====== messageID ======"+object);
		Map<String,Object> requestMap = (Map<String, Object>) object;
		Map<String,Object> requestHeaderMap = (Map<String, Object>) requestMap.get("header");
		JSONObject map = new JSONObject();
		JSONObject headerMap = new JSONObject();
		JSONObject playloadMap = new JSONObject();
		String responstStr = "Response";
//		response.sendRedirect("http://localhost:8201/user/forget/13522333323");
		if(requestHeaderMap.get("namespace").equals("DiscoveryDevicesResponse")){//天猫精灵发现设备



			headerMap.put("namespace","AliGenie.Iot.Device.Discovery");
			headerMap.put("name","DiscoveryDevicesResponse");
			headerMap.put("messageId",requestHeaderMap.get("messageId"));
			headerMap.put("payLoadVersion","1");
			map.put("header",headerMap);

			JSONArray jsonArray = new JSONArray();
			JSONArray propertiesJsonArray = new JSONArray();
			JSONObject propertiesMap = new JSONObject();
			propertiesMap.put("name","color");
			propertiesMap.put("value","Red");
			propertiesJsonArray.put(propertiesMap);
			JSONObject extensionsMap = new JSONObject();
			extensionsMap.put("extension1","");
			extensionsMap.put("extension2","");
			JSONObject devices = new JSONObject();

			devices.put("deviceId","34ea34cf2e63");
			devices.put("deviceName","light1");
			devices.put("deviceType","light");
			devices.put("zone","");
			devices.put("brand","1");
			devices.put("model","1");
			devices.put("icon","https://git.cn-hangzhou.oss-cdn.aliyun-inc.com/uploads/aicloud/aicloud-proxy-service/41baa00903a71c97e3533cf4e19a88bb/image.png");
			devices.put("properties",propertiesJsonArray);
			String[] actions = new String[2];
			actions[0] = "TurnOn";
			actions[1] = "TurnOff";
			devices.put("actions",actions);
			devices.put("extensions",extensionsMap);
			jsonArray.put(devices);
			playloadMap.put("devices",jsonArray);

			map.put("payload",playloadMap);

		}else{//天猫精灵控制设备
			Map<String,Object> playLoadMap = (Map<String, Object>) requestMap.get("payload");
			String name = (String)requestHeaderMap.get("name");
			headerMap.put("namespace","AliGenie.Iot.Device.Control");
			headerMap.put("name",name+"Response");
			headerMap.put("messageId",requestHeaderMap.get("messageId"));
			headerMap.put("payLoadVersion","1");
			map.put("header",headerMap);
			playloadMap.put("deviceId",playLoadMap.get("deviceId"));
			map.put("payload",playloadMap);
		}
		logger.info("map ====== "+map);
		return map.toString();
	}



}
