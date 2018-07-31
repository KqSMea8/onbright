package com.bright.apollo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TCreateTableSql;
import com.bright.apollo.dao.user.mapper.TCreateTableSqlMapper;
import com.bright.apollo.service.CreateTableSqlService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月27日  
 *@Version:1.1.0  
 */
@Service
public class CreateTableSqlServiceImpl implements CreateTableSqlService{

	@Autowired
	private TCreateTableSqlMapper mapper;

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.CreateTableSqlService#queryTCreateTableSqlByprefix(java.lang.String)  
	 */
	@Override
	public TCreateTableSql queryTCreateTableSqlByprefix(String prefix) {
		 
		return mapper.queryTCreateTableSqlByprefix(prefix);
	}
}
