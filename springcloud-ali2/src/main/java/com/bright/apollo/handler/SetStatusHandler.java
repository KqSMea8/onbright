package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.dto.OboxResp;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.enums.CMDEnum;
import com.bright.apollo.service.CMDMessageService;
import com.bright.apollo.service.OboxDeviceConfigService;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.tool.ByteHelper;
import org.springframework.beans.factory.annotation.Autowired;

public class SetStatusHandler extends BasicHandler{

    @Autowired
    private OboxService oboxService;

    @Autowired
    private OboxDeviceConfigService oboxDeviceConfigService;

    @Autowired
    private CMDMessageService cmdMessageService;

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        String obox_serial_id = data.substring(0, 10);
        String nodeAddr = data.substring(12, 14);
        String deley = data.substring(28, 30);
        TObox obox = oboxService.queryOboxsByOboxSerialId(obox_serial_id);
        if (obox == null) {
            Message<String>  replyMsg = new Message<String>();
            replyMsg.setData("00"+data);

            return replyMsg;
        }

        TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(obox.getOboxId(),nodeAddr);
//        TOboxDeviceConfig tOboxDeviceConfig = OboxBusiness.queryOboxConfigByAddr(obox.getOboxId(), nodeAddr);
        if (tOboxDeviceConfig == null) {
            Message<String>  replyMsg = new Message<String>();
            replyMsg.setData("00"+data);

            return replyMsg;
        }
        TObox bestOBOXChannel = oboxService.queryOboxsByDeviceChannelId(tOboxDeviceConfig.getOboxId());
//        TObox bestOBOXChannel = DeviceBusiness.queryBestDeviceChannel(tOboxDeviceConfig.getOboxId());
        if (bestOBOXChannel == null) {
            Message<String>  replyMsg = new Message<String>();
            replyMsg.setData("00"+data);

            return replyMsg;
        }
        byte [] bodyBytes = new byte[14];
        byte [] oboxSerildIdBytes = ByteHelper.hexStringToBytes(obox_serial_id);
        System.arraycopy(oboxSerildIdBytes, 0, bodyBytes, 0, oboxSerildIdBytes.length);
        bodyBytes[5] = 0x00;
        bodyBytes[6] = (byte)Integer.parseInt(nodeAddr, 16);
        byte [] stateBytes = ByteHelper.hexStringToBytes(data.substring(14, 28));
        System.arraycopy(stateBytes, 0, bodyBytes, 7, stateBytes.length);

        Message<String>  replyMsg = new Message<String>();
        if ("00".equals(deley)) {
            //TODO
            OboxResp oboxResp = cmdMessageService.send(bestOBOXChannel, CMDEnum.setting_node_status, bodyBytes);
            if (oboxResp.getType() == OboxResp.Type.success) {
                replyMsg.setData(oboxResp.getData());
                return replyMsg;
            }else {
                replyMsg.setData("00"+data);

                return replyMsg;
            }


        }else {
//            ScheduledExecutorService service= Executors.newSingleThreadScheduledExecutor();
//            service.schedule(new setStatusTask(bestOBOXChannel, bodyBytes), Integer.parseInt(deley, 16), TimeUnit.SECONDS);
//            replyMsg.setData("01" + msg.getData());
//            return replyMsg;
        }
        return null;
    }
}
