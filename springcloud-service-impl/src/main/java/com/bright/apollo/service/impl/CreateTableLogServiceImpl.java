package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TCreateTableLog;
import com.bright.apollo.dao.user.mapper.TCreateTableLogMapper;
import com.bright.apollo.service.CreateTableLogService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月25日  
 *@Version:1.1.0  
 */
@Service
public class CreateTableLogServiceImpl implements CreateTableLogService{
	@Autowired
	private TCreateTableLogMapper mapper;
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.CreateTableLogService#listCreateTableLogByNameWithLike(java.lang.String)  
	 */
	@Override
	public List<TCreateTableLog> listCreateTableLogByNameWithLike(String tUserOperationSuffix) {
 		return mapper.listCreateTableLogByNameWithLike(tUserOperationSuffix);
	}

}
