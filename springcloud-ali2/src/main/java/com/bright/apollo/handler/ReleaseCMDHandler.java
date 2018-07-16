package com.bright.apollo.handler;

import java.util.List;

import org.apache.log4j.Logger;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.session.ClientSession;

public class ReleaseCMDHandler extends BasicHandler {

    private Logger log = Logger.getLogger(ReleaseCMDHandler.class);

 


    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        String isSuccess = data.substring(0, 2);
        String oboxSerialId = data.substring(2, 12);
        String type = data.substring(16, 18);

        TObox tObox = oboxService.queryOboxsByOboxSerialId(oboxSerialId);
        if (tObox != null) {
            if ("01".equals(isSuccess)) {
                if (type.equals("03") || type.equals("01")) {
                    if (tObox.getOboxSerialId().equals(oboxSerialId)) {
                        List<TOboxDeviceConfig> tOboxDeviceConfigs = oboxDeviceConfigService.getOboxDeviceConfigByOboxId(tObox.getOboxId());

                        for (TOboxDeviceConfig tOboxDeviceConfig : tOboxDeviceConfigs) {
//                            if (!tOboxDeviceConfig.getGroupAddr().equals("00")) {
//                                TServerOboxGroup tServerOboxGroup = DeviceBusiness.queryOBOXGroupByAddr(tOboxDeviceConfig.getOboxSerialId(), tOboxDeviceConfig.getGroupAddr());
//                                if (tServerOboxGroup != null) {
//                                    TServerGroup tServerGroup = DeviceBusiness.querySererGroupById(tServerOboxGroup.getServerId());
//                                    if (tServerGroup != null) {
//                                        DeviceBusiness.deleteServerGroup(tServerGroup);
//                                    }
//
//                                    DeviceBusiness.deleteOBOXGroupByAddr(tOboxDeviceConfig.getOboxSerialId(), tOboxDeviceConfig.getGroupAddr());
//                                }
//                            }
//                            DeviceBusiness.deleteDeviceGroup(tOboxDeviceConfig.getOboxId());
//                            DeviceBusiness.deleteUserDeviceByDeviceId(tOboxDeviceConfig.getOboxId());
//                            DeviceBusiness.delDeviceChannel(tOboxDeviceConfig.getOboxId());

                        }
                        
                        oboxDeviceConfigService.deleteTOboxDeviceConfig(tObox.getOboxId());
//                        OboxBusiness.batchDelIrKeyByOboxSerialId(tObox.getOboxSerialId());
                        List<TScene> scenes = sceneService.getSceneByOboxSerialId(tObox.getOboxSerialId());
                        if (scenes != null) {
                            super.deleteSceneNumber(scenes,tObox);
                        }

                    }else {
//                        TKey tKey = OboxBusiness.queryKeyByID(tObox.getLicense());
//                        if (tKey != null) {
//                            if (tKey.getServerAddr().equals(oboxSerialId)) {
//                                //release all device at server
//                                List<TObox> tOboxs = OboxBusiness.queryOBOXByLicense(tObox.getLicense());
//                                for (TObox obox : tOboxs) {
//                                    List<TOboxDeviceConfig> tOboxDeviceConfigs = OboxBusiness.queryOboxConfigs(obox.getOboxId());
//                                    for (TOboxDeviceConfig tOboxDeviceConfig : tOboxDeviceConfigs) {
//                                        TServerOboxGroup tServerOboxGroup = DeviceBusiness.queryOBOXGroupByAddr(tOboxDeviceConfig.getGroupAddr(), tOboxDeviceConfig.getOboxSerialId());
//                                        if (tServerOboxGroup != null) {
//                                            TServerGroup tServerGroup = DeviceBusiness.querySererGroupById(tServerOboxGroup.getServerId());
//                                            if (tServerGroup != null) {
//                                                DeviceBusiness.deleteServerGroup(tServerGroup);
//                                            }
//                                            DeviceBusiness.deleteOBOXGroupByAddr(tOboxDeviceConfig.getOboxSerialId(), tOboxDeviceConfig.getGroupAddr());
//                                        }
//                                        DeviceBusiness.deleteDeviceGroup(tOboxDeviceConfig.getOboxId());
//                                        DeviceBusiness.deleteUserDeviceByDeviceId(tOboxDeviceConfig.getOboxId());
//                                        DeviceBusiness.delDeviceChannel(tOboxDeviceConfig.getOboxId());
//
//                                    }
//
//                                    OboxBusiness.delOboxDeviceConfigs(obox.getOboxId());
//                                    OboxBusiness.batchDelIrKeyByOboxSerialId(obox.getOboxSerialId());
//                                    List<TScene> scenes = OboxBusiness.queryOboxScenes(tObox.getOboxSerialId());
//                                    if (scenes != null) {
//                                        for (TScene tScene : scenes) {
//                                            List<TSceneCondition> tSceneConditions = SceneBusiness.querySceneConditionsBySceneNumber(tScene.getSceneNumber());
//                                            if (tSceneConditions != null) {
//                                                SceneBusiness.deleteSceneConditionBySceneNumber(tScene.getSceneNumber());
//                                            }
//
//                                            SceneBusiness.deleteUserScene(tScene.getSceneNumber());
//                                            SceneBusiness.deleteSceneActionsBySceneNumber(tScene.getSceneNumber());
//                                            SceneBusiness.deleteSceneBySceneNumber(tScene.getSceneNumber());
//                                            SceneBusiness.deleteSceneLocationBySceneNumber(tScene.getSceneNumber());
//                                        }
//                                        OboxBusiness.delOboxScenes(tObox.getOboxSerialId());
//                                    }
//                                }
//                            }
//                        }
                    }

                }else if (type.equals("04")) {
                    //reset control pw
                }
            }
        }
        return null;
    }
}
