package com.bright.apollo.handler;

import java.util.List;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.session.ClientSession;

public class SetSearchHandler extends BasicHandler {
 

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        String obox_serial_id = data.substring(0, 10);
        String action = data.substring(10, 12);

        TObox obox = oboxService.queryOboxsByOboxSerialId(obox_serial_id);
        if (obox == null) {
            Message<String>  replyMsg = new Message<String>();
            replyMsg.setData("00"+data);

            return replyMsg;
        }

        byte [] sendbodyBytes = new byte [5];


        if ("00".equals(action)) {
            sendbodyBytes[0] = 0;
            sendbodyBytes[1] = 0;
            sendbodyBytes[2] = 0;
            sendbodyBytes[3] = 0;
            sendbodyBytes[4] = 0;
        }else if ("01".equals(action)) {
            sendbodyBytes[0] = 0x02;
            List<TOboxDeviceConfig> tOboxDeviceConfigs = oboxDeviceConfigService.getAllOboxDeviceConfig("ff");
            int count = tOboxDeviceConfigs.size() + 1;
            sendbodyBytes[1] = (byte)(count & 0x0000ff);
            sendbodyBytes[2] = (byte)((count >> 8) & 0x0000ff);
            sendbodyBytes[3] = (byte)((count >> 16) & 0x0000ff);
            sendbodyBytes[4] = 0x3c;
        }else {
            Message<String>  replyMsg = new Message<String>();
            replyMsg.setData("00"+data);

            return replyMsg;
        }
        OboxResp oboxResp = cmdMessageService.send(obox, CMDEnum.search_new_device, sendbodyBytes);
        Message<String>  replyMsg = new Message<String>();
        replyMsg.setData(oboxResp.getDate());

        return replyMsg;
    }
}
