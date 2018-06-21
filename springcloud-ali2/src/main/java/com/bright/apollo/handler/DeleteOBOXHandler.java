package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.service.*;
import com.bright.apollo.session.ClientSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DeleteOBOXHandler extends BasicHandler {

    @Autowired
    private OboxService oboxService;

    @Autowired
    private OboxDeviceConfigService oboxDeviceConfigService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private DeviceChannelService deviceChannelService;

    @Autowired
    private SceneService sceneService;

    @Autowired
    private UserOboxService userOboxService;

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        if (data.substring(0, 2).equals("01")) {
            if (data.substring(2, 4).equals("00")) {
                String oboxId = clientSession.getUid();
                TObox tObox = oboxService.queryOboxById(Integer.parseInt(oboxId));
                if (tObox != null) {
                    List<TOboxDeviceConfig> tOboxDeviceConfigs = oboxDeviceConfigService.getOboxDeviceConfigByOboxId(tObox.getOboxId());
//                    List<TOboxDeviceConfig> tOboxDeviceConfigs = OboxBusiness.queryOboxConfigs(tObox.getOboxId());
                    for (TOboxDeviceConfig tOboxDeviceConfig : tOboxDeviceConfigs) {
                        if (!tOboxDeviceConfig.getGroupAddr().equals("00")) {
//                            DeviceBusiness.deleteOBOXGroupByAddr(tOboxDeviceConfig.getOboxSerialId(), tOboxDeviceConfig.getGroupAddr());
                        }
//                        DeviceBusiness.deleteDeviceGroup(tOboxDeviceConfig.getOboxId());
                        userDeviceService.deleteUserDevice(tOboxDeviceConfig.getDeviceSerialId());
//                        DeviceBusiness.deleteUserDeviceByDeviceId(tOboxDeviceConfig.getOboxId());
                        deviceChannelService.deleteDeviceChannel(tOboxDeviceConfig.getOboxId());
//                        DeviceBusiness.delDeviceChannel(tOboxDeviceConfig.getOboxId());

                    }

                    oboxDeviceConfigService.deleteTOboxDeviceConfigByOboxId(tObox.getOboxId());
//                    OboxBusiness.delOboxDeviceConfigs(tObox.getOboxId());
                    sceneService.deleteSceneByOboxSerialId(tObox.getOboxSerialId());
//                    OboxBusiness.delOboxScenes(tObox.getOboxSerialId());
                    deviceChannelService.delectDeviceChannelByOboxId(tObox.getOboxId());
//                    DeviceBusiness.deleteDeviceChannelByOBOXId(tObox.getOboxId());
                    userOboxService.delectUserOboxByOboxSerialId(tObox.getOboxSerialId());
//                    UserBusiness.deleteUserOBOXByoboxId(tObox.getOboxId());
                    oboxService.deleteOboxById(tObox);
//                    OboxBusiness.delObox(tObox.getOboxId());
                }
            }
        }

        return null;
    }
}
