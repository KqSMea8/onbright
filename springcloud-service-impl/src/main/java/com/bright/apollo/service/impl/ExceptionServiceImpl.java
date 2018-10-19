package com.bright.apollo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TException;
import com.bright.apollo.dao.user.mapper.ExceptionMapper;
import com.bright.apollo.service.ExceptionService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月19日  
 *@Version:1.1.0  
 */
@Service
public class ExceptionServiceImpl implements ExceptionService{
	@Autowired
	private ExceptionMapper mapper;

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.ExceptionService#addException(com.bright.apollo.common.entity.TException)  
	 */
	@Override
	public int addException(TException tException) {
 		return mapper.addException(tException);
	}
}
