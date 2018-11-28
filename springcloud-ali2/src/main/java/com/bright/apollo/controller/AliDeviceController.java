package com.bright.apollo.controller;

import java.util.*;

import com.bright.apollo.bean.*;
import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.entity.*;
import com.bright.apollo.service.*;
import com.bright.apollo.util.IndexUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
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
	private MqttGateWay mqttGateWay;

	@Autowired
	private SpringContextUtil springContextUtil;

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

	@RequestMapping(value = "/controllIR", method = RequestMethod.POST)
	ResponseObject<List<Map<String, String>>> controllIR(@RequestParam(required = true, value = "serialId") String serialId,
														   @RequestParam(required = true, value = "index") Integer index,
														   @RequestParam(required = true, value = "key") String key) {
		ResponseObject<List<Map<String, String>>> res = new ResponseObject<List<Map<String, String>>>();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		try {
			requestMap.put("command","set");
			com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
			com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
			com.alibaba.fastjson.JSONObject dataJson = new com.alibaba.fastjson.JSONObject();
			json.put("functionId",1);
			jsonArray.add(json);
			dataJson.put("data",ByteHelper.bytesToHexString(key.getBytes()));
			jsonArray.add(dataJson);
			requestMap.put("value",jsonArray);
			JSONObject jsonObject = topServer.pubIrRPC(requestMap);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
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
		Map<String,Object> resMap = new HashMap<String,Object>();
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

	// 一键匹配遥控方案——进入空调对码模式
	@RequestMapping(value = "/pairIrRemotecode", method = RequestMethod.POST)
	void pairIrRemotecode(@RequestParam(required = true, value = "brandId") String brandId,
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
			JSONObject jsonObject = topServer.pubIrRPC(resMap);
			logger.info(" ====== jsonObject ====== "+jsonObject);
			res.setData(jsonObject);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
	}

	public Map<String,Object> getRemoteControlList(String brandId,String deviceType) throws Exception {
		Map<String,Object> resMap = new HashMap<String,Object>();
		TYaokonyunDevice yaokonyunDevice = getYaoKongDevice();
		List<String> strings = new ArrayList<String>();
		strings.add("bid="+brandId);
		strings.add("t="+deviceType);
		strings.add("v=4");
		strings.add("zip=1");
		String result = yaoKongYunSend
				.postMethod(strings,yaokonyunDevice,yaoKongYunConfig.getUrlPrefix()+"?c=l");
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		MatchRemoteControlResult remoteControlResult = gson.fromJson(result,MatchRemoteControlResult.class);

		if(remoteControlResult==null||remoteControlResult.getSm()==0){
//			resMap.put("sm",0);
			resMap.put("rs",new ArrayList());
//			return new ArrayList<QueryRemoteBySrcDTO>();
//			return resMap;
		}else{
			List<MatchRemoteControl>  list = remoteControlResult.getRs();
			List<TYaokonyunRemoteControl> remoteControlList = new ArrayList<TYaokonyunRemoteControl>();
			List<QueryRemoteBySrcDTO> dtoList = new ArrayList<QueryRemoteBySrcDTO>();
			List<QueryRemoteBySrcDTO2> dtoSrcList = new ArrayList<QueryRemoteBySrcDTO2>();
			for(MatchRemoteControl matchRemoteControl :list){
				TYaokonyunRemoteControl tYaokonyunRemoteControl = new TYaokonyunRemoteControl(matchRemoteControl);
				remoteControlList.add(tYaokonyunRemoteControl);
				QueryRemoteBySrcDTO dto = new QueryRemoteBySrcDTO(matchRemoteControl);
				QueryRemoteBySrcDTO2 srcDto = new QueryRemoteBySrcDTO2(matchRemoteControl);
				Integer idx = IndexUtils.getIdx();
				cmdCache.addIrBrandId(idx.toString(),brandId);
				cmdCache.addIrDeviceType(idx.toString(),deviceType);
//				cmdCache.addIrTestCodeSerialId(serialId);
				dto.setIndex(idx);
				dto.setBrandType(Integer.valueOf(brandId));
				srcDto.setIndex(idx);
				srcDto.setBrandType(Integer.valueOf(brandId));
				dtoList.add(dto);
				dtoSrcList.add(srcDto);
			}
			cmdCache.setIRDeviceInfoList(brandId+"_"+deviceType+"_"+"_remoteControlList",dtoList);
			cmdCache.setIRDeviceInfoList(brandId+"_"+deviceType+"_"+"_remoteControlListSrc",dtoSrcList);
//			resMap.put("sm",dtoList.size());
			resMap.put("rs",dtoList);
//			return dtoList;
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
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
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
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
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
//				resMap.put("sm",yaoKongYunBrandList.size());
				resMap.put("rs",yaoKongYunBrandList);
//				res.setData(yaoKongYunBrandList);
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
//				resMap.put("sm",brandList.size());
				resMap.put("rs",brandList);
//				res.setData(brandList);
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
			cmdCache.addIrTestCodeSerialId(serialId,index);
			cmdCache.addIrTestCodeKeyName(index,key);
			cmdCache.addIrTestCodeKeyNameType(index,keyType);
			cmdCache.addIrIndex(index);
			resMap.put("command","set");
			com.alibaba.fastjson.JSONArray jsonArray = new com.alibaba.fastjson.JSONArray();
			com.alibaba.fastjson.JSONObject json = new com.alibaba.fastjson.JSONObject();
			json.put("functionId",4);
			json.put("data",timeout);
			jsonArray.add(json);
			resMap.put("value",jsonArray);
			JSONObject jsonObject = topServer.pubIrRPC(resMap);
			logger.info("response ===== "+jsonObject);
			res.setData(jsonObject);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
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
//				resMap.put("sm",yaokonyunDeviceTypeList.size());
				resMap.put("rs",yaokonyunDeviceTypeList);
//				res.setData(yaokonyunDeviceTypeList);
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
//				resMap.put("sm",deviceTypeList.size());
				resMap.put("rs",deviceTypeList);
//				res.setData(deviceTypeList);
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
            for(TYaokonyunKeyCode keyCode:yaokonyunKeyCodeList){
                dtoList.add(new QueryRemoteBySrcDTO(keyCode));
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
//            cmdCache.addIrBrandIdBySerialId(serialId,brandId);
//            cmdCache.addIrDeviceTypeBySerialId(serialId,deviceType);
			Integer idx = IndexUtils.getIdx();
			TYaokonyunKeyCode yaokonyunKeyCode = new TYaokonyunKeyCode();
			yaokonyunKeyCode.setKeyName("");
			yaokonyunKeyCode.setCustomName("");
			yaokonyunKeyCode.setIndex(idx);
			yaokonyunKeyCode.setLastOpTime(new Date());
			yaokonyunKeyCode.setBrandId(Integer.valueOf(brandId));
			yaokonyunKeyCode.setRmodel("");
			yaokonyunKeyCode.settId(Integer.valueOf(deviceType));
			yaokonyunKeyCode.setName(name);
			yaokonyunKeyCode.setVersion(0);
			yaokonyunKeyCode.setSrc("");
			yaokonyunKeyCode.setSerialId(serialId);
			yaokonyunKeyCode.setKey("");
			yaoKongYunService.addTYaokonyunKeyCode(yaokonyunKeyCode);
            QueryRemoteBySrcDTO dto = new QueryRemoteBySrcDTO();
            dto.setName(name);
            dto.setKeys(new com.alibaba.fastjson.JSONArray());
            dto.setExtendsKeys(new com.alibaba.fastjson.JSONArray());
            dto.setIndex(idx);
            dto.setBrandType(Integer.valueOf(brandId));
            dto.setType(Integer.valueOf(deviceType));
            dto.setRid("");
            dto.setRmodel("");
            dto.setVersion(0);
			resMap.put("remote",dto);

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
			TYaokonyunRemoteControl yaokonyunRemoteControl = yaoKongYunService.getYaokonyunRemoteControlByRemoteId(remoteId);
			String remoteControlSrc = null;
			if(yaokonyunRemoteControl!=null){
				remoteControlSrc = yaokonyunRemoteControl.getSrc();
			}
			List<QueryRemoteBySrcDTO2> list = cmdCache.getIRDeviceInfoList(brandId+"_"+deviceType+"_"+"_remoteControlListSrc");

			for(QueryRemoteBySrcDTO2 dto : list){
				version = dto.getVersion();
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

			String keyNameType = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("keyNameType_"+idx.toString());
			keyName = cmdCache.getIrTestCodeAppKeyBrandIdDeviceType("keyName_"+idx.toString());

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
					Map<String, LinkedTreeMap> keyCodeMap = mapList.get(0);
					Iterator<String> iterator = keyCodeMap.keySet().iterator();
					while (iterator.hasNext()){
						com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
						String key = iterator.next();
						jsonObject.put("key",key);
						jsonArray.add(jsonObject);
						LinkedTreeMap treeMap = keyCodeMap.get(key);
						String src = (String) treeMap.get("src");
						TYaokonyunKeyCode yaokonyunKeyCode = new TYaokonyunKeyCode();
						if(!keyName.equals("")&&remoteControlSrc !=null && src.equals(remoteControlSrc)){
							if(keyNameType.equals("0")){//标准按键
								yaokonyunKeyCode.setKeyName(keyName);
							}else{//拓展按键
								yaokonyunKeyCode.setCustomName(keyName);
							}
						}
						yaokonyunKeyCode.setIndex(idx);
						yaokonyunKeyCode.setLastOpTime(new Date());
						yaokonyunKeyCode.setBrandId(Integer.valueOf(brandId));
						yaokonyunKeyCode.setRmodel(rmodel);
						yaokonyunKeyCode.settId(Integer.valueOf(deviceType));
						yaokonyunKeyCode.setName(name);
						yaokonyunKeyCode.setVersion(version);
//						yaokonyunKeyCode.setRemoteId(yaokonyunRemoteControl.getId());
						yaokonyunKeyCode.setSrc(src);
						yaokonyunKeyCode.setSerialId(serialId);
						yaokonyunKeyCode.setKey(key);
						yaoKongYunService.addTYaokonyunKeyCode(yaokonyunKeyCode);
					}
				}
			}
			resMap.put("index",idx);
			resMap.put("name",name);
			resMap.put("brandType",brandId);
			resMap.put("keys",jsonArray);
			resMap.put("extendsKeys",new JSONArray());
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

	private TYaokonyunDevice initYaoKongDevice() throws Exception {
		String ir_appId = aliDevCache.getValue("ir_appId");
		TYaokonyunDevice yaokonyunDevice = null;
		// if(!StringUtils.isEmpty(deviceId)){//已经用完50次,更新状态
		// yaoKongYunService.updateYaoKongDevice(deviceId);
		// }

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
