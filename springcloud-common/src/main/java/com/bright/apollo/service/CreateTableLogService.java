package com.bright.apollo.service;

import java.util.List;

import com.bright.apollo.common.entity.TCreateTableLog;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月25日  
 *@Version:1.1.0  
 */
public interface CreateTableLogService {

	/**  
	 * @param tUserOperationSuffix
	 * @return  
	 * @Description:  
	 */
	List<TCreateTableLog> listCreateTableLogByNameWithLike(String tUserOperationSuffix);

}
