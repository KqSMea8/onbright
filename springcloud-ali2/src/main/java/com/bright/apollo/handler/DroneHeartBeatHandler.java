package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.service.CMDMessageService;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.tool.ByteHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DroneHeartBeatHandler extends BasicHandler {

    @Autowired
    private OboxService oboxService;

    @Autowired
    private OboxDeviceConfigService oboxDeviceConfigService;

    @Autowired
    private CMDMessageService cmdMessageService;

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        String oboxSerial = data.substring(0, 10);
        String rfAddr = data.substring(12, 14);

        //physical OBOX channel
        TObox tObox = oboxService.queryOboxsByOboxSerialId(oboxSerial);
        if (tObox == null) {
            return null;
        }
        TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(tObox.getOboxId(), rfAddr);

        if (tOboxDeviceConfig != null) {
            if (tOboxDeviceConfig.getDeviceType().equals("10")) {
                //receive OBOX channel
                TObox dbObox = oboxService.queryOboxsByOboxSerialId(clientSession.getUid());

                byte [] body = new byte [14];
                byte [] frontBytes = ByteHelper.hexStringToBytes(data.substring(0, 18));
                System.arraycopy(frontBytes, 0, body, 0, frontBytes.length);
                body[9] = 0x01;
                body[10] = 0x00;
                byte [] backBytes = ByteHelper.hexStringToBytes(data.substring(18, 24));
                System.arraycopy(backBytes, 0, body, 11, backBytes.length);

                cmdMessageService.sendToSession(clientSession, dbObox, CMDEnum.drone_update, body);
            }
        }
        return null;
    }
}
