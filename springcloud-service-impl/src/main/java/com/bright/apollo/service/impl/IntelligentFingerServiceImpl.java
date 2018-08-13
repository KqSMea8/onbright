package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerAuth;
import com.bright.apollo.common.entity.TIntelligentFingerPush;
import com.bright.apollo.common.entity.TIntelligentFingerRecord;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerUser;
import com.bright.apollo.common.entity.TIntelligentFingerWarn;
import com.bright.apollo.dao.device.mapper.TIntelligentFingerAbandonRemoteUserMapper;
import com.bright.apollo.dao.device.mapper.TIntelligentFingerAuthMapper;
import com.bright.apollo.dao.device.mapper.TIntelligentFingerPushMapper;
import com.bright.apollo.dao.device.mapper.TIntelligentFingerRemoteUserMapper;
import com.bright.apollo.dao.device.mapper.TIntelligentFingerUserMapper;
import com.bright.apollo.dao.device.mapper.TIntelligentFingerWarnMapper;
import com.bright.apollo.dao.device.mapper.TintelligentFingerRecordMapper;
import com.bright.apollo.request.IntelligentFingerWarnDTO;
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
	@Autowired
	private TIntelligentFingerWarnMapper warnMapper;
	@Autowired
	private TIntelligentFingerUserMapper userMapper;
	@Autowired
	private TIntelligentFingerRemoteUserMapper remoteMapper;
	@Autowired
	private TIntelligentFingerAbandonRemoteUserMapper abandonMapper;
	@Autowired
	private TIntelligentFingerPushMapper pushMapper;
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
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryTIntelligentFingerAbandonRemoteUsersBySerialId(java.lang.String)  
	 */
	@Override
	public List<TIntelligentFingerAbandonRemoteUser> queryTIntelligentFingerAbandonRemoteUsersBySerialId(
			String serialId) {
 		return abandonMapper.queryTIntelligentFingerAbandonRemoteUsersBySerialId(serialId);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#delIntelligentFingerAbandonRemoteUserById(java.lang.Integer)  
	 */
	@Override
	public void delIntelligentFingerAbandonRemoteUserById(Integer id) {
		abandonMapper.delIntelligentFingerAbandonRemoteUserById(id);
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryTIntelligentFingerRemoteUsersBySerialId(java.lang.String)  
	 */
	@Override
	public List<TIntelligentFingerRemoteUser> queryTIntelligentFingerRemoteUsersBySerialId(String serialId) {
	 
		return remoteMapper.queryTIntelligentFingerRemoteUsersBySerialId(serialId);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#addTIntelligentFingerRemoteUser(com.bright.apollo.common.entity.TIntelligentFingerRemoteUser)  
	 */
	@Override
	public Integer addTIntelligentFingerRemoteUser(TIntelligentFingerRemoteUser fingerRemoteUser) {
 		return remoteMapper.addTIntelligentFingerRemoteUser(fingerRemoteUser);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryTintelligentFingerRemoteUserById(int)  
	 */
	@Override
	public TIntelligentFingerRemoteUser queryTintelligentFingerRemoteUserById(int id) {
 		return remoteMapper.queryTintelligentFingerRemoteUserById(id);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#updateTintelligentFingerRemoteUser(com.bright.apollo.common.entity.TIntelligentFingerRemoteUser)  
	 */
	@Override
	public void updateTintelligentFingerRemoteUser(TIntelligentFingerRemoteUser fingerRemoteUser) {
		remoteMapper.updateTintelligentFingerRemoteUser(fingerRemoteUser);
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#addIntelligentFingerAbandonRemoteUser(com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser)  
	 */
	@Override
	public Integer addIntelligentFingerAbandonRemoteUser(TIntelligentFingerAbandonRemoteUser abandonRemoteUser) {  
		return abandonMapper.addIntelligentFingerAbandonRemoteUser(abandonRemoteUser);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#delTIntelligentFingerRemoteUserById(int)  
	 */
	@Override
	public void delTIntelligentFingerRemoteUserById(int id) {
		  remoteMapper.delTIntelligentFingerRemoteUserById(id);
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#updateTintelligentFingerAuth(com.bright.apollo.common.entity.TIntelligentFingerAuth)  
	 */
	@Override
	public void updateTintelligentFingerAuth(TIntelligentFingerAuth fingerAuth) {
		authMapper.updateTintelligentFingerAuth(fingerAuth);
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryTIntelligentFingerPushsBySerialId(java.lang.String)  
	 */
	@Override
	public List<TIntelligentFingerPush> queryTIntelligentFingerPushsBySerialId(String serialId) {
		 
		return pushMapper.queryTIntelligentFingerPushsBySerialId(serialId);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryTIntelligentFingerRemoteUserBySerialIdAndPin(java.lang.String, int)  
	 */
	@Override
	public TIntelligentFingerRemoteUser queryTIntelligentFingerRemoteUserBySerialIdAndPin(String serialId, int pin) {
		return remoteMapper.queryTIntelligentFingerRemoteUserBySerialIdAndPin(serialId,pin);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#updateTIntelligentFingerPushMobileBySerialId(java.lang.String, java.lang.String)  
	 */
	@Override
	public void updateTIntelligentFingerPushMobileBySerialId(String mobile, String serialId) {
		pushMapper.updateTIntelligentFingerPushMobileBySerialId(mobile,serialId);
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#updateTIntelligentFingerPushEnableBySerialIdAndValue(java.lang.Integer, java.lang.String, java.lang.Integer)  
	 */
	@Override
	public void updateTIntelligentFingerPushEnableBySerialIdAndValue(Integer enable, String serialId, Integer value) {
		pushMapper.updateTIntelligentFingerPushEnableBySerialIdAndValue(enable,serialId,value);
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#addIntelligentFingerUser(com.bright.apollo.common.entity.TIntelligentFingerUser)  
	 */
	@Override
	public int addIntelligentFingerUser(TIntelligentFingerUser intelligentFingerUser) {
		 
		return userMapper.addIntelligentFingerUser(intelligentFingerUser);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#addIntelligentFingerRecord(com.bright.apollo.common.entity.TIntelligentFingerRecord)  
	 */
	@Override
	public int addIntelligentFingerRecord(TIntelligentFingerRecord fingerRecord) {
		
		return recordMapper.addIntelligentFingerRecord(fingerRecord);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#deleteIntelligentFingerUserById(java.lang.Integer)  
	 */
	@Override
	public void deleteIntelligentFingerUserById(Integer id) {
		userMapper.deleteIntelligentFingerUserById(id);
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#deleteIntelligentFingerRecordBySerialIdAndFingerUserId(java.lang.String, java.lang.Integer)  
	 */
	@Override
	public void deleteIntelligentFingerRecordBySerialIdAndFingerUserId(String serialId, Integer fingerUserId) {
		recordMapper.deleteIntelligentFingerRecordBySerialIdAndFingerUserId(serialId,fingerUserId);
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#queryTIntelligentFingerPushBySerialIdAndCmd(java.lang.String, java.lang.String)  
	 */
	@Override
	public TIntelligentFingerPush queryTIntelligentFingerPushBySerialIdAndCmd(String serialId, String cmd) {
		return pushMapper.queryTIntelligentFingerPushBySerialIdAndCmd(serialId,cmd);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.IntelligentFingerService#addTIntelligentFingerWarn(com.bright.apollo.common.entity.TIntelligentFingerWarn)  
	 */
	@Override
	public int addTIntelligentFingerWarn(TIntelligentFingerWarn fingerWarn) {
		return warnMapper.addTIntelligentFingerWarn(fingerWarn);
	}
 
}
