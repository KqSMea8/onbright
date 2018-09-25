package com.bright.apollo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.bean.Message;
import com.bright.apollo.session.ClientSession;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年9月25日  
 *@Version:1.1.0  
 */
@Component
public class RemoteLedHandler extends BasicHandler {
	private static final Logger logger = LoggerFactory.getLogger(RemoteLedHandler.class);

	/* (non-Javadoc)  
	 * @see com.bright.apollo.handler.BasicHandler#process(com.bright.apollo.session.ClientSession, com.bright.apollo.bean.Message)  
	 */
	@Override
	public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
		String data = msg.getData();
		String isSuccess = data.substring(0, 2);
		String oboxSerialId = data.substring(0, 10);
		String addr = data.substring(12, 14);
		String cmdType = data.substring(14, 16);
		String cmdValue = data.substring(16, 18);
		String groupAddr = data.substring(18, 20);
		if(isSuccess.equals("01")){
			
		}else{
			logger.warn("===warn msg:"+data);
		}
		return null;
	}
}
