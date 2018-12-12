package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TRemoteLed;
import com.bright.apollo.dao.device.mapper.TRemoteLedMapper;
import com.bright.apollo.service.RemoteLedService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年12月11日  
 *@Version:1.1.0  
 */
@Service
public class RemoteLedServiceImpl implements RemoteLedService{

	@Autowired
	private TRemoteLedMapper mapper;

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.RemoteLedService#getListBySerialId(java.lang.String)  
	 */
	@Override
	public List<TRemoteLed> getListBySerialId(String serialId) {
 		return mapper.getListBySerialId(serialId);
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.RemoteLedService#batchRemoteLeds(java.util.List)  
	 */
	@Override
	public void batchRemoteLeds(List<TRemoteLed> list) {
		mapper.batchRemoteLeds(list);
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.RemoteLedService#batchUpdateRemotes(java.util.List)  
	 */
	@Deprecated
	@Override
	public void batchUpdateRemotes(List<TRemoteLed> updateList) {
		mapper.batchUpdateRemotes(updateList);
		
	}

	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.RemoteLedService#updateRemote(com.bright.apollo.common.entity.TRemoteLed)  
	 */
	@Override
	public void updateRemote(TRemoteLed remoteLed) {
		mapper.updateRemote(remoteLed);
		
	}
}
