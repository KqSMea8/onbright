package com.bright.apollo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.tool.ByteHelper;

@RestController
@RequestMapping("aliService")
public class aliServerController {

	@Autowired
	private TopicServer topicServer;

	@RequestMapping(value = "/toAli", method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<OboxResp> toAliService(@PathVariable CMDEnum cmd, @PathVariable String inMsg,
			@PathVariable String deviceSerial) {
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
	public ResponseObject<OboxResp> releaseObox(@PathVariable(required=true) String serialId) {
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		try {
			//from old code
			byte [] bodyBytes = new byte [8];
			byte [] oboxSerialIdBytes = ByteHelper.hexStringToBytes(serialId);
			System.arraycopy(oboxSerialIdBytes, 0, bodyBytes, 0, oboxSerialIdBytes.length);
			bodyBytes[5] = (byte)0xff;
			bodyBytes[6] = (byte)0xff;
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
}
