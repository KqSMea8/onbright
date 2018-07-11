package com.bright.apollo.controller;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.rokid.*;
import com.bright.apollo.config.NumberHelper;
import com.bright.apollo.config.VerificationCollection;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.enums.ErrorEnum;
import com.bright.apollo.enums.RokidSwitchEnum;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.redis.RedisBussines;
import com.bright.apollo.response.ResponseObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.zz.common.util.ObjectUtils;
import com.zz.common.util.StringUtils;
import io.swagger.annotations.Api;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年3月16日  
 *@Version:1.1.0  
 */
@Api("device Controller")
@RequestMapping("rokid")
@RestController
public class RokidController {
	Logger logger = Logger.getLogger(RokidController.class);

 	@Autowired
	private FeignDeviceClient feignDeviceClient;

	@Autowired
 	private FeignUserClient feignUserClient;

	@Autowired
	private RedisBussines redisBussines;

	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@ApiOperation(value = "get deivcie by device serialId", httpMethod = "GET", produces = "application/json")
//	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public String getRokidList(HttpServletRequest request) {
		List<TOboxDeviceConfig>  list = null;
		try{
			RokidRequest rokidRequest = (RokidRequest) ObjectUtils.fromJsonToObject(printRequsetBody(request), RokidRequest.class);
			if (rokidRequest == null||rokidRequest.getUserAuth()==null) {
				return ErrorEnum.request_param_invalid.getValue();
			}
			logger.info(" ====== RokidRequest ====== "+rokidRequest);
			String userToken = rokidRequest.getUserAuth().getUserToken();
			String uid = (String) redisBussines.get(userToken);
			ResponseObject<TUser> rsUser= feignUserClient.getUserById(Integer.valueOf(uid));
			TUser user = rsUser.getData();
			if(user == null){
				return ErrorEnum.user_not_found.getValue();
			}
			ResponseObject<List<TOboxDeviceConfig>>  responseObject =  feignDeviceClient.getOboxDeviceConfigByUserId(user.getId());
			list = responseObject.getData();
		}catch (Exception e ){
			logger.error(" ====== RokidController getRokidList ====== "+e.getMessage());
		}

		return getListRokid(list).toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@ApiOperation(value = "get deivcie by device serialId", httpMethod = "GET", produces = "application/json")
//	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/execute", method = RequestMethod.GET)
	@ResponseBody
	public String executeRokid(HttpServletRequest request) {
		TOboxDeviceConfig oboxDeviceConfig = null;
		RokidRequest rokidRequest = null;
		String userToken = null;
		try{
			rokidRequest = (RokidRequest) ObjectUtils.fromJsonToObject(printRequsetBody(request), RokidRequest.class);
			if (rokidRequest == null||rokidRequest.getUserAuth()==null) {
				return ErrorEnum.request_param_invalid.getValue();
			}
			logger.info(" ====== RokidRequest ====== "+rokidRequest);
			userToken = rokidRequest.getUserAuth().getUserToken();
			String uid = (String) redisBussines.get(userToken);
			ResponseObject<TUser> rsUser= feignUserClient.getUserById(Integer.valueOf(uid));
			TUser user = rsUser.getData();
			if(user == null){
				return ErrorEnum.user_not_found.getValue();
			}
			ResponseObject<TOboxDeviceConfig> responseObject = feignDeviceClient.getDevice(rokidRequest.getDevice().getDeviceId());
			oboxDeviceConfig = responseObject.getData();
			if(oboxDeviceConfig == null){
				return ErrorEnum.request_param_invalid.getValue();
			}
		}catch (Exception e ){
			logger.error(" ====== RokidController executeRokid ====== "+e.getMessage());
		}

		return getExecuteRokid(oboxDeviceConfig, rokidRequest.getAction(), userToken).toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@ApiOperation(value = "get deivcie by device serialId", httpMethod = "GET", produces = "application/json")
//	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	@ResponseBody
	public String getRokidState(HttpServletRequest request) {
		TOboxDeviceConfig oboxDeviceConfig = null;
		RokidRequest rokidRequest = null;
		String userToken = null;
		try{
			rokidRequest = (RokidRequest) ObjectUtils.fromJsonToObject(printRequsetBody(request), RokidRequest.class);
			if (rokidRequest == null||rokidRequest.getUserAuth()==null) {
				return ErrorEnum.request_param_invalid.getValue();
			}
			logger.info(" ====== RokidRequest ====== "+rokidRequest);
			userToken = rokidRequest.getUserAuth().getUserToken();
			String uid = (String) redisBussines.get(userToken);
			ResponseObject<TUser> rsUser= feignUserClient.getUserById(Integer.valueOf(uid));
			TUser user = rsUser.getData();
			if(user == null){
				return ErrorEnum.user_not_found.getValue();
			}
			ResponseObject<TOboxDeviceConfig> responseObject = feignDeviceClient.getDevice(rokidRequest.getDevice().getDeviceId());
			oboxDeviceConfig = responseObject.getData();
			if(oboxDeviceConfig == null){
				return ErrorEnum.request_param_invalid.getValue();
			}
		}catch (Exception e ){
			logger.error(" ====== RokidController getRokidState ====== "+e.getMessage());
		}

		return getRokidState(oboxDeviceConfig, rokidRequest.getDevice()).toString();
	}

	private JsonObject getRokidState(TOboxDeviceConfig tOboxDeviceConfig, RokidDevice rokidDevice) {

		JsonObject respJsonObject = new JsonObject();
		Gson g2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		// switch
		if (tOboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.socket.getValue())
				&& tOboxDeviceConfig.getDeviceChildType().equals(DeviceTypeEnum.socket_jbox.getValue())
				&& rokidDevice.getType().equals(RokidConstants.SWITCHTYPE)) {
			respJsonObject.addProperty("status", 0);
			RokidSwitchDate data = new RokidSwitchDate();
			RokidSwitchState rokidState = new RokidSwitchState();
			data.setType(RokidConstants.SWITCHTYPE);
			RokidSwitchAction action = new RokidSwitchAction();
			action.setSwitching(RokidConstants.SWITCH);
			data.setActions(action);
			data.setDeviceId(tOboxDeviceConfig.getDeviceSerialId());
			data.setName(tOboxDeviceConfig.getDeviceId());
			if (tOboxDeviceConfig.getDeviceState().startsWith("01")) {
				rokidState.setSwitching(RokidSwitchEnum.on.getValue());
				data.setState(rokidState);
				respJsonObject.add("data", g2.toJsonTree(data));
			} else if (tOboxDeviceConfig.getDeviceState().startsWith("00")) {
				rokidState.setSwitching(RokidSwitchEnum.off.getValue());
				data.setState(rokidState);
				respJsonObject.add("data", g2.toJsonTree(data));
			} else {
				return errorStatusResp();
			}
		} else if (tOboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.led.getValue())
				&& tOboxDeviceConfig.getDeviceChildType().equals(DeviceTypeEnum.led_double.getValue())
				&& rokidDevice.getType().equals(RokidConstants.LIGHT)) {// led_double
			if (StringUtils.isEmpty(tOboxDeviceConfig.getDeviceState())) {
				return errorStatusResp();
			}
			respJsonObject.addProperty("status", 0);
			RokidLedDate date = new RokidLedDate();
			date.setType(RokidConstants.LIGHT);
			date.setDeviceId(tOboxDeviceConfig.getDeviceSerialId());
			date.setName(tOboxDeviceConfig.getDeviceId());
			RokidLedAction action = new RokidLedAction();
			action.setBrightness(RokidConstants.BRINTNESS);
			action.setColor_temperature(RokidConstants.COLORTEMPERATURE);
			action.setSwitching(RokidConstants.SWITCH);
			date.setActions(action);
			RokidLedState ledState = getLedState(tOboxDeviceConfig.getDeviceState());
			if (ledState == null) {
				return errorStatusResp();
			}
			date.setState(ledState);
			respJsonObject.add("data", g2.toJsonTree(date));
		} else {
			// not support
			respJsonObject.addProperty("status", 1);
			respJsonObject.addProperty("message", RokidConstants.UNKONWDEVICE);
			respJsonObject.addProperty("errorName", RokidConstants.E_DRIVER_DEVICE_NO_FOUND);
		}
		return respJsonObject;

	}

	private JsonObject errorStatusResp() {
		JsonObject respJsonObject = new JsonObject();
		respJsonObject.addProperty("status", 1);
		respJsonObject.addProperty("message", RokidConstants.ERRORSTATE);
		respJsonObject.addProperty("errorName", RokidConstants.E_DRIVER_ERROR);
		return respJsonObject;
	}

	private JsonObject setNodeStatus(Map<String, String[]> map,TOboxDeviceConfig tOboxDeviceConfig){
//		SettingNodeStatusHandler handler = (SettingNodeStatusHandler) HandlerManager
//				.getHandler(CMDEnum.getCMDEnum("setting_node_status"));
		JsonObject respJsonObject = new JsonObject();
		Gson g2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//		JsonObject jsonObject = handler.process(new RequestParam(map));
		JsonObject jsonObject = null;
		boolean success = jsonObject.get("success").getAsBoolean();
		if (success) {
			String status = jsonObject.get("status").getAsString();
			if (tOboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.socket.getValue())
					&& tOboxDeviceConfig.getDeviceChildType().equals(DeviceTypeEnum.socket_jbox.getValue())) {
				if (status.startsWith("00")) {
					respJsonObject.addProperty("status", 0);
					RokidSwitchState rokidState = new RokidSwitchState();
					rokidState.setSwitching(RokidSwitchEnum.off.getValue());
					respJsonObject.add("data", g2.toJsonTree(rokidState));
				} else if (status.startsWith("01")) {
					respJsonObject.addProperty("status", 0);
					RokidSwitchState rokidState = new RokidSwitchState();
					rokidState.setSwitching(RokidSwitchEnum.on.getValue());
					respJsonObject.add("data", g2.toJsonTree(rokidState));

				} else {
					respJsonObject.addProperty("status", 1);
					respJsonObject.addProperty("message", "device state is wrong");
				}
			}else{//double led
				if (status.startsWith("00")) {
					respJsonObject.addProperty("status", 0);
					RokidLedState rokidState = new RokidLedState();
					rokidState.setSwitching(RokidSwitchEnum.off.getValue());
					rokidState.setBrightness("0");
					rokidState.setColor_temperature("0");
					respJsonObject.add("data", g2.toJsonTree(rokidState));
				}else{
					respJsonObject.addProperty("status", 0);
					RokidLedState rokidState = new RokidLedState();
					rokidState.setSwitching(RokidSwitchEnum.on.getValue());
					rokidState.setBrightness(((Integer.parseInt(status.substring(0, 2), 16) - 126) * 100 / 128) + "");
					rokidState.setColor_temperature(((Integer.parseInt(status.substring(2, 4), 16)) * 100 / 255) + "");
					respJsonObject.add("data", g2.toJsonTree(rokidState));
				}
			}
		} else {
			respJsonObject.addProperty("status", 1);
			respJsonObject.addProperty("message", "execute failed");
		}
		return respJsonObject;
	}

	private JsonObject getExecuteRokid(TOboxDeviceConfig tOboxDeviceConfig, RokidExeAction action,
											 String userToken){

		if (StringUtils.isEmpty(userToken) || action == null || StringUtils.isEmpty(action.getProperty())
				|| StringUtils.isEmpty(action.getName())
				|| !VerificationCollection.useSet(RokidConstants.PROPERTY, action.getProperty())) {
			return errorStatusResp();
		}
		logger.info("================================================");
		Map<String, String[]> map = new HashMap<String, String[]>();
		String[] token = { userToken };
		map.put("access_token", token);
		String[] serialId = { tOboxDeviceConfig.getDeviceSerialId() };
		map.put("serialId", serialId);
		if (tOboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.socket.getValue())
				&& tOboxDeviceConfig.getDeviceChildType().equals(DeviceTypeEnum.socket_jbox.getValue())) {
			if (action.getProperty().equals(RokidConstants.SWITCHTYPE)
					&& !VerificationCollection.useSet(RokidConstants.SWITCH, action.getName())) {
				if (action.getName().equals("off")) {
					String[] status = { "00000000000000" };
					map.put("status", status);
				} else {
					String[] status = { "01000000000000" };
					map.put("status", status);
				}
				return setNodeStatus(map,tOboxDeviceConfig);
			} else {
				logger.info("================================================");
				return errorStatusResp();
			}
		} else if (tOboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.led.getValue())
				&& tOboxDeviceConfig.getDeviceChildType().equals(DeviceTypeEnum.led_double.getValue())) {
			if (action.getProperty().equals(RokidConstants.SWITCHTYPE)
					&& VerificationCollection.useSet(RokidConstants.SWITCH, action.getName())) {
				// on ff off 00
				if (action.getName().equals("off")) {
					String[] status = { "00000000000000" };
					map.put("status", status);
				} else {
					String[] status = { "ff000000000000" };
					map.put("status", status);
				}
				return setNodeStatus(map,tOboxDeviceConfig);
			} else if ((action.getProperty().equals(RokidConstants.BRIGHTNESSTYPE)
					|| action.getProperty().equals(RokidConstants.COLORTEMPERATURETYPE))
					&& !VerificationCollection.useSet(RokidConstants.BRINTNESS, action.getName())) {
				String state = tOboxDeviceConfig.getDeviceState();
				if (action.getName().equals("max")) {
					if (action.getProperty().equals(RokidConstants.BRIGHTNESSTYPE)) {
						String[] status = { "ff" + state.substring(2) };
						map.put("status", status);
					} else {
						String[] status = { state.substring(0, 2) + "ff" + state.substring(4) };
						map.put("status", status);
					}
					return setNodeStatus(map,tOboxDeviceConfig);
				} else if (action.getName().equals("min")) {
					if (action.getProperty().equals(RokidConstants.BRIGHTNESSTYPE)) {
						String[] status = { "00" + state.substring(2) };
						map.put("status", status);
					} else {
						String[] status = { state.substring(0, 2) + "00" + state.substring(4) };
						map.put("status", status);
					}
					return setNodeStatus(map,tOboxDeviceConfig);
				} else if (action.getName().equals("up")) {
					if (action.getProperty().equals(RokidConstants.BRIGHTNESSTYPE)) {
						if (Integer.parseInt(state.substring(0, 2), 16) < 245) {
							String[] status = { Integer.toHexString(Integer.parseInt(state.substring(0, 2), 16) + 10)
									+ state.substring(2) };
							map.put("status", status);
						} else {
							String[] status = { "ff" + state.substring(2) };
							map.put("status", status);
						}
					} else {
						if (Integer.parseInt(state.substring(0, 2), 16) < 245) {
							String[] status = { state.substring(0, 2)
									+ Integer.toHexString(Integer.parseInt(state.substring(2, 4), 16) + 10)
									+ state.substring(4) };
							map.put("status", status);
						} else {
							String[] status = { state.substring(0, 2) + "ff" + state.substring(4) };
							map.put("status", status);
						}
					}
					return setNodeStatus(map,tOboxDeviceConfig);
				} else if (action.getName().equals("down")) {
					if (action.getProperty().equals(RokidConstants.BRIGHTNESSTYPE)) {
						if (Integer.parseInt(state.substring(0, 2), 16) > 0) {
							String[] status = { Integer.toHexString(Integer.parseInt(state.substring(0, 2), 16) - 10)
									+ state.substring(2) };
							map.put("status", status);
						} else {
							String[] status = { "00" + state.substring(2) };
							map.put("status", status);
						}
					} else {
						if (Integer.parseInt(state.substring(0, 2), 16) > 10) {
							String[] status = { state.substring(0, 2)
									+ Integer.toHexString(Integer.parseInt(state.substring(2, 4), 16) - 10)
									+ state.substring(4) };
							map.put("status", status);
						} else {
							String[] status = { state.substring(0, 2) + "00" + state.substring(4) };
							map.put("status", status);
						}
					}
					return setNodeStatus(map,tOboxDeviceConfig);
				} else if (action.getName().equals("num")) {
					if (StringUtils.isEmpty(action.getValue()) || !NumberHelper.isNumeric(action.getValue())
							||Integer.parseInt(action.getValue())>100||Integer.parseInt(action.getValue())<0
							)

						return errorStatusResp();
					if (action.getProperty().equals(RokidConstants.BRIGHTNESSTYPE)) {
						String[] status = { Integer.toHexString((128*Integer.parseInt(action.getValue())/100)+126)
								+ state.substring(2) };
						map.put("status", status);

					}else{
						String[] status = {state.substring(0,2)+ Integer.toHexString((255*Integer.parseInt(action.getValue())/100))
								+ state.substring(4) };
						map.put("status", status);
					}
					logger.info("================================================");
					return setNodeStatus(map,tOboxDeviceConfig);
				} else {
					logger.info("================================================");
					return errorStatusResp();
				}
			} else {
				logger.info("================================================");
				return errorStatusResp();
			}
		} else {
			logger.info("================================================");
			return errorStatusResp();
		}
	}

	private String printRequsetBody(HttpServletRequest request) throws Exception {
		logger.info(" ====== printRequsetBody begin  ====== ");
		logger.info(" ====== url ====== "+request.getRequestURL());
		request.setCharacterEncoding("UTF-8");
		BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(), "utf-8"));
		StringBuffer sb = new StringBuffer("");
		String temp;
		while ((temp = br.readLine()) != null) {
			sb.append(temp);
		}
		br.close();
		logger.info(" ====== request param ====== "+sb.toString());
		logger.info(" ====== printRequsetBody end  ====== ");
		return sb.toString();
	}

	private JsonObject getListRokid(List<TOboxDeviceConfig> tOboxDeviceConfigs) {
		JsonObject respJsonObject = new JsonObject();
		Gson g2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		if (tOboxDeviceConfigs == null || tOboxDeviceConfigs.size() == 0) {
			respJsonObject.addProperty("errorName", "E_DRIVER_DEVICE_NO_FOUND");
			respJsonObject.addProperty("status", 1);
			return respJsonObject;
		} else {
			List<RokidDate> list = new ArrayList<RokidDate>();
			for (TOboxDeviceConfig tOboxDeviceConfig : tOboxDeviceConfigs) {
				if (tOboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.socket.getValue())
						&& tOboxDeviceConfig.getDeviceChildType().equals(DeviceTypeEnum.socket_jbox.getValue())) {
					RokidDate rokidDate = new RokidDate();
					rokidDate.setDeviceId(tOboxDeviceConfig.getDeviceSerialId());
					rokidDate.setName(tOboxDeviceConfig.getDeviceId());
					rokidDate.setType(RokidConstants.SWITCHTYPE);
					// .....
					RokidSwitchAction rokidAction = new RokidSwitchAction();
					rokidAction.setSwitching(RokidConstants.SWITCH);
					rokidDate.setActions(rokidAction);
					RokidSwitchState rokidState = new RokidSwitchState();
					// modify the state
					rokidState.setSwitching("off");
					rokidDate.setState(rokidState);
					list.add(rokidDate);
				} else if (tOboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.led.getValue())
						&& tOboxDeviceConfig.getDeviceChildType().equals(DeviceTypeEnum.led_double.getValue())) {
					// new Gson().toJsonTree(list)
					RokidDate rokidDate = new RokidDate();
					rokidDate.setDeviceId(tOboxDeviceConfig.getDeviceSerialId());
					rokidDate.setName(tOboxDeviceConfig.getDeviceId());
					rokidDate.setType(RokidConstants.LIGHT);
					// .....
					RokidLedAction rokidAction = new RokidLedAction();
					rokidAction.setColor_temperature(RokidConstants.COLORTEMPERATURE);
					rokidAction.setBrightness(RokidConstants.BRINTNESS);
					rokidAction.setSwitching(RokidConstants.SWITCH);
					rokidDate.setActions(rokidAction);

					rokidDate.setState(getLedState(tOboxDeviceConfig.getDeviceState()));
					list.add(rokidDate);
				}
			}
			if (list.size() < 1) {
				respJsonObject.addProperty("errorName", "E_DRIVER_DEVICE_NO_FOUND");
				respJsonObject.addProperty("status", 1);
				return respJsonObject;
			}
			respJsonObject.add("data", g2.toJsonTree(list));
			respJsonObject.addProperty("status", 0);
		}
		return respJsonObject;
	}

	public RokidLedState getLedState(String deviceState) {
		RokidLedState state = new RokidLedState();
		if (StringUtils.isEmpty(deviceState))
			return null;
		if (deviceState.startsWith("00")) {
			state.setBrightness("0");
			state.setColor_temperature("0");
			state.setSwitching("off");
		} else {
			state.setSwitching("on");
			state.setBrightness(((Integer.parseInt(deviceState.substring(0, 2), 16) - 126) * 100 / 128) + "");
			state.setColor_temperature(((Integer.parseInt(deviceState.substring(2, 4), 16)) * 100 / 255) + "");
		}
		return state;
	}

}
