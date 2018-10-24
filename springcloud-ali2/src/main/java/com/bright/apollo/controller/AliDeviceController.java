package com.bright.apollo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TYaoKongYunBrand;
import com.bright.apollo.common.entity.TYaokonyunDevice;
import com.bright.apollo.enums.ALIDevTypeEnum;
import com.bright.apollo.enums.AliIotDevTypeEnum;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.mqtt.MqttGateWay;
import com.bright.apollo.response.AliDevInfo;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.AliDeviceService;
import com.bright.apollo.service.CMDMessageService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.service.YaoKongYunService;
import com.bright.apollo.service.AliRequest.AliService;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.tool.MD5;
import com.bright.apollo.util.SpringContextUtil;
import com.bright.apollo.vo.IotDevConncetion;

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
	ResponseObject<List<Map<String, String>>> sendLearn2IR(@RequestParam(required = true, value = "serialId") String serialId,
														   @RequestParam(required = true, value = "index") Integer index,
														   @RequestParam(required = true, value = "key") String key) {
		ResponseObject<List<Map<String, String>>> res = new ResponseObject<List<Map<String, String>>>();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		try {
			topServer.pubIRTopic(null, null, (String) requestMap.get("deviceId"), requestMap);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// 进入学习
	@RequestMapping(value = "/toLearn", method = RequestMethod.POST)
	ResponseObject<List<Map<String, String>>> learnCode(@RequestParam(required = true, value = "serialId") String serialId,
														@RequestParam(required = true, value = "timeOut") String timeOut,
														@RequestParam(required = true, value = "index") Integer index,
														@RequestParam(required = true, value = "keyOrName") String keyOrName,
														@RequestParam(required = true, value = "learnKeyType") String learnKeyType) {
		ResponseObject<List<Map<String, String>>> res = new ResponseObject<List<Map<String, String>>>();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		try {
			requestMap.put("serialId",serialId);
			String deviceId = (String) requestMap.get("deviceId");// 设备ID
			aliDevCache.setKey("ir_" + deviceId, serialId, 30000);
			topServer.pubIRTopic(null, null, deviceId, requestMap);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// 修改/新增红外方案
	@RequestMapping(value = "/modifyIR", method = RequestMethod.POST)
	ResponseObject<List<Map<String, String>>> modifyIR(@RequestParam(required = true, value = "serialId") String serialId,
													   @RequestParam(required = true, value = "irProgram") String irProgram) {
		ResponseObject<List<Map<String, String>>> res = new ResponseObject<List<Map<String, String>>>();
		try {

			// todo 更新套数
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	// 获取遥控器列表
	@RequestMapping(value = "/getIrList", method = RequestMethod.POST)
	ResponseObject<List<Map<String, String>>> getIrList(@RequestParam(required = true, value = "brandId") String brandId,
														@RequestParam(required = true, value = "deviceType") String deviceType) {
		ResponseObject<List<Map<String, String>>> res = new ResponseObject<List<Map<String, String>>>();
//		Map<String, Object> requestMap = (Map<String, Object>) object;
		try {
//			String brandId = (String) requestMap.get("brandId");// 品牌ID
//			String deviceType = (String) requestMap.get("deviceType");// 设备类型
			List<TYaoKongYunBrand> brandList = yaoKongYunService.getYaoKongYunByTIdAndDeviceType(brandId, deviceType);

			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
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
			createYaoKongYunDevice();
		}
		aliDevCache.setKey("createYaoKonYun",
				yaokonyunDevice.getAppId() + ":" + yaokonyunDevice.getDeviceId() + ":" + yaokonyunDevice.getUseTime(),
				60 * 60 * 24 * 7);
		return yaokonyunDevice;
	}

	private TYaokonyunDevice createYaoKongYunDevice() throws Exception {
		TYaokonyunDevice device = new TYaokonyunDevice();
		device.setDeviceId(MD5.getMD5Str(new Date().getTime() + ""));
		yaoKongYunService.addYaoKongDevice(device);
		return yaoKongYunService.getYaoKongYunDevice();
	}

}
