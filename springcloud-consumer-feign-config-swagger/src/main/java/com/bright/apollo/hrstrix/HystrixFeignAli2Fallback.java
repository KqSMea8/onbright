package com.bright.apollo.hrstrix;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.controller.UserController;
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
}