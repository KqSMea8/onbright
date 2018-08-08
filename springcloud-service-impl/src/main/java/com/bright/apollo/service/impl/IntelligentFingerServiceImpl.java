package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TIntelligentFingerAuth;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerUser;
import com.bright.apollo.dao.device.mapper.TIntelligentDFingerRemoteUserMapper;
import com.bright.apollo.dao.device.mapper.TIntelligentFingerAuthMapper;
import com.bright.apollo.dao.device.mapper.TIntelligentFingerUserMapper;
import com.bright.apollo.dao.device.mapper.TIntelligentFingerWarnMapper;
import com.bright.apollo.dao.device.mapper.TintelligentFingerRecordMapper;
import com.bright.apollo.request.IntelligentFingerWarnDTO;
import com.bright.apollo.request.IntelligentOpenRecordDTO;
import com.bright.apollo.request.IntelligentUserDTO;
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
	@Autowired
	private TIntelligentFingerWarnMapper warnMapper;
	@Autowired
	private TIntelligentFingerUserMapper userMapper;
	@Autowired
	private TIntelligentDFingerRemoteUserMapper remoteMapper;
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
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryCountIntelligentWarnBySerialId(java.lang.String)  
	 */
	@Override
	public Integer queryCountIntelligentWarnBySerialId(String serialId) {
		return warnMapper.queryCountIntelligentWarnBySerialId(serialId);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryIntelligentWarnByDate(java.lang.String, java.lang.String, java.lang.String)  
	 */
	@Override
	public List<IntelligentFingerWarnDTO> queryIntelligentWarnByDate(String serialId, String end, String start) {
		return warnMapper.queryIntelligentWarnByDate(serialId,end,start);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryCountIntelligentUserBySerialId(java.lang.String)  
	 */
	@Override
	public Integer queryCountIntelligentUserBySerialId(String serialId) {
		return userMapper.queryCountIntelligentUserBySerialId(serialId);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryIntelligentUserBySerialId(java.lang.String)  
	 */
	@Override
	public List<TIntelligentFingerUser> queryIntelligentUserBySerialId(String serialId) {
		return userMapper.queryIntelligentUserBySerialId(serialId);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryIntelligentFingerUserBySerialIdAndPin(java.lang.String, java.lang.String)  
	 */
	@Override
	public TIntelligentFingerUser queryIntelligentFingerUserBySerialIdAndPin(String serialId, String pin) {
		return userMapper.queryIntelligentFingerUserBySerialIdAndPin(serialId,pin);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#updatentelligentFingerUser(com.bright.apollo.common.entity.TIntelligentFingerUser)  
	 */
	@Override
	public void updatentelligentFingerUser(TIntelligentFingerUser fingerUser) {
		userMapper.updatentelligentFingerUser(fingerUser);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryIntelligentAuthBySerialId(java.lang.String)  
	 */
	@Override
	public TIntelligentFingerAuth queryIntelligentAuthBySerialId(String serialId) {
		return authMapper.queryIntelligentAuthBySerialId(serialId);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#addIntelligentFingerAuth(com.bright.apollo.common.entity.TIntelligentFingerAuth)  
	 */
	@Override
	public int addIntelligentFingerAuth(TIntelligentFingerAuth auth) {
		return authMapper.addIntelligentFingerAuth(auth);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryIntelligentFingerRemoteUsersBySerialId(java.lang.String)  
	 */
	@Override
	public List<TIntelligentFingerRemoteUser> queryIntelligentFingerRemoteUsersBySerialId(String serialId) {
		return remoteMapper.queryIntelligentFingerRemoteUsersBySerialId(serialId);
	}
 
}
