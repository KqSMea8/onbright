package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TUSerGroup;
import com.bright.apollo.dao.device.mapper.TUserGroupMapper;
import com.bright.apollo.service.UserGroupService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月30日  
 *@Version:1.1.0  
 */
@Service
public class UserGroupServiceImpl implements UserGroupService{

	@Autowired
	private TUserGroupMapper mapper;
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.UserGroupService#queryUserGroup(java.lang.Integer)  
	 */
	@Override
	public List<TUSerGroup> queryUserGroup(Integer userId) {
 		return mapper.queryUserGroup(userId);
	}

}
