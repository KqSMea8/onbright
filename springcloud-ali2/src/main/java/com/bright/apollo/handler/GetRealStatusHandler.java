package com.bright.apollo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TDeviceStatus;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.session.ClientSession;
import com.zz.common.exception.AppException;
import com.zz.common.util.StringUtils;

@Component
public class GetRealStatusHandler extends BasicHandler {

	private static final Logger logger = LoggerFactory.getLogger(GetRealStatusHandler.class);
    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        logger.info("=======GetRealStatusHandler start=========");
        logger.info("========msg=======:"+msg.toString());
        String data = msg.getData();
        String oboxSerialId = data.substring(0, 10);
        String state = data.substring(14,28);
        String addr = data.substring(12, 14);
        TObox tObox = oboxService.queryOboxsByOboxSerialId(oboxSerialId);
        TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(tObox.getOboxId(),addr);
//        TOboxDeviceConfig tOboxDeviceConfig = OboxBusiness
//                .queryOboxConfigByAddr(tObox.getOboxId(), addr);
        if(tObox!=null&&tOboxDeviceConfig!=null){
            updateDeviceState(tOboxDeviceConfig, state);
        }
        logger.info("=======GetRealStatusHandler end=========");
        return null;
    }
    
    private void updateDeviceState(TOboxDeviceConfig config,String state) throws AppException {
		if (config != null&&!StringUtils.isEmpty(state)) {
			if (config.getDeviceType().equals(DeviceTypeEnum.sensor.getValue())
					&& config.getDeviceChildType().equals(
							DeviceTypeEnum.sensor_environment.getValue())) {
				if(state.substring(0, 1).equals("0")){
					if(config.getDeviceState().length()>12){
						config.setDeviceState(state.substring(0, state.length()-2)
								+config.getDeviceState().substring(12)  );
					}else{
						config.setDeviceState(state.substring(0, state.length()-2));
					}
				} else  {
					 
					if(config.getDeviceState().length()>12){
						config.setDeviceState(config.getDeviceState().substring(0,12)
								+state.substring(0, state.length()-2) );
					}else{
						config.setDeviceState(config.getDeviceState()+state.substring(0, state.length()-2));
					}	 
					addOrUpdateDeviceState(state, config);
				}
				oboxDeviceConfigService.updateTOboxDeviceConfig(config);
			}else {
				config.setDeviceState(state);
				oboxDeviceConfigService.updateTOboxDeviceConfig(config);
				addOrUpdateDeviceState(state, config);
			}
		}
	}
	private void addOrUpdateDeviceState(String state, TOboxDeviceConfig tOboxDeviceConfig)
			throws AppException {
		TDeviceStatus tDeviceStatus = new TDeviceStatus();
		tDeviceStatus.setDeviceSerialId(tOboxDeviceConfig
				.getDeviceSerialId());
		tDeviceStatus.setDeviceState(state);
		deviceStatusService.addDeviceStatus(tDeviceStatus);
	}
}
