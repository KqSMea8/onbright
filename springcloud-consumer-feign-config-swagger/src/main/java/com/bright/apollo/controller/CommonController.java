package com.bright.apollo.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.ErrorEnum;
import com.bright.apollo.enums.OperateTypeEnum;
import com.bright.apollo.enums.SceneTypeEnum;
import com.bright.apollo.request.OboxDTO;
import com.bright.apollo.request.RequestParam;
import com.bright.apollo.request.SceneDTO;
import com.bright.apollo.request.TIntelligentFingerPushDTO;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.tool.MobileUtil;
import com.bright.apollo.tool.NumberHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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
	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
	@Autowired
	private FacadeController facadeController;
	@Autowired
	private SceneController sceneController;
	// @Autowired
	// private DeviceController deviceController;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/common")
	public ResponseObject common(HttpServletRequest request, HttpServletResponse response)
			throws AppException, UnsupportedEncodingException {
		ResponseObject res = null;
		request.setCharacterEncoding("UTF-8");
		// printRequest(request);
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
		else if (CMDEnum.add_obox.toString().equals(cmdEnum.toString())) {
			logger.info(" ------ add obox begin ------ ");
			logger.info(" ------ requestParam.getValue('obox') ------ " + requestParam.getValue("obox"));
			ResponseObject addObox = facadeController
					.addObox((OboxDTO) ObjectUtils.fromJsonToObject(requestParam.getValue("obox"), OboxDTO.class));
			logger.info("addObox obj  ------" + addObox.getData());
			if (addObox != null && addObox.getStatus() < 300) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("obox_serial_id",
						((OboxDTO) ObjectUtils.fromJsonToObject(requestParam.getValue("obox"), OboxDTO.class))
								.getOboxSerialId());
				addObox.setData(map);
				logger.info("map  ------" + map);
			}
			logger.info(" ------ add obox end ------ ");
			return addObox;
		} else if (CMDEnum.query_device_count.toString().equals(cmdEnum.toString())) {
			logger.info("===query_device_count===");
			ResponseObject queryDevcieCount = facadeController.queryDevcieCount();
			if (queryDevcieCount != null && queryDevcieCount.getData() != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("devices", queryDevcieCount.getData());
				queryDevcieCount.setData(map);
			}
			return queryDevcieCount;
		} else if (CMDEnum.query_device.toString().equals(cmdEnum.toString())) {
			ResponseObject device = facadeController.getDevice(requestParam.getValue("device_type"));
			if (device != null && device.getData() != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("config", device.getData());
				device.setData(map);
			}
			return device;
		} else if (CMDEnum.query_obox.toString().equals(cmdEnum.toString())) {
			ResponseObject oboxByUser = facadeController.getOboxByUser();
			if (oboxByUser != null && oboxByUser.getData() != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("oboxs", oboxByUser.getData());
				oboxByUser.setData(map);
			}
			return oboxByUser;
			// 之前的代码要改回来
		} else if (CMDEnum.query_obox_config.toString().equals(cmdEnum.toString())) {
			ResponseObject deviceByObox = facadeController.getDeviceByObox(requestParam.getValue("obox_serial_id"));
			if (deviceByObox != null && deviceByObox.getData() != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("config", deviceByObox.getData());
				deviceByObox.setData(map);
			}
			return deviceByObox;
		} else if (CMDEnum.setting_node_status.toString().equals(cmdEnum.toString())) {
			ResponseObject controlDevice = facadeController.controlDevice(requestParam.getValue("serialId"),
					requestParam.getValue("status"));
			if (controlDevice != null && controlDevice.getStatus() < 300) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("serialId", requestParam.getValue("serialId"));
				map.put("status", requestParam.getValue("status"));
				controlDevice.setData(map);
			}
			return controlDevice;
		} else if (CMDEnum.search_new_device.toString().equals(cmdEnum.toString())) {
			String state = requestParam.getValue("state");
			if (StringUtils.isEmpty(state)) {
				res = new ResponseObject();
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			} else if (state.equals("01")) {
				return facadeController.searchDevicesByOldStyle(requestParam.getValue("obox_serial_id"),
						requestParam.getValue("device_type"), requestParam.getValue("device_child_type"),
						requestParam.getValue("serialId"));
			} else if (state.equals("00")) {
				return facadeController.stopScan(requestParam.getValue("obox_serial_id"));
			} else if (state.equals("02")) {
				return facadeController.searchDevicesByNewStyle(requestParam.getValue("obox_serial_id"),
						requestParam.getValue("device_type"), requestParam.getValue("device_child_type"),
						requestParam.getValue("serialId"));
			} else if (state.equals("03")) {
				return facadeController.searchDevicesByInitiative(requestParam.getValue("obox_serial_id"),
						requestParam.getValue("device_type"), requestParam.getValue("device_child_type"),
						requestParam.getValue("serialId"));
			} else {
				res = new ResponseObject();
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
		} else if (CMDEnum.getting_new_device.toString().equals(cmdEnum.toString())) {
			ResponseObject searchNewDevice = facadeController
					.getSearchNewDevice(requestParam.getValue("obox_serial_id"));
			if (searchNewDevice != null && searchNewDevice.getData() != null) {
				Object data = searchNewDevice.getData();
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("nodes", data);
				searchNewDevice.setData(map);
			}
			return searchNewDevice;
		} else if (CMDEnum.setting_sc_info.toString().equals(cmdEnum.toString())) {
			String sceneString = requestParam.getValue("scene");
			SceneDTO sceneDTO = (SceneDTO) ObjectUtils.fromJsonToObject(sceneString, SceneDTO.class);
			if (sceneDTO == null) {
				res = new ResponseObject();
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if ((sceneDTO.getSceneNumber() == null || sceneDTO.getSceneNumber().intValue() == 0)
					&& (StringUtils.isEmpty(sceneDTO.getSceneType())
							|| sceneDTO.getSceneType().equals(SceneTypeEnum.server.getValue()))) {
				sceneDTO.setSceneType(SceneTypeEnum.server.getValue());
				return facadeController.addServerScene(sceneDTO);
			} else if ((sceneDTO.getSceneNumber() == null || sceneDTO.getSceneNumber().intValue() == 0)
					&& (sceneDTO.getSceneType().equals(SceneTypeEnum.local.getValue()))) {
				return facadeController.addLocalScene(sceneDTO);
			} else if (sceneDTO.getSceneNumber() != null && sceneDTO.getSceneNumber().intValue() != 0) {
				ResponseObject<TScene> sceneRes = sceneController.getSceneBySceneNumber(sceneDTO.getSceneNumber());
				if (sceneRes != null && sceneRes.getData() != null) {
					if (sceneRes.getData().getSceneType().equals(SceneTypeEnum.server.getValue())) {
						return facadeController.modifyServerScene(sceneDTO);
					} else {
						return facadeController.modifyLocalScene(sceneDTO);
					}
				}
			}
		} else if (CMDEnum.execute_sc.toString().equals(cmdEnum.toString())) {
			String sceneNumber = requestParam.getValue("scene_number");
			String sceneStatus = requestParam.getValue("scene_status");
			if (!StringUtils.isEmpty(sceneNumber) && !StringUtils.isEmpty(sceneStatus)
					&& NumberHelper.isNumeric(sceneNumber)) {
				if (sceneStatus.equals("03")) {
					return facadeController.deleteScene(Integer.parseInt(sceneNumber));
				} else if (sceneStatus.equals("02")) {
					return facadeController.excuteScene(Integer.parseInt(sceneNumber));
				} else if (sceneStatus.equals("01") || sceneStatus.equals("00")) {
					return facadeController.enableScene(Integer.parseInt(sceneNumber), sceneStatus);
				} else if (sceneStatus.equals("10") || sceneStatus.equals("11") || sceneStatus.equals("12")
						|| sceneStatus.equals("13")) {
					return sceneController.updateSceneSendSetting(Integer.parseInt(sceneNumber), sceneStatus);
				}
			}
		} else if (CMDEnum.modify_device.toString().equals(cmdEnum.toString())) {
			String device_serial_id = requestParam.getValue("serialId");
			String operate_type = requestParam.getValue("operate_type");
			String name = requestParam.getValue("name");
			if (!StringUtils.isEmpty(requestParam)) {
				if (operate_type.equals("00")) {
					return facadeController.deleteDevice(device_serial_id);
				} else if (operate_type.equals("01")) {
					return facadeController.modifyDeviceName(device_serial_id, name);
				}
			}
		} else if (CMDEnum.query_intelligent_fingerHome.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			if (!StringUtils.isEmpty(serialId)) {
				return facadeController.getIntelligentFingerHome(serialId);
			}
		} else if (CMDEnum.query_intelligent_openRecord.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			if (!StringUtils.isEmpty(serialId)) {
				return facadeController.getIntelligentFingerOpenRecord(serialId);
			}
		} else if (CMDEnum.query_intelligent_warningRecord.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			if (!StringUtils.isEmpty(serialId)) {
				return facadeController.getIntelligentFingerWarnRecord(serialId);
			}
		} else if (CMDEnum.query_intelligent_useringRecord.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			if (!StringUtils.isEmpty(serialId)) {
				return facadeController.getIntelligentUseringRecord(serialId);
			}
		} else if (CMDEnum.edit_intelligent_user.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String pin = requestParam.getValue("pin");
			String nickName = requestParam.getValue("nickName");
			String mobile = requestParam.getValue("mobile");
			String validateCode = requestParam.getValue("validateCode");
			if (!StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(pin)) {
				return facadeController.updateIntelligentUser(serialId, pin, nickName, mobile, validateCode);
			}
		} else if (CMDEnum.send_intelligent_validateCode.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String pin = requestParam.getValue("pin");
			String mobile = requestParam.getValue("mobile");
			if (!StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(pin)) {
				return facadeController.sendIntelligentValidateCode(serialId, pin, mobile);
			}
		} else if (CMDEnum.add_intelligent_authPwd.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String pwd = requestParam.getValue("pwd");
			if (!StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(pwd)) {
				return facadeController.addIntelligentAuthPwd(serialId, pwd);
			}
		} else if (CMDEnum.query_intelligent_authPwd.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String pwd = requestParam.getValue("pwd");
			if (!StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(pwd)) {
				return facadeController.getIntelligentAuthPwd(serialId, pwd);
			}
		} else if (CMDEnum.query_intelligent_remote_unLocking.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String authToken = requestParam.getValue("authToken");
			if (!StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(authToken)) {
				return facadeController.getIntelligentRemoteUnLocking(serialId, authToken);
			}
		} else if (CMDEnum.add_intelligent_remote_user.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String authToken = requestParam.getValue("authToken");
			String nickName = requestParam.getValue("nickName");
			String startTime = requestParam.getValue("startTime");
			String endTime = requestParam.getValue("endTime");
			String times = requestParam.getValue("times");
			String mobile = requestParam.getValue("mobile");
			String isMax = requestParam.getValue("isMax");
			if (!StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(authToken) && !StringUtils.isEmpty(nickName)
					&& !StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime)
					&& NumberHelper.isNumeric(startTime) && NumberHelper.isNumeric(endTime) && startTime.length() == 13
					&& endTime.length() == 13 && Long.parseLong(endTime) > Long.parseLong(startTime)
					&& Long.parseLong(endTime) > new Date().getTime()) {
				return facadeController.addIntelligentRemoteUser(serialId, authToken, nickName, startTime, endTime,
						times, mobile, isMax);
			}
		} else if (CMDEnum.del_intelligent_remote_user.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String id = requestParam.getValue("id");
			String authToken = requestParam.getValue("authToken");
			if (!StringUtils.isEmpty(id) && !StringUtils.isEmpty(authToken) && !StringUtils.isEmpty(serialId)) {
				return facadeController.delIntelligentRemoteUser(Integer.parseInt(id), serialId, authToken);
			}
		} else if (CMDEnum.reset_intelligent_pwd.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String oldPwd = requestParam.getValue("oldPwd");
			String newPwd = requestParam.getValue("newPwd");
			if (!StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(oldPwd) && !StringUtils.isEmpty(newPwd)) {
				return facadeController.updateIntelligentPwd(serialId, oldPwd, newPwd);
			}
		} else if (CMDEnum.query_intelligent_push_list.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			if (!StringUtils.isEmpty(serialId)) {
				return facadeController.getIntelligentPushList(serialId);
			}
		} else if (CMDEnum.modify_intelligent_remote_user.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String id = requestParam.getValue("id");
			String authToken = requestParam.getValue("authToken");
			String nickName = requestParam.getValue("nickName");
			String startTime = requestParam.getValue("startTime");
			String endTime = requestParam.getValue("endTime");
			String times = requestParam.getValue("times");
			String mobile = requestParam.getValue("mobile");
			String isMax = requestParam.getValue("isMax");
			if (!StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(authToken) && !StringUtils.isEmpty(id)
					&& NumberHelper.isNumeric(id) && !StringUtils.isEmpty(nickName) && !StringUtils.isEmpty(startTime)
					&& NumberHelper.isNumeric(startTime)
					&& (!StringUtils.isEmpty(endTime) && NumberHelper.isNumeric(endTime))) {
				return facadeController.updateIntelligentRemoteUser(serialId, Integer.parseInt(id), authToken, nickName,
						Long.parseLong(startTime), Long.parseLong(endTime), times, mobile, isMax);
			}
		} else if (CMDEnum.modify_intelligent_push.toString().equals(cmdEnum.toString())) {
			// modify
			String serialId = requestParam.getValue("serialId");
			String mobile = requestParam.getValue("mobile");
			String pushInfo = requestParam.getValue("pushInfo");
			if (StringUtils.isEmpty(serialId) || StringUtils.isEmpty(pushInfo)) {
			} else {
				return facadeController.updateIntelligentRemoteUser(serialId, mobile,
						getObjectList(pushInfo, TIntelligentFingerPushDTO.class));
			}
		} else if (CMDEnum.send_remote_pwd.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String pin = requestParam.getValue("pin");
			String authToken = requestParam.getValue("authToken");
			String mobile = requestParam.getValue("mobile");
			if (StringUtils.isEmpty(serialId) || StringUtils.isEmpty(authToken) || StringUtils.isEmpty(mobile)
					|| !MobileUtil.checkMobile(mobile) || StringUtils.isEmpty(pin)) {
			} else {
				return facadeController.sendRemotePwd(serialId, pin, authToken, mobile);
			}
		} else if (CMDEnum.reset_intelligent_pwd_by_code.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String appkey = requestParam.getValue("appkey");
			String pwd = requestParam.getValue("pwd");
			String code = requestParam.getValue("code");
			if (StringUtils.isEmpty(serialId) || StringUtils.isEmpty(appkey) || StringUtils.isEmpty(pwd)
					|| StringUtils.isEmpty(code)) {
			} else {
				return facadeController.resetIntelligentPwdByCode(serialId, appkey, pwd, code);
			}
		} else if (CMDEnum.query_fingerprint_user.toString().equals(cmdEnum.toString())) {
			// String accessToken = requestParam.getValue(ACCESS_TOKEN);
			String serialId = requestParam.getValue("serialId");
			// String startIndex = requestParam.getValue("start");
			// String countIndex = requestParam.getValue("count");
			String type = requestParam.getValue("type");
			if (!StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(type)) {
				if (type.equals("01"))
					return facadeController.queryIntelligentUser(serialId);
			}
		} else if (CMDEnum.query_user_operation_history.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String type = requestParam.getValue("type");
			String fromDate = requestParam.getValue("from_date");
			String toDate = requestParam.getValue("to_date");
			String startIndex = requestParam.getValue("start_index");
			String countIndex = requestParam.getValue("count");
			if (!StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(type)) {
				return facadeController.queryUserOperationHistory(serialId, type, fromDate, toDate, startIndex,
						countIndex);
			}
		} else if (CMDEnum.query_device_status_history.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			String type = requestParam.getValue("type");
			// 00
			String start = requestParam.getValue("start");
			String count = requestParam.getValue("count");
			// 01
			String from = requestParam.getValue("from_data");
			String to = requestParam.getValue("to_data");
			if (!StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(type)) {
				return facadeController.queryDeviceStatusHistory(serialId, type, start, count, from, to);
			}
		} else if (CMDEnum.get_status.toString().equals(cmdEnum.toString())) {
			String nodeString = requestParam.getValue("nodes");
			if (!StringUtils.isEmpty(nodeString)) {
				List<String> mList = (List<String>) ObjectUtils.fromJsonToObject(nodeString, List.class);
				return facadeController.queryStatus(mList);
			}
		} else if (CMDEnum.query_node_real_status.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			if (!StringUtils.isEmpty(serialId)) {
				return facadeController.queryNodeStatus(serialId);
			}
		} else if (CMDEnum.query_scenes.toString().equals(cmdEnum.toString())) {
			String start = requestParam.getValue("start");
			String count = requestParam.getValue("count");
			return facadeController.queryScenes(start, count);
		} else if (CMDEnum.query_scenenumber_by_addr.toString().equals(cmdEnum.toString())) {
			String oboxSceneNumber = requestParam.getValue("obox_scene_number");
			String oboxSerialId = requestParam.getValue("obox_serial_id");
			if (!StringUtils.isEmpty(oboxSceneNumber) && !StringUtils.isEmpty(oboxSerialId)
					&& NumberHelper.isNumeric(oboxSceneNumber) && Integer.parseInt(oboxSceneNumber) > 0) {
				return facadeController.querySceneNumberByAddr(oboxSceneNumber, oboxSerialId);
			}
		} else if (CMDEnum.delete_obox.toString().equals(cmdEnum.toString())) {
			// modify
			String obox_serial_id = requestParam.getValue("obox_serial_id");
			if (!StringUtils.isEmpty(obox_serial_id)) {
				return facadeController.deleteObox(obox_serial_id);
			}
		} else if (CMDEnum.set_pwd.toString().equals(cmdEnum.toString())) {
			// String type = requestParam.getValue("type");
			String pwd = requestParam.getValue("pwd");
			if (!StringUtils.isEmpty(pwd)) {
				return facadeController.modifyUserPwd(pwd);
			}
		} else if (CMDEnum.setting_remote_led.toString().equals(cmdEnum.toString())) {
			String type = requestParam.getValue("type");
			String obox_serial_id = requestParam.getValue("obox_serial_id");
			String serialId = requestParam.getValue("serialId");
			String status = requestParam.getValue("status");
			String addr = requestParam.getValue("addr");
			// addr
			if (!StringUtils.isEmpty(type) && !StringUtils.isEmpty(obox_serial_id)) {
				if (type.equals("1")) {
					// add
					return facadeController.addRemoteLed(obox_serial_id);
				} else if (type.equals("2") && !StringUtils.isEmpty(serialId)) {
					// del
					return facadeController.delRemoteLed(obox_serial_id, serialId);
				} else if (type.equals("3") && !StringUtils.isEmpty(serialId) && !StringUtils.isEmpty(status)) {
					// control
					return facadeController.controlRemoteLed(obox_serial_id, serialId, status);
				}
			}
		} else if (CMDEnum.query_ali_dev.toString().equals(cmdEnum.toString())) {// 阿里wifi设备查询
			return facadeController.queryAliDevice();
		} else if (CMDEnum.set_ali_dev.toString().equals(cmdEnum.toString())) {
			String value = requestParam.getValue("value");
			String deviceId = requestParam.getValue("deviceId");
			return facadeController.setAliDevice(value, deviceId);
		} else if (CMDEnum.read_ali_dev.toString().equals(cmdEnum.toString())) {
			String functionId = requestParam.getValue("functionId");
			String deviceId = requestParam.getValue("deviceId");
			String value = requestParam.getValue("value");
			return facadeController.readAliDevice(functionId, deviceId);
		} else if (CMDEnum.query_timer.toString().equals(cmdEnum.toString())) {
			String deviceId = requestParam.getValue("deviceId");
			return facadeController.queryAliDeviceTimer(deviceId);
		} else if (CMDEnum.set_timer.toString().equals(cmdEnum.toString())) {
			String command = requestParam.getValue("command");
			String deviceId = requestParam.getValue("deviceId");
			String timer = requestParam.getValue("timer");
			String timerValue = requestParam.getValue("value");
			String timerId = requestParam.getValue("timerId");
			if (timerId == null || StringUtils.isEmpty(timerId)) {
				timerId = "0";
			}
			return facadeController.setAliTimer(deviceId, command, timer, timerValue, timerId);
		} else if (CMDEnum.upload_config.toString().equals(cmdEnum.toString())) {
			String deviceName = requestParam.getValue("deviceName");
			String productKey = requestParam.getValue("productKey");
			String config = requestParam.getValue("config");
			return facadeController.uploadAliDevice(deviceName, productKey, config);
		} else if (CMDEnum.set_countdown.toString().equals(cmdEnum.toString())) {
			String command = requestParam.getValue("command");
			String deviceId = requestParam.getValue("deviceId");
			String timer = requestParam.getValue("timer");
			String timerValue = requestParam.getValue("value");
			return facadeController.setAliCountdown(deviceId, command, timer, timerValue);
		} else if (CMDEnum.delete_ali_dev.toString().equals(cmdEnum.toString())) {
			String deviceId = requestParam.getValue("deviceId");
			// String value = requestParam.getValue("value");
			return facadeController.delAliDevice(deviceId);
		} else if (CMDEnum.query_countdown.toString().equals(cmdEnum.toString())) {
			String deviceId = requestParam.getValue("deviceId");
			return facadeController.queryCountDown(deviceId);
		} else if (CMDEnum.query_remote_control.toString().equals(cmdEnum.toString())) {// 获取遥控器列表
			String brandId = requestParam.getValue("brandId");
			String deviceType = requestParam.getValue("deviceType");
			return facadeController.getIrList(brandId, deviceType);
		} else if (CMDEnum.register_device.toString().equals(cmdEnum.toString())) {// 获取遥控云遥控类型

			return null;
		} else if (CMDEnum.query_brand.toString().equals(cmdEnum.toString())) {// 获取遥控云品牌类型

			return null;
		} else if (CMDEnum.query_remote_control_id.toString().equals(cmdEnum.toString())) {// 获取某个遥控器对应的详情码库

			return null;
		} else if (CMDEnum.query_remote_control_src.toString().equals(cmdEnum.toString())) {// 根据品牌ID、设备类型一键匹配遥控器列表

			return null;
		} else if (CMDEnum.bind_remote_control.toString().equals(cmdEnum.toString())) {// 绑定/解绑红外

			return null;
		} else if (CMDEnum.modify_ir_program.toString().equals(cmdEnum.toString())) {// 修改红外转发器方案
			String serialId = requestParam.getValue("serialId");
			String irProgram = requestParam.getValue("ir_ program");
			return facadeController.modifyIR(serialId, irProgram);
		} else if (CMDEnum.to_key_learn.toString().equals(cmdEnum.toString())) {// 进入学习状态
			String serialId = requestParam.getValue("serialId");
			String timeOut = requestParam.getValue("timeOut");
			String index = requestParam.getValue("index");
			String keyOrName = requestParam.getValue("key_or_name");
			String learnKeyType = requestParam.getValue("key_or_name");
			return facadeController.toLearn(serialId, timeOut, Integer.valueOf(index), keyOrName, learnKeyType);
		} else if (CMDEnum.to_pair_code.toString().equals(cmdEnum.toString())) {// 进入对码模式

			return null;
		} else if (CMDEnum.send_test_code.toString().equals(cmdEnum.toString())) {// 发送测试码

			return null;
		} else if (CMDEnum.control_device.toString().equals(cmdEnum.toString())) {// 控制(红外)
			String serialId = requestParam.getValue("serialId");
			String index = requestParam.getValue("index");
			String key = requestParam.getValue("key");
			return facadeController.controllIR(serialId, Integer.valueOf(index), key);
		} else if (CMDEnum.query_msg.toString().equals(cmdEnum.toString())) {
			String count = requestParam.getValue("count");
			String type = requestParam.getValue("type");
			String start = requestParam.getValue("start");
			return facadeController.queryMsg(type, start, count);
		} else if (CMDEnum.query_group.toString().equals(cmdEnum.toString())) {
			return facadeController.queryGroup();
		} else if (CMDEnum.set_group.toString().equals(cmdEnum.toString())) {
			String groupId = requestParam.getValue("group_id");
			String groupName = requestParam.getValue("group_name");
			String groupState = requestParam.getValue("group_state");
			String operateType = requestParam.getValue("operate_type");
			String groupMember = requestParam.getValue("group_member");
			String groupStyle = requestParam.getValue("group_style");
			String oboxSerialId = requestParam.getValue("obox_serial_id");
			String groupAddr = requestParam.getValue("groupAddr");
			if (!StringUtils.isEmpty(operateType)) {
				// 00/01/02/03/04／05/06
				// 删除／设置／覆盖成员/添加成员／删除成员/改名/执行
				/*
				 * if(operateType.equals("00")){
				 * 
				 * }else if(operateType.equals("01")){
				 * 
				 * }else if(operateType.equals("02")){
				 * 
				 * }else if(operateType.equals("03")){
				 * 
				 * }else if(operateType.equals("04")){
				 * 
				 * }else if(operateType.equals("05")){
				 * 
				 * }else if(operateType.equals("06")){
				 * 
				 * }
				 */
				OperateTypeEnum operation = OperateTypeEnum.getEnum(operateType);
				if (!operation.equals(OperateTypeEnum.set)) {
					/*
					 * if (StringUtils.isEmpty(groupId)) { if
					 * (StringUtils.isEmpty(oboxSerialId)||
					 * StringUtils.isEmpty(groupAddr)) { res = new
					 * ResponseObject();
					 * res.setStatus(ResponseEnum.RequestParamError.getStatus())
					 * ;
					 * res.setMessage(ResponseEnum.RequestParamError.getMsg());
					 * return res; } }
					 */
					if (operateType.equals(OperateTypeEnum.delete)) {
						if (!StringUtils.isEmpty(groupId) && !NumberHelper.isNumeric(groupId)) {
							return facadeController.deleteServerGroup(Integer.parseInt(groupId));
						}
					} else if (operateType.equals(OperateTypeEnum.coverChild)) {
						if (!StringUtils.isEmpty(groupId) && !NumberHelper.isNumeric(groupId)) {
							List<String> mList = null;
							if (!StringUtils.isEmpty(groupMember))
								mList = (List<String>) ObjectUtils.fromJsonToObject(groupMember, List.class);
							return facadeController.coverChildGroup(Integer.parseInt(groupId), mList);
						}
					} else if (operateType.equals(OperateTypeEnum.removeChild)) {
						if (!StringUtils.isEmpty(groupId) && !NumberHelper.isNumeric(groupId)
								&& !StringUtils.isEmpty(groupMember)) {
							List<String> mList = null;
							if (!StringUtils.isEmpty(groupMember))
								mList = (List<String>) ObjectUtils.fromJsonToObject(groupMember, List.class);
							return facadeController.removeChildGroup(Integer.parseInt(groupId), mList);
						}
					}else if (operateType.equals(OperateTypeEnum.addChild)) {
						if (!StringUtils.isEmpty(groupId) && !NumberHelper.isNumeric(groupId)
								&& !StringUtils.isEmpty(groupMember)) {
							List<String> mList = null;
							if (!StringUtils.isEmpty(groupMember))
								mList = (List<String>) ObjectUtils.fromJsonToObject(groupMember, List.class);
							return facadeController.addChildGroup(Integer.parseInt(groupId), mList);
						}
					
					}else if (operateType.equals(OperateTypeEnum.rename)) {
						if (!StringUtils.isEmpty(groupId) && !NumberHelper.isNumeric(groupId)
								&& !StringUtils.isEmpty(groupName)) {
							return facadeController.reNameGroup(Integer.parseInt(groupId), groupName);
						}
					}else if (operateType.equals(OperateTypeEnum.action)) {
						if (!StringUtils.isEmpty(groupId) && !NumberHelper.isNumeric(groupId)
								&& !StringUtils.isEmpty(groupState)) {
							return facadeController.actionGroup(Integer.parseInt(groupId), groupState);
						}
					}
				} else {
					if (!StringUtils.isEmpty(groupName)) {
						List<String> mList = null;
						if (!StringUtils.isEmpty(groupMember))
							mList = (List<String>) ObjectUtils.fromJsonToObject(groupMember, List.class);
						return facadeController.addServerGroup(groupName, mList);
					}
				}
			}
		} else if (CMDEnum.test.toString().equals(cmdEnum.toString())) {
			String serialId = requestParam.getValue("serialId");
			if (!StringUtils.isEmpty(serialId)) {
				return facadeController.test(serialId);
			}
		}
		res = new ResponseObject();
		res.setStatus(ResponseEnum.RequestParamError.getStatus());
		res.setMessage(ResponseEnum.RequestParamError.getMsg());
		return res;
	}

	public void printRequest(HttpServletRequest request) {
		logger.debug("=============RECV REQUEST PARAMS START=============");
		logger.debug("URL=" + request.getRequestURL());
		for (String name : request.getParameterMap().keySet()) {
			logger.debug("name=" + name + ";value=" + request.getParameter(name));
		}
		logger.debug("============RECV REQUEST PARAMS END=============");
	}

	private static <T> List<T> getObjectList(String jsonString, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			Gson gson = new Gson();
			JsonArray arry = new JsonParser().parse(jsonString).getAsJsonArray();
			for (JsonElement jsonElement : arry) {
				list.add(gson.fromJson(jsonElement, cls));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) {
		List<String> mList = (List<String>) ObjectUtils.fromJsonToObject(null, List.class);
		System.out.println(mList);
	}
}