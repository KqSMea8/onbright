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

import java.util.List;

public class SetChannelHandler extends BasicHandler {
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
        TObox obox = oboxService.queryOboxsByOboxSerialId(obox_serial_id);
        String nodeAddr = data.substring(12, 14);
        Message<String>  replyMsg = new Message<String>();
        if (obox == null) {
            replyMsg.setData("00" + data);

            return replyMsg;
        }
        TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(obox.getId(),nodeAddr);
//        TOboxDeviceConfig tOboxDeviceConfig = OboxBusiness.queryOboxConfigByAddr(obox.getOboxId(), nodeAddr);
        if (tOboxDeviceConfig == null) {

            replyMsg.setData("00" + data);

            return replyMsg;
        }


        byte [] sendbodyBytes = new byte [14];
        sendbodyBytes[0] = (byte)Integer.parseInt("01", 16);
        sendbodyBytes[7] = (byte)Integer.parseInt(tOboxDeviceConfig.getDeviceRfAddr(), 16);
        sendbodyBytes[13] = (byte)Integer.parseInt("1e", 16);
        byte[] oboxSerialByte = ByteHelper.hexStringToBytes(obox.getOboxSerialId());
        System.arraycopy(oboxSerialByte, 0, sendbodyBytes, 1, oboxSerialByte.length);

        byte[] serialByte = ByteHelper.hexStringToBytes(tOboxDeviceConfig.getDeviceSerialId());
        System.arraycopy(serialByte, 0, sendbodyBytes, 8, serialByte.length);

        //TODO
        OboxResp oboxResp = cmdMessageService.send(obox, CMDEnum.setting_channel, sendbodyBytes);
        if (oboxResp.getType() == OboxResp.Type.success) {
            Thread.sleep(400);

            List<TObox> tOboxs = oboxService.getOboxsByDeviceChannel(tOboxDeviceConfig.getId(),tOboxDeviceConfig.getOboxId());
//            List<TObox> tOboxs = DeviceBusiness.queryDeviceChannels(tOboxDeviceConfig.getId(),tOboxDeviceConfig.getOboxId());

            if (tOboxs == null) {
                replyMsg.setData("01" + data);

                return replyMsg;
            }

            if (!tOboxs.isEmpty()) {
                TObox bestChannel = oboxService.queryOboxsByDeviceChannelId(tOboxDeviceConfig.getId());
//                TObox bestChannel = DeviceBusiness.queryBestDeviceChannel(tOboxDeviceConfig.getId());

                byte [] bodyBytes = new byte [23];
                bodyBytes[0] = (byte)Integer.parseInt("02", 16);
                bodyBytes[7] = (byte)Integer.parseInt(tOboxDeviceConfig.getDeviceRfAddr(), 16);
                bodyBytes[13] = (byte)Integer.parseInt("1e", 16);
                System.arraycopy(oboxSerialByte, 0, bodyBytes, 1, oboxSerialByte.length);

                System.arraycopy(serialByte, 0, bodyBytes, 8, serialByte.length);

                for (int i = 0; i < tOboxs.size(); i++) {
                    TObox tmpObox = tOboxs.get(i);
                    byte[] tmpSerialByte = ByteHelper.hexStringToBytes(tmpObox.getOboxSerialId());
                    System.arraycopy(tmpSerialByte, 0, bodyBytes, 13+i*5, tmpSerialByte.length);
                }
                oboxResp = cmdMessageService.send(bestChannel, CMDEnum.setting_channel, bodyBytes);

                if (oboxResp.getType() == OboxResp.Type.success) {
                    replyMsg.setData(oboxResp.getData());

                    return replyMsg;
                }else {
                    replyMsg.setData("01" + data);

                    return replyMsg;
                }
            }
        }else {
            replyMsg.setData(oboxResp.getData());

            return replyMsg;
        }

        replyMsg.setData("00" + data);

        return replyMsg;
    }
}
