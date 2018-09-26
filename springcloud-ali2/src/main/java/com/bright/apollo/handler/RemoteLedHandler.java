package com.bright.apollo.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TDeviceStatus;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.enums.DeviceTypeEnum;
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
		String oboxSerialId = data.substring(2, 12);
		String addr = data.substring(14, 16);
		String cmdType = data.substring(16, 18);
		String cmdValue = data.substring(18, 20);
		String groupAddr = data.substring(22, 22);
		if(isSuccess.equals("01")){
			TObox obox = oboxService.queryOboxsByOboxSerialId(oboxSerialId);
			if(obox==null){
				logger.info("===the obox is not exist===");
				return null;
			}
			TOboxDeviceConfig device = oboxDeviceConfigService.getTOboxDeviceConfigByDeviceSerialId(oboxSerialId);
			if(cmdType.equals("01")){
				List<TUserObox> tUserOboxs = userOboxService.getUserOboxBySerialId(oboxSerialId);
				//入网/退网
				if(cmdValue.equals("01")){
					//add
					if(device==null){
						device=new TOboxDeviceConfig();
						device.setOboxSerialId(oboxSerialId);
						device.setDeviceSerialId(oboxSerialId);
						device.setDeviceRfAddr(addr);
						device.setOboxId(obox.getId());
						device.setDeviceType(DeviceTypeEnum.remote_led.getValue());
						device.setDeviceChildType(DeviceTypeEnum.remote_child_led.getValue());
						oboxDeviceConfigService.addTOboxDeviceConfig(device);
						if(tUserOboxs!=null&&tUserOboxs.size()>0){
							for(TUserObox tUserObox: tUserOboxs){
								TUserDevice tUserDevice=new TUserDevice();
								tUserDevice.setDeviceSerialId(oboxSerialId);
								tUserDevice.setUserId(tUserObox.getUserId());
								userDeviceService.addUserDevice(tUserDevice);
							}
						}
					}
				}else if(cmdValue.equals("02")){
					if(device==null){
						logger.warn("===delete the remote led is not exist===");
						return null;
					}
					oboxDeviceConfigService.deleteTOboxDeviceConfigById(device.getId());
					userDeviceService.deleteUserDeviceBySerialId(oboxSerialId);
				} 
			}else if(cmdType.equals("03")){
				//control 
				if(device==null){
					logger.warn("===control the remote led is not exist===");
					return null;
				}
				device.setDeviceState(data.substring(16,32));
				oboxDeviceConfigService.updateTOboxDeviceConfig(device);
			}
		}else{
			logger.warn("===warn msg:"+data);
		}
		TDeviceStatus deviceStatus=new TDeviceStatus();
		deviceStatus.setDeviceSerialId(oboxSerialId);
		deviceStatus.setDeviceState(data.substring(16,32));
		deviceStatusService.addDeviceStatus(deviceStatus);
		return null;
	}
}
