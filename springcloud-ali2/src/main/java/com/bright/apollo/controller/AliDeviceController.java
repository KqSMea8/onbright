package com.bright.apollo.controller;

import java.util.*;

import com.bright.apollo.bean.*;
import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.entity.*;
import com.bright.apollo.enums.IREnum;
import com.bright.apollo.service.*;
import com.bright.apollo.util.IndexUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import javafx.beans.binding.ObjectBinding;
import net.sf.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.cache.AliDevCache;
import com.bright.apollo.enums.AliIotDevTypeEnum;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.mqtt.MqttGateWay;
import com.bright.apollo.response.AliDevInfo;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.AliRequest.AliService;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.tool.MD5;
import com.bright.apollo.util.SpringContextUtil;
import com.bright.apollo.vo.IotDevConncetion;
import java.lang.reflect.Type;

@RestController
@RequestMapping("aliDevice")
public class AliDeviceController {
	@Autowired
	private AliDevCache aliDevCache;
	@Autowired
	private AliDeviceService aliDeviceService;
	@Autowired
	private AliService aliService;
	@Autowired
	private CMDMessageService cMDMessageService;

	@Autowired
	@Lazy
	private TopicServer topServer;

	@Autowired
	private YaoKongYunService yaoKongYunService;

	@Autowired
	private IotDevConncetion iotOboxConncetion;

	@Autowired
	private YaoKongYunSend yaoKongYunSend;

	@Autowired
	private YaoKongYunConfig yaoKongYunConfig;

	@Autowired
	private CmdCache cmdCache;

	@Autowired
	private PushService pushService;

	@Autowired
	private UserAliDevService userAliDevService;


	private static final Logger logger = LoggerFactory.getLogger(AliDeviceController.class);

	@RequestMapping(value = "/sendToMqtt", method = RequestMethod.GET)
	public String testMqtt() {

		// mqttGateWay.sendToMqtt("ob-smart\\F1640B28-33A6-4A19-BB49-91A24E76EBF7","12312312qqqqqq3");

		return "";
	}

	@RequestMapping(value = "/registAliDev/{type}", method = RequestMethod.GET)
	public ResponseObject<AliDevInfo> registAliDev(@PathVariable(required = true, value = "type") String type,
			@RequestParam(required = false, value = "zone") String zone) {
		logger.info("===start registAliDev===");
		ResponseObject<AliDevInfo> res = new ResponseObject<AliDevInfo>();
		try {
			AliDevInfo aliDevInfo = new AliDevInfo();
			// ALIDevTypeEnum aliDevTypeEnum = ALIDevTypeEnum.getType(type);
			AliIotDevTypeEnum aliIotDevTypeEnum = AliIotDevTypeEnum.getType(type);
			AliRegionEnum region;
			Boolean isFound = false;
			if (aliIotDevTypeEnum == null) {
				res.setStatus(ResponseEnum.RequestParamError.getStatus());
				res.setMessage(ResponseEnum.RequestParamError.getMsg());
				return res;
			}
			if (StringUtils.isEmpty(zone) || !zone.split("/")[0].equals("America")) {
				region = AliRegionEnum.SOURTHCHINA;
				aliDevInfo.setKitCenter(region.getValue());
				List<TAliDevice> tAliDeviceList = null;
				if (!type.equals(AliIotDevTypeEnum.OBOX.name())) {
					tAliDeviceList = aliDeviceService.getAliDeviceByProductKeyAndDeviceSerialId(
							iotOboxConncetion.getDeviceSouthChinaName(), "available");
				} else {
					tAliDeviceList = aliDeviceService.getAliDeviceByProductKeyAndDeviceSerialId(
							iotOboxConncetion.getOboxSouthChinaName(), "available");
				}
				for (TAliDevice tAliDevice2 : tAliDeviceList) {
					if (StringUtils.isEmpty(
							aliDevCache.getAliDevAvailable(tAliDevice2.getProductKey(), tAliDevice2.getDeviceName()))) {
						if (StringUtils.isEmpty(tAliDevice2.getDeviceSecret())) {
							String deviceSecret = aliService.queryDeviceByName(tAliDevice2.getProductKey(),
									tAliDevice2.getDeviceName(), region);
							if (deviceSecret != null) {
								tAliDevice2.setDeviceSecret(deviceSecret);
								aliDeviceService.updateAliDevice(tAliDevice2);
								aliDevInfo.setDeviceName(tAliDevice2.getDeviceName());
								aliDevInfo.setProductKey(tAliDevice2.getProductKey());
								aliDevInfo.setDeviceSecret(tAliDevice2.getDeviceSecret());
								aliDevCache.saveAliDevWait(tAliDevice2.getProductKey(), tAliDevice2.getDeviceName(),
										region.getValue());
								isFound = true;
								break;
							}
						} else {
							aliDevInfo.setDeviceName(tAliDevice2.getDeviceName());
							aliDevInfo.setProductKey(tAliDevice2.getProductKey());
							aliDevInfo.setDeviceSecret(tAliDevice2.getDeviceSecret());
							aliDevCache.saveAliDevWait(tAliDevice2.getProductKey(), tAliDevice2.getDeviceName(),
									region.getValue());
							isFound = true;
							break;
						}
					}
				}

			} else {
				region = AliRegionEnum.AMERICA;
				aliDevInfo.setKitCenter(AliRegionEnum.AMERICA.getValue());
				List<TAliDeviceUS> tAliDeviceList = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceSerialId(
						iotOboxConncetion.getOboxSouthChinaName(), "available");
				for (TAliDeviceUS tAliDevice2 : tAliDeviceList) {
					if (StringUtils.isEmpty(
							aliDevCache.getAliDevAvailable(tAliDevice2.getProductKey(), tAliDevice2.getDeviceName()))) {
						if (StringUtils.isEmpty(tAliDevice2.getDeviceSecret())) {
							String deviceSecret = aliService.queryDeviceByName(tAliDevice2.getProductKey(),
									tAliDevice2.getDeviceName(), region);
							if (deviceSecret != null) {
								tAliDevice2.setDeviceSecret(deviceSecret);
								aliDeviceService.updateAliUSDevice(tAliDevice2);
								aliDevInfo.setDeviceName(tAliDevice2.getDeviceName());
								aliDevInfo.setProductKey(tAliDevice2.getProductKey());
								aliDevInfo.setDeviceSecret(tAliDevice2.getDeviceSecret());
								aliDevCache.saveAliDevWait(tAliDevice2.getProductKey(), tAliDevice2.getDeviceName(),
										region.getValue());
								isFound = true;
								break;
							}
						} else {
							aliDevInfo.setDeviceName(tAliDevice2.getDeviceName());
							aliDevInfo.setProductKey(tAliDevice2.getProductKey());
							aliDevInfo.setDeviceSecret(tAliDevice2.getDeviceSecret());
							aliDevCache.saveAliDevWait(tAliDevice2.getProductKey(), tAliDevice2.getDeviceName(),
									region.getValue());
							isFound = true;
							break;
						}
					}
				}

			}
			if (!isFound) {
				TAliDevice tAliDevice;
				if (region.equals(AliRegionEnum.AMERICA)) {
					if (type.equals(AliIotDevTypeEnum.OBOX.name())) {
						tAliDevice = aliService.registDevice(iotOboxConncetion.getOboxAmericaName(), null, region);
					} else {
						tAliDevice = aliService.registDevice(iotOboxConncetion.getDeviceAmericaName(), null, region);
					}
				} else {
					if (type.equals(AliIotDevTypeEnum.OBOX.name())) {
						tAliDevice =aliService.registDevice(iotOboxConncetion.getOboxSouthChinaName(), null, region);
					} else {
						tAliDevice = aliService.registDevice(iotOboxConncetion.getDeviceSouthChinaName(), null, region);
					}
				}
				if (tAliDevice == null) {
					res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
					res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
					return res;
				} else {
					aliDevInfo.setDeviceName(tAliDevice.getDeviceName());
					aliDevInfo.setProductKey(tAliDevice.getProductKey());
					aliDevInfo.setDeviceSecret(tAliDevice.getDeviceSecret());
					aliDevCache.saveAliDevWait(tAliDevice.getProductKey(), tAliDevice.getDeviceName(),
							region.getValue());
				}
			}
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(aliDevInfo);
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getSearchNewDevice", method = RequestMethod.PUT)
	ResponseObject<List<Map<String, String>>> getSearchNewDevice(@RequestBody(required = true) TObox obox) {
		ResponseObject<List<Map<String, String>>> res = new ResponseObject<List<Map<String, String>>>();
		try {
			List<String> replyList = cMDMessageService.searchReply(obox);
			List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
			if (!replyList.isEmpty()) {

				for (String string : replyList) {

					byte[] bodyBytes = ByteHelper.hexStringToBytes(string);
					byte[] deviceTypeBytes = { bodyBytes[1] };
					byte device_type_int = (byte) (deviceTypeBytes[0] & 0x1f);
					deviceTypeBytes[0] = device_type_int;
					String device_type = ByteHelper.bytesToHexString(deviceTypeBytes);
					if (!device_type.equals("1e")) {
						byte[] deviceChildTypeBytes = { bodyBytes[2] };
						String device_child_type = ByteHelper.bytesToHexString(deviceChildTypeBytes);

						byte[] idBytes = new byte[16];
						System.arraycopy(bodyBytes, 3, idBytes, 0, idBytes.length);
						String ID = ByteHelper.bytesToHexString(idBytes);
						ID = ByteHelper.fromHexAscii(ID);
						if (ID.equals("")) {
							continue;
						}
						byte[] stateBytes = new byte[5];
						System.arraycopy(bodyBytes, 19, stateBytes, 0, stateBytes.length);
						String deviceSerialId = ByteHelper.bytesToHexString(stateBytes);

						byte[] oboxSerial = new byte[5];
						System.arraycopy(bodyBytes, 24, oboxSerial, 0, oboxSerial.length);
						String oboxSerialId = ByteHelper.bytesToHexString(oboxSerial);

						byte[] addrByte = { bodyBytes[30] };
						String addr = ByteHelper.bytesToHexString(addrByte);

						Map<String, String> nodeMap = new HashMap<String, String>();

						nodeMap.put("name", ID);
						nodeMap.put("state", "01000000000000");
						nodeMap.put("serialId", deviceSerialId);
						nodeMap.put("device_type", device_type);
						nodeMap.put("device_child_type", device_child_type);
						nodeMap.put("obox_serial_id", oboxSerialId);
						nodeMap.put("addr", addr);
						nodeMap.put("groupAddr", "00");
						mapList.add(nodeMap);
					}
				}

			}
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			res.setData(mapList);
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getUserIRDevice", method = RequestMethod.POST)
	ResponseObject getUserIRDevice(@RequestParam(required = true, value = "userId") Integer userId) {
		ResponseObject res = new ResponseObject();
		List<Map<String,Object>> mapList = yaoKongYunService.getUserIRDevice(userId);
		res.setData(mapList);
		return  res;
	}

	@RequestMapping(value = "/getIRDeviceByIndex", method = RequestMethod.POST)
	ResponseObject getIRDeviceByIndex(@RequestParam(required = true, value = "index") Integer index) {
		ResponseObject res = new ResponseObject();
		List<TYaokonyunKeyCode> yaokonyunKeyCode = yaoKongYunService.getIRDeviceByIndex(index);
		res.setData(yaokonyunKeyCode);
		return  res;
	}

	@RequestMapping(value = "/getIRDeviceByIndexAndKey", method = RequestMethod.POST)
	ResponseObject<TYaokonyunKeyCode> getIRDeviceByIndexAndKey(@RequestParam(required = true, value = "index") Integer index,
											@RequestParam(required = true, value = "key") String key) {
		ResponseObject<TYaokonyunKeyCode> res = new ResponseObject<TYaokonyunKeyCode>();
		TYaokonyunKeyCode yaokonyunKeyCode = yaoKongYunService.getIRDeviceByIndexAndKey(index,key);
		res.setData(yaokonyunKeyCode);
		return  res;
	}

	@RequestMapping(value = "/controllIR", method = RequestMethod.POST)
	ResponseObject controllIR(@RequestParam(required = true, value = "serialId") String serialId,
														   @RequestParam(required = true, value = "index") Integer index,
														   @RequestParam(required = true, value = "key") String key) {
		ResponseObject res = new ResponseObject();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		try {
			TYaokonyunKeyCode yaokonyunKeyCode = yaoKongYunService.getYaoKongKeyCodeByKeyAndSerialIdAndIndex(index,serialId,key);
			String brandId = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("brandId_"+index);
			String deviceType = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("deviceType_"+index);
			if(brandId !=null &&!brandId.equals("")&&yaokonyunKeyCode==null){
				List<QueryRemoteBySrcDTO2> dtoSrcList = cmdCache.getIRDeviceInfoList(brandId+"_"+deviceType+"_"+"_remoteControlListSrc");
				for(QueryRemoteBySrcDTO2 dto :dtoSrcList){
					Integer dotIdx = dto.getIndex();
					if(dotIdx.equals(index)){
						com.alibaba.fastjson.JSONArray jsonArray = dto.getKeys();
						for(int i=0;i<jsonArray.size();i++){
							com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(i);
							Map map = (Map) jsonObject.get("keyCodeMap");
							Iterator keyMapItor = map.keySet().iterator();
							while (keyMapItor.hasNext()){
								if(keyMapItor.next().equals(key)){
									KeyCode keyCode = (KeyCode)map.get(key);
									if(yaokonyunKeyCode==null){
										yaokonyunKeyCode = new TYaokonyunKeyCode();
										yaokonyunKeyCode.setSrc(keyCode.getSrcCode());
									}
								}
							}
						}
					}
				}
			}
			if(yaokonyunKeyCode==null){
				res.setStatus(ResponseEnum.NoIRKey.getStatus());
				res.setMessage(ResponseEnum.NoIRKey.getMsg());
			}else{
				String srcCode = yaokonyunKeyCode.getSrc();
				logger.info(" src ========= "+srcCode);
				requestMap.put("command","set");
				com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
				com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
				json.put("functionId",1);
				jsonArray.add(json);
				json.put("data",yaokonyunKeyCode.getSrc());
				requestMap.put("value",jsonArray);
				topServer.pubIrRPC(requestMap,serialId);
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// 手动匹配遥控方案
	@RequestMapping(value = "/getIrList", method = RequestMethod.POST)
	ResponseObject getIrList(@RequestParam(required = true, value = "brandId") String brandId,
							 @RequestParam(required = true, value = "deviceType") String deviceType) {
		ResponseObject res = new ResponseObject();
		try {
			res.setData(getRemoteControlList(brandId,deviceType));
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	//本地遥控方案——新增方案
	@RequestMapping(value = "/localIrDeviceDownload", method = RequestMethod.POST)
	ResponseObject localIrDeviceDownload(@RequestParam(required = true, value = "index") Integer index,
										 @RequestParam(required = true, value = "timeout") Integer timeout,
										 @RequestParam(required = true, value = "serialId") String serialId) {
		ResponseObject res = new ResponseObject();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		try {
			List<TYaokonyunKeyCode> yaokonyunKeyCodeList = yaoKongYunService.getYaoKongKeyCodeBySerialIdAndIndex(serialId,index);
			if(yaokonyunKeyCodeList.size()==0){
				res.setStatus(ResponseEnum.NoIRKey.getStatus());
				res.setMessage(ResponseEnum.NoIRKey.getMsg());
			}else{
				cmdCache.setIRDeviceInfoList("ir_keCodeList_"+index,yaokonyunKeyCodeList);
				cmdCache.addIrIndexBySerialId(serialId,index.toString());
				requestMap.put("command","set");
				com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
				com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
				json.put("functionId",6);
				jsonArray.add(json);
				json.put("data",1);
				json.put("count",yaokonyunKeyCodeList.size());
				requestMap.put("value",jsonArray);
				JSONObject jsonObject = topServer.pubIrRPC(requestMap,serialId);
				Integer respCode = (Integer)jsonObject.get("respCode");
				org.json.JSONArray resJsonArr = (org.json.JSONArray) jsonObject.get("value");
				org.json.JSONObject jsonVal = (org.json.JSONObject) resJsonArr.get(0);
				if(respCode.equals(200)){
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("respCode",respCode);
					map.put("data",jsonVal.get("data"));
					map.put("index",jsonVal.get("index"));
					res.setData(map);
					res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
					res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
				}else{
					res.setStatus(ResponseEnum.Error.getStatus());
					res.setMessage(ResponseEnum.Error.getMsg());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	private String crcSum(String code){
		Integer subtractVal = new Integer(0);
		if(code.length()>=4){
			Integer total = new Integer(0);
			int j=0;
			int len = j+4;
			for(j=0;len<=code.length();j=j+4) {
				String subCode = code.substring(0, 4);
				String begin =subCode.substring(0,2);
				String end =subCode.substring(2,4);
				Integer beginInt = Integer.parseInt(begin, 16);
				Integer endInt = Integer.parseInt(end,16);
				total += beginInt+endInt;
				code = code.substring(len,code.length());
			}
			String lowInt = Integer.toHexString(total);
			lowInt = lowInt.substring(lowInt.length()-2, lowInt.length());
			subtractVal = 255-Integer.parseInt(lowInt,16);
		}else{
			subtractVal = 255-Integer.parseInt(code,16);
		}
		String returnVal = Integer.toHexString(subtractVal);
		return returnVal;
	}

	//本地遥控方案——下载方案
	@RequestMapping(value = "/irDownLoad", method = RequestMethod.POST)
	ResponseObject irDownLoad(@RequestParam(required = true, value = "index") Integer index,
							  @RequestParam(required = true, value = "serialId") String serialId,
							  @RequestParam(required = true, value = "codeIndex") Integer codeIndex) {
		ResponseObject res = new ResponseObject();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		com.alibaba.fastjson.JSONObject resMap = new com.alibaba.fastjson.JSONObject();
		resMap.put("type",22);
		resMap.put("serialId",serialId);
		resMap.put("index",codeIndex);
		resMap.put("localaddr",index);
		List<TUserAliDevice> userAliDevice = userAliDevService.queryAliUserId(serialId);
		Set<TUserAliDevice> userSet = new HashSet<TUserAliDevice>();
		for(TUserAliDevice user : userAliDevice){
			userSet.add(user);
		}
		try {
			logger.info(" ====== 创建本地码库后，进入下载码库 ====== ");
			List<TYaokonyunKeyCode> yaokonyunKeyCodeList = cmdCache.getIRDeviceInfoList("ir_keCodeList_"+codeIndex);
			cmdCache.addIrDownloadCount(codeIndex,yaokonyunKeyCodeList.size());

			JSONObject jsonObject = null;
			Integer respCode = new Integer(0);
			for(int i=0;i<yaokonyunKeyCodeList.size();i++){
				TYaokonyunKeyCode yaokonyunKeyCode = yaokonyunKeyCodeList.get(i);
				requestMap.put("command","set");
				com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
				com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
				String code =yaokonyunKeyCode.getSrc();
				code += crcSum(code);
				json.put("functionId",7);
				json.put("data",code);
				json.put("index",index);
				json.put("count",i+1);
				String sendkey = yaokonyunKeyCode.getKey();
				String[] arr = sendkey.split("_");
				if(yaokonyunKeyCode.getVersion()==1){//版本v1截取方式不同
					arr = sendkey.split("");
				}
				if(sendkey.indexOf("*")>=0){
					continue;
				}
				StringBuffer sb = new StringBuffer();
				for(int j=0;j<arr.length;j++){
					String detail = arr[j];
					if(j==2&&!detail.equals("")){
						String temperature = Integer.toHexString(Integer.valueOf(detail));
						sb.append(temperature);
					}else{
						if(detail.equals("")){
							detail = "_";
						}
						if(detail.equals("p0")){
							break;
						}
						IREnum enums = IREnum.getRegion(detail);
						sb.append(enums.getValue());
					}
				}
				String keyStr = sb.toString();
				keyStr +=crcSum(keyStr);
				json.put("key",keyStr);
				jsonArray.add(json);
				requestMap.put("value",jsonArray);
				jsonObject = topServer.pubIrRPC(requestMap,serialId);
				respCode = (Integer)jsonObject.get("respCode");

				if(respCode!=200){
					restransCode(jsonObject,keyStr,requestMap,serialId,resMap,userSet);
				}
			}
			if(respCode==200){
				resMap.put("success",true);
				pushService.pairIrRemotecode(resMap,userSet);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resMap.put("success",false);
			pushService.pairIrRemotecode(resMap,userSet);
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	private void restransCode(JSONObject jsonObject,String keyStr,Map<String, Object> requestMap,String serialId,com.alibaba.fastjson.JSONObject resMap,Set<TUserAliDevice> userSet) throws Exception{
		Integer respCode = (Integer)jsonObject.get("respCode");
		logger.info("respCode ====== "+respCode);
		org.json.JSONArray resJsonArr = (org.json.JSONArray) jsonObject.get("value");
		org.json.JSONObject jsonVal = (org.json.JSONObject) resJsonArr.get(0);
		Integer resdata = (Integer)jsonVal.get("data");
		if(resdata==0){
			String times = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("ir_retrans_key_"+keyStr)==null?"0":cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("ir_retrans_key_"+keyStr);
			Integer restransTimes= Integer.parseInt(times);
			if(restransTimes<3){
				cmdCache.restranTimes(keyStr,restransTimes+1);
				JSONObject resjsonObject = topServer.pubIrRPC(requestMap,serialId);
				restransCode(resjsonObject,keyStr,requestMap,serialId,resMap,userSet);
			}else{
				resMap.put("success",false);
				pushService.pairIrRemotecode(resMap,userSet);
				logger.info(" ======= 红外下载码库失败 ====== ");
				throw new Exception();
			}
		}else{
			resMap.put("success",true);
			pushService.pairIrRemotecode(resMap,userSet);
		}
	}

	//本地遥控方案——删除方案
	@RequestMapping(value = "/localIrDeviceDelete", method = RequestMethod.POST)
	ResponseObject localIrDeviceDelete(@RequestParam(required = true, value = "index") Integer index,
									   @RequestParam(required = true, value = "serialId") String serialId) {
		ResponseObject res = new ResponseObject();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		try {
			List<TYaokonyunKeyCode> yaokonyunKeyCodeList = yaoKongYunService.getYaoKongKeyCodeBySerialIdAndIndex(serialId,index);
			if(yaokonyunKeyCodeList.size()==0){
				res.setStatus(ResponseEnum.NoIRKey.getStatus());
				res.setMessage(ResponseEnum.NoIRKey.getMsg());
			}else{
				TYaokonyunKeyCode yaokonyunKeyCode = yaokonyunKeyCodeList.get(0);
				requestMap.put("command","set");
				com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
				com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
				json.put("functionId",6);
				json.put("data",2);
				json.put("index",yaokonyunKeyCode.getIndex());
				jsonArray.add(json);
				requestMap.put("value",jsonArray);
				topServer.pubIrRPC(requestMap,serialId);
				res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
				res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			}

		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// 一键匹配遥控方案——进入空调对码模式
	@RequestMapping(value = "/pairIrRemotecode", method = RequestMethod.POST)
	ResponseObject pairIrRemotecode(@RequestParam(required = true, value = "brandId") String brandId,
						  @RequestParam(required = true, value = "serialId") String serialId,
						  @RequestParam(required = true, value = "timeout") Integer timeout) {
		ResponseObject res = new ResponseObject();
		Map<String,Object> resMap = new HashMap<String,Object>();
		try {
			cmdCache.addIrBrandIdBySerialId(serialId,brandId);
			resMap.put("command","set");
			com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
			com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
			json.put("data",timeout);
			json.put("functionId",5);
			jsonArray.add(json);
			resMap.put("value",jsonArray);
			topServer.pubIrRPC(resMap,serialId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.PairCodeFailed.getStatus());
			res.setMessage(ResponseEnum.PairCodeFailed.getMsg());
		}
		return res;
	}

	public Map<String,Object> getRemoteControlList(String brandId,String deviceType) throws Exception {
		Map<String,Object> resMap = new HashMap<String,Object>();
		TYaokonyunDevice yaokonyunDevice = getYaoKongDevice();
		List<String> strings = new ArrayList<String>();
		strings.add("bid="+brandId);
		strings.add("t="+deviceType);
		strings.add("v=3");
		strings.add("zip=1");
		String result = yaoKongYunSend
				.postMethod(strings,yaokonyunDevice,yaoKongYunConfig.getUrlPrefix()+"?c=l");
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		MatchRemoteControlResult remoteControlResult = gson.fromJson(result,MatchRemoteControlResult.class);

		if(remoteControlResult==null||remoteControlResult.getSm()==0){
			resMap.put("rs",new ArrayList());
		}else{
			List<MatchRemoteControl>  list = remoteControlResult.getRs();
			List<QueryRemoteBySrcDTO> dtoList = new ArrayList<QueryRemoteBySrcDTO>();
			List<QueryRemoteBySrcDTO2> dtoSrcList = new ArrayList<QueryRemoteBySrcDTO2>();
			for(MatchRemoteControl matchRemoteControl :list){
				QueryRemoteBySrcDTO dto = new QueryRemoteBySrcDTO(matchRemoteControl);
				QueryRemoteBySrcDTO2 srcDto = new QueryRemoteBySrcDTO2(matchRemoteControl);
				Integer idx = IndexUtils.getIdx();
				cmdCache.addIrBrandId(idx.toString(),brandId);
				cmdCache.addIrDeviceType(idx.toString(),deviceType);
				dto.setIndex(idx);
				dto.setBrandId(Integer.valueOf(brandId));
				srcDto.setIndex(idx);
				srcDto.setBrandType(Integer.valueOf(brandId));
				dtoList.add(dto);
				dtoSrcList.add(srcDto);
			}
			cmdCache.setIRDeviceInfoList(brandId+"_"+deviceType+"_"+"_remoteControlList",dtoList);
			cmdCache.setIRDeviceInfoList(brandId+"_"+deviceType+"_"+"_remoteControlListSrc",dtoSrcList);
			resMap.put("rs",dtoList);
		}
		return resMap;
	}

	// 删除红外遥控方案
	@RequestMapping(value = "/deleteIrDevice", method = RequestMethod.POST)
	ResponseObject deleteIrDevice(@RequestParam(required = true, value = "serialId") String serialId,
							 @RequestParam(required = true, value = "index") String index) {
		ResponseObject res = new ResponseObject();
		try {

			yaoKongYunService.deleteTYaokonyunKeyCode(serialId,index);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// 删除方案中特定按键
	@RequestMapping(value = "/deleteIrDeviceKey", method = RequestMethod.POST)
	ResponseObject deleteIrDeviceKey(@RequestParam(required = true, value = "serialId") String serialId,
								     @RequestParam(required = true, value = "index") String index,
									 @RequestParam(required = true, value = "key") String key,
									 @RequestParam(required = true, value = "keyType") String keyType) {
		ResponseObject res = new ResponseObject();
		try {
			yaoKongYunService.deleteTYaokonyunKeyCodeByKeyName(serialId,index,key,keyType);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// 获取遥控云品牌类型
	@RequestMapping(value = "/getIrBrandList", method = RequestMethod.POST)
	ResponseObject getIrBrandList(@RequestParam(required = true, value = "deviceType") String deviceType) {
		ResponseObject res = new ResponseObject();
		Map<String,Object> resMap = new HashMap<String,Object>();
		try {

			List<TYaoKongYunBrand> yaoKongYunBrandList = yaoKongYunService.getYaoKongYunBrandByDeviceType(deviceType);
			if(yaoKongYunBrandList!=null&&yaoKongYunBrandList.size()>0){
				resMap.put("rs",yaoKongYunBrandList);
			}else{
				TYaokonyunDevice yaokonyunDevice = getYaoKongDevice();
				List<String> strings = new ArrayList<String>();
				strings.add("t="+deviceType);
				String result = yaoKongYunSend
						.postMethod(strings,yaokonyunDevice,yaoKongYunConfig.getUrlPrefix()+"?c=f");
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				BrandResult brandResult = gson.fromJson(result,BrandResult.class);
				List<Brand> brandList = brandResult.getRs();
				for(Brand brand : brandList){
					TYaoKongYunBrand yaoKongYunBrand = new TYaoKongYunBrand();
					yaoKongYunBrand.setbId(brand.getBid());
					yaoKongYunBrand.setCommon(brand.getCommon());
					yaoKongYunBrand.setDeviceType(Integer.valueOf(deviceType));
					yaoKongYunBrand.setLastOpTime(new Date());
					yaoKongYunBrand.setName(brand.getName());
					yaoKongYunService.addTYaoKongYunBrand(yaoKongYunBrand);
				}
				resMap.put("rs",brandList);
			}
			res.setData(resMap);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}


	// 学习遥控方案——进入按键学习模式
	@RequestMapping(value = "/learnIrDeviceKey", method = RequestMethod.POST)
	ResponseObject learnIrDeviceKey(@RequestParam(required = true, value = "serialId") String serialId,
								   @RequestParam(required = true, value = "index") String index,
								   @RequestParam(required = true, value = "keyType") String keyType,
								   @RequestParam(required = true, value = "key") String key,
								   @RequestParam(required = true, value = "timeout") String timeout) {
		ResponseObject res = new ResponseObject();
		Map<String,Object> resMap = new HashMap<String,Object>();
		try {
            cmdCache.addIrIndexBySerialId(serialId,index);
			cmdCache.addIrTestCodeKeyName(index,key);
			cmdCache.addIrTestCodeKeyNameType(index,keyType);
			List<TYaokonyunKeyCode> list = yaoKongYunService.getYaoKongKeyCodeBySerialIdAndIndex(serialId,Integer.valueOf(index));
			if(list.size()>0){
				TYaokonyunKeyCode keyCode = list.get(0);
				logger.info("======= "+keyCode.getName());
				cmdCache.addIrDeviceTypeByIRName(index,keyCode.getName());
			}
			resMap.put("command","set");
			com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
			com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
			json.put("functionId",4);
			json.put("data",Integer.valueOf(timeout));
			jsonArray.add(json);
			resMap.put("value",jsonArray);
			topServer.pubIrRPC(resMap,serialId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.LearnKeyFailed.getStatus());
			res.setMessage(ResponseEnum.LearnKeyFailed.getMsg());
		}
		return res;
	}

	//获取遥控云遥控类型
	@RequestMapping(value = "/getIrTypeList", method = RequestMethod.POST)
	ResponseObject getIrTypeList() {
		ResponseObject res = new ResponseObject();
		try {
			Map<String,Object> resMap = new HashMap<String,Object>();
			List<TYaokonyunDeviceType> yaokonyunDeviceTypeList = yaoKongYunService.getYaoKongYunDeviceType();
			if(yaokonyunDeviceTypeList.size()>0){
				resMap.put("rs",yaokonyunDeviceTypeList);
			}else{
				TYaokonyunDevice yaokonyunDevice = getYaoKongDevice();
				String result = yaoKongYunSend.postMethod(null,yaokonyunDevice,yaoKongYunConfig.getUrlPrefix()+"?c=t&appid="+yaokonyunDevice.getAppId()+"f="+yaokonyunDevice.getDeviceId());
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				DeviceTypeResult deviceTypeResult = gson.fromJson(result,DeviceTypeResult.class);
				List<DeviceType> deviceTypeList = deviceTypeResult.getRs();
				for (DeviceType deviceType:deviceTypeList) {
					TYaokonyunDeviceType yaokonyunDeviceType = new TYaokonyunDeviceType();
					yaokonyunDeviceType.setLastOpTime(new Date());
					yaokonyunDeviceType.setName(deviceType.getName());
					yaokonyunDeviceType.settId(deviceType.getTid());
					yaoKongYunService.addTYaokonyunDeviceType(yaokonyunDeviceType);
				}
				resMap.put("rs",deviceTypeList);
			}

			res.setData(resMap);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

    //获取红外遥控方案
    @RequestMapping(value = "/queryIrDevice", method = RequestMethod.POST)
    ResponseObject queryIrDevice(@RequestParam(required = true, value = "serialId") String serialId) {
        ResponseObject res = new ResponseObject();
        try {
            Map<String,Object> resMap = new HashMap<String,Object>();
            List<QueryRemoteBySrcDTO> dtoList = new ArrayList<QueryRemoteBySrcDTO>();
            List<TYaokonyunKeyCode> yaokonyunKeyCodeList = yaoKongYunService.getYaoKongKeyCodeBySerialId(serialId);
            List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> filterList = new ArrayList<Map<String,Object>>();
            com.alibaba.fastjson.JSONArray keyArray = null;
			Map<String,Object> map = null;
			for(TYaokonyunKeyCode keyCode:yaokonyunKeyCodeList){
				map = new HashMap<String, Object>();
				keyArray = new com.alibaba.fastjson.JSONArray();
            	com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
				com.alibaba.fastjson.JSONArray nonArr = new com.alibaba.fastjson.JSONArray();
				String key = keyCode.getKey();
				jsonObject.put("key",key);
				keyArray.add(jsonObject);
				map.put("version",keyCode.getVersion());
				map.put("rmodel",keyCode.getRmodel());
				map.put("name",keyCode.getName());
				Integer index = keyCode.getIndex();
				map.put("index",index);
				map.put("type",keyCode.gettId());
				map.put("brandType",keyCode.getBrandId());
				if(keyCode.getKeyType()==0){
					map.put("keys",keyArray);
					map.put("extendsKeys",nonArr);
				}else{
					map.put("keys",nonArr);
					map.put("extendsKeys",keyArray);
				}
				mapList.add(map);
            }
			String idxs = "";
            if(mapList.size()>0){
				for(Map<String,Object> dtomap :mapList){
					com.alibaba.fastjson.JSONArray dtoArray = (com.alibaba.fastjson.JSONArray)dtomap.get("keys");
					com.alibaba.fastjson.JSONArray dtoArray2 = (com.alibaba.fastjson.JSONArray)dtomap.get("extendsKeys");
					Integer dtoIdx = (Integer)dtomap.get("index");
					if(filterList.size()==0){
						filterList.add(dtomap);
						idxs += dtoIdx+",";
					}else{
						for(int i=0;i<filterList.size();i++){
							Map<String,Object> filterMap = filterList.get(i);
							com.alibaba.fastjson.JSONArray filterArray = (com.alibaba.fastjson.JSONArray)filterMap.get("keys");
							Integer filterIdx = (Integer) filterMap.get("index");
							if(filterIdx.equals(dtoIdx)&&dtoArray.size()>0
									&&!filterArray.equals(dtoArray)){
								com.alibaba.fastjson.JSONObject dtoJson = dtoArray.getJSONObject(0);
								filterArray.add(dtoJson);
							}else if(idxs.indexOf(dtoIdx.toString())<0){
								filterList.add(dtomap);
								idxs += dtoIdx+",";
							}
							com.alibaba.fastjson.JSONArray filterArray2 = (com.alibaba.fastjson.JSONArray)filterMap.get("extendsKeys");
							if(filterIdx.equals(dtoIdx)&&dtoArray2.size()>0
									&&!filterArray2.equals(dtoArray2)){
								com.alibaba.fastjson.JSONObject dtoJson = dtoArray2.getJSONObject(0);
								filterArray2.add(dtoJson);
							}else if(idxs.indexOf(dtoIdx.toString())<0){
								filterList.add(dtomap);
								idxs += dtoIdx+",";
							}
						}
					}
				}
				for(Map<String,Object> dtomap :filterList){
					dtoList.add(new QueryRemoteBySrcDTO(dtomap));
				}
			}
            resMap.put("rs",dtoList);
            res.setData(resMap);
            res.setStatus(ResponseEnum.SelectSuccess.getStatus());
            res.setMessage(ResponseEnum.SelectSuccess.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(ResponseEnum.Error.getStatus());
            res.setMessage(ResponseEnum.Error.getMsg());
        }
        return res;
    }

    //重命名红外遥控方案
    @RequestMapping(value = "/renameIrDevice", method = RequestMethod.POST)
    ResponseObject renameIrDevice(@RequestParam(required = true, value = "serialId") String serialId,
                                  @RequestParam(required = true, value = "index") String index,
                                  @RequestParam(required = true, value = "name") String name) {
        ResponseObject res = new ResponseObject();
        try {
            yaoKongYunService.updateYaoKongKeyCodeNameBySerialIdAndIndex(serialId,index,name);
            res.setStatus(ResponseEnum.SelectSuccess.getStatus());
            res.setMessage(ResponseEnum.SelectSuccess.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(ResponseEnum.Error.getStatus());
            res.setMessage(ResponseEnum.Error.getMsg());
        }
        return res;
    }

    //学习遥控方案——新建自定义遥控器
    @RequestMapping(value = "/createIrDevice", method = RequestMethod.POST)
    ResponseObject createIrDevice(@RequestParam(required = true, value = "serialId") String serialId,
                                  @RequestParam(required = true, value = "deviceType") String deviceType,
                                  @RequestParam(required = true, value = "name") String name,
                                  @RequestParam(required = true, value = "brandId") String brandId) {
        ResponseObject res = new ResponseObject();
        Map<String,Object> resMap = new HashMap<String,Object>();
        try {
            cmdCache.addIrBrandIdBySerialId(serialId,brandId);
            cmdCache.addIrDeviceTypeBySerialId(serialId,deviceType);
            cmdCache.addIrDeviceTypeByIRName(serialId,name);
			Integer idx = IndexUtils.getIdx();
			cmdCache.addIrIndexBySerialId(serialId,idx.toString());
            QueryRemoteBySrcDTO dto = new QueryRemoteBySrcDTO();
            dto.setName(name);
            dto.setKeys(new com.alibaba.fastjson.JSONArray());
            dto.setExtendsKeys(new com.alibaba.fastjson.JSONArray());
            dto.setIndex(idx);
            dto.setBrandId(Integer.valueOf(brandId));
            dto.setDeviceType(Integer.valueOf(deviceType));
            dto.setRid("");
            dto.setRmodel("");
            dto.setVersion("0");
			resMap.put("remote",dto);

            res.setData(resMap);
            res.setStatus(ResponseEnum.AddSuccess.getStatus());
            res.setMessage(ResponseEnum.AddSuccess.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            res.setStatus(ResponseEnum.Error.getStatus());
            res.setMessage(ResponseEnum.Error.getMsg());
        }
        return res;
    }

	// 手动匹配/一键匹配遥控方案——绑定码库方案
	@RequestMapping(value = "/bindIrRemotecode", method = RequestMethod.POST)
	ResponseObject bindIrRemotecode(@RequestParam(required = true, value = "brandId") String brandId,
									@RequestParam(required = true, value = "deviceType") String deviceType,
									@RequestParam(required = true, value = "remoteId") String remoteId,
									@RequestParam(required = true, value = "name") String name,
									@RequestParam(required = true, value = "serialId") String serialId) {
		ResponseObject res = new ResponseObject();
		try {
			Map<String,Object> resMap = new HashMap<String,Object>();
			JSONArray jsonArray = new JSONArray();
			String keyName = "";
			Integer idx = 0;
			Integer version = 0;
			String rmodel = "";
			String remoteControlSrc = null;
			List<QueryRemoteBySrcDTO2> list = cmdCache.getIRDeviceInfoList(brandId+"_"+deviceType+"_"+"_remoteControlListSrc");

			for(QueryRemoteBySrcDTO2 dto : list){
				version = Integer.valueOf(dto.getVersion());
				idx = dto.getIndex();
				rmodel = dto.getRmodel();
				String rid = dto.getRid();
				dto.getBrandType();
				com.alibaba.fastjson.JSONObject jsonObject = dto.getKeys().getJSONObject(0);
				if(rid.equals(remoteId)){
					Map<String,Object> keyCodeMap = (Map<String, Object>) jsonObject.get("keyCodeMap");
					Iterator<String> keys = keyCodeMap.keySet().iterator();
					while (keys.hasNext()){
						keyName = keys.next();
						KeyCode keyCode = (KeyCode) keyCodeMap.get(keyName);
						remoteControlSrc = keyCode.getSrcCode();
					}
				}

			}

			List<TYaokonyunKeyCode> yaokonyunKeyCodeList = yaoKongYunService.getYaoKongKeyCodeByRemoteId(idx);
			if(yaokonyunKeyCodeList.size()>0){
				for(TYaokonyunKeyCode yaokonyunKeyCode : yaokonyunKeyCodeList){
					com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
					jsonObject.put("key",yaokonyunKeyCode.getKey());
					jsonArray.add(jsonObject);
				}
			}else{
				TYaokonyunDevice yaokonyunDevice = getYaoKongDevice();
				List<String> strings = new ArrayList<String>();
				strings.add("r="+remoteId);
				strings.add("zip=1");
				String result = yaoKongYunSend.postMethod(strings,yaokonyunDevice,yaoKongYunConfig.getUrlPrefix()+"?c=d");
				Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
				Type type = new TypeToken<List<RemoteControl>>() {
				}.getType();
				List<RemoteControl> remoteControlList = gson.fromJson(result,type);
				RemoteControl remoteControl = remoteControlList.get(0);
				List<Map<String, LinkedTreeMap>> mapList = gson.fromJson("["+remoteControl.getRcCommand()+"]",List.class);

				if(mapList.size()>0){
					SaveThread saveThread = new SaveThread();
					saveThread.setMapList(mapList);
					saveThread.setBrandId(brandId);
					saveThread.setDeviceType(deviceType);
					saveThread.setIdx(idx);
					saveThread.setKeyName(keyName);
					saveThread.setName(name);
					saveThread.setRemoteControlSrc(remoteControlSrc);
					saveThread.setRmodel(rmodel);
					saveThread.setSerialId(serialId);
					saveThread.setVersion(version);
					Thread t = new Thread(saveThread);
					t.start();
					Map<String, LinkedTreeMap> keyCodeMap = mapList.get(0);
					Iterator<String> iterator = keyCodeMap.keySet().iterator();
					while (iterator.hasNext()){
						com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
						String key = iterator.next();
						jsonObject.put("key",key);
						jsonArray.add(jsonObject);
					}
				}
			}
			Map<String,Object> dataMap = new HashMap<String, Object>();
			dataMap.put("remote",resMap);
			resMap.put("index",idx);
			resMap.put("name",name);
			resMap.put("brandId",brandId);
			resMap.put("deviceType",deviceType);
			resMap.put("keys",jsonArray);
			resMap.put("extendsKeys",new JSONArray());
			res.setData(dataMap);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	class SaveThread implements Runnable{
		public List<Map<String, LinkedTreeMap>> getMapList() {
			return mapList;
		}

		public void setMapList(List<Map<String, LinkedTreeMap>> mapList) {
			this.mapList = mapList;
		}

		private List<Map<String, LinkedTreeMap>> mapList;

		public String getKeyName() {
			return keyName;
		}

		public void setKeyName(String keyName) {
			this.keyName = keyName;
		}

		public String getRemoteControlSrc() {
			return remoteControlSrc;
		}

		public void setRemoteControlSrc(String remoteControlSrc) {
			this.remoteControlSrc = remoteControlSrc;
		}

		public Integer getIdx() {
			return idx;
		}

		public void setIdx(Integer idx) {
			this.idx = idx;
		}

		public String getBrandId() {
			return brandId;
		}

		public void setBrandId(String brandId) {
			this.brandId = brandId;
		}

		public String getRmodel() {
			return rmodel;
		}

		public void setRmodel(String rmodel) {
			this.rmodel = rmodel;
		}

		public String getDeviceType() {
			return deviceType;
		}

		public void setDeviceType(String deviceType) {
			this.deviceType = deviceType;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}

		public String getSerialId() {
			return serialId;
		}

		public void setSerialId(String serialId) {
			this.serialId = serialId;
		}

		private String keyName;
		private String remoteControlSrc;
		private Integer idx;
		private String brandId;
		private String rmodel;
		private String deviceType;
		private String name;
		private Integer version;
		private String serialId;

		@Override
		public void run() {
			Map<String, LinkedTreeMap> keyCodeMap = mapList.get(0);
			Iterator<String> iterator = keyCodeMap.keySet().iterator();
			while (iterator.hasNext()){

				com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
				String key = iterator.next();
				LinkedTreeMap treeMap = keyCodeMap.get(key);
				String src = (String) treeMap.get("src");
				TYaokonyunKeyCode yaokonyunKeyCode = new TYaokonyunKeyCode();
				yaokonyunKeyCode.setIndex(idx);
				yaokonyunKeyCode.setLastOpTime(new Date());
				yaokonyunKeyCode.setBrandId(Integer.valueOf(brandId));
				yaokonyunKeyCode.setRmodel(rmodel);
				yaokonyunKeyCode.settId(Integer.valueOf(deviceType));
				yaokonyunKeyCode.setName(name);
				yaokonyunKeyCode.setVersion(version);
				yaokonyunKeyCode.setSrc(src);
				yaokonyunKeyCode.setSerialId(serialId);
				yaokonyunKeyCode.setKey(key);
				yaokonyunKeyCode.setKeyType(0);
				yaoKongYunService.addTYaokonyunKeyCode(yaokonyunKeyCode);
			}
		}
	}

	private TYaokonyunDevice initYaoKongDevice() throws Exception {
		String ir_appId = aliDevCache.getValue("ir_appId");
		TYaokonyunDevice yaokonyunDevice = null;

		if (StringUtils.isEmpty(ir_appId)) {
			yaokonyunDevice = getYaoKongDevice();
		} else {
			ir_appId.split(":");
		}
		return yaokonyunDevice;
	}

	public TYaokonyunDevice useTimesExpire(String deviceId) throws Exception {
		yaoKongYunService.updateYaoKongDevice(deviceId);
		return getYaoKongDevice();
	}

	private TYaokonyunDevice getYaoKongDevice() throws Exception {
		TYaokonyunDevice yaokonyunDevice = null;
		yaokonyunDevice = yaoKongYunService.getYaoKongYunDevice();
		if (yaokonyunDevice == null) {
			yaokonyunDevice = createYaoKongYunDevice();
			List<String> strings = new ArrayList<String>();
			strings.add("appid="+yaokonyunDevice.getAppId());
			strings.add("f="+yaokonyunDevice.getDeviceId());
			yaoKongYunSend.postMethod(null,yaokonyunDevice,yaoKongYunConfig.getUrlPrefix()+"?c=r");
		}

		return yaokonyunDevice;
	}

	private TYaokonyunDevice createYaoKongYunDevice() throws Exception {
		TYaokonyunDevice device = new TYaokonyunDevice();
		device.setDeviceId(MD5.getMD5Str(new Date().getTime() + ""));
		device.setAppId("15027861733449");
		yaoKongYunService.addYaoKongDevice(device);
		return yaoKongYunService.getYaoKongYunDevice();
	}

}
