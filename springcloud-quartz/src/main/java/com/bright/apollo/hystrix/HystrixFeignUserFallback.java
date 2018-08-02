package com.bright.apollo.hystrix;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.common.entity.TCreateTableLog;
import com.bright.apollo.common.entity.TCreateTableSql;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.common.entity.TUserOperation;
import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.feign.FeignUserClient;
import com.bright.apollo.response.ResponseObject;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月25日  
 *@Version:1.1.0  
 */
@Component
public class HystrixFeignUserFallback extends BasicHystrixFeignFallback implements FeignUserClient  {
	private static final Logger logger = LoggerFactory.getLogger(HystrixFeignUserFallback.class);
	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#register(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject register(String mobile) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#forget(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject forget(String mobile) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#deleteUserOboxByOboxSerialId(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteUserOboxByOboxSerialId(String serialId) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#deleteUserDeviceBySerialId(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteUserDeviceBySerialId(String serialId) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#deleteUserSceneBySceneNumber(java.lang.Integer)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject deleteUserSceneBySceneNumber(Integer sceneNumber) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}
 
	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#getUser(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject<TUser> getUser(String username) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	@Override
	public ResponseObject<TUser> getUserById(Integer id) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#addOauthClientDetails(com.bright.apollo.common.entity.OauthClientDetails)  
	 */
	@Override
	public ResponseObject<OauthClientDetails> addOauthClientDetails(OauthClientDetails oauthClientDetails) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#addUserObox(com.bright.apollo.common.entity.TUserObox)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addUserObox(TUserObox tUserObox) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#addUserDevice(com.bright.apollo.common.entity.TUserDevice)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addUserDevice(TUserDevice tUserDevice) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#addUserScene(com.bright.apollo.common.entity.TUserScene)  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseObject<TUserScene> addUserScene(TUserScene tUserScene) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#getUserDevcieByUser(java.lang.Integer)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject<List<TUserDevice>> getUserDevcieByUser(Integer userId) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#getUserObox(java.lang.Integer, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject<TUserObox> getUserObox(Integer userId, String oboxSerialId) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#getUserDevcieByUserIdAndSerialId(java.lang.Integer, java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject<TUserDevice> getUserDevcieByUserIdAndSerialId(Integer userId, String deviceSerialId) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#getUserOperation(java.lang.Long, java.lang.Long, java.lang.String, java.lang.Integer, java.lang.Integer)  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseObject<List<TUserOperation>> getUserOperation(Long fromDate, Long toDate, String serialId,
			Integer startIndex, Integer countIndex) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#queryUserOperationByDate(long, long, java.lang.String)  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseObject<List<TUserOperation>> queryUserOperationByDate(long from, long to, String serialId) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#queryUserOperationByMonth(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseObject<List<TUserOperation>> queryUserOperationByMonth(String tableName, String serialId,
			String day) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#queryUserOperationByMonthDayList(java.lang.String)  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseObject<List<TUserOperation>> queryUserOperationByMonthDayList(String tableName) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#listCreateTableLogByNameWithLike(java.lang.String)  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseObject<List<TCreateTableLog>> listCreateTableLogByNameWithLike(String tUserOperationSuffix) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#queryUserOperation(java.lang.String, java.lang.String)  
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseObject<List<TUserOperation>> queryUserOperation(String name, String serialId) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#dropTable(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject dropTable(String tableName) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#createTable(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject createTable(String createTableSql) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#queryTCreateTableSqlByprefix(java.lang.String)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject<TCreateTableSql> queryTCreateTableSqlByprefix(String prefix) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#addCreateTableLog(com.bright.apollo.common.entity.TCreateTableLog)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addCreateTableLog(TCreateTableLog tCreateTableLog) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}
}

