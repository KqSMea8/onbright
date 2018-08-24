package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.session.ClientSession;
import org.springframework.stereotype.Component;

@Component
public class RemoveOBOXHandler extends BasicHandler {
    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        String obox_serial_id = data.substring(0, 10);

        TObox obox = oboxService.queryOboxsByOboxSerialId(obox_serial_id);
        Message<String>  replyMsg = new Message<String>();

        if (obox == null) {
            replyMsg.setData("00"+data);

            return replyMsg;
        }
//TODO
        ClientSession OBOXclientSession = sessionManager.getClientSession(obox.getOboxSerialId());
        if (OBOXclientSession == null) {
            replyMsg.setData("00"+data);

            return replyMsg;
        }

        byte [] bodyBytes = new byte [1];
        bodyBytes[0] = 00;
        OboxResp oboxResp = cmdMessageService.send(obox, CMDEnum.delete_obox, bodyBytes);
        replyMsg.setData(oboxResp.getDate());

        sceneService.deleteSceneByOboxSerialId(obox.getOboxSerialId());
//        OboxBusiness.delOboxScenes(obox.getOboxSerialId());
        oboxDeviceConfigService.deleteTOboxDeviceConfigByOboxId(obox.getOboxId());
//        OboxBusiness.delOboxDeviceConfigs(obox.getOboxId());
        oboxService.deleteOboxById(obox);
//        OboxBusiness.delObox(obox.getOboxId());

        return replyMsg;
    }
}
