package com.bright.apollo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONArray;
import com.bright.apollo.cache.AliDevCache;
import com.bright.apollo.common.entity.*;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.request.AliDeviceDTO;
import com.bright.apollo.service.*;
import com.bright.apollo.service.AliRequest.BaseRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.NodeTypeEnum;
import com.bright.apollo.request.SceneActionDTO;
import com.bright.apollo.request.SceneConditionDTO;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.session.SceneActionThreadPool;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.util.FingerUtil;
import com.zz.common.util.StringUtils;

@RestController
@RequestMapping("aliService")
public class AliServerController {
	private static final Logger logger = LoggerFactory.getLogger(AliServerController.class);
	@Autowired
	private TopicServer topicServer;
	@Autowired
	private OboxDeviceConfigService oboxDeviceConfigService;
	@Autowired
	private SceneActionThreadPool sceneActionThreadPool;
	@Autowired
	private CmdCache cmdCache;
	@Autowired
	private FingerUtil fingerUtil;
	// for search new device
	private static String timeout = "30";

	@Autowired
	private AliDeviceConfigService aliDeviceConfigService;

	@Autowired
	private AliDeviceService aliDeviceService;

	@Autowired
	private AliDevCache aliDevCache;

	@Autowired
	private BaseRequest baseRequest;

//	@Autowired
//	private QuartzService quartzService;

	@RequestMapping(value = "/toAli", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<OboxResp> toAliService(@PathVariable(value = "cmd") CMDEnum cmd,
			@PathVariable(value = "inMsg") String inMsg, @PathVariable(value = "deviceSerial") String deviceSerial) {
		byte[] inMsgByte = inMsg.getBytes();
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			topicServer.request(cmd, inMsgByte, deviceSerial);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/release/{serialId}", method = RequestMethod.GET)
	public ResponseObject<OboxResp> releaseObox(@PathVariable(required = true, value = "serialId") String serialId) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			// from old code
			byte[] bodyBytes = new byte[8];
			byte[] oboxSerialIdBytes = ByteHelper.hexStringToBytes(serialId);
			System.arraycopy(oboxSerialIdBytes, 0, bodyBytes, 0, oboxSerialIdBytes.length);
			bodyBytes[5] = (byte) 0xff;
			bodyBytes[6] = (byte) 0xff;
			bodyBytes[7] = 0x03;
			topicServer.request(CMDEnum.release_all_devices, bodyBytes, serialId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/stopScan/{oboxSerialId}", method = RequestMethod.DELETE)
	ResponseObject<OboxResp> stopScan(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] sendbodyBytes = new byte[15];
			sendbodyBytes[0] = (byte) Integer.parseInt("00", 16);
			topicServer.request(CMDEnum.search_new_device, sendbodyBytes, oboxSerialId);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param oboxSerialId
	 * @param deviceType
	 * @param deviceChildType
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scanByRestart/{oboxSerialId}", method = RequestMethod.POST)
	@ResponseBody
	ResponseObject<OboxResp> scanByRestart(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId,
			@RequestParam(required = false, value = "deviceType") String deviceType,
			@RequestParam(required = false, value = "deviceChildType") String deviceChildType,
			@RequestParam(required = false, value = "serialId") String serialId) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] sendbodyBytes = new byte[15];
			sendbodyBytes[0] = (byte) Integer.parseInt("01", 16);
			if (!StringUtils.isEmpty(deviceType)) {
				sendbodyBytes[13] = (byte) Integer.parseInt(deviceType, 16);
				if (!StringUtils.isEmpty(deviceChildType))
					sendbodyBytes[14] = (byte) Integer.parseInt(deviceChildType, 16);
			}
			if (!StringUtils.isEmpty(serialId)) {
				byte[] oboxSerialIdBytes = ByteHelper.hexStringToBytes(serialId);
				System.arraycopy(oboxSerialIdBytes, 0, sendbodyBytes, 6, oboxSerialIdBytes.length);
			}
			topicServer.request(CMDEnum.search_new_device, sendbodyBytes, oboxSerialId);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param oboxSerialId
	 * @param deviceType
	 * @param deviceChildType
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scanByUnRestart/{oboxSerialId}", method = RequestMethod.POST)
	@ResponseBody
	ResponseObject<OboxResp> scanByUnStop(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId,
			@RequestParam(required = false, value = "deviceType") String deviceType,
			@RequestParam(required = false, value = "deviceChildType") String deviceChildType,
			@RequestParam(required = false, value = "serialId") String serialId,
			@RequestParam(required = true, value = "countOfDevice") Integer countOfDevice) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] sendbodyBytes = new byte[15];
			sendbodyBytes[0] = (byte) Integer.parseInt("02", 16);
			sendbodyBytes[1] = (byte) (countOfDevice.intValue() & 0x0000ff);
			sendbodyBytes[2] = (byte) ((countOfDevice.intValue() >> 8) & 0x0000ff);
			sendbodyBytes[3] = (byte) ((countOfDevice.intValue() >> 16) & 0x0000ff);
			sendbodyBytes[4] = (byte) Integer.parseInt(timeout, 16);
			if (!StringUtils.isEmpty(deviceType)) {
				sendbodyBytes[13] = (byte) Integer.parseInt(deviceType, 16);
				if (!StringUtils.isEmpty(deviceChildType))
					sendbodyBytes[14] = (byte) Integer.parseInt(deviceChildType, 16);
			}
			if (!StringUtils.isEmpty(serialId)) {
				byte[] oboxSerialIdBytes = ByteHelper.hexStringToBytes(serialId);
				System.arraycopy(oboxSerialIdBytes, 0, sendbodyBytes, 6, oboxSerialIdBytes.length);
			}
			topicServer.request(CMDEnum.search_new_device, sendbodyBytes, oboxSerialId);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param oboxSerialId
	 * @param deviceType
	 * @param deviceChildType
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/scanByInitiative/{oboxSerialId}", method = RequestMethod.POST)
	@ResponseBody
	ResponseObject<OboxResp> scanByInitiative(
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId,
			@RequestParam(required = false, value = "deviceType") String deviceType,
			@RequestParam(required = false, value = "deviceChildType") String deviceChildType,
			@RequestParam(required = false, value = "serialId") String serialId,
			@RequestParam(required = true, value = "countOfDevice") Integer countOfDevice) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] sendbodyBytes = new byte[15];
			sendbodyBytes[0] = (byte) Integer.parseInt("03", 16);
			sendbodyBytes[1] = (byte) (countOfDevice.intValue() & 0x0000ff);
			sendbodyBytes[2] = (byte) ((countOfDevice.intValue() >> 8) & 0x0000ff);
			sendbodyBytes[3] = (byte) ((countOfDevice.intValue() >> 16) & 0x0000ff);
			sendbodyBytes[4] = (byte) Integer.parseInt(timeout, 16);
			if (!StringUtils.isEmpty(deviceType)) {
				sendbodyBytes[13] = (byte) Integer.parseInt(deviceType, 16);
				if (!StringUtils.isEmpty(deviceChildType))
					sendbodyBytes[14] = (byte) Integer.parseInt(deviceChildType, 16);
			}
			if (!StringUtils.isEmpty(serialId)) {
				byte[] oboxSerialIdBytes = ByteHelper.hexStringToBytes(serialId);
				System.arraycopy(oboxSerialIdBytes, 0, sendbodyBytes, 6, oboxSerialIdBytes.length);
			}
			topicServer.request(CMDEnum.search_new_device, sendbodyBytes, oboxSerialId);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/controlServerScene/{sceneNumber}", method = RequestMethod.PUT)
	ResponseObject controlServerScene(@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			// check the param by the invoker
			sceneActionThreadPool.addSceneAction(sceneNumber);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param oboxSerialId
	 * @param status
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/setDeviceStatus/{oboxSerialId}", method = RequestMethod.PUT)
	ResponseObject<OboxResp> setDeviceStatus(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId,
			@RequestParam(required = true, value = "status") String status,
			@RequestParam(required = true, value = "rfAddr") String rfAddr) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] stateBytes = ByteHelper.hexStringToBytes(status);
			byte[] bodyBytes = new byte[7 + stateBytes.length];
			byte[] oboxSerialIdBytes = ByteHelper.hexStringToBytes(oboxSerialId);
			System.arraycopy(oboxSerialIdBytes, 0, bodyBytes, 0, oboxSerialIdBytes.length);
			bodyBytes[5] = 0x00;
			bodyBytes[6] = (byte) Integer.parseInt(rfAddr, 16);
			System.arraycopy(stateBytes, 0, bodyBytes, 7, stateBytes.length);
			topicServer.pubTopic(CMDEnum.setting_node_status, bodyBytes, oboxSerialId);
			// topicServer.request(CMDEnum.setting_node_status, bodyBytes,
			// oboxSerialId);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param sceneName
	 * @param sceneGroup
	 * @Description:
	 */
	@RequestMapping(value = "/addLocalScene", method = RequestMethod.POST)
	ResponseObject<OboxResp> addLocalScene(@RequestParam(required = true, value = "sceneName") String sceneName,
			@RequestParam(required = true, value = "oboxSerialId") String oboxSerialId,
			@RequestParam(required = false, value = "sceneGroup") String sceneGroup) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] bodyBytes = new byte[21];
			bodyBytes[0] = 0x01;
			bodyBytes[1] = 0x11;
			bodyBytes[2] = 0x00;
			String newName = ByteHelper.bytesToHexString(sceneName.getBytes("UTF-8"));
			byte[] namebytes = ByteHelper.hexStringToBytes(newName);
			System.arraycopy(namebytes, 0, bodyBytes, 3, namebytes.length);

			if (!StringUtils.isEmpty(sceneGroup)) {
				byte[] groupbytes = ByteHelper.hexStringToBytes(sceneGroup);
				System.arraycopy(groupbytes, 0, bodyBytes, 19, groupbytes.length);
			}
			Future<OboxResp> future = topicServer.request(CMDEnum.setting_sc_info, bodyBytes, oboxSerialId);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
			res.setData(future.get());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/addLocalSceneCondition/{sceneNumber}/{oboxSerialId}", method = RequestMethod.POST)
	ResponseObject<OboxResp> addLocalSceneCondition(
			@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber,
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId,
			@RequestBody(required = true) List<List<SceneConditionDTO>> sceneConditionDTOs) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			for (int i = 0; i < sceneConditionDTOs.size(); i++) {
				byte[] comBytes = new byte[50];
				comBytes[0] = 0x02;
				comBytes[2] = (byte) (int) sceneNumber;
				comBytes[3] = (byte) (i + 1);
				int condType = 0;
				int choice = 0;
				List<SceneConditionDTO> list = sceneConditionDTOs.get(i);
				for (int j = 0; j < list.size(); j++) {
					choice |= (0x01 << j);
					SceneConditionDTO sceneConditionDTO = list.get(j);
					byte[] condition = ByteHelper.hexStringToBytes(sceneConditionDTO.getCondition());
					System.arraycopy(condition, 0, comBytes, 12 + j * 15, condition.length);
					if (sceneConditionDTO.getDeviceSerialId() != null) {
						// node condition
						String serialID = sceneConditionDTO.getDeviceSerialId();
						// TOboxDeviceConfig tOboxDeviceConfig =
						// queryDeviceByWeight(tUser, serialID);
						TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService
								.getTOboxDeviceConfigByDeviceSerialId(serialID);
						if (tOboxDeviceConfig != null) {
							condType |= (0x02 << 2 * j);
							byte[] serial = ByteHelper.hexStringToBytes(tOboxDeviceConfig.getOboxSerialId());
							System.arraycopy(serial, 0, comBytes, 5 + j * 15, serial.length);
							comBytes[10 + j * 15] = 0x00;
							comBytes[11 + j * 15] = (byte) Integer.parseInt(tOboxDeviceConfig.getDeviceRfAddr(), 16);
						}
					} else {
						// time condition
						condType |= (0x01 << 2 * j);
					}
				}
				comBytes[1] = (byte) choice;
				comBytes[4] = (byte) condType;
				// topicServer.pubTopic(CMDEnum.setting_sc_info, comBytes,
				// oboxSerialId);
				topicServer.request(CMDEnum.setting_sc_info, comBytes, oboxSerialId);
			}
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param nodeActionDTOs
	 * @param sceneNumber
	 * @param oboxSerialId
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addLocalSceneAction/{sceneNumber}/{oboxSerialId}", method = RequestMethod.POST)
	ResponseObject addLocalSceneAction(@RequestBody(required = true) List<SceneActionDTO> nodeActionDTOs,
			@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber,
			@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId) {
		ResponseObject res = new ResponseObject();
		try {
			new Thread(new sceneAction(nodeActionDTOs, sceneNumber, oboxSerialId, oboxDeviceConfigService, topicServer,
					cmdCache)).start();
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param oboxSerialId
	 * @param name
	 * @Description:
	 */
	@RequestMapping(value = "/modifyDeviceName/{oboxSerialId}/{name}/{address}", method = RequestMethod.PUT)
	ResponseObject<OboxResp> modifyDeviceName(@PathVariable(value = "oboxSerialId") String oboxSerialId,
			@PathVariable(value = "name") String name, @PathVariable(value = "address") String address) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] bodyBytes = new byte[24];
			byte[] oboxSerialIdBytes = ByteHelper.hexStringToBytes(oboxSerialId);
			System.arraycopy(oboxSerialIdBytes, 0, bodyBytes, 1, oboxSerialIdBytes.length);
			bodyBytes[6] = 0x00;
			bodyBytes[7] = (byte) Integer.parseInt(address, 16);
			bodyBytes[0] = 0x02;
			String newName = ByteHelper.bytesToHexString(name.getBytes("UTF-8"));
			byte[] namebytes = ByteHelper.hexStringToBytes(newName);
			System.arraycopy(namebytes, 0, bodyBytes, 8, namebytes.length);
			topicServer.request(CMDEnum.modify_device, bodyBytes, oboxSerialId);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	/**
	 * @param oboxSerialId
	 * @param deviceRfAddr
	 * @Description:
	 */
	@RequestMapping(value = "/deleteDevice/{oboxSerialId}/{address}/{deviceName}", method = RequestMethod.PUT)
	ResponseObject<OboxResp> deleteDevice(@PathVariable(value = "oboxSerialId") String oboxSerialId,
			@PathVariable(value = "address") String address, @PathVariable(value = "deviceName") String deviceName) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] bodyBytes = new byte[24];
			byte[] oboxSerialIdBytes = ByteHelper.hexStringToBytes(oboxSerialId);
			System.arraycopy(oboxSerialIdBytes, 0, bodyBytes, 1, oboxSerialIdBytes.length);
			bodyBytes[6] = 0x00;
			bodyBytes[7] = (byte) Integer.parseInt(address, 16);
			bodyBytes[0] = 0x04;
			byte[] namebytes = ByteHelper.stringToAsciiBytes(deviceName, deviceName.length());
			System.arraycopy(namebytes, 0, bodyBytes, 8, namebytes.length);
			topicServer.request(CMDEnum.modify_device, bodyBytes, oboxSerialId);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param operation
	 * @param data
	 * @param deviceConfig
	 * @param startTime
	 * @param endTime
	 * @param times
	 * @param userSerialId
	 * @param randomNum
	 * @Description:
	 */
	@RequestMapping(value = "/sendMessageToFinger/{operation}/{startTime}/{endTime}/{times}/{userSerialId}/{randomNum}/{serialId}/{address}", method = RequestMethod.POST)
	ResponseObject<OboxResp> sendMessageToFinger(@PathVariable(value = "operation") String operation,
			@PathVariable(value = "startTime") String startTime, @PathVariable(value = "endTime") String endTime,
			@PathVariable(value = "times") String times, @PathVariable(value = "userSerialId") Integer userSerialId,
			@PathVariable(value = "randomNum") String randomNum, @PathVariable(value = "serialId") String serialId,
			@PathVariable(value = "address") String address) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] buildBytes = fingerUtil.buildBytes(operation, serialId, address, startTime, endTime, times,
					userSerialId, randomNum);
			topicServer.request(CMDEnum.set_finger_remote_user, buildBytes, serialId);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getRealNodeStatus", method = RequestMethod.PUT)
	ResponseObject<OboxResp> getRealNodeStatus(@RequestBody TOboxDeviceConfig deviceConfig) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] sendbodyBytes = new byte[7];
			byte[] oboxSerialByte = ByteHelper.hexStringToBytes(deviceConfig.getOboxSerialId());
			sendbodyBytes[6] = (byte) Integer.parseInt(deviceConfig.getDeviceRfAddr(), 16);
			System.arraycopy(oboxSerialByte, 0, sendbodyBytes, 0, oboxSerialByte.length);
			Future<OboxResp> request = topicServer.request(CMDEnum.query_node_real_status, sendbodyBytes,
					deviceConfig.getOboxSerialId());
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
			res.setData(request.get());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param serialId
	 * @Description:
	 */
	@RequestMapping(value = "/deleteObox/{serialId}", method = RequestMethod.DELETE)
	ResponseObject<OboxResp> deleteObox(@PathVariable(value = "serialId") String serialId) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] bodyBytes = new byte[8];
			byte[] oboxSerialIdBytes = ByteHelper.hexStringToBytes(serialId);
			System.arraycopy(oboxSerialIdBytes, 0, bodyBytes, 0, oboxSerialIdBytes.length);
			bodyBytes[5] = 0x00;
			bodyBytes[6] = 0x00;
			bodyBytes[7] = 0x02;
			Future<OboxResp> request = topicServer.request(CMDEnum.release_all_devices, bodyBytes, serialId);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;

	}
	/**  
	 * @param serialId  
	 * @Description:  
	 */
	@RequestMapping(value = "/addRemoteLed/{serialId}", method = RequestMethod.POST)
	ResponseObject<OboxResp> addRemoteLed(@PathVariable(value = "serialId")String serialId){
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] bodyBytes = new byte[12];
			byte[] oboxSerialIdBytes = ByteHelper.hexStringToBytes(serialId);
			System.arraycopy(oboxSerialIdBytes, 0, bodyBytes, 0, oboxSerialIdBytes.length);
			bodyBytes[5] = 0x00;
			bodyBytes[6] = (byte) 0xfe;
			bodyBytes[7] = (byte) 0x01;
			bodyBytes[8] = (byte) 0x01;
 			topicServer.request(CMDEnum.setting_remote_led, bodyBytes, serialId);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	}
	/**  
	 * @param oboxSerialId  
	 * @Description:  
	 */
	@RequestMapping(value = "/delRemoteLed/{serialId}", method = RequestMethod.DELETE)
	ResponseObject<OboxResp> delRemoteLed(@PathVariable(value = "serialId")String serialId){
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] bodyBytes = new byte[12];
			byte[] oboxSerialIdBytes = ByteHelper.hexStringToBytes(serialId);
			System.arraycopy(oboxSerialIdBytes, 0, bodyBytes, 0, oboxSerialIdBytes.length);
			bodyBytes[5] = 0x00;
			bodyBytes[6] = (byte) 0xfe;
			bodyBytes[7] = (byte) 0x01;
			bodyBytes[8] = (byte) 0x02;
 			topicServer.request(CMDEnum.setting_remote_led, bodyBytes, serialId);
			res.setStatus(ResponseEnum.DeleteSuccess.getStatus());
			res.setMessage(ResponseEnum.DeleteSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;	
	}
	/**  
	 * @param oboxSerialId
	 * @param status  
	 * @Description:  
	 */
	@RequestMapping(value = "/controlRemoteLed/{serialId}/{status}", method = RequestMethod.PUT)
	ResponseObject<OboxResp> controlRemoteLed(@PathVariable(value = "serialId")String serialId, 
			@PathVariable(value = "status")String status){
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] stateBytes = ByteHelper.hexStringToBytes(status);
			byte[] bodyBytes = new byte[7 + stateBytes.length];
			byte[] oboxSerialIdBytes = ByteHelper.hexStringToBytes(serialId);
			System.arraycopy(oboxSerialIdBytes, 0, bodyBytes, 0, oboxSerialIdBytes.length);
			bodyBytes[5] = 0x00;
			bodyBytes[6] = (byte)0xfe;
			System.arraycopy(stateBytes, 0, bodyBytes, 7, stateBytes.length);
			topicServer.pubTopic(CMDEnum.setting_remote_led, bodyBytes, serialId);
  			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
	static class sceneAction implements Runnable {

		private List<SceneActionDTO> lists;

		private int sceneNumber;

		private String oboxSerialId;

		private TopicServer topicServer;

		private OboxDeviceConfigService oboxDeviceConfigService;

		private CmdCache cmdCache;

		public sceneAction(List<SceneActionDTO> lists, int sceneNumber, String oboxSerialId,
				OboxDeviceConfigService oboxDeviceConfigService, TopicServer topicServer, CmdCache cmdCache) {
			// TODO Auto-generated constructor stub
			this.lists = lists;
			this.sceneNumber = sceneNumber;
			this.oboxSerialId = oboxSerialId;
			this.oboxDeviceConfigService = oboxDeviceConfigService;
			this.topicServer = topicServer;
			this.cmdCache = cmdCache;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < lists.size(); i += 3) {
					byte[] comBytes = new byte[48];
					comBytes[0] = 0x03;
					comBytes[1] = 0x00;
					comBytes[2] = (byte) (int) sceneNumber;
					SceneActionDTO sceneActionDTO = lists.get(i);
					if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.single.getValue())) {
						TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService
								.queryDeviceConfigBySerialID(sceneActionDTO.getDeviceSerialId());
						if (tOboxDeviceConfig != null) {
							if (sceneActionDTO.getActionType() == 1) {
								// add
								comBytes[1] |= 0x01;
							} else if (sceneActionDTO.getActionType() == 2) {
								// modify
								comBytes[1] |= 0x02;
							}

							byte[] action = ByteHelper.hexStringToBytes(sceneActionDTO.getAction());
							byte[] serial = ByteHelper.hexStringToBytes(tOboxDeviceConfig.getOboxSerialId());
							System.arraycopy(serial, 0, comBytes, 3, serial.length);
							comBytes[8] = 0x00;
							comBytes[9] = (byte) Integer.parseInt(tOboxDeviceConfig.getDeviceRfAddr(), 16);
							System.arraycopy(action, 0, comBytes, 10, action.length);
						} else {
							comBytes[1] |= 0x03;
						}
					} else if (sceneActionDTO.getNodeType().equals(NodeTypeEnum.group.getValue())) {
						/*
						 * TServerGroup tServerGroup = DeviceBusiness
						 * .querySererGroupById(Integer
						 * .parseInt(sceneActionDTO.getGroupId())); if
						 * (tServerGroup != null) { if
						 * (tServerGroup.getGroupStyle().equals("00")) { //
						 * local group TServerOboxGroup tServerOboxGroup =
						 * DeviceBusiness .queryOBOXGroupByServerID(tServerGroup
						 * .getId()); if (sceneActionDTO.getActionType() == 1) {
						 * // add comBytes[1] |= 0x01; } else if
						 * (sceneActionDTO.getActionType() == 2) { // modify
						 * comBytes[1] |= 0x02; } byte[] action = ByteHelper
						 * .hexStringToBytes(sceneActionDTO .getAction());
						 * byte[] serial = ByteHelper
						 * .hexStringToBytes(tServerOboxGroup
						 * .getOboxSerialId()); System.arraycopy(serial, 0,
						 * comBytes, 3, serial.length); byte[] addrBytes =
						 * ByteHelper .hexStringToBytes(tServerOboxGroup
						 * .getGroupAddr()); System.arraycopy(addrBytes, 0,
						 * comBytes, 8, addrBytes.length); comBytes[9] = (byte)
						 * 0xff; System.arraycopy(action, 0, comBytes, 10,
						 * action.length); } } else { comBytes[1] |= 0x03; }
						 */}

					if (i + 1 < lists.size()) {
						SceneActionDTO sceneActionDTO1 = lists.get(i + 1);
						if (sceneActionDTO1.getNodeType().equals(NodeTypeEnum.single.getValue())) {
							TOboxDeviceConfig tOboxDeviceConfig1 = oboxDeviceConfigService
									.queryDeviceConfigBySerialID(sceneActionDTO1.getDeviceSerialId());
							if (tOboxDeviceConfig1 != null) {
								if (sceneActionDTO1.getActionType() == 1) {
									// add
									comBytes[1] |= 0x04;
								} else if (sceneActionDTO1.getActionType() == 2) {
									// modify
									comBytes[1] |= 0x08;
								}
								byte[] action = ByteHelper.hexStringToBytes(sceneActionDTO1.getAction());
								byte[] serial = ByteHelper.hexStringToBytes(tOboxDeviceConfig1.getOboxSerialId());
								System.arraycopy(serial, 0, comBytes, 18, serial.length);
								comBytes[23] = 0x00;
								comBytes[24] = (byte) Integer.parseInt(tOboxDeviceConfig1.getDeviceRfAddr(), 16);
								System.arraycopy(action, 0, comBytes, 25, action.length);
							} else {
								comBytes[1] |= 0x0c;
							}
						} else if (sceneActionDTO1.getNodeType().equals(NodeTypeEnum.group.getValue())) {
							/*
							 * TServerGroup tServerGroup = DeviceBusiness
							 * .querySererGroupById(Integer
							 * .parseInt(sceneActionDTO1 .getGroupId())); if
							 * (tServerGroup != null) { if
							 * (tServerGroup.getGroupStyle().equals("00")) { //
							 * local group TServerOboxGroup tServerOboxGroup =
							 * DeviceBusiness
							 * .queryOBOXGroupByServerID(tServerGroup .getId());
							 * if (tServerGroup != null) { if
							 * (sceneActionDTO1.getActionType() == 1) { // add
							 * comBytes[1] |= 0x04; } else if (sceneActionDTO1
							 * .getActionType() == 2) { // modify comBytes[1] |=
							 * 0x08; } byte[] action = ByteHelper
							 * .hexStringToBytes(sceneActionDTO1 .getAction());
							 * byte[] serial = ByteHelper
							 * .hexStringToBytes(tServerOboxGroup
							 * .getOboxSerialId()); System.arraycopy(serial, 0,
							 * comBytes, 18, serial.length); byte[] addrBytes =
							 * ByteHelper .hexStringToBytes(tServerOboxGroup
							 * .getGroupAddr()); System.arraycopy(addrBytes, 0,
							 * comBytes, 23, addrBytes.length); comBytes[24] =
							 * (byte) 0xff; System.arraycopy(action, 0,
							 * comBytes, 25, action.length); } } } else {
							 * comBytes[1] |= 0x0c; }
							 * 
							 */}

						if (i + 2 < lists.size()) {
							SceneActionDTO sceneActionDTO2 = lists.get(i + 2);
							if (sceneActionDTO2.getNodeType().equals(NodeTypeEnum.single.getValue())) {
								TOboxDeviceConfig tOboxDeviceConfig2 = oboxDeviceConfigService
										.queryDeviceConfigBySerialID(sceneActionDTO2.getDeviceSerialId());
								if (tOboxDeviceConfig2 != null) {
									if (sceneActionDTO2.getActionType() == 1) {
										// add
										comBytes[1] |= 0x10;
									} else if (sceneActionDTO2.getActionType() == 2) {
										// modify
										comBytes[1] |= 0x20;
									}

									byte[] action = ByteHelper.hexStringToBytes(sceneActionDTO2.getAction());
									byte[] serial = ByteHelper.hexStringToBytes(tOboxDeviceConfig2.getOboxSerialId());
									System.arraycopy(serial, 0, comBytes, 33, serial.length);
									comBytes[38] = 0x00;
									comBytes[39] = (byte) Integer.parseInt(tOboxDeviceConfig2.getDeviceRfAddr(), 16);
									System.arraycopy(action, 0, comBytes, 40, action.length);
								} else {
									comBytes[1] |= 0x30;
								}
							} else if (sceneActionDTO2.getNodeType().equals(NodeTypeEnum.group.getValue())) {
								/*
								 * TServerGroup tServerGroup = DeviceBusiness
								 * .querySererGroupById(Integer
								 * .parseInt(sceneActionDTO2 .getGroupId())); if
								 * (tServerGroup != null) { if
								 * (tServerGroup.getGroupStyle().equals( "00"))
								 * { // local group TServerOboxGroup
								 * tServerOboxGroup = DeviceBusiness
								 * .queryOBOXGroupByServerID(tServerGroup
								 * .getId()); if (tServerGroup != null) { if
								 * (sceneActionDTO2.getActionType() == 1) { //
								 * add comBytes[1] |= 0x10; } else if
								 * (sceneActionDTO2 .getActionType() == 2) { //
								 * modify comBytes[1] |= 0x20; } byte[] action =
								 * ByteHelper .hexStringToBytes(sceneActionDTO2
								 * .getAction()); byte[] serial = ByteHelper
								 * .hexStringToBytes(tServerOboxGroup
								 * .getOboxSerialId()); System.arraycopy(serial,
								 * 0, comBytes, 33, serial.length); byte[]
								 * addrBytes = ByteHelper
								 * .hexStringToBytes(tServerOboxGroup
								 * .getGroupAddr()); System.arraycopy(addrBytes,
								 * 0, comBytes, 38, addrBytes.length);
								 * comBytes[39] = (byte) 0xff;
								 * System.arraycopy(action, 0, comBytes, 40,
								 * action.length); } } } else { comBytes[1] |=
								 * 0x30; }
								 */}

						}
					}

					// TopicService topicService = TopicService.getInstance();
					// topicServer.pubTopic(CMDEnum.setting_sc_info, comBytes,
					// oboxSerialId);
					topicServer.request(CMDEnum.setting_sc_info, comBytes, oboxSerialId);
					TimeUnit.MILLISECONDS.sleep(500);
					cmdCache.deleteWrite(oboxSerialId);
					// CMDMessageService.send(tObox, CMDEnum.setting_sc_info,
					// comBytes);
				}
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("===error msg:" + e.getMessage());
			}
		}

	}

	@RequestMapping(value = "/queryAliDevice/{userId}", method = RequestMethod.GET)
	public ResponseObject queryAliDevice(@PathVariable(required = true, value = "userId") Integer userId) {
		ResponseObject res = new ResponseObject();
		try {
			// from old code
			List<AliDeviceDTO> sendlist = new ArrayList<AliDeviceDTO>();
			List<TAliDeviceConfig> aliDeviceConfigs = aliDeviceConfigService.getAliDeviceConfigByUserId(userId);
			for(TAliDeviceConfig aliDeviceConfig : aliDeviceConfigs){
				AliDeviceDTO deviceDTO = new AliDeviceDTO(aliDeviceConfig);
				TAliDevice aliDevice = aliDeviceService.getAliDeviceBySerializeId(aliDeviceConfig.getDeviceSerialId());
				if(aliDevice!=null){
					deviceDTO.setOnline(aliDevice.getOffline());
					sendlist.add(deviceDTO);
				}
			}
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
			res.setData(sendlist);
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/setAliDevice", method = RequestMethod.POST)
	public ResponseObject setAliDevice(@RequestParam(required = true, value = "value") Object value,@RequestParam(required = true, value = "deviceId") String deviceId) {
		ResponseObject res = new ResponseObject();
		try {
			String val = (String)value;
			JSONArray array = JSONArray.parseArray(val);
			for (int i = 0; i < array.size(); i++) {
				com.alibaba.fastjson.JSONObject jsonObject = array.getJSONObject(i);
				if (jsonObject.getBoolean("data")) {
					jsonObject.put("data", true);
				}else {
					jsonObject.put("data", false);
				}
			}
			net.sf.json.JSONObject object = new net.sf.json.JSONObject();
			object.put("command", "set");
			object.element("value",array);
//			object.put("value", value);
			JSONObject resJson = topicServer.requestDev(object,deviceId,array.toJSONString());
			if(resJson==null){
				res.setStatus(ResponseEnum.RequestTimeout.getStatus());
				res.setMessage(ResponseEnum.RequestTimeout.getMsg());
				return res;
			}
			TAliDeviceConfig aliDeviceConfig = aliDeviceConfigService.getAliDeviceConfigBySerializeId(deviceId);
			if(aliDeviceConfig !=null){
				aliDeviceConfig.setState(array.toJSONString());
				aliDeviceConfigService.update(aliDeviceConfig);
			}
			logger.info("array ====== "+array.toJSONString());
			res.setData(array.toJSONString());
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/readAliDevice", method = RequestMethod.POST)
	public ResponseObject readAliDevice(@RequestParam(required = true, value = "functionId") String functionId,
										@RequestParam(required = true, value = "deviceId") String deviceId) {
		ResponseObject res = new ResponseObject();
		try {
			JSONArray array = JSONArray.parseArray(functionId);
			logger.info("array ====== "+array);
			net.sf.json.JSONObject object = new net.sf.json.JSONObject();
			object.put("command", "read");
			object.element("functionId",array);
			logger.info("object ====== "+object);

			JSONObject resJson =topicServer.requestDev(object,deviceId,array.toJSONString());
			TAliDeviceConfig aliDeviceConfig = aliDeviceConfigService.getAliDeviceConfigBySerializeId(deviceId);
			String val = null;
			if(aliDeviceConfig !=null){
				val =resJson.getString("value");
				JSONArray resArry = JSONArray.parseArray(val);
				if(!resArry.isEmpty()){
					aliDeviceConfig.setState(val);
					aliDeviceConfigService.update(aliDeviceConfig);
				}

			}
			res.setData(val);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/queryAliDeviceTimer/{deviceId}", method = RequestMethod.GET)
	public ResponseObject queryAliDeviceTimer(@PathVariable(required = true, value = "deviceId") String deviceId) {
		ResponseObject res = new ResponseObject();
		try {
			List<net.sf.json.JSONObject> count = new ArrayList<net.sf.json.JSONObject>();
			List<TAliDevTimer> aliDevTimers = aliDeviceService.getAliDevTimerByDeviceSerialId(deviceId);
			for(TAliDevTimer aliDevTimer : aliDevTimers ){
				net.sf.json.JSONObject object = new net.sf.json.JSONObject();
				object.element("timerId", aliDevTimer.getId());
				object.element("state", aliDevTimer.getState());
				object.element("value", JSONArray.parseArray(aliDevTimer.getTimerValue()));
				object.element("timer", aliDevTimer.getTimer());
				count.add(object);
			}
			res.setData(count);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/setAliCountdown", method = RequestMethod.POST)
	public ResponseObject setAliCountdown(@RequestParam(value = "deviceId") String deviceId) {
		ResponseObject res = new ResponseObject();
		try {
			TAliDevice aliDevice = aliDeviceService.getAliDeviceBySerializeId(deviceId);
			if(aliDevice == null){
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
				return res;
			}

			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}


	@RequestMapping(value = "/setAliTimer", method = RequestMethod.POST)
	public ResponseObject setAliTimer(@RequestParam(value = "deviceId") String deviceId) {
		ResponseObject res = new ResponseObject();
		try {
			TAliDevice aliDevice = aliDeviceService.getAliDeviceBySerializeId(deviceId);
			if(aliDevice == null){
				res.setStatus(ResponseEnum.RequestObjectNotExist.getStatus());
				res.setMessage(ResponseEnum.RequestObjectNotExist.getMsg());
				return res;
			}

			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/uploadAliDevice", method = RequestMethod.POST)
	public ResponseObject uploadAliDevice(@RequestParam(required = true,value = "deviceName") String deviceName,
										  @RequestParam(required = true,value = "productKey") String productKey,
										  @RequestParam(required = true,value = "config") Object config,@RequestParam(required = true,value = "userId") Integer userId) {
		ResponseObject res = new ResponseObject();
		try {

			JSONObject object = new JSONObject((String)config);
			TAliDeviceConfig aliDeviceConfig = aliDeviceConfigService.getAliDeviceConfigBySerializeId(object.getString("deviceId"));
			if(aliDeviceConfig == null){
				aliDeviceConfig = new TAliDeviceConfig();
				aliDeviceConfig.setAction(object.getString("action"));
				aliDeviceConfig.setDeviceSerialId(object.getString("deviceId"));
				aliDeviceConfig.setName(object.getString("name"));
				aliDeviceConfig.setState(object.getString("state"));
				aliDeviceConfig.setType(object.getString("type"));
				aliDeviceConfigService.addAliDevice(aliDeviceConfig);
			}else{
				aliDeviceConfig.setAction(object.getString("action"));
				aliDeviceConfig.setDeviceSerialId(object.getString("deviceId"));
				aliDeviceConfig.setName(object.getString("name"));
				aliDeviceConfig.setState(object.getString("state"));
				aliDeviceConfig.setType(object.getString("type"));
				aliDeviceConfigService.update(aliDeviceConfig);
			}
			aliDeviceService.deleteAliDeviceUser(aliDeviceConfig.getDeviceSerialId());
			TAliDevice aliDevice = aliDeviceService.getAliDeviceBySerializeId(aliDeviceConfig.getDeviceSerialId());

			if(aliDevice!=null&&!aliDevice.getDeviceName().equals(deviceName)&&aliDevice.getProductKey().equals(productKey)){
				logger.info("========= upload available ====== ");
				aliDevice.setOboxSerialId("available");
				aliDeviceService.updateAliDevice(aliDevice);
				aliDevCache.DelDevInfo(aliDeviceConfig.getDeviceSerialId());
			}
			TAliDevice taliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(productKey,deviceName);
			if(taliDevice != null){
				logger.info("========= upload deviceId ====== ");
				taliDevice.setOboxSerialId(object.getString("deviceId"));
				aliDeviceService.updateAliDevice(taliDevice);
				aliDevCache.saveDevInfo(productKey,object.getString("deviceId"),deviceName,AliRegionEnum.SOURTHCHINA);
			}
			aliDevCache.delAliDevWait(productKey,deviceName);
			TUserAliDev userAliDev = new TUserAliDev();
			userAliDev.setDeviceSerialId(object.getString("deviceId"));
			userAliDev.setUserId(userId);
			aliDeviceService.addAliDevUser(userAliDev);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getAliDevTimerByDeviceSerialIdAndCountDown/{deviceId}", method = RequestMethod.GET)
	public ResponseObject getAliDevTimerByDeviceSerialIdAndCountDown(@PathVariable(value = "deviceId") String deviceId) {
		ResponseObject res = new ResponseObject();
		try {
//			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			TAliDevTimer aliDevTimer = aliDeviceService.getAliDevTimerByDeviceSerialIdAndCountDown(deviceId);
//			jsonObject.element("aliDevTimer",aliDevTimer);
			res.setData(aliDevTimer);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getAliDevTimerByIdAndDeviceId/{deviceId}/{timerId}", method = RequestMethod.GET)
	public ResponseObject getAliDevTimerByIdAndDeviceId(@PathVariable(value = "deviceId") String deviceId,@PathVariable(value = "timerId") Integer timerId) {
		ResponseObject res = new ResponseObject();
		try {
//			net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
			TAliDevTimer aliDevTimer = aliDeviceService.getAliDevTimerByIdAndDeviceId(deviceId,timerId);
//			jsonObject.element("aliDevTimer",aliDevTimer);
			res.setData(aliDevTimer);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/delAliDevTimerByDeviceSerialId/{id}", method = RequestMethod.GET)
	public ResponseObject delAliDevTimerByDeviceSerialId(@PathVariable(value = "id") Integer id) {
		ResponseObject res = new ResponseObject();
		try {
			aliDeviceService.deleteAliDevTimerById(id);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/addAliDevTimer", method = RequestMethod.POST)
	public ResponseObject addAliDevTimer(@RequestBody TAliDevTimer aliDevTimer) {
		ResponseObject res = new ResponseObject();
		try {
			Integer id = aliDeviceService.addAliDevTimer(aliDevTimer);
			res.setData(aliDevTimer.getId());
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/updateAliDevTimer", method = RequestMethod.POST)
	public ResponseObject updateAliDevTimer(@RequestBody TAliDevTimer aliDevTimer) {
		ResponseObject res = new ResponseObject();
		try {
			aliDeviceService.updateAliDevTimer(aliDevTimer);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/delAliDevice", method = RequestMethod.POST)
	public ResponseObject delAliDevice(@RequestParam(required = true, value = "deviceId") String deviceId) {
		ResponseObject res = new ResponseObject();
		try {

			aliDeviceService.deleteAliDeviceUser(deviceId);
			aliDeviceConfigService.deleteAliDeviceConfig(deviceId);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/queryCountDown/{deviceId}", method = RequestMethod.GET)
	public ResponseObject queryCountDown(@PathVariable(required = true, value = "deviceId") String deviceId) {
		ResponseObject res = new ResponseObject();
		try {

			net.sf.json.JSONObject json = new net.sf.json.JSONObject();
			TAliDevTimer aliDevTimer = aliDeviceService.getAliDevTimerByDeviceSerialIdAndCountDown(deviceId);
			if(aliDevTimer !=null){
				json.element("timer",aliDevTimer.getTimer());
				json.element("value",JSONArray.parseArray(aliDevTimer.getTimerValue()));
			}else{
				List<String> aList = new ArrayList<String>();
				json.element("timer","");
				json.element("value",aList);
			}
			res.setData(json);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/getAliDevTimerById/{id}", method = RequestMethod.GET)
	public ResponseObject getAliDevTimerById(@PathVariable(value = "id") Integer id) {
		ResponseObject res = new ResponseObject();
		try {
			TAliDevTimer tAliDevTimer = aliDeviceService.getAliDevTimerById(id);
			res.setData(tAliDevTimer);
			res.setStatus(ResponseEnum.SelectSuccess.getStatus());
			res.setMessage(ResponseEnum.SelectSuccess.getMsg());
		} catch (Exception e) {
			logger.error("===error msg:" + e.getMessage());
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

}
