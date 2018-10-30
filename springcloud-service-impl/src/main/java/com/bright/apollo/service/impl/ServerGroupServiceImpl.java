package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TServerGroup;
import com.bright.apollo.dao.device.mapper.TServerGroupMapper;
import com.bright.apollo.service.ServerGroupService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月30日  
 *@Version:1.1.0  
 */
@Service
public class ServerGroupServiceImpl implements ServerGroupService{

	@Autowired
	private TServerGroupMapper mapper;
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.ServerGroupService#querySererGroupById(java.lang.Integer)  
	 */
	@Override
	public TServerGroup querySererGroupById(Integer groupId) {
		return mapper.querySererGroupById(groupId);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.ServerGroupService#queryServerGroupByAddr(java.lang.String)  
	 */
	@Override
	public List<TServerGroup> queryServerGroupByAddr(String addr) {
 		return mapper.queryServerGroupByAddr(addr);
	}

}
