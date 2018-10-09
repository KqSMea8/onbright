package com.bright.apollo.session;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.bright.apollo.bean.PushSystemMsg;
import com.bright.apollo.cache.AliDevCache;
import com.bright.apollo.common.entity.TAliDevTimer;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.enums.NodeTypeEnum;
import com.bright.apollo.enums.SystemEnum;
import com.bright.apollo.service.AliDeviceService;
import com.bright.apollo.service.JPushService;
import com.bright.apollo.service.MsgService;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.SceneActionService;
import com.bright.apollo.service.SceneService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.service.UserSceneService;
import com.bright.apollo.service.UserService;
import com.bright.apollo.tool.ByteHelper;
import com.bright.apollo.tool.MobileUtil;
import com.zz.common.util.StringUtils;

@Component
public class SceneActionThreadPool {
	private static ExecutorService executor;

	@Autowired
	private AliDeviceService aliDeviceService;

	@Autowired
	private AliDevCache aliDevCache;

	@Autowired
	@Lazy
	private TopicServer topicServer;

	@Autowired
	private SceneActionService sceneActionService;

	@Autowired
	private OboxDeviceConfigService oboxDeviceConfigService;

	@Autowired
	private SceneService sceneService;
	
	@Autowired
	private UserSceneService userSceneService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private MsgService msgService;
	
	@Autowired
	private PushObserverManager pushObserverManager;
	
	private final Logger log = Logger.getLogger(SceneActionThreadPool.class);

	public SceneActionThreadPool() {
		executor = Executors.newFixedThreadPool(5);
	}

	public void addTimerAction(Integer timerId) {
		log.info("===SceneActionThreadPool timerAction:" + timerId);
		executor.submit(new timerAction(timerId));
	}

	class timerAction implements Runnable {

		private Integer timerId;

		public timerAction(Integer timerId) {
			this.timerId = timerId;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {

				TAliDevTimer tAliDevTimer = aliDeviceService.getAliDevTimerByTimerId(timerId);
				if (tAliDevTimer == null) {
					return;
				}
				String productKey = aliDevCache.getProductKey(tAliDevTimer.getDeviceSerialId());
				String deviceName = aliDevCache.getProductKey(tAliDevTimer.getDeviceSerialId());
				String region = aliDevCache.getProductRegion(tAliDevTimer.getDeviceSerialId());
				AliRegionEnum eAliRegionEnum = AliRegionEnum.SOURTHCHINA;
				if (StringUtils.isEmpty(productKey)) {
					TAliDevice tAliDevice = aliDeviceService
							.getAliDeviceBySerializeId(tAliDevTimer.getDeviceSerialId());
					if (tAliDevice != null) {
						productKey = tAliDevice.getProductKey();
						deviceName = tAliDevice.getDeviceName();
						region = AliRegionEnum.SOURTHCHINA.name();
						aliDevCache.saveDevInfo(productKey, tAliDevTimer.getDeviceSerialId(), deviceName,
								AliRegionEnum.SOURTHCHINA);
					} else {
						TAliDeviceUS tAliDeviceUS = aliDeviceService
								.getAliUSDeviceBySerializeId(tAliDevTimer.getDeviceSerialId());
						if (tAliDeviceUS == null) {
							return;
						}
						productKey = tAliDeviceUS.getProductKey();
						deviceName = tAliDeviceUS.getDeviceName();
						region = AliRegionEnum.AMERICA.name();
						eAliRegionEnum = AliRegionEnum.AMERICA;
						aliDevCache.saveDevInfo(productKey, tAliDevTimer.getDeviceSerialId(), deviceName,
								AliRegionEnum.AMERICA);
					}
				} else {
					eAliRegionEnum = AliRegionEnum.getRegion(region);
				}

				if (tAliDevTimer != null) {
					if (!StringUtils.isEmpty(tAliDevTimer.getTimerValue())) {
						JSONObject sendObject = new JSONObject();
						sendObject.put("command", "set");
						sendObject.put("value", JSONArray.parseArray(tAliDevTimer.getTimerValue()));
						topicServer.pubTopicDev(sendObject, productKey, deviceName, eAliRegionEnum);

						if (tAliDevTimer.getIsCountdown() != 0) {
							aliDeviceService.deleteAliDevTimerBySerializeIdAndType(tAliDevTimer.getDeviceSerialId(),
									tAliDevTimer.getIsCountdown());
							// AliDevBusiness.deleteAliDevTimerByType(tAliDevTimer.getDeviceSerialId(),
							// tAliDevTimer.getIsCountdown());
						}
					}
				}

			} catch (Exception e) {
				log.error("===error msg:" + e.getMessage());
			}
		}
	}

	public void addSceneAction(int sceneNumber) {
		log.info("===SceneActionThreadPool sceneNumber:" + sceneNumber);
		executor.submit(new setAction(sceneNumber));
	}

	class setAction implements Runnable {

		private int sceneNumber;

		private long startTime;

		private String urlString = null;

		private SessionManager sessionManager = SessionManager.getInstance();

		// private static MsgService msgService = MsgService.getInstance();

		public setAction(int sceneNumber) {

			this.sceneNumber = sceneNumber;
			this.startTime = new Date().getTime();
		}

		@Override
		public void run() {
			try {
				List<TSceneAction> tSceneActions = sceneActionService.getSceneActionBySceneNumber(sceneNumber);
				for (TSceneAction tSceneAction : tSceneActions) {
					if (tSceneAction.getNodeType().equals(NodeTypeEnum.single.getValue())) {
						TOboxDeviceConfig oboxDeviceConfig = oboxDeviceConfigService
								.getTOboxDeviceConfigByDeviceSerialId(tSceneAction.getActionid());
						if (oboxDeviceConfig != null) {
							byte[] bodyBytes = new byte[16];
							byte[] oboxSerildIdBytes = ByteHelper.hexStringToBytes(oboxDeviceConfig.getOboxSerialId());
							System.arraycopy(oboxSerildIdBytes, 0, bodyBytes, 0, oboxSerildIdBytes.length);
							bodyBytes[5] = 0x00;
							bodyBytes[6] = (byte) Integer.parseInt(oboxDeviceConfig.getDeviceRfAddr(), 16);
							byte[] sBytes = ByteHelper.hexStringToBytes(tSceneAction.getAction());
							System.arraycopy(sBytes, 0, bodyBytes, 7, sBytes.length);
							if (topicServer != null) {
								CMDEnum cmd=CMDEnum.setting_node_status;
								if(oboxDeviceConfig.getDeviceType().equals(DeviceTypeEnum.remote_led.getValue())){
									cmd=CMDEnum.setting_remote_led;
								}
								topicServer.pubTopic(cmd, bodyBytes,
										oboxDeviceConfig.getOboxSerialId());
								//TimeUnit.MILLISECONDS.sleep(250);
							}
						}
					}
				}
				TScene tScene = sceneService.getSceneBySceneNumber(sceneNumber);
				if (tScene != null) {
					tScene.setSceneRun((byte) 0);
					sceneService.updateScene(tScene);
					if (tScene.getMsgAlter() != 0) {
						if (tScene.getMsgAlter() == 1 || tScene.getMsgAlter() == 3) {
							List<TUser> tUsers = userService.queryUserBySceneNumber(tScene.getSceneNumber());
							for (TUser tUser : tUsers) {
								log.info("====before push====");
								JPushService.sendAlter(tScene.getSceneName(), tUser.getUserName(), urlString);
							}
						}
					}
					List<TUserScene> tUserScenes = userSceneService
							.getUserSceneBySceneNum(tScene
									.getSceneNumber());
					if (!tUserScenes.isEmpty()) {
						for (TUserScene tUserScene : tUserScenes) {
							TUser user = userService
									.getUserByUserId(tUserScene
											.getUserId());
							if (user != null) {
								if (tScene.getMsgAlter() == 2
										|| tScene.getMsgAlter() == 3) {
									if (MobileUtil.checkMobile(user.getUserName())) {
										log.info("===tUserPhones:"
												+ user.getUserName());
										msgService.sendAlter(
												tScene.getSceneName(),
												user.getUserName());
										PushSystemMsg systemMsg = new PushSystemMsg(
												SystemEnum.system
														.getValue(),
												SystemEnum.scene
														.getValue(),
												sceneNumber, null,
												sceneNumber
														+ ",请注意！【昂宝电子】");
										pushObserverManager
												.sendMessage(null,
														systemMsg);
										tScene.setAlterNeed((byte)0);
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("===error msg:" + e.getMessage());
			}
		}
	}
}
