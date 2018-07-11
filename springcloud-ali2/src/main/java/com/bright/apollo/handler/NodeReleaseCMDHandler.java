package com.bright.apollo.handler;

import java.util.List;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.session.ClientSession;

public class NodeReleaseCMDHandler extends BasicHandler{

    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        TObox tObox = oboxService.queryOboxsByOboxSerialId(clientSession.getUid());
        if (tObox != null) {
            if ("01".equals(data.substring(0, 2))) {
//                List<TOboxDeviceConfig> tOboxDeviceConfigs = OboxBusiness.queryOboxConfigs(tObox.getOboxId());
//                for (TOboxDeviceConfig tOboxDeviceConfig : tOboxDeviceConfigs) {
//                    if (!tOboxDeviceConfig.getGroupAddr().equals("00")) {
//                        TServerOboxGroup tServerOboxGroup = DeviceBusiness.queryOBOXGroupByAddr(tOboxDeviceConfig.getGroupAddr(), tOboxDeviceConfig.getOboxSerialId());
//                        if (tServerOboxGroup != null) {
//                            TServerGroup tServerGroup = DeviceBusiness.querySererGroupById(tServerOboxGroup.getServerId());
//                            if (tServerGroup != null) {
//                                DeviceBusiness.deleteServerGroup(tServerGroup);
//                            }
//                            DeviceBusiness.deleteOBOXGroupByAddr(tOboxDeviceConfig.getOboxSerialId(), tOboxDeviceConfig.getGroupAddr());
//                        }
//                    }
//                    DeviceBusiness.deleteDeviceGroup(tOboxDeviceConfig.getOboxId());
//                    DeviceBusiness.deleteUserDeviceByDeviceId(tOboxDeviceConfig.getOboxId());
//                    DeviceBusiness.delDeviceChannel(tOboxDeviceConfig.getOboxId());
//
//                }

//                OboxBusiness.delOboxDeviceConfigs(tObox.getOboxId());
                oboxDeviceConfigService.deleteTOboxDeviceConfigByOboxId(tObox.getOboxId());
//                List<TScene> scenes = OboxBusiness.queryOboxScenes(tObox.getOboxSerialId());
                List<TScene> scenes = sceneService.getSceneByOboxSerialId(tObox.getOboxSerialId());
                if (scenes != null) {
//                    for (TScene tScene : scenes) {
//                        List<TSceneCondition> tSceneConditions = SceneBusiness.querySceneConditionsBySceneNumber(tScene.getSceneNumber());
//                        if (tSceneConditions != null) {
//                            SceneBusiness.deleteSceneConditionBySceneNumber(tScene.getSceneNumber());
//                        }
//
//                        SceneBusiness.deleteUserScene(tScene.getSceneNumber());
//                        SceneBusiness.deleteSceneActionsBySceneNumber(tScene.getSceneNumber());
//                        SceneBusiness.deleteSceneBySceneNumber(tScene.getSceneNumber());
//                        SceneBusiness.deleteSceneLocationBySceneNumber(tScene.getSceneNumber());
//                    }
                    super.deleteSceneNumber(scenes,tObox);
//                    OboxBusiness.delOboxScenes(tObox.getOboxSerialId());
                }

            }
        }
        return null;
    }
}
