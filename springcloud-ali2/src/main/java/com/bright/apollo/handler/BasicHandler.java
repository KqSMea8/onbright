package com.bright.apollo.handler;

import java.util.ArrayList;
import java.util.List;

import com.bright.apollo.service.*;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.bright.apollo.bean.Message;
import com.bright.apollo.cache.AliDevCache;
import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.enums.FingerWarnEnum;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.session.PushObserverManager;
import com.bright.apollo.session.SceneActionThreadPool;
import com.bright.apollo.session.SessionManager;
import com.bright.apollo.util.FingerUtil;

@Component
public abstract class BasicHandler {
	@Autowired
	@Lazy
	protected PushObserverManager pushObserverManager;
	@Autowired
	protected PushService pushservice;
	@Autowired
	protected UserOperationService userOperationService;
	@Autowired
	protected SceneService sceneService;
	@Autowired
	protected SceneActionThreadPool sceneActionThreadPool;
	@Autowired
	protected SceneConditionService sceneConditionService;
	@Autowired
	protected UserService userService;
	@Autowired
	protected UserSceneService userSceneService;
	@Autowired
	protected SceneActionService sceneActionService;
	@Autowired
	protected OboxService oboxService;
	@Autowired
	protected OboxDeviceConfigService oboxDeviceConfigService;
	@Autowired
	protected GroupDeviceService groupDeviceService;
	@Autowired
	protected ServerGroupService serverGroupService;
	@Autowired
	protected UserDeviceService userDeviceService;
	@Autowired
	protected DeviceChannelService deviceChannelService;
	@Autowired
	protected UserOboxService userOboxService;
	@Autowired
	protected CMDMessageService cmdMessageService;
	@Autowired
	protected CmdCache cmdCache;
	@Autowired
	protected SessionManager sessionManager;

	@Autowired
	protected IntelligentFingerService intelligentFingerService;
	@Autowired
	protected FingerUtil fingerUtil;
	@Autowired
	protected TopicServer topicServer;

	@Autowired
	protected MsgService msgService;

	@Autowired
	protected DeviceStatusService deviceStatusService;

	@Autowired
	protected AliDeviceConfigService aliDeviceConfigService;

	@Autowired
	protected AliDeviceService aliDeviceService;
	@Autowired
	protected AliDevCache aliDevCache;
	@Autowired
	@Lazy
	protected TopicServer topServer;
	public abstract Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception;

	protected void deleteSceneNumber(List<TScene> scenes, TObox tObox) {
		for (TScene tScene : scenes) {
			List<TSceneCondition> tSceneConditions = sceneConditionService
					.getSceneConditionBySceneNum(tScene.getSceneNumber());
			if (tSceneConditions != null) {
				sceneConditionService.deleteSceneConditionBySceneNum(tScene.getSceneNumber());
			}
			userSceneService.deleteUserSceneBySceneNum(tScene.getSceneNumber());
			// SceneBusiness.deleteUserScene(tScene.getSceneNumber());
			// SceneBusiness.deleteSceneActionsBySceneNumber(tScene.getSceneNumber());
			sceneActionService.deleteSceneActionBySceneNumber(tScene.getSceneNumber());
			// SceneBusiness.deleteSceneBySceneNumber(tScene.getSceneNumber());
			sceneService.deleteSceneActionBySceneNumber(tScene.getSceneNumber());
			// SceneBusiness.deleteSceneLocationBySceneNumber(tScene.getSceneNumber());
		}
		sceneService.deleteSceneByOboxSerialId(tObox.getOboxSerialId());
		// OboxBusiness.delOboxScenes(tObox.getOboxSerialId());

	}

	public SceneService getSceneService() {
		return sceneService;
	}

	public void setSceneService(SceneService sceneService) {
		this.sceneService = sceneService;
	}

	public SceneConditionService getSceneConditionService() {
		return sceneConditionService;
	}

	public void setSceneConditionService(SceneConditionService sceneConditionService) {
		this.sceneConditionService = sceneConditionService;
	}

	public UserSceneService getUserSceneService() {
		return userSceneService;
	}

	public void setUserSceneService(UserSceneService userSceneService) {
		this.userSceneService = userSceneService;
	}

	public SceneActionService getSceneActionService() {
		return sceneActionService;
	}

	public void setSceneActionService(SceneActionService sceneActionService) {
		this.sceneActionService = sceneActionService;
	}

	public OboxService getOboxService() {
		return oboxService;
	}

	public void setOboxService(OboxService oboxService) {
		this.oboxService = oboxService;
	}

	public OboxDeviceConfigService getOboxDeviceConfigService() {
		return oboxDeviceConfigService;
	}

	public void setOboxDeviceConfigService(OboxDeviceConfigService oboxDeviceConfigService) {
		this.oboxDeviceConfigService = oboxDeviceConfigService;
	}

	public UserDeviceService getUserDeviceService() {
		return userDeviceService;
	}

	public void setUserDeviceService(UserDeviceService userDeviceService) {
		this.userDeviceService = userDeviceService;
	}

	public DeviceChannelService getDeviceChannelService() {
		return deviceChannelService;
	}

	public void setDeviceChannelService(DeviceChannelService deviceChannelService) {
		this.deviceChannelService = deviceChannelService;
	}

	public UserOboxService getUserOboxService() {
		return userOboxService;
	}

	public void setUserOboxService(UserOboxService userOboxService) {
		this.userOboxService = userOboxService;
	}

	public CMDMessageService getCmdMessageService() {
		return cmdMessageService;
	}

	public void setCmdMessageService(CMDMessageService cmdMessageService) {
		this.cmdMessageService = cmdMessageService;
	}

	public SessionManager getSessionManager() {
		return sessionManager;
	}

	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public SceneActionThreadPool getSceneActionThreadPool() {
		return sceneActionThreadPool;
	}

	public void setSceneActionThreadPool(SceneActionThreadPool sceneActionThreadPool) {
		this.sceneActionThreadPool = sceneActionThreadPool;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserOperationService getUserOperationService() {
		return userOperationService;
	}

	public void setUserOperationService(UserOperationService userOperationService) {
		this.userOperationService = userOperationService;
	}

	public CmdCache getCmdCache() {
		return cmdCache;
	}

	public void setCmdCache(CmdCache cmdCache) {
		this.cmdCache = cmdCache;
	}

	public IntelligentFingerService getIntelligentFingerService() {
		return intelligentFingerService;
	}

	public void setIntelligentFingerService(IntelligentFingerService intelligentFingerService) {
		this.intelligentFingerService = intelligentFingerService;
	}

	public FingerUtil getFingerUtil() {
		return fingerUtil;
	}

	public void setFingerUtil(FingerUtil fingerUtil) {
		this.fingerUtil = fingerUtil;
	}

	public TopicServer getTopicServer() {
		return topicServer;
	}

	public void setTopicServer(TopicServer topicServer) {
		this.topicServer = topicServer;
	}

	public MsgService getMsgService() {
		return msgService;
	}

	public void setMsgService(MsgService msgService) {
		this.msgService = msgService;
	}

	public PushService getPushservice() {
		return pushservice;
	}

	public void setPushservice(PushService pushservice) {
		this.pushservice = pushservice;
	}
	
	public PushObserverManager getPushObserverManager() {
		return pushObserverManager;
	}

	public void setPushObserverManager(PushObserverManager pushObserverManager) {
		this.pushObserverManager = pushObserverManager;
	}

	public ServerGroupService getServerGroupService() {
		return serverGroupService;
	}

	public void setServerGroupService(ServerGroupService serverGroupService) {
		this.serverGroupService = serverGroupService;
	}

	public DeviceStatusService getDeviceStatusService() {
		return deviceStatusService;
	}

	public void setDeviceStatusService(DeviceStatusService deviceStatusService) {
		this.deviceStatusService = deviceStatusService;
	}

	public AliDeviceConfigService getAliDeviceConfigService() {
		return aliDeviceConfigService;
	}

	public void setAliDeviceConfigService(AliDeviceConfigService aliDeviceConfigService) {
		this.aliDeviceConfigService = aliDeviceConfigService;
	}

	public AliDeviceService getAliDeviceService() {
		return aliDeviceService;
	}

	public void setAliDeviceService(AliDeviceService aliDeviceService) {
		this.aliDeviceService = aliDeviceService;
	}

	public static List<TIntelligentFingerPush> initPush(String serialId) {
		List<TIntelligentFingerPush> list = new ArrayList<TIntelligentFingerPush>();
		TIntelligentFingerPush push = new TIntelligentFingerPush(serialId, FingerWarnEnum.jimmy.getCmd(),
				FingerWarnEnum.jimmy.getId());
		TIntelligentFingerPush push1 = new TIntelligentFingerPush(serialId, FingerWarnEnum.stress.getCmd(),
				FingerWarnEnum.stress.getId());
		TIntelligentFingerPush push2 = new TIntelligentFingerPush(serialId, FingerWarnEnum.multiple_validation_failed.getCmd(),
				FingerWarnEnum.multiple_validation_failed.getId());
		TIntelligentFingerPush push3 = new TIntelligentFingerPush(serialId, FingerWarnEnum.overdoor.getCmd(),
				FingerWarnEnum.overdoor.getId());
		TIntelligentFingerPush push4 = new TIntelligentFingerPush(serialId, FingerWarnEnum.back_lock.getCmd(),
				FingerWarnEnum.back_lock.getId());
		TIntelligentFingerPush push5 = new TIntelligentFingerPush(serialId, FingerWarnEnum.low_betty.getCmd(),
				FingerWarnEnum.low_betty.getId());
		list.add(push);
		list.add(push1);
		list.add(push2);
		list.add(push3);
		list.add(push4);
		list.add(push5);
		return list;
	}
}
