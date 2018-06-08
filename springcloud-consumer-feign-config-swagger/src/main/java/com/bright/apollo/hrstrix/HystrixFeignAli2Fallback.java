package com.bright.apollo.hrstrix;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.feign.FeignAliClient;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年3月16日
 * @Version:1.1.0
 */
@Component
public class  HystrixFeignAli2Fallback extends BasicHystrixFeignFallback implements FeignAliClient {
    private Logger logger = Logger.getLogger(getClass());

    @SuppressWarnings("unchecked")
	@Override
    public ResponseObject<OboxResp> toAliService(CMDEnum cmd, String inMsg, String deviceSerial) {
        logger.warn("===device server is break===");
        return serverError();
    } 

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#releaseObox(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> releaseObox(String oboxSerialId) {
		logger.warn("===device server is break===");
        return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#stopScan(java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> stopScan(String oboxSerialId) {
		logger.warn("===device server is break===");
        return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#scanByRestart(java.lang.String, java.lang.String, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> scanByRestart(String oboxSerialId, String deviceType, String deviceChildType,
			String serialId) {
		logger.warn("===device server is break===");
        return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#scanByUnStop(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> scanByUnStop(String oboxSerialId, String deviceType, String deviceChildType,
			String serialId, Integer countOfDevice) {
		logger.warn("===device server is break===");
        return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#scanByInitiative(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)  
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject<OboxResp> scanByInitiative(String oboxSerialId, String deviceType, String deviceChildType,
			String serialId, Integer countOfDevice) {
		logger.warn("===device server is break===");
        return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#controlServerScene(java.lang.Integer)  
	 */
	@Override
	public ResponseObject controlServerScene(Integer sceneNumber) {
		logger.warn("===device server is break===");
        return serverError();
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignAliClient#setDeviceStatus(java.lang.String, java.lang.String)  
	 */
	@Override
	public ResponseObject<OboxResp> setDeviceStatus(String oboxSerialId, String status) {
		logger.warn("===device server is break===");
        return serverError();
	}
}