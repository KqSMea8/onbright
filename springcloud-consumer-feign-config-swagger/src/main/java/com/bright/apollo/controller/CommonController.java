package com.bright.apollo.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.request.OboxDTO;
import com.bright.apollo.request.RequestParam;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.zz.common.exception.AppException;
import com.zz.common.log.LogService;
import com.zz.common.util.ObjectUtils;

/**
 * @Title:
 * @Description: for old api
 * @Author:JettyLiu
 * @Since:2018年6月29日
 * @Version:1.1.0
 */
@RestController
public class CommonController {
	private Logger logger = Logger.getLogger(getClass());
	@Autowired
	private FacadeController facadeController;
	 
	@SuppressWarnings("rawtypes")
	@RequestMapping("/common")
	public ResponseObject common(HttpServletRequest request, HttpServletResponse response) throws AppException,
			UnsupportedEncodingException { 
		ResponseObject res = null;
		request.setCharacterEncoding("UTF-8");
		printRequest(request);
		String CMD = request.getParameter("CMD");
		RequestParam requestParam = new RequestParam(request.getParameterMap());
		if (StringUtils.isEmpty(CMD)) {
			res = new ResponseObject();
			res.setStatus(ResponseEnum.RequestParamError.getStatus());
			res.setMessage(ResponseEnum.RequestParamError.getMsg());
			return res;
		}
		CMDEnum cmdEnum = CMDEnum.getCMDEnum(CMD);
		if (cmdEnum == null) {
			res = new ResponseObject();
			res.setStatus(ResponseEnum.RequestParamError.getStatus());
			res.setMessage(ResponseEnum.RequestParamError.getMsg());
			return res;
		}
		if (CMDEnum.regist_aliDev.toString().equals(cmdEnum.toString()))
			return facadeController.registAliDev(requestParam.getValue("type"), requestParam.getValue("zone"));
		else if(CMDEnum.add_obox.toString().equals(cmdEnum.toString())){
			ResponseObject addObox = facadeController.addObox((OboxDTO) ObjectUtils.fromJsonToObject(requestParam.getValue("obox"), OboxDTO.class));
			if(addObox!=null&&addObox.getStatus()<300){
				Map<String, Object>map=new HashMap<String, Object>();
				map.put("obox_serial_id", ((OboxDTO) ObjectUtils.fromJsonToObject(requestParam.getValue("obox"), OboxDTO.class)).getOboxSerialId());
				addObox.setData(map);
			}
			return addObox;
		}else if(CMDEnum.query_device_count.toString().equals(cmdEnum.toString())){
			ResponseObject queryDevcieCount = facadeController.queryDevcieCount();
			if(queryDevcieCount!=null&&queryDevcieCount.getData()!=null){
				Map<String, Object>map=new HashMap<String, Object>();
				map.put("devices", queryDevcieCount.getData());
				queryDevcieCount.setData(map);
			}
			return queryDevcieCount;
		}else if(CMDEnum.query_device.toString().equals(cmdEnum.toString())){
			ResponseObject device = facadeController.getDevice(requestParam.getValue("device_type"));
			if(device!=null&&device.getData()!=null){
				Map<String, Object>map=new HashMap<String, Object>();
				map.put("config", device.getData());
				device.setData(map);
			}
			return device;
		}else if(CMDEnum.query_obox.toString().equals(cmdEnum.toString())){
			ResponseObject oboxByUser = facadeController.getOboxByUser();
			if(oboxByUser!=null&&oboxByUser.getData()!=null){
				Map<String, Object>map=new HashMap<String, Object>();
				map.put("oboxs", oboxByUser.getData());
				oboxByUser.setData(map);
			}
			return oboxByUser;
			//之前的代码要改回来
		}else if(CMDEnum.query_obox_config.toString().equals(cmdEnum.toString())){
			ResponseObject deviceByObox = facadeController.getDeviceByObox(requestParam.getValue("obox_serial_id"));
			if(deviceByObox!=null&&deviceByObox.getData()!=null){
				Map<String, Object>map=new HashMap<String, Object>();
				map.put("config", deviceByObox.getData());
				deviceByObox.setData(map);
			}
			return deviceByObox;
		}else if(CMDEnum.setting_node_status.toString().equals(cmdEnum.toString())){
			ResponseObject controlDevice = facadeController.controlDevice(requestParam.getValue("serialId"),requestParam.getValue("status"));
			if(controlDevice!=null&&controlDevice.getStatus()<300){
				Map<String, Object>map=new HashMap<String, Object>();
				map.put("serialId",requestParam.getValue("serialId"));
				map.put("status",requestParam.getValue("status"));
 				controlDevice.setData(map);
			}
			return controlDevice;
		}else if(CMDEnum.search_new_device.toString().equals(cmdEnum.toString())){
			String state = requestParam.getValue("state");
			if(StringUtils.isEmpty(state)){
				res = new ResponseObject();
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}else if(state.equals("01")){
				return facadeController.searchDevicesByOldStyle(requestParam.getValue("obox_serial_id"),requestParam.getValue("device_type"),
						requestParam.getValue("device_child_type"),requestParam.getValue("serialId")
						);
			}else if(state.equals("00")){
				return facadeController.stopScan(requestParam.getValue("obox_serial_id")
						);
			}else if(state.equals("02")){
				return facadeController.searchDevicesByNewStyle(requestParam.getValue("obox_serial_id"),requestParam.getValue("device_type"),
						requestParam.getValue("device_child_type"),requestParam.getValue("serialId")
						);
			}else if(state.equals("03")){
				return facadeController.searchDevicesByInitiative(requestParam.getValue("obox_serial_id"),requestParam.getValue("device_type"),
						requestParam.getValue("device_child_type"),requestParam.getValue("serialId")
						);
			}else{
				res = new ResponseObject();
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
		}else if(CMDEnum.getting_new_device.toString().equals(cmdEnum.toString())){
			ResponseObject searchNewDevice = facadeController.getSearchNewDevice(requestParam.getValue("obox_serial_id"));
			if(searchNewDevice!=null&&searchNewDevice.getData()!=null){
				Object data = searchNewDevice.getData();
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("nodes", data);
				searchNewDevice.setData(map);
			}
			return searchNewDevice;
		}else {
			res = new ResponseObject();
			res.setStatus(ResponseEnum.RequestParamError.getStatus());
			res.setMessage(ResponseEnum.RequestParamError.getMsg());
			return res;
		}
	}
	public  void printRequest(HttpServletRequest request){
		logger.debug("=============RECV REQUEST PARAMS START=============");
		logger.debug("URL="+request.getRequestURL());
		for (String name : request.getParameterMap().keySet()) {
			logger.debug("name=" + name + ";value=" + request.getParameter(name)); 
		}
		logger.debug("============RECV REQUEST PARAMS END=============");
	}
}
