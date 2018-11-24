package com.bright.apollo.socket;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.aliyun.mns.model.Message;
import com.bright.apollo.common.entity.PushMessage;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.PushMessageType;
import com.bright.apollo.handler.CMDHandlerManager;
import com.bright.apollo.handler.CommandHandler;
import com.bright.apollo.service.AliDeviceService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.service.PushService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.service.UserOboxService;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.vo.IotDevConncetion;
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
	@Autowired
	@Lazy
	private UserOboxService userOboxService;
	@Autowired
	@Lazy
	private PushService pushservice;
	@Autowired
	@Lazy
	private IotDevConncetion iotOboxConncetion;
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
				//傻逼代码写死了地区  后面再看
				commandHandler.processWiFiStatus(new JSONObject(aString),AliRegionEnum.SOURTHCHINA);
				JSONObject object = new JSONObject(aString);
				logger.info(" productKey ------ " + object.get("productKey"));
				logger.info(" deviceName ------ " + object.get("deviceName"));
				// String obox_serial_id = object.getString("deviceName");

				logger.info("status PopMessage Body: " + aString); // 获取原始消息
			} else if (messageType.equals("upload")) {
				// upload topic
				String topic = (String) map.get("topic");
				String[] topicArray = topic.split("/");
				logger.info("topic:" + topic + " PopMessage Body: " + aString); // 获取原始消息
				logger.info("topic:" + topic + " PopMessage Body length : " + aString.length()); // 获取原始消息长度
				if(!StringUtils.isEmpty(topicArray[1])&&(iotOboxConncetion.getOboxSouthChinaName().equals(topicArray[1])||
						iotOboxConncetion.getOboxAmericaName().equals(topicArray[1])
						)){
					 cmdHandlerManager.processTopic(topicArray[1],topicArray[2],aString);
				} else {
					commandHandler.process(topicArray[1], topicArray[2], aString);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
 			logger.info("------ MNService WorkerFunc Exception ------" + e.getMessage());
		}

	}

	/**  
	 * @param dbObox  
	 * @Description:  
	 */
	private void pushMessage(TObox dbObox) {

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
