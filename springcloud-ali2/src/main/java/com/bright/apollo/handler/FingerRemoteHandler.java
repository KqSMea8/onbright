package com.bright.apollo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.enums.RemoteUserEnum;
import com.bright.apollo.session.ClientSession;
import org.springframework.stereotype.Component;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年8月10日
 * @Version:1.1.0
 */
@Component
public class FingerRemoteHandler extends BasicHandler {
	private static final Logger log = LoggerFactory.getLogger(FingerRemoteHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.socket.handler.BasicHandler#process(com.bright.apollo.
	 * socket.session.ClientSession, com.bright.apollo.socket.bean.Message)
	 */
	@Override
	public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
		String data = msg.getData();
		String oboxSerialId = data.substring(0, 10);
		String addr = data.substring(12, 14);
		String pin = Integer.parseInt(data.substring(16, 18), 16) + "";
		log.info("===oboxSerialId:" + oboxSerialId + "===pin:" + pin + "===addr:" + addr);
		TObox tObox = oboxService.queryOboxsByOboxSerialId(oboxSerialId);
		TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(tObox.getOboxId(), addr);
		if (tObox != null && tOboxDeviceConfig != null
				&& tOboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.doorlock.getValue())
				&& tOboxDeviceConfig.getDeviceChildType().equals(DeviceTypeEnum.capacity_finger.getValue())) {
			log.info("===deviceSerialId:" + tOboxDeviceConfig.getDeviceSerialId());
			TIntelligentFingerRemoteUser fingerRemoteUser = intelligentFingerService
					.queryTIntelligentFingerRemoteUserBySerialIdAndPin(tOboxDeviceConfig.getDeviceSerialId(),
							Integer.parseInt(pin));
			if (fingerRemoteUser == null) {
				return null;
			}
			log.info("===judge the fingerRemoteUser===");
			byte[] buildBytes = fingerUtil.buildBytes(RemoteUserEnum.add.getValue(), tObox.getOboxSerialId(),
					tOboxDeviceConfig.getDeviceRfAddr(), fingerRemoteUser.getStartTime(), fingerRemoteUser.getEndTime(),
					fingerRemoteUser.getTimes().intValue() - fingerRemoteUser.getUseTimes().intValue() + "",
					fingerRemoteUser.getUserSerialid(), fingerRemoteUser.getPwd());
			topicServer.request(CMDEnum.respone_finger_remote_user, buildBytes, tObox.getOboxSerialId());
		}
		return null;
	}

}
