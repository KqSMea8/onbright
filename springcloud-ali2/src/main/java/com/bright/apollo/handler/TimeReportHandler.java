package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.session.ClientSession;
import org.springframework.stereotype.Component;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月10日  
 *@Version:1.1.0  
 */

@Component
public class TimeReportHandler extends BasicHandler {
	  
	/* (non-Javadoc)  
	 * @see com.bright.apollo.socket.handler.BasicHandler#process(com.bright.apollo.socket.session.ClientSession, com.bright.apollo.socket.bean.Message)  
	 */
	@Override
	public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
		String data = msg.getData();
		String oboxSerialId = data.substring(0, 10);
		String addr = data.substring(12, 14);
		String uploadTag = data.substring(14, 16);
		String state = data.substring(16, 32);  
		TObox tObox =oboxService.queryOboxsByOboxSerialId(oboxSerialId);
		if (tObox != null) {
			TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(tObox.getOboxId(), addr);
 			if(tOboxDeviceConfig!=null){
				tOboxDeviceConfig.setDeviceState(state);
				oboxDeviceConfigService.updateTOboxDeviceConfig(tOboxDeviceConfig);
				//OboxBusiness.updateOboxDeviceConfig(tOboxDeviceConfig);
			}
		}
		return null;
	}

}
