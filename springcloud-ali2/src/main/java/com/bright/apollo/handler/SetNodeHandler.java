package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.service.CMDMessageService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.tool.ByteHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class SetNodeHandler extends BasicHandler{

    @Autowired
    private OboxService oboxService;

    @Autowired
    private CMDMessageService cmdMessageService;

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        String set = data.substring(0, 2);
        String obox_serial_id = data.substring(2, 12);
        String groupAddr = data.substring(12, 14);
        String addr = data.substring(14, 16);
        String name = data.substring(16, 48);
        Message<String>  replyMsg = new Message<String>();
        TObox obox = oboxService.queryOboxsByOboxSerialId(obox_serial_id);
        if (obox == null) {
            replyMsg.setData("00"+data);

            return replyMsg;
        }


        if ("00".equals(set) || "01".equals(set) || "02".equals(set)) {
            byte [] bodyBytes = new byte[24];
            bodyBytes[0] = (byte)Integer.parseInt(set, 16);
            byte [] oboxSerialIdBytes = ByteHelper.hexStringToBytes(obox_serial_id);
            System.arraycopy(oboxSerialIdBytes, 0, bodyBytes, 1, oboxSerialIdBytes.length);
            bodyBytes[6] = (byte)Integer.parseInt(groupAddr, 16);
            bodyBytes[7] = (byte)Integer.parseInt(addr, 16);
            byte [] namebytes = ByteHelper.hexStringToBytes(name);
            System.arraycopy(namebytes, 0, bodyBytes, 8, namebytes.length);
            OboxResp oboxResp = cmdMessageService.send(obox, CMDEnum.update_node_name, bodyBytes);
            replyMsg.setData(oboxResp.getData());

            return replyMsg;

        }else {

            replyMsg.setData("00"+data);

            return replyMsg;
        }
    }
}
