package com.bright.apollo.handler;

import org.apache.log4j.Logger;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.enums.DeviceTypeEnum;
import com.bright.apollo.session.ClientSession;

public class GetRealStatusHandler extends BasicHandler {

    private Logger log = Logger.getLogger(GetRealStatusHandler.class);
    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        log.info("=======GetRealStatusHandler start=========");
        log.info("========msg=======:"+msg.toString());
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
        log.info("=======GetRealStatusHandler end=========");
        return null;
    }
    private void updateDeviceState(TOboxDeviceConfig config,String state){
        if (config != null&&!state.equals("")) {
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
//                    addOrUpdateDeviceState(state, config);
                }
                oboxDeviceConfigService.updateTOboxDeviceConfig(config);
//                OboxBusiness
//                        .updateOboxDeviceConfig(config);
            }else {
                config.setDeviceState(state);
//                OboxBusiness
//                        .updateOboxDeviceConfig(config);
                oboxDeviceConfigService.updateTOboxDeviceConfig(config);
//                addOrUpdateDeviceState(state, config);
            }
        }
    }
//    private void addOrUpdateDeviceState(String state, TOboxDeviceConfig tOboxDeviceConfig)
//            throws AppException {
//        TDeviceStatus tDeviceStatus = new TDeviceStatus();
//        tDeviceStatus.setDeviceSerialId(tOboxDeviceConfig
//                .getDeviceSerialId());
//        tDeviceStatus.setDeviceState(state);
//        DeviceBusiness.addDeviceStatus(tDeviceStatus);
//    }
}
