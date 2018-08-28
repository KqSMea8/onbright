package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TUserOperation;
import com.bright.apollo.dao.user.mapper.TUserOperationMapper;
import com.bright.apollo.service.UserOperationService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年7月25日  
 *@Version:1.1.0  
 */
@Service
public class UserOperationServiceImpl implements UserOperationService{
	@Autowired
    private TUserOperationMapper userOperationMapper;
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserOperationService#getUserOperation(java.lang.Long, java.lang.Long, java.lang.String, java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public List<TUserOperation> getUserOperation(Long fromDate, Long toDate, String serialId, Integer startIndex,
			Integer countIndex) {
		return userOperationMapper.getUserOperation(fromDate,toDate,serialId,startIndex,countIndex);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserOperationService#queryUserOperationByMonth(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@Override
	public List<TUserOperation> queryUserOperationByMonth(String tableName, String serialId, String day) {
		return userOperationMapper.queryUserOperationByMonth(tableName,serialId,day);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserOperationService#queryUserOperationByMonthDayList(java.lang.String)  
	 */
	@Override
	public List<TUserOperation> queryUserOperationByMonthDayList(String tableName) {
		 
		return userOperationMapper.queryUserOperationByMonthDayList(tableName);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserOperationService#queryUserOperation(java.lang.String, java.lang.String)  
	 */
	@Override
	public List<TUserOperation> queryUserOperation(String name, String serialId) {
		 
		return userOperationMapper.queryUserOperation(name,serialId);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserOperationService#addUserOperation(com.bright.apollo.common.entity.TUserOperation)  
	 */
	@Override
	public int addUserOperation(TUserOperation tUserOperation) {
		 
		return userOperationMapper.addUserOperation(tUserOperation);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserOperationService#queryUserOperationByDate(long, long, java.lang.String)  
	 */
	@Override
	public List<TUserOperation> queryUserOperationByDate(long from, long to, String serialId) {
		return userOperationMapper.getUserOperationByDate(from,to,serialId);
	}

}
