package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.service.OboxService;
import com.bright.apollo.session.ClientSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class ControlPWCMDHandler extends BasicHandler {

    private Logger log = Logger.getLogger(ControlPWCMDHandler.class);

    @Autowired
    private OboxService oboxService;

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {

        String data = msg.getData();
        String isSuccess = data.substring(0, 2);

        if ("01".equals(isSuccess)) {
            TObox obox = oboxService.queryOboxsByOboxSerialId(clientSession.getUid());
            if (obox == null) {
                log.error(String.format("not found %s obox!", clientSession.getUid()));
                return null;
            }

//            obox.setOboxPwd(ByteHelper.fromHexAscii(data.substring(18, 34)));
//            obox.setOboxActivate(1);
            oboxService.update(obox);
        }

        return null;
    }
}
