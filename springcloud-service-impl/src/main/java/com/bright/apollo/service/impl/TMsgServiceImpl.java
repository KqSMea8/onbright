package com.bright.apollo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bright.apollo.common.entity.TMsg;
import com.bright.apollo.dao.user.mapper.TMsgMapper;
import com.bright.apollo.response.MsgExceptionDTO;
import com.bright.apollo.service.TMsgService;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月18日  
 *@Version:1.1.0  
 */
@Service
public class TMsgServiceImpl implements TMsgService{
	@Autowired
	private TMsgMapper mapper;
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.TMsgService#addMsg(com.bright.apollo.common.entity.TMsg)  
	 */
	@Override
	public void addMsg(TMsg tMsg) {
		mapper.addMsg(tMsg);
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.TMsgService#countMsgList(java.lang.Integer)  
	 */
	@Override
	public Integer countMsgList(Integer userId) {
		 
		return mapper.countMsgList(userId);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.TMsgService#queryMsgExceList(java.lang.Integer, java.lang.Integer, int, int)  
	 */
	@Override
	public List<MsgExceptionDTO> queryMsgExceList(Integer userId, Integer type, int start, int count) {
		if(type==-1){
			return mapper.queryMsgExceList(userId, type,start,count);
		}else{
			return mapper.queryMsgSysList(userId, type,start,count);
		}
		
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.TMsgService#countMsgExceList(java.lang.Integer, java.lang.Integer)  
	 */
	@Override
	public Integer countMsgExceList(Integer userId, Integer type) {
		 
		return mapper.countMsgExceList(userId,type);
	}
	/* (non-Javadoc)  
	 * @see com.bright.apollo.service.TMsgService#updateMsgState(java.lang.Integer, int)  
	 */
	@Override
	public void updateMsgState(Integer id, int statue) {
		mapper.updateMsgState(id,statue);
		
	}

}
