package com.bright.apollo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.request.SceneConditionDTO;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.session.SceneActionThreadPool;
import com.bright.apollo.tool.ByteHelper;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月17日
 * @Version:1.1.0
 */
@RestController
@RequestMapping("aliScene")
public class AliSceneController {

	@Autowired
	private SceneActionThreadPool sceneActionThreadPool;

	@Autowired
	private TopicServer topicServer;

	@Autowired
	private OboxDeviceConfigService oboxDeviceConfigService;
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addSceneAction/{sceneNumber}", method = RequestMethod.POST)
	public ResponseObject addSceneAction(@PathVariable(value = "sceneNumber") Integer sceneNumber) {
		ResponseObject res = new ResponseObject();
		try {
			sceneActionThreadPool.addSceneAction(sceneNumber);
			// OboxResp resp = topicServer.request(cmd, inMsgByte,
			// deviceSerial);
			res.setStatus(ResponseEnum.AddSuccess.getStatus());
			res.setMessage(ResponseEnum.AddSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/setLocalScene/{sceneStatus}/{oboxSceneNumber}/{sceneName}/{oboxSerialId}", method = RequestMethod.PUT)
	public ResponseObject setLocalScene(@PathVariable(value = "sceneStatus") Byte sceneStatus,
			@PathVariable(value = "oboxSceneNumber") Integer oboxSceneNumber,
			@PathVariable(value = "sceneName") String sceneName,
			@PathVariable(value = "oboxSerialId") String oboxSerialId,
			@RequestParam(required = false, value = "groupAddr") String groupAddr) {
		ResponseObject res = new ResponseObject();
		try {
			byte[] bodyBytes = new byte[21];
			bodyBytes[0] = 0x01;
			if (sceneStatus == (byte) 1) {
				bodyBytes[1] = 0x12;
			} else {
				bodyBytes[1] = 0x02;
			}
			bodyBytes[2] = (byte) oboxSceneNumber.intValue();
			String newName = ByteHelper.bytesToHexString(sceneName.getBytes("UTF-8"));
			byte[] namebytes = ByteHelper.hexStringToBytes(newName);
			System.arraycopy(namebytes, 0, bodyBytes, 3, namebytes.length);
			if (groupAddr != null) {
				byte[] groupbytes = ByteHelper.hexStringToBytes(groupAddr);
				System.arraycopy(groupbytes, 0, bodyBytes, 19, groupbytes.length);
			}
			topicServer.request(CMDEnum.setting_sc_info, bodyBytes, oboxSerialId);
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/modifyLocalSceneCondition/{oboxSceneNumber}/{oboxSerialId}/{userId}", method = RequestMethod.PUT)
	ResponseObject modifyLocalSceneCondition(@PathVariable(value = "oboxSceneNumber") Integer oboxSceneNumber,
			@PathVariable(value = "oboxSerialId") String oboxSerialId,
			@PathVariable(value = "userId") Integer userId,
			@RequestBody List<List<SceneConditionDTO>> sceneConditionDTOs) {
		ResponseObject res = new ResponseObject();
		try {
			for (int i = 0; i < sceneConditionDTOs.size(); i++) {
				byte[] comBytes = new byte[50];
				comBytes[0] = 0x02;
				comBytes[2] = (byte) oboxSceneNumber.intValue();
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
						TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.getDeviceByUserAndSerialId(userId, serialID);
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
				topicServer.request(CMDEnum.setting_sc_info, comBytes, oboxSerialId);
			}
			res.setStatus(ResponseEnum.UpdateSuccess.getStatus());
			res.setMessage(ResponseEnum.UpdateSuccess.getMsg());
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(ResponseEnum.Error.getStatus());
			res.setMessage(ResponseEnum.Error.getMsg());
		}
		return res;
	}
}
