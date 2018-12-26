package com.bright.apollo.handler;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.PushMessage;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.PushMessageType;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.tool.ByteHelper;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月26日  
 *@Version:1.1.0  
 */
@Component
public class HeartBeatHandler extends BasicHandler {
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);
	/* (non-Javadoc)  
	 * @see com.bright.apollo.handler.BasicHandler#process(com.bright.apollo.session.ClientSession, com.bright.apollo.bean.Message)  
	 */
	@Override
	public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
 		if (msg.getData().substring(0, 2).equals("01")) {
			String random_number = msg.getData().substring(2, 34);
			String serialId = msg.getData().substring(34, 44);
			String oboxVersion = msg.getData().substring(44, 60);
			String oboxName = ByteHelper.fromHexAscii(msg.getData().substring(62,
					62 + 2 * Integer.parseInt(msg.getData().substring(60, 62), 16)));
			//client.setUid(serialId);

			AliRegionEnum enum1 = AliRegionEnum.SOURTHCHINA;
			TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(clientSession.getProductKey(),
					clientSession.getDeviceName());
			// TAliDevice tAliDevice =
			// AliDevBusiness.queryAliDevByName(clientSession.getProductKey(),
			// clientSession.getDeviceName());
			if (tAliDevice != null) {
				logger.info("===tAliDevice device name:" + tAliDevice.getDeviceName());
				if (!tAliDevice.getOboxSerialId().equals(serialId)) {
					TAliDevice tAliDevice2 = aliDeviceService.getAliDeviceBySerializeId(serialId);
					// TAliDevice tAliDevice2 =
					// AliDevBusiness.queryAliDevBySerial(serialId);
					if (tAliDevice2 != null) {
						tAliDevice2.setOboxSerialId("available");
						aliDeviceService.updateAliDevice(tAliDevice2);
						// AliDevBusiness.updateAliDev(tAliDevice2);
					}

					tAliDevice.setOboxSerialId(serialId);
					aliDeviceService.updateAliDevice(tAliDevice);
					// AliDevBusiness.updateAliDev(tAliDevice);
				}
				aliDevCache.saveDevInfo(clientSession.getProductKey(), serialId, clientSession.getDeviceName(), AliRegionEnum.SOURTHCHINA);
			} else {
				TAliDeviceUS TAliDeviceUS = aliDeviceService
						.getAliUSDeviceByProductKeyAndDeviceName(clientSession.getProductKey(), clientSession.getDeviceName());
				// TAliDeviceUS TAliDeviceUS =
				// AliDevBusiness.queryAliDevUSByName(clientSession.getProductKey(),
				// clientSession.getDeviceName());
				if (TAliDeviceUS != null) {
					if (!TAliDeviceUS.getDeviceSerialId().equals(serialId)) {
						TAliDeviceUS tAliDeviceUS2 = aliDeviceService.getAliUSDeviceBySerializeId(serialId);
						// TAliDeviceUS tAliDeviceUS2 =
						// AliDevBusiness.queryAliDevUSBySerial(serialId);
						if (tAliDeviceUS2 != null) {
							tAliDeviceUS2.setDeviceSerialId("available");
							aliDeviceService.updateAliUSDevice(tAliDeviceUS2);
							// AliDevBusiness.updateAliDevUS(tAliDeviceUS2);
						}

						TAliDeviceUS.setDeviceSerialId(serialId);
						aliDeviceService.updateAliUSDevice(TAliDeviceUS);
						// AliDevBusiness.updateAliDevUS(TAliDeviceUS);
					}
					enum1 = AliRegionEnum.AMERICA;
					aliDevCache.saveDevInfo(clientSession.getProductKey(), serialId, clientSession.getDeviceName(), AliRegionEnum.AMERICA);
				}
			}
			TObox dbObox = oboxService.queryOboxsByOboxSerialId(serialId);
			// TObox dbObox =
			// OboxBusiness.queryOboxsByOboxSerialId(serialId);
			// TKey tKey =
			// OboxBusiness.queryKeyByProductKey(clientSession.getProductKey());
			if (dbObox == null) {
				logger.info("===add obox===");
				dbObox = new TObox();
				// if (tKey != null) {
				// dbObox.setLicense(tKey.getId());
				// }
				dbObox.setOboxVersion(oboxVersion);
				dbObox.setOboxName(oboxName);
				dbObox.setOboxSerialId(serialId);
				dbObox.setOboxStatus((byte) 1);
				// dbObox.setOboxPwd("88888888");
				// dbObox.setOboxActivate(0);
				// dbObox.setOboxIP(ip[0].substring(1));
				dbObox.setOboxIp("0.0.0.0");
				oboxService.addObox(dbObox);
				// OboxBusiness.addObox(dbObox);
				dbObox = oboxService.queryOboxsByOboxSerialId(serialId);
			} else {
				// if (tKey != null) {
				// dbObox.setLicense(tKey.getId());
				// }
				dbObox.setOboxStatus((byte) 1);
				dbObox.setOboxName(oboxName);
				// dbObox.setOboxActivate(0);
				dbObox.setOboxVersion(oboxVersion);
				// dbObox.setOboxIP(ip[0].substring(1));
				oboxService.update(dbObox);
				// OboxBusiness.updateObox(dbObox);
			}

			// send server addr to obox
			byte[] body = new byte[22];
			body[0] = 1;
			topServer.pubTopic(CMDEnum.time, body, clientSession.getProductKey(), clientSession.getDeviceName(), enum1);
			pushMessage( dbObox);
		}
		return null;
	}
	private void pushMessage( TObox dbObox) {
		if(org.springframework.util.StringUtils.isEmpty(dbObox))
			return;
		logger.info("===pushMessage===:" + dbObox.toString());
 		PushMessage pushMessage;
		pushMessage = new PushMessage();
		pushMessage.setSerialId(dbObox.getOboxSerialId());
		pushMessage.setType(PushMessageType.OBOX_ONLINE.getValue());
		pushMessage.setOnLine(dbObox.getOboxStatus() == 0 ? false : true);
		List<TUserObox> list = userOboxService.getUserOboxBySerialId(dbObox.getOboxSerialId());
		Set<Integer> setuser=new ConcurrentSkipListSet<Integer>();
  	  	for(TUserObox userobox:list){
  		  setuser.add(userobox.getUserId());
  	  }
  	  pushservice.pushToApp(pushMessage, setuser);
	}
}
