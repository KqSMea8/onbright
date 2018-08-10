package com.bright.apollo.handler;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bright.apollo.bean.Message;
import com.bright.apollo.cache.CmdCache;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.service.CMDMessageService;
import com.bright.apollo.service.DeviceChannelService;
import com.bright.apollo.service.IntelligentFingerService;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.service.SceneActionService;
import com.bright.apollo.service.SceneConditionService;
import com.bright.apollo.service.SceneService;
import com.bright.apollo.service.TopicServer;
import com.bright.apollo.service.UserDeviceService;
import com.bright.apollo.service.UserOboxService;
import com.bright.apollo.service.UserOperationService;
import com.bright.apollo.service.UserSceneService;
import com.bright.apollo.service.UserService;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.session.SceneActionThreadPool;
import com.bright.apollo.session.SessionManager;
import com.bright.apollo.util.FingerUtil;

@Component
public abstract class BasicHandler {
	 
	protected UserOperationService userOperationService;
	
	protected SceneService sceneService;
	
	protected SceneActionThreadPool sceneActionThreadPool;
	 
	protected SceneConditionService sceneConditionService;

	protected UserService userService; 
	
	protected UserSceneService userSceneService;
	
	protected SceneActionService sceneActionService;

	protected OboxService oboxService;

	protected OboxDeviceConfigService oboxDeviceConfigService;

	protected UserDeviceService userDeviceService;

	protected DeviceChannelService deviceChannelService;

	protected UserOboxService userOboxService;
	
	protected CMDMessageService cmdMessageService;
	
	protected CmdCache cmdCache;
	
	protected SessionManager sessionManager;
	
	protected IntelligentFingerService intelligentFingerService;
	
	protected FingerUtil fingerUtil;
	
	protected TopicServer topicServer; 
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

}
