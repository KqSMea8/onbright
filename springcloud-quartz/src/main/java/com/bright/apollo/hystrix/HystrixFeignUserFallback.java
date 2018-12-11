package com.bright.apollo.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TCreateTableLog;
import com.bright.apollo.common.entity.TCreateTableSql;
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
	 * @see com.bright.apollo.feign.FeignUserClient#addCreateTableLog(com.bright.apollo.common.entity.TCreateTableLog)  
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public ResponseObject addCreateTableLog(TCreateTableLog tCreateTableLog) {
		logger.warn("===user server is break===");
		ResponseObject res = serverError();
		return res;
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.feign.FeignUserClient#queryTCreateTableSqlByprefix(java.lang.String)  
	 */
	@Override
	public ResponseObject<TCreateTableSql> queryTCreateTableSqlByprefix(String prefix) {
		logger.warn("===user server is break===");
		@SuppressWarnings("rawtypes")
		ResponseObject res = serverError();
		return res;
	}
}