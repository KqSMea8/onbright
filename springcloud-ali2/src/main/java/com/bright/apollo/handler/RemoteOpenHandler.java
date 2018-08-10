/*package com.bright.apollo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.session.ClientSession;

*//**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月10日  
 *@Version:1.1.0  
 *//*
public class RemoteOpenHandler extends BasicHandler {
	private static final Logger log = LoggerFactory.getLogger(RemoteOpenHandler.class);
	 (non-Javadoc)  
	 * @see com.bright.apollo.socket.handler.BasicHandler#process(com.bright.apollo.socket.session.ClientSession, com.bright.apollo.socket.bean.Message)  
	 
	@Override
	public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
		
		String serialId="";
		String userSerialId="";
		TIntelligentFingerRemoteUser fingerRemoteUser =intelligentFingerService.queryIntelligentFingerUserBySerialIdAndPin(serialId,userSerialId);
		if(fingerRemoteUser!=null&&fingerRemoteUser.getIsEnd()!=1){
			log.info("=====fingerRemoteUser:"+fingerRemoteUser.toString());
			Integer times = fingerRemoteUser.getTimes();
			Integer useTimes = fingerRemoteUser.getUseTimes();
			fingerRemoteUser.setIsEnd(0);
			if(useTimes<times){
				useTimes++;
				fingerRemoteUser.setUseTimes(useTimes);
				if(times==useTimes){
					fingerRemoteUser.setIsEnd(1);
					//fingerRemoteUser.setUserSerialId(0);
					TIntelligentFingerAbandonRemoteUser abandonRemoteUser=new TIntelligentFingerAbandonRemoteUser();
					abandonRemoteUser.setSerialId(userSerialId);
				}
				UserBusiness.updateTintelligentFingerRemote(fingerRemoteUser);
			}	
		}
		//记录远程开锁
		
		return null;
	}

}
*/