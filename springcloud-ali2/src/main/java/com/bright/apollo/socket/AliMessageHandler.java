package com.bright.apollo.socket;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.aliyun.mns.model.Message;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.enums.ALIDevTypeEnum;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.handler.CMDHandlerManager;
import com.bright.apollo.handler.CommandHandler;
import com.bright.apollo.service.AliDeviceService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.tool.ByteHelper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月26日
 * @Version:1.1.0
 */
@Component
public class AliMessageHandler {
	private static final Logger logger = LoggerFactory.getLogger(AliMessageHandler.class);
	private static Gson gson = new Gson();

	@Autowired
	private AliDeviceService aliDeviceService;
	@Autowired
	private OboxService oboxService;
	@Autowired
	private TopicServer topicService;
	@Autowired
	private CMDHandlerManager cmdHandlerManager;
	@Autowired
	private CommandHandler commandHandler;

	/**
	 * @param message
	 * @Description:
	 */
	@Async
	public void handler(Message message) {
		logger.info("===start handler iot message===");
		try {
			String body = message.getMessageBody();
			LinkedTreeMap map = gson.fromJson(body, LinkedTreeMap.class);
			String messageType = (String) map.get("messagetype");
			String payload = (String) map.get("payload");
			byte[] contentBytes = Base64.decodeBase64(payload);
			String aString = new String(contentBytes, "utf-8");
			if (messageType.equals("status")) {
				// update device status
				JSONObject object = new JSONObject(aString);
				logger.info(" productKey ------ " + object.get("productKey"));
				logger.info(" deviceName ------ " + object.get("deviceName"));
				// String obox_serial_id = object.getString("deviceName");
				if (object.getString("status").equals("offline")) {
					if (ALIDevTypeEnum.getTypebyValue(object.getString("productKey")).equals(ALIDevTypeEnum.OBOX)) {
						TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(
								object.getString("productKey"), object.getString("deviceName"));
						// TAliDevice tAliDevice =
						// AliDevBusiness.queryAliDevByName(object.getString("productKey"),object.getString("deviceName"));
						if (tAliDevice != null) {
							tAliDevice.setOffline(0);
							aliDeviceService.updateAliDevice(tAliDevice);
							// AliDevBusiness.updateAliDev(tAliDevice);
							TObox dbObox = oboxService.queryOboxsByOboxSerialId(tAliDevice.getOboxSerialId());
							// TObox dbObox =
							// OboxBusiness.queryOboxsByOboxSerialId(tAliDevice.getOboxSerialId());
							if (dbObox != null) {
								// AliDevCache.DelDevInfo(tAliDevice.getOboxSerialId());

								dbObox.setOboxStatus((byte) 0);
								// dbObox.setOboxIP("0.0.0.0");
								oboxService.update(dbObox);
							}
						} else {
							TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(
									object.getString("productKey"), object.getString("deviceName"));
							// TAliDeviceUS tAliDeviceUS =
							// AliDevBusiness.queryAliDevUSByName(object.getString("productKey"),object.getString("deviceName"));
							if (tAliDeviceUS != null) {
								tAliDeviceUS.setOffline(0);
								aliDeviceService.updateAliUSDevice(tAliDeviceUS);
								TObox dbObox = oboxService.queryOboxsByOboxSerialId(tAliDeviceUS.getDeviceSerialId());
								// TObox dbObox =
								// OboxBusiness.queryOboxsByOboxSerialId(tAliDeviceUS.getDeviceSerialId());
								if (dbObox != null) {
									// AliDevCache.DelDevInfo(tAliDeviceUS.getDeviceSerialId());

									dbObox.setOboxStatus((byte) 0);
									// dbObox.setOboxIP("0.0.0.0");
									oboxService.update(dbObox);
								}
							}
						}
						// else {
						// tAliDevice = new TAliDevice();
						// tAliDevice.setDeviceName(object.getString("deviceName"));
						// tAliDevice.setProductKey(object.getString("productKey"));
						// AliDevBusiness.addAliDev(tAliDevice);
						// }
					} else {

						TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(
								object.getString("productKey"), object.getString("deviceName"));
						// TAliDeviceUS tAliDeviceUS =
						// AliDevBusiness.queryAliDevUSByName(object.getString("productKey"),object.getString("deviceName"));
						if (tAliDeviceUS != null) {
							tAliDeviceUS.setOffline(1);
							aliDeviceService.updateAliUSDevice(tAliDeviceUS);
						} else {
							TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(
									object.getString("productKey"), object.getString("deviceName"));
							// TAliDevice tAliDevice =
							// AliDevBusiness.queryAliDevByName(object.getString("productKey"),object.getString("deviceName"));
							if (tAliDevice != null) {

								tAliDevice.setOffline(1);
								aliDeviceService.updateAliDevice(tAliDevice);
							}
						}
					}

				}

				if (object.getString("status").equals("online")) {
					TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
					Calendar date = Calendar.getInstance(tz);

					byte[] body1 = new byte[9];
					body1[0] = 2;
					body1[1] = 8; // 时区
					body1[2] = (byte) (date.get(Calendar.YEAR) % 2000); // 年
					body1[3] = (byte) (date.get(Calendar.MONTH) + 1); // 月
					body1[4] = (byte) date.get(Calendar.DATE); // 日
					body1[5] = (byte) (date.get(Calendar.DAY_OF_WEEK)); // 星期几
					body1[6] = (byte) date.get(Calendar.HOUR_OF_DAY); // 小时
					body1[7] = (byte) date.get(Calendar.MINUTE); // 分钟
					body1[8] = (byte) date.get(Calendar.SECOND); // 秒

					if (ALIDevTypeEnum.getTypebyValue(object.getString("productKey")).equals(ALIDevTypeEnum.OBOX)) {
						topicService.pubTopic(CMDEnum.time, body1, object.getString("productKey"),
								object.getString("deviceName"), AliRegionEnum.SOURTHCHINA);
					} else {
						String time = ByteHelper.bytesToHexString(body1);
						JSONObject object2 = new JSONObject();
						object2.put("time", time.substring(2));
						object2.put("command", "time_sync");
						topicService.pubTopicDev(object2, object.getString("productKey"),
								object.getString("deviceName"), AliRegionEnum.SOURTHCHINA);

						TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(
								object.getString("productKey"), object.getString("deviceName"));
						// TAliDeviceUS tAliDeviceUS =
						// AliDevBusiness.queryAliDevUSByName(object.getString("productKey"),object.getString("deviceName"));
						if (tAliDeviceUS != null) {
							tAliDeviceUS.setOffline(0);
							aliDeviceService.updateAliUSDevice(tAliDeviceUS);
							// AliDevBusiness.updateAliDevUS(tAliDeviceUS);
						} else {
							TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(
									object.getString("productKey"), object.getString("deviceName"));
							// TAliDevice tAliDevice =
							// AliDevBusiness.queryAliDevByName(object.getString("productKey"),object.getString("deviceName"));
							if (tAliDevice != null) {
								tAliDevice.setOffline(0);
								aliDeviceService.updateAliDevice(tAliDevice);
							}
						}
					}

				}

				logger.info("status PopMessage Body: " + aString); // 获取原始消息
			} else if (messageType.equals("upload")) {
				// upload topic
				String topic = (String) map.get("topic");
				String[] topicArray = topic.split("/");
				logger.info("topic:" + topic + " PopMessage Body: " + aString); // 获取原始消息
				logger.info("topic:" + topic + " PopMessage Body length : " + aString.length()); // 获取原始消息长度
				if (ALIDevTypeEnum.getTypebyValue(topicArray[1]).equals(ALIDevTypeEnum.OBOX)) {

					 cmdHandlerManager.processTopic(topicArray[1],topicArray[2],aString);
				} else {
					commandHandler.process(topicArray[1], topicArray[2], aString);
				}
			}
		} catch (Exception e) {
 			logger.info("------ MNService WorkerFunc Exception ------" + e.getMessage());
		}

	}

}
