package com.bright.apollo.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.request.OboxDTO;
import com.bright.apollo.request.RequestParam;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.zz.common.exception.AppException;
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

	@Autowired
	private FacadeController facadeController;
	 
	@SuppressWarnings("rawtypes")
	@RequestMapping("/common")
	public ResponseObject common(HttpServletRequest request, HttpServletResponse response) throws AppException,
			UnsupportedEncodingException { 
		ResponseObject res = null;
		request.setCharacterEncoding("UTF-8");
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
			return facadeController.addObox((OboxDTO) ObjectUtils.fromJsonToObject(requestParam.getValue("obox"), OboxDTO.class));
		}else if(CMDEnum.query_device_count.toString().equals(cmdEnum.toString())){
			return facadeController.queryDevcieCount();
		}else if(CMDEnum.query_device.toString().equals(cmdEnum.toString())){
			return facadeController.getDevice(requestParam.getValue("device_type"));
		}else if(CMDEnum.query_obox.toString().equals(cmdEnum.toString())){
			return facadeController.getOboxByUser();
		}else if(CMDEnum.query_obox_config.toString().equals(cmdEnum.toString())){
			return facadeController.getDeviceByObox(requestParam.getValue("obox_serial_id"));
		}else if(CMDEnum.setting_node_status.toString().equals(cmdEnum.toString())){
			return facadeController.controlDevice(requestParam.getValue("serialId"),requestParam.getValue("status"));
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
			return facadeController.getSearchNewDevice(requestParam.getValue("obox_serial_id"));
		}else {
			res = new ResponseObject();
			res.setStatus(ResponseEnum.RequestParamError.getStatus());
			res.setMessage(ResponseEnum.RequestParamError.getMsg());
			return res;
		}
	}
	 
}
