package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.dao.device.mapper.TIntelligentFingerAuthMapper;
import com.bright.apollo.dao.device.mapper.TintelligentFingerRecordMapper;
import com.bright.apollo.request.IntelligentOpenRecordDTO;
import com.bright.apollo.service.IntelligentFingerService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月6日  
 *@Version:1.1.0  
 */
@Service
public class IntelligentFingerServiceImpl implements IntelligentFingerService{

	@Autowired
	private TIntelligentFingerAuthMapper authMapper;
	@Autowired
	private TintelligentFingerRecordMapper recordMapper;
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#countFingerAuth(java.lang.String)  
	 */
	@Override
	public Integer countFingerAuth(String serialId) {
		 
		return authMapper.countFingerAuth(serialId);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryIntelligentOpenRecordByDate(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@Override
	public List<IntelligentOpenRecordDTO> queryIntelligentOpenRecordByDate(String serialId, String end, String start) {
 		return recordMapper.queryIntelligentOpenRecordByDate(serialId,end,start);
	}

}
