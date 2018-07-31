package com.bright.apollo.service;

import com.bright.apollo.common.entity.TCreateTableSql;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月27日  
 *@Version:1.1.0  
 */
public interface CreateTableSqlService {

	/**  
	 * @param prefix
	 * @return  
	 * @Description:  
	 */
	TCreateTableSql queryTCreateTableSqlByprefix(String prefix);

	 

}
