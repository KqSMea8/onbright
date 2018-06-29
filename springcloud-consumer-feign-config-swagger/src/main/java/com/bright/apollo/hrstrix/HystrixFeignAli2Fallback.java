package com.bright.apollo.hrstrix;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.request.SceneActionDTO;
import com.bright.apollo.request.SceneConditionDTO;
import com.bright.apollo.response.AliDevInfo;
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
	private Logger logger = Logger.getLogger(getClass());

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
		logger.warn("===ali server is break===");
		return serverError();
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
	 * com.bright.apollo.feign.FeignAliClient#setDeviceStatus(java.lang.String,
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> setDeviceStatus(String oboxSerialId, String status) {
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

	 

}