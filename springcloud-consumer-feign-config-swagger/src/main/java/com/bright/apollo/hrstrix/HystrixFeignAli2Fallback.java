package com.bright.apollo.hrstrix;

import java.util.List;
import java.util.Map;

import com.bright.apollo.common.entity.TAliDevTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.request.SceneActionDTO;
import com.bright.apollo.request.SceneConditionDTO;
import com.bright.apollo.response.AliDevInfo;
import com.bright.apollo.response.ResponseEnum;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Component
public class HystrixFeignAli2Fallback extends BasicHystrixFeignFallback implements FeignAliClient {
	
	private static final Logger logger = LoggerFactory.getLogger(HystrixFeignAli2Fallback.class);
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> toAliService(CMDEnum cmd, String inMsg, String deviceSerial) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.feign.FeignAliClient#releaseObox(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> releaseObox(String oboxSerialId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.feign.FeignAliClient#stopScan(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> stopScan(String oboxSerialId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignAliClient#scanByRestart(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> scanByRestart(String oboxSerialId, String deviceType, String deviceChildType,
			String serialId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignAliClient#scanByUnStop(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> scanByUnStop(String oboxSerialId, String deviceType, String deviceChildType,
			String serialId, Integer countOfDevice) {
		logger.warn("===no respone===");
		ResponseObject<OboxResp> res = new ResponseObject<OboxResp>();
		OboxResp oboxResp = new OboxResp(OboxResp.Type.success);
		res.setStatus(ResponseEnum.AddSuccess.getStatus());
		res.setMessage(ResponseEnum.AddSuccess.getMsg());
		res.setData(oboxResp);
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignAliClient#scanByInitiative(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> scanByInitiative(String oboxSerialId, String deviceType, String deviceChildType,
			String serialId, Integer countOfDevice) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bright.apollo.feign.FeignAliClient#controlServerScene(java.lang.
	 * Integer)
	 */
	@Override
	public ResponseObject controlServerScene(Integer sceneNumber) {
		logger.warn("===ali server is break===");
		return serverError();
	}
 

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignAliClient#addLocalScene(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> addLocalScene(String sceneName, String oboxSerialId, String sceneGroup) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#registAliDev(java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<AliDevInfo> registAliDev(String type, String zone) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#addLocalSceneCondition(java.lang.Integer, java.lang.String, java.util.List)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> addLocalSceneCondition(Integer sceneNumber, String oboxSerialId,
			List<List<SceneConditionDTO>> sceneConditionDTOs) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#addLocalSceneAction(java.util.List, java.lang.Integer, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addLocalSceneAction(List<SceneActionDTO> nodeActionDTOs, Integer sceneNumber,
			String oboxSerialId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#getSearchNewDevice(com.bright.apollo.common.entity.TObox)  
	 */
	@Override
	public ResponseObject<List<Map<String, String>>> getSearchNewDevice(TObox obox) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#setDeviceStatus(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> setDeviceStatus(String oboxSerialId, String status, String rfAddr) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#setLocalScene(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject setLocalScene(Byte sceneStatus, Integer oboxSceneNumber, String sceneName,
			String oboxSerialId, String groupAddr) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#modifyLocalSceneCondition(java.lang.Integer, java.lang.String, java.lang.Integer, java.util.List)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject modifyLocalSceneCondition(Integer oboxSceneNumber, String oboxSerialId, Integer userId,
			List<List<SceneConditionDTO>> sceneConditionDTOs) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#deleteLocalScene(java.lang.Integer, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteLocalScene(Integer oboxSceneNumber, String sceneName, String oboxSerialId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#excuteLocalScene(java.lang.Integer, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject excuteLocalScene(Integer oboxSceneNumber, String sceneName, String oboxSerialId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#addSceneAction(java.lang.Integer)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addSceneAction(Integer sceneNumber) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#excuteLocalScene(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject excuteLocalScene(Integer oboxSceneNumber, String sceneName, String oboxSerialId,
			String sceneStatus) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#modifyDeviceName(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> modifyDeviceName(String oboxSerialId, String name, String address) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#deleteDevice(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> deleteDevice(String oboxSerialId, String address, String deviceName) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject<List<Map<String, String>>> sendLearn2IR(Object object) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#getRealNodeStatus(com.bright.apollo.common.entity.TOboxDeviceConfig)  
	 */
	@Override
	public ResponseObject<OboxResp> getRealNodeStatus(TOboxDeviceConfig deviceConfig) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#sendMessageToFinger(java.lang.String, com.bright.apollo.common.entity.TOboxDeviceConfig, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)  
	 */
	@Override
	public ResponseObject<OboxResp> sendMessageToFinger(String operation,
			String startTime, String endTime, String times, Integer userSerialId, String randomNum, String serialId,
			String address) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#deleteObox(java.lang.String)  
	 */
	@Override
	public ResponseObject<OboxResp> deleteObox(String serialId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#addRemoteLed(java.lang.String)  
	 */
	@Override
	public ResponseObject<OboxResp> addRemoteLed(String serialId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#delRemoteLed(java.lang.String)  
	 */
	@Override
	public ResponseObject<OboxResp> delRemoteLed(String serialId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#controlRemoteLed(java.lang.String, java.lang.String)  
	 */
	@Override
	public ResponseObject<OboxResp> controlRemoteLed(String serialId, String status) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject queryAliDevice(Integer userId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject setAliDevice(Object value, String deviceId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject queryAliDeviceTimer(String deviceId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject setAliCountdown(String deviceId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject setAliTimer(String deviceId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject uploadAliDevice(String deviceName, String productKey, Object config, Integer userId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject getAliDevTimerByDeviceSerialIdAndCountDown(String deviceId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject delAliDevTimerByDeviceSerialId(Integer id) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject addAliDevTimer(TAliDevTimer aliDevTimer) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject updateAliDevTimer(TAliDevTimer aliDevTimer) {
		logger.warn("===ali server is break===");
		return serverError();
	}

 
	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#readAliDevice(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@Override
	public ResponseObject readAliDevice(String functionId, String deviceId) {
		logger.warn("===ali server is break===");
		return serverError();
	
	}
 	@Override
	public ResponseObject delAliDevice(String deviceId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject queryCountDown(String deviceId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject getAliDevTimerByIdAndDeviceId(String deviceId, Integer timerId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject getIrList(String brandId, String deviceType) {
		logger.warn("===ali server is break===");
		return serverError();
	}


	@Override
	public ResponseObject toLearn(String serialId, String timeOut, Integer index, String keyOrName, String learnKeyType) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject modifyIR(String serialId, Object irProgram) {
		logger.warn("===ali server is break===");
		return serverError();
	}


	@Override
	public ResponseObject controllIR(String serialId, Integer index, String key) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject getIrTypeList() {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject getIrBrandList(String deviceType) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject bindIrRemotecode(String brandId, String deviceType, String remoteId, String name, String serialId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject deleteIrDevice(String serialId, String index) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject deleteIrDeviceKey(String serialId, String index, String key, String keyType) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject pairIrRemotecode(String brandId, String serialId, Integer timeout) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject learnIrDeviceKey(String serialId, String index, String keyType, String key, String timeout) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject queryIrDevice(String serialId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject renameIrDevice(String serialId, String index, String name) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject createIrDevice(String serialId, String deviceType, String name, String brandId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject getUserIRDevice(Integer userId) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject getIRDeviceByIndex(Integer index) {
		logger.warn("===ali server is break===");
		return serverError();
	}

	@Override
	public ResponseObject getIRDeviceByIndexAndKey(Integer index, String key) {
		logger.warn("===ali server is break===");
		return serverError();
	}

//	@Override
//	public ResponseObject registAliDevice(String zone, String type) {
//		logger.warn("===ali server is break===");
//		return serverError();
//	}


}