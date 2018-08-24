package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.session.ClientSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VersionCMDHandler extends BasicHandler {
 

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        // TODO Auto-generated method stub
        String data = msg.getData();
        String oboxSerialId = data.substring(4, 14);
        String type = data.substring(2, 4);
        String infoType = data.substring(0, 2);

        TObox dbObox = oboxService.queryOboxsByOboxSerialId(oboxSerialId);
        if (dbObox == null) {
            return null;
        }

        String version = "";
        if (infoType.equals("00")) {
            version = data.substring(18, 34);
        }else if (infoType.equals("02")) {
            version = data.substring(28, 44);
        }else {
            return null;
        }



        if ("00".equals(type)) {
            //obox
            dbObox.setOboxVersion(version);
            oboxService.update(dbObox);

        }else {
            //node
            String addr = data.substring(16, 18);
            TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(dbObox.getOboxId(), addr);
            if (tOboxDeviceConfig != null) {
                tOboxDeviceConfig.setDeviceVersion(version);
                oboxDeviceConfigService.updateTOboxDeviceConfig(tOboxDeviceConfig);
            }else {
                return null;
            }
        }
        return null;
    }
}
