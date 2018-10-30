package com.bright.apollo.hrstrix;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.common.entity.TDeviceStatus;
import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerAuth;
import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerUser;
import com.bright.apollo.common.entity.TNvr;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TYSCamera;
import com.bright.apollo.feign.FeignDeviceClient;
import com.bright.apollo.request.IntelligentFingerWarnDTO;
import com.bright.apollo.request.IntelligentOpenRecordDTO;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Component
public class HystrixFeignDeviceFallback extends BasicHystrixFeignFallback implements FeignDeviceClient {
	private static final Logger logger = LoggerFactory.getLogger(HystrixFeignDeviceFallback.class);

	/* 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignDeviceClient#getDevice(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TOboxDeviceConfig> getDevice(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignDeviceClient#updateDevice(java.lang.String,
	 * com.bright.apollo.common.entity.TOboxDeviceConfig)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public ResponseObject<TOboxDeviceConfig> updateDevice(String serialId, TOboxDeviceConfig device) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignDeviceClient#addDevice(java.lang.String,
	 * com.bright.apollo.common.entity.TOboxDeviceConfig)
	 */
	@SuppressWarnings({"unchecked" })
	@Override
	public ResponseObject<TOboxDeviceConfig> addDevice(String serialId, TOboxDeviceConfig device) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bright.apollo.feign.FeignDeviceClient#delDevice(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject delDevice(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#deleleDeviceByOboxSerialId(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleleDeviceByOboxSerialId(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDeviceByUserAndPage(java.lang.Integer, java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByUserAndPage(Integer userId, Integer pageIndex,
			Integer pageSize) {
		logger.warn("===device server is break===");
		return serverError();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getOboxDeviceConfigByUserId(Integer userId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDevicesByOboxSerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDevicesByOboxSerialId(String oboxSerialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDeviceByUser(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceByUser(Integer userId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getYSCameraBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TYSCamera> getYSCameraBySerialId(String deviceSerialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getNvrByIP(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TNvr> getNvrByIP(String ip) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDeviceTypeByUser(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDeviceTypeByUser(Integer userId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDevciesByUserIdAndType(java.lang.Integer, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TOboxDeviceConfig>> getDevciesByUserIdAndType(Integer userId, String deviceType) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getYSCameraByUserId(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TYSCamera>> getYSCameraByUserId(Integer userId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDeviceByUserAndSerialId(java.lang.Integer, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TOboxDeviceConfig> getDeviceByUserAndSerialId(Integer userId, String serialID) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#countFingerAuth(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<Integer> countFingerAuth(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#queryIntelligentOpenRecordByDate(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<IntelligentOpenRecordDTO>> queryIntelligentOpenRecordByDate(String serialId, String end,
			String start) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getCountIntelligentWarnBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<Integer> getCountIntelligentWarnBySerialId(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getIntelligentWarnByDate(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<IntelligentFingerWarnDTO>> getIntelligentWarnByDate(String serialId, String end,
			String start) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getCountIntelligentUserBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<Integer> getCountIntelligentUserBySerialId(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getIntelligentUserBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TIntelligentFingerUser>> getIntelligentUserBySerialId(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getIntelligentFingerUserBySerialIdAndPin(java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TIntelligentFingerUser> getIntelligentFingerUserBySerialIdAndPin(String serialId,
			String pin) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#updatentelligentFingerUser(com.bright.apollo.common.entity.TIntelligentFingerUser)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject updatentelligentFingerUser(TIntelligentFingerUser fingerUser) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getIntelligentAuthBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TIntelligentFingerAuth> getIntelligentAuthBySerialId(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#addIntelligentFingerAuth(com.bright.apollo.common.entity.TIntelligentFingerAuth)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addIntelligentFingerAuth(TIntelligentFingerAuth auth) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getIntelligentFingerRemoteUsersBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TIntelligentFingerRemoteUser>> getIntelligentFingerRemoteUsersBySerialId(
			String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getTIntelligentFingerAbandonRemoteUsersBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TIntelligentFingerAbandonRemoteUser>> getTIntelligentFingerAbandonRemoteUsersBySerialId(
			String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#delIntelligentFingerAbandonRemoteUserById(java.lang.Integer)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject delIntelligentFingerAbandonRemoteUserById(Integer id) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getTIntelligentFingerRemoteUsersBySerialId(java.lang.String)  
	 */
	@Override
	public ResponseObject<List<TIntelligentFingerRemoteUser>> getTIntelligentFingerRemoteUsersBySerialId(
			String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#addTIntelligentFingerRemoteUser(com.bright.apollo.common.entity.TIntelligentFingerRemoteUser)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<Integer> addTIntelligentFingerRemoteUser(TIntelligentFingerRemoteUser fingerRemoteUser) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getIntelligentFingerRemoteUserById(int)  
	 */
	@Override
	public ResponseObject<TIntelligentFingerRemoteUser> getIntelligentFingerRemoteUserById(int fingerRemoteUserId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#delTIntelligentFingerRemoteUserById(int)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject delTIntelligentFingerRemoteUserById(int id) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#updateTintelligentFingerAuth(com.bright.apollo.common.entity.TIntelligentFingerAuth)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject updateTintelligentFingerAuth(TIntelligentFingerAuth fingerAuth) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getTIntelligentFingerPushsBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TIntelligentFingerPush>> getTIntelligentFingerPushsBySerialId(String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#updateTIntelligentFingerRemoteUser(com.bright.apollo.common.entity.TIntelligentFingerRemoteUser)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject updateTIntelligentFingerRemoteUser(TIntelligentFingerRemoteUser fingerRemoteUser) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getTIntelligentFingerRemoteUserBySerialIdAndPin(java.lang.String, int)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TIntelligentFingerRemoteUser> getTIntelligentFingerRemoteUserBySerialIdAndPin(String serialId,
			int pin) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#updateTIntelligentFingerPushMobileBySerialId(java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject updateTIntelligentFingerPushMobileBySerialId(String mobile, String serialId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#updateTIntelligentFingerPushEnableBySerialIdAndValue(java.lang.Integer, java.lang.String, java.lang.Integer)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject updateTIntelligentFingerPushEnableBySerialIdAndValue(Integer enable, String serialId,
			Integer value) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#delIntelligentFingerAbandonRemoteUserBySerialIdAndPin(java.lang.String, java.lang.Integer)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject delIntelligentFingerAbandonRemoteUserBySerialIdAndPin(String serialId, Integer pin) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDeviceStatusByCount(java.lang.String, int, int)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TDeviceStatus>> getDeviceStatusByCount(String serialId, int start, int count) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDeviceStatusByData(java.lang.String, long, long)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TDeviceStatus>> getDeviceStatusByData(String serialId, long from, long to) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#getDeviceStatusByDataNoGroup(java.lang.String, long, long)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<List<TDeviceStatus>> getDeviceStatusByDataNoGroup(String serialId, long from, long to) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#batchTIntelligentFingerPush(java.util.List, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject batchTIntelligentFingerPush(List<TIntelligentFingerPush> pushList) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#queryAliDevConfigBySerial(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<TAliDeviceConfig> queryAliDevConfigBySerial(String deviceId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#addAliDevConfig(com.bright.apollo.common.entity.TAliDeviceConfig)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addAliDevConfig(TAliDeviceConfig tAliDeviceConfig) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#updateAliDevConfig(com.bright.apollo.common.entity.TAliDeviceConfig)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject updateAliDevConfig(TAliDeviceConfig tAliDeviceConfig) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#queryGroup(java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<Map<String, Object>> queryGroup(Integer userId) {
		logger.warn("===device server is break===");
		return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignDeviceClient#addServerGroup(java.lang.String, java.util.List)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<Map<String, Object>> addServerGroup(String groupName, List<String> mList) {
		logger.warn("===device server is break===");
		return serverError();
	}
}
