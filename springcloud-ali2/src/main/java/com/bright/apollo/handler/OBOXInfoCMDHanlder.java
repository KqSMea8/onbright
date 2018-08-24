package com.bright.apollo.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.tool.ByteHelper;
import org.springframework.stereotype.Component;

@Component
public class OBOXInfoCMDHanlder extends BasicHandler{

	private static Logger logger = LoggerFactory.getLogger(OBOXInfoCMDHanlder.class);
   
    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {

        String data = msg.getData();
        String isSuccess = data.substring(0, 2);

        if ("01".equals(isSuccess)) {
            TObox obox = oboxService.queryOboxsByOboxSerialId(clientSession.getUid());
            if (obox == null) {
                logger.error(String.format("not found %s obox!", clientSession.getUid()));
                return null;
            }

            if (data.substring(2, 4).equals("02")) {
                String obox_id	= ByteHelper.fromHexAscii(data.substring(4, 20));
                logger.info(String.format("%s obox name as !", clientSession.getUid(),obox_id));
                obox.setOboxName(obox_id);
                oboxService.update(obox);
            }
        }

        return null;
    }
}
