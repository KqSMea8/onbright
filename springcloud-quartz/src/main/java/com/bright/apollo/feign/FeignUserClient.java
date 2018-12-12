package com.bright.apollo.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bright.apollo.common.entity.OauthClientDetails;
import com.bright.apollo.common.entity.TCreateTableLog;
import com.bright.apollo.common.entity.TCreateTableSql;
import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.common.entity.TUserOperation;
import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.config.FeignConfig;
import com.bright.apollo.hystrix.HystrixFeignUserFallback;
import com.bright.apollo.response.ResponseObject;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月25日
 * @Version:1.1.0
 */
@FeignClient(name = "springcloud-provider-user", fallback = HystrixFeignUserFallback.class, configuration = FeignConfig.class)
public interface FeignUserClient {
	/**
	 * @param createTableSql
	 * @Description:
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/createTable/{createTableSql}", method = RequestMethod.POST)
	public ResponseObject createTable(@PathVariable(required = true, value = "createTableSql") String createTableSql);
/**  
	 * @param tCreateTableLog  
	 * @Description:  
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/user/addCreateTableLog", method = RequestMethod.POST)
	public ResponseObject addCreateTableLog(@RequestBody TCreateTableLog tCreateTableLog);
		/**
	 * @param prefix
	 * @return
	 * @Description:
	 */
	@RequestMapping(value = "/user/queryTCreateTableSqlByprefix/{prefix}", method = RequestMethod.GET)
	public ResponseObject<TCreateTableSql> queryTCreateTableSqlByprefix(
			@PathVariable(required = true, value = "prefix") String prefix);
}