package com.bright.apollo.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.tool.ByteHelper;

public class SetReleaseHandler extends BasicHandler{
    @Autowired
    private OboxService oboxService;

    
    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        String obox_serial_id = data.substring(0, 10);
        TObox obox = oboxService.queryOboxsByOboxSerialId(obox_serial_id);
        if (obox == null) {
            Message<String>  replyMsg = new Message<String>();
            replyMsg.setData("00"+data);

            return replyMsg;
        }


        byte [] bodyBytes = new byte [8];
        byte [] oboxSerialIdBytes = ByteHelper.hexStringToBytes(obox_serial_id);
        System.arraycopy(oboxSerialIdBytes, 0, bodyBytes, 0, oboxSerialIdBytes.length);
        bodyBytes[5] = 0x00;
        bodyBytes[6] = 0x00;
        bodyBytes[7] = 0x03;
        OboxResp oboxResp = cmdMessageService.send(obox, CMDEnum.release_all_devices, bodyBytes);
        Message<String>  replyMsg = new Message<String>();
        replyMsg.setData(oboxResp.getDate());

        return replyMsg;
    }
}
