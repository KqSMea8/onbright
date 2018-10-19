package com.bright.apollo.handler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentSkipListSet;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.bright.apollo.cache.AliDevCache;
import com.bright.apollo.common.entity.PushMessage;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TUserAliDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.enums.ALIDevTypeEnum;
import com.bright.apollo.enums.AliCmdTypeEnum;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.PushMessageType;
import com.bright.apollo.service.AliDeviceService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.service.PushService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.service.UserAliDevService;
import com.bright.apollo.service.UserOboxService;
import com.bright.apollo.session.PushObserverManager;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.vo.IotDevConncetion;
import com.zz.common.util.StringUtils;

@Component
public class CommandHandler {
	private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);

	@Autowired
	private AliDevCache aliDevCache;

	@Autowired
	private AliDeviceService aliDeviceService;

	@Autowired
	private UploadHandler uploadHandler;
	@Autowired
	@Lazy
	private IotDevConncetion iotOboxConncetion;
	@Autowired
	private OboxService oboxService;
	@Autowired
	private UserOboxService userOboxService;
	@Autowired
	private PushService pushservice;
	@Autowired
	@Lazy
	private TopicServer topServer;
	@Autowired
	private UserAliDevService userAliDevService;
	public CommandHandler() {
		cmdHandlers = new HashMap<String, AliBaseHandler>();

	}

	private Map<String, AliBaseHandler> cmdHandlers;

	// static {
	// cmdHandlers.put(AliCmdTypeEnum.UPLOAD.getCmd(), new UploadHandler());
	// }

	// public static AliBaseHandler getCMDHandler(Command cmd) {
	// return cmdHandlers.get(cmd);
	// }

	public void process(String ProductKey, String DeviceName, String aString) {
		try {
			if (cmdHandlers.get(AliCmdTypeEnum.UPLOAD.getCmd()) == null) {
				cmdHandlers.put(AliCmdTypeEnum.UPLOAD.getCmd(), uploadHandler);
			}

			logger.info("======topic msg=====:key:" + ProductKey + " device:" + DeviceName + " payload" + aString);

			JSONObject object = new JSONObject(aString);
			if (StringUtils.isEmpty(object.getString("command"))) {
				return;
			}
			AliBaseHandler handler = cmdHandlers.get(object.getString("command"));
			if (handler == null) {
				logger.error("not exist this cmd : " + object.getString("command"));
				return;
			}

			String deviceSerialId = aliDevCache.getOboxSerialId(ProductKey, DeviceName);
			if (StringUtils.isEmpty(deviceSerialId)) {
				TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(ProductKey, DeviceName);
				// TAliDevice tAliDevice =
				// AliDevBusiness.queryAliDevByName(ProductKey, DeviceName);
				if (tAliDevice != null) {
					if (!tAliDevice.getOboxSerialId().equals("available")) {
						deviceSerialId = tAliDevice.getOboxSerialId();
						aliDevCache.saveDevInfo(tAliDevice.getProductKey(), tAliDevice.getOboxSerialId(),
								tAliDevice.getDeviceName(), AliRegionEnum.SOURTHCHINA);
					} else {
						return;
					}
				} else {
					TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(ProductKey,
							DeviceName);
					// TAliDeviceUS tAliDeviceUS =
					// AliDevBusiness.queryAliDevUSByName(ProductKey,
					// DeviceName);
					if (!tAliDeviceUS.getDeviceSerialId().equals("available")) {
						deviceSerialId = tAliDeviceUS.getDeviceSerialId();
						aliDevCache.saveDevInfo(tAliDeviceUS.getProductKey(), tAliDeviceUS.getDeviceSerialId(),
								tAliDeviceUS.getDeviceName(), AliRegionEnum.AMERICA);
					} else {
						return;
					}
				}
			}

			handler.process(deviceSerialId, object);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * @param jsonObject
	 * @param sourthchina
	 * @throws Exception 
	 * @throws JSONException 
	 * @Description:
	 */
	public void processWiFiStatus(JSONObject object, AliRegionEnum eAliRegionEnum) throws JSONException, Exception {
		//topicService topicService = TopicService.getInstance();
		if (object.getString("status").equals("offline")) {
			// 判断是否是obox
			String productKey = object.getString("productKey");
			if (StringUtils.isEmpty(productKey))
				return;
			if (iotOboxConncetion.getOboxSouthChinaName().equals(productKey)
					|| iotOboxConncetion.getOboxAmericaName().equals(productKey)) {
				TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(productKey,
						!StringUtils.isEmpty(object.getString("deviceName")) ? object.getString("deviceName")
								: "available");
				// TAliDevice tAliDevice =
				// AliDevBusiness.queryAliDevByName(object.getString("productKey"),
				// object.getString("deviceName"));
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
						dbObox.setOboxIp("0.0.0.0");
						oboxService.update(dbObox);
						// OboxBusiness.updateObox(dbObox);
					}
				} else {
					TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(productKey,
							!StringUtils.isEmpty(object.getString("deviceName")) ? object.getString("deviceName")
									: "available");
					// TAliDeviceUS tAliDeviceUS =
					// AliDevBusiness.queryAliDevUSByName(object.getString("productKey"),
					// object.getString("deviceName"));
					if (tAliDeviceUS != null) {
						tAliDeviceUS.setOffline(0);
						aliDeviceService.updateAliUSDevice(tAliDeviceUS);
						// AliDevBusiness.updateAliDevUS(tAliDeviceUS);
						TObox dbObox = oboxService.queryOboxsByOboxSerialId(tAliDeviceUS.getDeviceSerialId());
						// TObox dbObox =
						// OboxBusiness.queryOboxsByOboxSerialId(tAliDeviceUS.getDeviceSerialId());
						if (dbObox != null) {
							// AliDevCache.DelDevInfo(tAliDeviceUS.getDeviceSerialId());
							dbObox.setOboxStatus((byte) 0);
							dbObox.setOboxIp("0.0.0.0");
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
				TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(productKey,
						!StringUtils.isEmpty(object.getString("deviceName")) ? object.getString("deviceName")
								: "available");
				if (tAliDeviceUS != null) {
					tAliDeviceUS.setOffline(1);
					aliDeviceService.updateAliUSDevice(tAliDeviceUS);
				} else {
					TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(productKey,
							!StringUtils.isEmpty(object.getString("deviceName")) ? object.getString("deviceName")
									: "available");
					if (tAliDevice != null) {
						tAliDevice.setOffline(1);
						aliDeviceService.updateAliDevice(tAliDevice);
					}
				}
				pushMsg(object.getString("productKey"), object.getString("deviceName"), false);
			}
		} else if (object.getString("status").equals("online")) {
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
			logger.info("contentBytes ========"+object.toString());
			if (!StringUtils.isEmpty(object.getString("productKey"))&&(iotOboxConncetion.getOboxSouthChinaName().equals(object.getString("productKey"))||
					iotOboxConncetion.getOboxAmericaName().equals(object.getString("productKey"))
			)) {
				topServer.pubTopic(CMDEnum.time, body1, object.getString("productKey"),
						object.getString("deviceName"), eAliRegionEnum);
			} else {
				String time = ByteHelper.bytesToHexString(body1);
				JSONObject object2 = new JSONObject();
				object2.put("time", time.substring(2));
				object2.put("command", "time_sync");
				topServer.pubTopicDev(object2, object.getString("productKey"), object.getString("deviceName"),
						eAliRegionEnum);
				TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(
						object.getString("productKey"), !StringUtils.isEmpty(object.getString("deviceName"))
								? object.getString("deviceName") : "available");
				if (tAliDeviceUS != null) {
					tAliDeviceUS.setOffline(0);
					aliDeviceService.updateAliUSDevice(tAliDeviceUS);
				} else {
					TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(
							object.getString("productKey"), !StringUtils.isEmpty(object.getString("deviceName"))
									? object.getString("deviceName") : "available");
					if (tAliDevice != null) {
						tAliDevice.setOffline(0);
						aliDeviceService.updateAliDevice(tAliDevice);
					}
				}
				pushMsg(object.getString("productKey"), object.getString("deviceName"), true);
			}

		}
	}

	/**
	 * @param productKey
	 * @param deviceName
	 * @param onLine
	 * @Description:
	 */
	private void pushMsg(String productKey, String deviceName, Boolean onLine) {
		//PushObserverManager pushObserverManager = PushObserverManager.getInstance();
		PushMessage pushMessage = new PushMessage();
		pushMessage.setType(PushMessageType.WIFI_ONLINE.getValue());
		pushMessage.setOnLine(onLine);
		String deviceSerialId = aliDevCache.getOboxSerialId(productKey, deviceName);
		if (StringUtils.isEmpty(deviceSerialId)) {
			TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(productKey, deviceName);
			if (tAliDevice != null) {
				if (!tAliDevice.getOboxSerialId().equals("available")) {
					deviceSerialId = tAliDevice.getOboxSerialId();
					aliDevCache.saveDevInfo(tAliDevice.getProductKey(), tAliDevice.getOboxSerialId(),
							tAliDevice.getDeviceName(), AliRegionEnum.SOURTHCHINA);
				} else {
					return;
				}
			} else {
				TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(productKey, deviceName);
				if (!tAliDeviceUS.getDeviceSerialId().equals("available")) {
					deviceSerialId = tAliDeviceUS.getDeviceSerialId();
					aliDevCache.saveDevInfo(tAliDeviceUS.getProductKey(), tAliDeviceUS.getDeviceSerialId(),
							tAliDeviceUS.getDeviceName(), AliRegionEnum.AMERICA);
				} else {
					return;
				}
			}
		}
		//pushservice
		pushMessage.setSerialId(deviceSerialId);
		List<TUserAliDevice> list=userAliDevService.queryAliUserId(deviceSerialId);
		Set<Integer> setuser=new ConcurrentSkipListSet<Integer>();
		for (TUserAliDevice tUserAliDevice : list) {
			setuser.add(tUserAliDevice.getUserId());
		}
		pushservice.pushToApp(pushMessage, setuser);
	}
}
