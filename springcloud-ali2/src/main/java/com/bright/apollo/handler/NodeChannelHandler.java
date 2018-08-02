package com.bright.apollo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.session.ClientSession;

public class NodeChannelHandler extends BasicHandler  {

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        // TODO Auto-generated method stub
        String data = msg.getData();
        String isSuccess = data.substring(0, 2);
        if ("01".equals(isSuccess)) {
            String setting = data.substring(2, 4);
            if ("01".equals(setting)) {
                //call after asking node to broadcast signal to be found by other OBOX channel
            }else if ("02".equals(setting)) {
                //call after set channel to node
            }
        }else if ("03".equals(isSuccess)) {
            //node call back through another OBOX channel
            String intensity = data.substring(4, 6);
            if (!"00".equals(intensity)) {
                String oboxSerial = data.substring(6, 16);
                String rfAddr = data.substring(18, 20);

                //physical OBOX channel
                TObox tObox = oboxService.queryOboxsByOboxSerialId(oboxSerial);
                if (tObox == null) {
                    return null;
                }
                TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.queryOboxConfigByRFAddr(tObox.getOboxId(), rfAddr);

                //receive OBOX channel
                TObox dbObox = oboxService.queryOboxsByOboxSerialId(clientSession.getUid());

//                if (tOboxDeviceConfig != null) {
//                    if (tOboxDeviceConfig.getLicense() != 0) {
//                        if (tOboxDeviceConfig.getLicense() == dbObox.getLicense()) {
//                            log.info(String.format("%s found at %s obox intensity %s", tOboxDeviceConfig.getDeviceSerialId(),dbObox.getOboxSerialId(),intensity));
//                            TDeviceChannel tDeviceChannel = new TDeviceChannel();
//                            tDeviceChannel.setDeviceId(tOboxDeviceConfig.getId());
//                            tDeviceChannel.setOboxId(dbObox.getOboxId());
//                            tDeviceChannel.setSignalIntensity(Integer.parseInt(intensity,16));
//
//                            DeviceBusiness.addDeviceChannel(tDeviceChannel);
//                        }
//                    }else {
//                        log.info(String.format("%s found at %s obox intensity %s", tOboxDeviceConfig.getDeviceSerialId(),dbObox.getOboxSerialId(),intensity));
//                        TDeviceChannel tDeviceChannel = new TDeviceChannel();
//                        tDeviceChannel.setDeviceId(tOboxDeviceConfig.getId());
//                        tDeviceChannel.setOboxId(dbObox.getOboxId());
//                        tDeviceChannel.setSignalIntensity(Integer.parseInt(intensity,16));
//
//                        DeviceBusiness.addDeviceChannel(tDeviceChannel);
//                    }
//                }
            }
        }
        return null;
    }
}
