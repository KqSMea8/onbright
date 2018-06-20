package com.bright.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.DeviceService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.session.SceneActionThreadPool;
import com.bright.apollo.tool.ByteHelper;
import com.zz.common.util.StringUtils;

@RestController
@RequestMapping("aliService")
public class AliServerController {

	@Autowired
	private TopicServer topicServer;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private SceneActionThreadPool sceneActionThreadPool;
	// for search new device
	private static String timeout = "30";

	@RequestMapping(value = "/toAli", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<OboxResp> toAliService(@PathVariable(value = "cmd") CMDEnum cmd,
			@PathVariable(value = "inMsg") String inMsg, @PathVariable(value = "deviceSerial") String deviceSerial) {
		byte[] inMsgByte = inMsg.getBytes();
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			OboxResp resp = topicServer.request(cmd, inMsgByte, deviceSerial);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
			res.setData(resp);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
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

			OboxResp resp = topicServer.request(CMDEnum.release_all_devices, bodyBytes, serialId);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
			res.setData(resp);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@RequestMapping(value = "/stopScan/{oboxSerialId}", method = RequestMethod.DELETE)
	ResponseObject<OboxResp> stopScan(@PathVariable(required = true, value = "oboxSerialId") String oboxSerialId) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			byte[] sendbodyBytes = new byte[15];
			sendbodyBytes[0] = (byte) Integer.parseInt("00", 16);
			OboxResp resp = topicServer.request(CMDEnum.search_new_device, sendbodyBytes, oboxSerialId);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
			res.setData(resp);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
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
			OboxResp resp = topicServer.request(CMDEnum.search_new_device, sendbodyBytes, oboxSerialId);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
			res.setData(resp);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
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
			OboxResp resp = topicServer.request(CMDEnum.search_new_device, sendbodyBytes, oboxSerialId);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
			res.setData(resp);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
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
			OboxResp resp = topicServer.request(CMDEnum.search_new_device, sendbodyBytes, oboxSerialId);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
			res.setData(resp);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;

	}

	/**
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "controlServerScene/{sceneNumber}", method = RequestMethod.PUT)
	ResponseObject controlServerScene(@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			// check the param by the invoker
			sceneActionThreadPool.addSceneAction(sceneNumber);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
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
			OboxResp resp = topicServer.request(CMDEnum.setting_node_status, bodyBytes, oboxSerialId);
			res.setCode(ResponseEnum.Success.getCode());
			res.setMsg(ResponseEnum.Success.getMsg());
			res.setData(resp);
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
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
			OboxResp oboxResp = topicServer.request(CMDEnum.setting_sc_info, bodyBytes, oboxSerialId);
			if (oboxResp == null || oboxResp.getData() == null || oboxResp.getData().substring(0, 2).equals("00")) {
				res.setCode(ResponseEnum.SendOboxFail.getCode());
				res.setMsg(ResponseEnum.SendOboxFail.getMsg());
			} else {
				res.setCode(ResponseEnum.Success.getCode());
				res.setMsg(ResponseEnum.Success.getMsg());
				res.setData(oboxResp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}/*
	
	@RequestMapping(value = "/aliService/addLocalSceneCondition/{sceneNumber}", method = RequestMethod.POST)
	ResponseObject<OboxResp> addLocalSceneCondition(
			@PathVariable(required = true, value = "sceneNumber") Integer sceneNumber,
			@RequestParam(required = true, value = "conditions") List<TSceneCondition> conditions) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			for (int i = 0; i < conditions.size(); i++) {
				byte[] comBytes = new byte[50];
				comBytes[0] = 0x02;
				comBytes[2] = (byte) (int) sceneNumber;
				comBytes[3] = (byte) (i + 1);
				int condType = 0;
				int choice = 0;
				for (int j = 0; j < list.size(); j++) {
					choice |= (0x01 << j);

					SceneConditionDTO sceneConditionDTO = list.get(j);
					byte[] condition = ByteHelper
							.hexStringToBytes(sceneConditionDTO
									.getCondition());
					System.arraycopy(condition, 0, comBytes,
							12 + j * 15, condition.length);
					if (sceneConditionDTO.getDeviceSerialId() != null) {
						// node condition
						String serialID = sceneConditionDTO
								.getDeviceSerialId();
						TOboxDeviceConfig tOboxDeviceConfig = queryDeviceByWeight(
								tUser, serialID);
						if (tOboxDeviceConfig != null) {
							condType |= (0x02 << 2 * j);
							byte[] serial = ByteHelper
									.hexStringToBytes(tOboxDeviceConfig
											.getOboxSerialId());
							System.arraycopy(serial, 0, comBytes,
									5 + j * 15, serial.length);
							comBytes[10 + j * 15] = 0x00;
							comBytes[11 + j * 15] = (byte) Integer
									.parseInt(tOboxDeviceConfig
											.getDeviceRfAddr(), 16);
						}
					} else {
						// time condition
						condType |= (0x01 << 2 * j);
					}
				
				comBytes[1] = (byte) choice;
				comBytes[4] = (byte) condType;
				topicService.request(getCmd(), comBytes, tObox.getOboxSerialId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setCode(ResponseEnum.Error.getCode());
			res.setMsg(ResponseEnum.Error.getMsg());
		}
		return res;
	}*/
}
