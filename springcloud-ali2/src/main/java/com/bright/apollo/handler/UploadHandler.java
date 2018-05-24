package com.bright.apollo.handler;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.service.AliDeviceConfigService;
import com.zz.common.util.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class UploadHandler extends AliBaseHandler {

    @Autowired
    private AliDeviceConfigService aliDeviceConfigService;

    @Override
    public void process(String deviceSerialId, JSONObject object) throws Exception {
        // TODO Auto-generated method stub

        TAliDeviceConfig tAliDeviceConfig = aliDeviceConfigService.getAliDeviceConfigBySerializeId(deviceSerialId);
//        TAliDeviceConfig tAliDeviceConfig = AliDevBusiness.queryAliDevConfigBySerial(deviceSerialId);
        if (tAliDeviceConfig != null) {
            if (!StringUtils.isEmpty(object.getString("value"))) {
                tAliDeviceConfig.setState(object.getString("value"));
                aliDeviceConfigService.update(tAliDeviceConfig);
            }

        }
    }
}
