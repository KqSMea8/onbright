package com.bright.apollo.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.rokid.RokidAuth;
import com.bright.apollo.common.rokid.RokidConstants;
import com.bright.apollo.common.rokid.RokidDate;
import com.bright.apollo.common.rokid.RokidExeAction;
import com.bright.apollo.common.rokid.RokidLedAction;
import com.bright.apollo.common.rokid.RokidLedDate;
import com.bright.apollo.common.rokid.RokidLedState;
import com.bright.apollo.common.rokid.RokidRequest;
import com.bright.apollo.common.rokid.RokidSwitchAction;
import com.bright.apollo.common.rokid.RokidSwitchDate;
import com.bright.apollo.common.rokid.RokidSwitchState;
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
	private static final Logger logger = LoggerFactory.getLogger(RokidController.class);
 	@Autowired
	private FeignDeviceClient feignDeviceClient;

	@Autowired
 	private FeignUserClient feignUserClient;

	@Autowired
	private RedisBussines redisBussines;

	@Autowired
	private CommonController commonController;

	@Autowired
	private FacadeController facadeController;

	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@ApiOperation(value = "get deivcie by device serialId", httpMethod = "GET", produces = "application/json")
//	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public String getRokidList(HttpServletRequest request) {
		logger.info("======= /rokid/list begin ======= ");
		List<TOboxDeviceConfig>  list = null;
		try{
			RokidRequest rokidRequest = (RokidRequest) ObjectUtils.fromJsonToObject(printRequsetBody(request), RokidRequest.class);
			if (rokidRequest == null||rokidRequest.getUserAuth()==null) {
				return ErrorEnum.request_param_invalid.getValue();
			}
			logger.info(" ====== /rokid/list rokidRequest ====== "+rokidRequest);
			RokidAuth auth = rokidRequest.getUserAuth();
			String userToken = auth.getUserToken();

			OAuth2Authentication defaultOAuth2AccessToken = redisBussines.getObject("auth:"+userToken,OAuth2Authentication.class);
			User redisUser = (User) defaultOAuth2AccessToken.getPrincipal();
			ResponseObject<TUser> userResponseObject = feignUserClient.getUser(redisUser.getUsername());
			TUser tUser = userResponseObject.getData();
			logger.info(" ====== /rokid/list tUser ====== "+tUser.getId());
			String uid = String.valueOf(tUser.getId());

			if(uid==null||uid.equals("")){
				uid = auth.getUserId();
			}
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
		logger.info("======= /rokid/list end ======= ");
		return getListRokid(list).toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@ApiOperation(value = "get deivcie by device serialId", httpMethod = "GET", produces = "application/json")
//	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/execute", method = RequestMethod.POST)
	@ResponseBody
	public String executeRokid(HttpServletRequest request) {
		logger.info("======= /rokid/execute begin ======= ");
		TOboxDeviceConfig oboxDeviceConfig = null;
		RokidRequest rokidRequest = null;
		String userToken = null;
		try{
			rokidRequest = (RokidRequest) ObjectUtils.fromJsonToObject(printRequsetBody(request), RokidRequest.class);
			if (rokidRequest == null||rokidRequest.getDevice().getUserAuth()==null) {
				return ErrorEnum.request_param_invalid.getValue();
			}
			logger.info(" ====== /rokid/execute rokidRequest ====== "+rokidRequest);
			RokidAuth auth = rokidRequest.getDevice().getUserAuth();
			userToken = auth.getUserToken();
			OAuth2Authentication defaultOAuth2AccessToken = redisBussines.getObject("auth:"+userToken,OAuth2Authentication.class);
			User redisUser = (User) defaultOAuth2AccessToken.getPrincipal();
			ResponseObject<TUser> userResponseObject = feignUserClient.getUser(redisUser.getUsername());
			TUser tUser = userResponseObject.getData();
			logger.info(" ====== /rokid/execute tUser ====== "+tUser.getId());
			String uid = String.valueOf(tUser.getId());
			if(uid==null||uid.equals("")){
				uid = auth.getUserId();
			}
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
		logger.info("======= /rokid/execute end ======= ");
		return getExecuteRokid(oboxDeviceConfig, rokidRequest.getAction(), userToken,request).toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@ApiOperation(value = "get deivcie by device serialId", httpMethod = "GET", produces = "application/json")
//	@ApiResponse(code = 200, message = "success", response = ResponseObject.class)
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	@ResponseBody
	public String getRokidState(HttpServletRequest request,@RequestBody Object obj)  {
		logger.info("======= /rokid/get begin ======= ");
		logger.info("======= /rokid/get param ======= "+obj);
		Map<String,Object> paramMap = (Map<String,Object>)obj;
		TOboxDeviceConfig oboxDeviceConfig = null;

		Map<String,String> userMap  = (Map<String, String>) paramMap.get("userAuth");
		Map<String,String> deviceMap  = (Map<String, String>) paramMap.get("device");
		try{

			ResponseObject<TUser> rsUser= feignUserClient.getUserById(Integer.valueOf(userMap.get("userId")));
			TUser user = rsUser.getData();
			if(user == null){
				return ErrorEnum.user_not_found.getValue();
			}
			ResponseObject<TOboxDeviceConfig> responseObject = feignDeviceClient.getDevice(deviceMap.get("deviceId"));
			oboxDeviceConfig = responseObject.getData();
			if(oboxDeviceConfig == null){
				return ErrorEnum.request_param_invalid.getValue();
			}
		}catch (Exception e ){
			logger.error(" ====== RokidController getRokidState ====== "+e.getMessage());
		}
		logger.info("======= /rokid/get end ======= ");
		return getRokidState(oboxDeviceConfig, deviceMap).toString();
	}

	private JsonObject getRokidState(TOboxDeviceConfig tOboxDeviceConfig, Map<String,String> deviceMap) {
		logger.info("======= getRokidState begin ======= ");
		JsonObject respJsonObject = new JsonObject();
		Gson g2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		// switch
		if (tOboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.socket.getValue())
				&& tOboxDeviceConfig.getDeviceChildType().equals(DeviceTypeEnum.socket_jbox.getValue())
				&& deviceMap.get("type").equals(RokidConstants.SWITCHTYPE)) {
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
				&& deviceMap.get("type").equals(RokidConstants.LIGHT)) {// led_double
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
		logger.info("======= getRokidState end ======= ");
		return respJsonObject;

	}

	private JsonObject errorStatusResp() {
		JsonObject respJsonObject = new JsonObject();
		respJsonObject.addProperty("status", 1);
		respJsonObject.addProperty("message", RokidConstants.ERRORSTATE);
		respJsonObject.addProperty("errorName", RokidConstants.E_DRIVER_ERROR);
		return respJsonObject;
	}

	private JsonObject setNodeStatus(Map<String, String[]> map,TOboxDeviceConfig tOboxDeviceConfig,HttpServletRequest request){
//		SettingNodeStatusHandler handler = (SettingNodeStatusHandler) HandlerManager
//				.getHandler(CMDEnum.getCMDEnum("setting_node_status"));
		logger.info(" ====== setNodeStatus begin  ====== ");
		ResponseObject controlDevice = facadeController.controlDevice(map.get("serialId")[0],map.get("status")[0]);

		if(controlDevice!=null&&controlDevice.getStatus()<300){
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("serialId",map.get("serialId")[0]);
			map2.put("status",map.get("status")[0]);
			controlDevice.setData(map2);
		}

		JsonObject respJsonObject = new JsonObject();
		Gson g2 = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//		JsonObject jsonObject = handler.process(new RequestParam(map));
		Map<String,Object> resMap = (Map<String, Object>) controlDevice.getData();
		boolean success = true;
		if (success) {
			String status = (String)resMap.get("status");
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
					rokidState.setBrightness(0);
					rokidState.setColor_temperature(0);
					respJsonObject.add("data", g2.toJsonTree(rokidState));
				}else{
					respJsonObject.addProperty("status", 0);
					RokidLedState rokidState = new RokidLedState();
					rokidState.setSwitching(RokidSwitchEnum.on.getValue());
					rokidState.setBrightness(((Integer.parseInt(status.substring(0, 2), 16) - 126) * 100 / 128) );
					rokidState.setColor_temperature(((Integer.parseInt(status.substring(2, 4), 16)) * 100 / 255) );
					respJsonObject.add("data", g2.toJsonTree(rokidState));
				}
			}
		} else {
			respJsonObject.addProperty("status", 1);
			respJsonObject.addProperty("message", "execute failed");
		}
		logger.info(" ====== setNodeStatus end  ====== ");
		return respJsonObject;
	}

	private JsonObject getExecuteRokid(TOboxDeviceConfig tOboxDeviceConfig, RokidExeAction action,
											 String userToken,HttpServletRequest request){

		if (StringUtils.isEmpty(userToken) || action == null || StringUtils.isEmpty(action.getProperty())
				|| StringUtils.isEmpty(action.getName())
				|| !VerificationCollection.useSet(RokidConstants.PROPERTY, action.getProperty())) {
			return errorStatusResp();
		}
		logger.info(" ====== getExecuteRokid begin  ====== ");
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
				return setNodeStatus(map,tOboxDeviceConfig,request);
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
				return setNodeStatus(map,tOboxDeviceConfig,request);
			} else if ((action.getProperty().equals(RokidConstants.BRIGHTNESSTYPE)
					|| action.getProperty().equals(RokidConstants.COLORTEMPERATURETYPE))
					&& VerificationCollection.useSet(RokidConstants.BRINTNESS, action.getName())) {
				String state = tOboxDeviceConfig.getDeviceState();
				if (action.getName().equals("max")) {
					if (action.getProperty().equals(RokidConstants.BRIGHTNESSTYPE)) {
						String[] status = { "ff" + state.substring(2) };
						map.put("status", status);
					} else {
						String[] status = { state.substring(0, 2) + "ff" + state.substring(4) };
						map.put("status", status);
					}
					return setNodeStatus(map,tOboxDeviceConfig,request);
				} else if (action.getName().equals("min")) {
					if (action.getProperty().equals(RokidConstants.BRIGHTNESSTYPE)) {
						String[] status = { "00" + state.substring(2) };
						map.put("status", status);
					} else {
						String[] status = { state.substring(0, 2) + "00" + state.substring(4) };
						map.put("status", status);
					}
					return setNodeStatus(map,tOboxDeviceConfig,request);
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
					return setNodeStatus(map,tOboxDeviceConfig,request);
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
					return setNodeStatus(map,tOboxDeviceConfig,request);
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
					return setNodeStatus(map,tOboxDeviceConfig,request);
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
		logger.info(" ====== getListRokid begin  ====== ");
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
		logger.info(" ====== getListRokid end  ====== ");
		return respJsonObject;
	}

	public RokidLedState getLedState(String deviceState) {
		logger.info(" ====== getLedState begin  ====== ");
		RokidLedState state = new RokidLedState();
		if (StringUtils.isEmpty(deviceState))
			return null;
		if (deviceState.startsWith("00")) {
			state.setBrightness(0);
			state.setColor_temperature(0);
			state.setSwitching("off");
		} else {
			state.setSwitching("on");
			state.setBrightness(((Integer.parseInt(deviceState.substring(0, 2), 16) - 126) * 100 / 128) );
			state.setColor_temperature(((Integer.parseInt(deviceState.substring(2, 4), 16)) * 100 / 255));
		}
		logger.info(" ====== getLedState end  ====== ");
		return state;
	}

}
