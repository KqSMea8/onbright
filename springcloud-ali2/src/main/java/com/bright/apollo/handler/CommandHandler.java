package com.bright.apollo.handler;

import java.util.HashMap;
import java.util.Map;

import com.bright.apollo.service.TopicServer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bright.apollo.cache.AliDevCache;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import com.bright.apollo.enums.AliCmdTypeEnum;
import com.bright.apollo.enums.AliRegionEnum;
import com.bright.apollo.enums.Command;
import com.bright.apollo.service.AliDeviceService;
import com.zz.common.util.StringUtils;

@Component
public class CommandHandler {
	private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);

    @Autowired
    private AliDevCache aliDevCache;

    @Autowired
    private AliDeviceService aliDeviceService;

    @Autowired
    private UploadHandler uploadHandler;

    public CommandHandler(){
        cmdHandlers = new HashMap<String, AliBaseHandler>();
        cmdHandlers.put(AliCmdTypeEnum.UPLOAD.getCmd(), uploadHandler);
    }

    private Map<String, AliBaseHandler> cmdHandlers;

//    static {
//        cmdHandlers.put(AliCmdTypeEnum.UPLOAD.getCmd(), new UploadHandler());
//    }

//    public static AliBaseHandler getCMDHandler(Command cmd) {
//        return cmdHandlers.get(cmd);
//    }

    public void process(String ProductKey,String DeviceName, String aString) {
        try {
            logger.info("======topic msg=====:key:"+ProductKey+" device:"+DeviceName+" payload"+aString);

            JSONObject object = new JSONObject(aString);
            if (StringUtils.isEmpty(object.getString("command"))) {
                return;
            }
            AliBaseHandler handler = cmdHandlers.get(object.getString("command"));
            if (handler == null) {
                logger.error("not exist this cmd : " + object.getString("command"));
                return;
            }

            String deviceSerialId = aliDevCache.getOboxSerialId(ProductKey, DeviceName);
            if (StringUtils.isEmpty(deviceSerialId)) {
                TAliDevice tAliDevice = aliDeviceService.getAliDeviceByProductKeyAndDeviceName(ProductKey, DeviceName);
//                TAliDevice tAliDevice = AliDevBusiness.queryAliDevByName(ProductKey, DeviceName);
                if (tAliDevice != null) {
                    if (!tAliDevice.getOboxSerialId().equals("available")) {
                        deviceSerialId = tAliDevice.getOboxSerialId();
                        aliDevCache.saveDevInfo(tAliDevice.getProductKey(), tAliDevice.getOboxSerialId(), tAliDevice.getDeviceName(),AliRegionEnum.SOURTHCHINA);
                    }else{
                        return;
                    }
                }else{
                    TAliDeviceUS tAliDeviceUS = aliDeviceService.getAliUSDeviceByProductKeyAndDeviceName(ProductKey, DeviceName);
//                    TAliDeviceUS tAliDeviceUS = AliDevBusiness.queryAliDevUSByName(ProductKey, DeviceName);
                    if (!tAliDeviceUS.getDeviceSerialId().equals("available")) {
                        deviceSerialId = tAliDeviceUS.getDeviceSerialId();
                        aliDevCache.saveDevInfo(tAliDeviceUS.getProductKey(), tAliDeviceUS.getDeviceSerialId(), tAliDeviceUS.getDeviceName(),AliRegionEnum.AMERICA);
                    }else{
                        return;
                    }
                }
            }

            handler.process(deviceSerialId, object);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
}
