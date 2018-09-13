package com.bright.apollo.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.enums.NodeTypeEnum;
import com.bright.apollo.session.ClientSession;
import com.bright.apollo.tool.ByteHelper;
import org.springframework.stereotype.Component;

@Component
public class SceneCMDHandler extends BasicHandler{

	private static Logger logger = LoggerFactory.getLogger(SceneCMDHandler.class);
    @Override
    public Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception {
        String data = msg.getData();
        String isSuccess = data.substring(0, 2);

        TObox dbObox = oboxService.queryOboxsByOboxSerialId(clientSession.getUid());
        if (dbObox == null) {
            logger.error(String.format("not found %s obox!", clientSession.getUid()));
            return null;
        }

        if ("01".equals(isSuccess)) {
            int operte_type = Integer.parseInt(data.substring(4, 6), 16) & 0x0f;
            int scene_number = Integer.parseInt(data.substring(6, 8), 16);
            logger.info("====operte_type:"+operte_type);
            logger.info("====scene_number:"+scene_number);
            if ("01".equals(data.substring(2, 4))) {
                //scene info
                String scene_id	= ByteHelper.fromHexAscii(data.substring(8, 40));

                int sceneStatus = (Integer.parseInt(data.substring(4, 6), 16)>>4) & 0x03;
                TScene scene = null;

                if (operte_type == 0) {
                    //delete scene
                    logger.info("====delete scene====");
                    //scene = sceneService.getTSceneByOboxSerialIdAndSceneNumber(dbObox.getOboxSerialId(), scene_number);
                    scene = sceneService.getTSceneByOboxSerialIdAndOboxSceneNumber(dbObox.getOboxSerialId(), scene_number);
//                    scene = OboxBusiness.querySceneBySNumber(dbObox.getOboxSerialId(), scene_number);
                    if (scene == null) {
                        logger.error(String.format("not found scene_number %s in obox!", scene_number));
                        return null;
                    }
                    sceneService.deleteSceneBySceneNum(scene.getSceneNumber());
                    sceneConditionService.deleteSceneConditionBySceneNum(scene.getSceneNumber());
                    userSceneService.deleteUserSceneBySceneNum(scene.getSceneNumber());
                    sceneActionService.deleteSceneActionBySceneNumber(scene.getSceneNumber());
                   // sceneService.deleteSceneActionBySceneNumber(scene.getSceneNumber());
//                    SceneBusiness.deleteUserScene(scene.getSceneNumber());
//                    SceneBusiness.deleteSceneActionsBySceneNumber(scene.getSceneNumber());
//                    SceneBusiness.deleteSceneBySceneNumber(scene.getSceneNumber());
//                    SceneBusiness.deleteSceneLocationBySceneNumber(scene.getSceneNumber());

                }else if (operte_type == 1) {
                    //add scene
                    logger.info("====add scene====");
                    scene = sceneService.getTSceneByOboxSerialIdAndOboxSceneNumber(dbObox.getOboxSerialId(), scene_number);
                    if (scene != null) {
//                        SceneBusiness.deleteUserScene(scene.getSceneNumber());
                        userSceneService.deleteUserSceneBySceneNum(scene.getSceneNumber());
                    }
                    scene = new TScene();
                    scene.setOboxSerialId(dbObox.getOboxSerialId());
//                    scene.setLicense(dbObox.getLicense());
                    scene.setSceneName(scene_id);
                    scene.setOboxSceneNumber(scene_number);
                    scene.setSceneStatus((byte)sceneStatus);
                    scene.setSceneType("01");
                    String scene_group	= data.substring(40, 42);
                    if (!scene_group.equals("00")) {
                        scene.setSceneGroup(scene_group);
                    }
//                    int ret = OboxBusiness.addOboxScene(scene);
                    int ret = sceneService.addScene(scene);
                    cmdCache.saveAddLocalSceneInfo(scene_id, dbObox.getOboxSerialId(), scene_group,scene_number,ret);
//                    List<TUserObox> tUserOboxs = OboxBusiness.queryUserOboxsByOboxId(dbObox.getOboxId());
//                    for (TUserObox tUserObox : tUserOboxs) {
//                        TUserOboxScene tUserScene = new TUserScene();
//                        tUserScene.setUserId(tUserObox.getUserId());
//                        tUserScene.setSceneNumber(ret);
//                        SceneBusiness.addUserScene(tUserScene);
//                    }

                }else if (operte_type == 2) {
                    //modify scene
                    logger.info("====modify scene====");
                    scene = sceneService.getTSceneByOboxSerialIdAndSceneNumber(dbObox.getOboxSerialId(), scene_number);
                    if (scene == null) {
                        logger.error(String.format("not found scene_number %s in obox!", scene_number));
                        return null;
                    }
                    if (sceneStatus == 2) {
                        //execute
                        List<TSceneAction> sceneActions = sceneActionService.getSceneActionBySceneNumber(scene.getSceneNumber());
                        for (TSceneAction sceneAction : sceneActions) {
                            if (NodeTypeEnum.single.getValue().equals(sceneAction.getNodeType())) {

                                TOboxDeviceConfig config = oboxDeviceConfigService.getTOboxDeviceConfigByDeviceSerialId(sceneAction.getActionid());
                                if (config != null) {
                                    config.setDeviceState(sceneAction.getAction());
                                    oboxDeviceConfigService.updateTOboxDeviceConfig(config);
//                                    OboxBusiness.updateOboxDeviceConfig(config);
                                }
                            } else if (NodeTypeEnum.group.getValue().equals(sceneAction.getNodeType())) {
//                                TServerGroup tServerGroup = DeviceBusiness.querySererGroupById(sceneAction.getActionID());
//                                if (tServerGroup != null) {
//                                    tServerGroup.setGroupState(sceneAction.getAction());
//                                    DeviceBusiness.updateServerGroupName(tServerGroup);
//
//                                    List<TOboxDeviceConfig> tOboxDeviceConfigs = DeviceBusiness.queryDeviceByGroupId(tServerGroup.getId());
//                                    for (TOboxDeviceConfig tOboxDeviceConfig : tOboxDeviceConfigs) {
//                                        tOboxDeviceConfig.setDeviceState(sceneAction.getAction());
//                                        OboxBusiness.updateOboxDeviceConfig(tOboxDeviceConfig);
//                                    }
//                                }
                            }
                        }
                    }else {
                        //modify name
                        scene.setSceneName(scene_id);
                        scene.setSceneStatus((byte)sceneStatus);
                        sceneService.updateScene(scene);
//                        OboxBusiness.updateOboxSceneById(scene);
                    }

                }

	            }else if ("02".equals(data.substring(2, 4))) {
	                //scene condition
	                int index = Integer.parseInt(data.substring(8, 10), 16);
	                int choice = Integer.parseInt(data.substring(4, 6), 16);
	                int condType = Integer.parseInt(data.substring(10, 12), 16);
	                TScene scene = sceneService.getTSceneByOboxSerialIdAndOboxSceneNumber(dbObox.getOboxSerialId(), scene_number);
	                if (scene == null) {
	                    logger.error(String.format("not found scene_number %s in obox!", scene_number));
	                    return null;
	                }
	                sceneConditionService.deleteSceneConditionBySceneNumberAndGroup(scene.getSceneNumber(), index-1);
	//                SceneBusiness.deleteSceneConditionBySceneNumberAndGroup(scene.getSceneNumber(), index-1);
	                for (int i = 0; i < 3; i++) {
	                    int cond = (choice >>i) & 0x01;
	                    if (cond != 0) {
	                        int type = (condType >>(i*2)) & 0x03;
	                        TSceneCondition tSceneCondition = new TSceneCondition();
	                        tSceneCondition.setSceneNumber(scene.getSceneNumber());
	                        tSceneCondition.setConditionGroup(index-1);
	                        tSceneCondition.setCond(data.substring(26 + i*30, 26 + i*30 + 16));
	                        if (type == 1) {
	                            //time
	                        }else if (type == 2) {
	                            //sensor
	                            String SerialString = data.substring(12 + i*30, 12 + i*30 + 10);
	                            String addrString = data.substring(24 + i*30, 24 + i*30 + 2);
	                            TOboxDeviceConfig tOboxDeviceConfig = oboxDeviceConfigService.getTOboxDeviceConfigByDeviceSerialIdAndAddress(SerialString, addrString);
	//                            TOboxDeviceConfig tOboxDeviceConfig = DeviceBusiness.queryDeviceConfigByAddr(SerialString, addrString);
	                            if (tOboxDeviceConfig != null) {
	                                tSceneCondition.setSerialid(tOboxDeviceConfig.getDeviceSerialId());
	                            }
	                        }else if (type == 3) {
	                            //remoter
	                        }
	                        sceneConditionService.addSceneCondition(tSceneCondition);
	//                        SceneBusiness.addSceneCondition(tSceneCondition);
	
	                    }
	                }
	
	            }else if ("03".equals(data.substring(2, 4))) {
                //scene action
                TScene scene = sceneService.getTSceneByOboxSerialIdAndOboxSceneNumber(dbObox.getOboxSerialId(), scene_number);
                if (scene == null) {
                    logger.error(String.format("not found scene_number %s in obox!", scene_number));
                    return null;
                }


                if (data.substring(4, 6).equals("3f")) {
                    sceneActionService.deleteSceneActionBySceneNumber(scene.getSceneNumber());
//                    SceneBusiness.deleteSceneActionsBySceneNumber(scene.getSceneNumber());
                }else {
                    int choice = Integer.parseInt(data.substring(4, 6), 16);
                    for (int i = 0; i < 3; i++) {
                        int cond = (choice >> (i*2)) & 0x03;
                        String action = data.substring(22 + i*30, 22 + i*30 + 16);
                        String SerialString = data.substring(8 + i*30, 8 + i*30 + 10);
                        String groupString = data.substring(18 + i*30, 18 + i*30 + 2);
                        String addrString = data.substring(20 + i*30, 20 + i*30 + 2);
                        logger.info("===cond:"+cond+"===action:"+action+"===SerialString:"+SerialString+"===groupString:"+groupString
                        		+"===addrString:"+addrString
                        		);
                        if (groupString.equals("00")) {
                            TOboxDeviceConfig tOboxDeviceConfig =  oboxDeviceConfigService.getTOboxDeviceConfigByDeviceSerialIdAndAddress(SerialString, addrString);
//                            TOboxDeviceConfig tOboxDeviceConfig = DeviceBusiness.queryDeviceConfigByAddr(SerialString, addrString);
                            if (tOboxDeviceConfig != null) {
                                if (cond == 0) {
                                    //delete
                                    sceneActionService.deleteSceneActionByBySceneNumberAndActionId(scene.getSceneNumber(),tOboxDeviceConfig.getDeviceSerialId());
//                                    SceneBusiness.deleteSceneActionBySceneNumberAndActionId(scene.getSceneNumber(), tOboxDeviceConfig.getId());
                                }else if (cond == 1) {
                                    //add
                                    TSceneAction tSceneAction = new TSceneAction();
                                    tSceneAction.setAction(action);
                                    tSceneAction.setActionid(tOboxDeviceConfig.getDeviceSerialId());
                                    tSceneAction.setSceneNumber(scene.getSceneNumber());
                                    tSceneAction.setNodeType(NodeTypeEnum.single.getValue());
                                    sceneActionService.addSceneAction(tSceneAction);
//                                    SceneBusiness.addSceneAction(tSceneAction);
                                }else if (cond == 2) {
                                	logger.info("===sceneNumer:"+scene.getSceneNumber()+"===serialId:"+tOboxDeviceConfig.getDeviceSerialId());
                                    //modify
                                    TSceneAction tSceneAction =  sceneActionService.getSceneActionBySceneNumberAndActionId(scene.getSceneNumber(),tOboxDeviceConfig.getDeviceSerialId());
//                                    TSceneAction tSceneAction = SceneBusiness.querySceneAction(scene.getSceneNumber(), tOboxDeviceConfig.getId());
                                    if (tSceneAction != null) {
                                        tSceneAction.setAction(action);
//                                        SceneBusiness.updateSceneAction(tSceneAction);
                                        sceneActionService.updateSceneAction(tSceneAction);
                                    }else {
                                        tSceneAction = new TSceneAction();
                                        tSceneAction.setAction(action);
                                        tSceneAction.setActionid(tOboxDeviceConfig.getDeviceSerialId());
                                        tSceneAction.setSceneNumber(scene.getSceneNumber());
//                                        SceneBusiness.addSceneAction(tSceneAction);
                                        sceneActionService.addSceneAction(tSceneAction);
                                    }

                                }
                            }
                        }else {
//                            TServerOboxGroup tServerOboxGroup = DeviceBusiness.queryOBOXGroupByAddr(SerialString, groupString);
//                            if (tServerOboxGroup != null) {
//                                TServerGroup tServerGroup = DeviceBusiness.querySererGroupById(tServerOboxGroup.getServerId());
//                                if (tServerGroup != null) {
//                                    if (cond == 0) {
//                                        //delete
//                                        SceneBusiness.deleteSceneActionBySceneNumberAndActionId(scene.getSceneNumber(), tServerGroup.getId());
//                                    }else if (cond == 1) {
//                                        //add
//                                        TSceneAction tSceneAction = new TSceneAction();
//                                        tSceneAction.setAction(action);
//                                        tSceneAction.setActionID(tServerGroup.getId());
//                                        tSceneAction.setSceneNumber(scene.getSceneNumber());
//                                        tSceneAction.setNodeType(NodeTypeEnum.group.getValue());
//                                        SceneBusiness.addSceneAction(tSceneAction);
//                                    }else if (cond == 2) {
//                                        //modify
//                                        TSceneAction tSceneAction = SceneBusiness.querySceneAction(scene.getSceneNumber(), tServerGroup.getId());
//                                        if (tSceneAction != null) {
//                                            tSceneAction.setAction(action);
//                                            SceneBusiness.updateSceneAction(tSceneAction);
//                                        }else {
//                                            tSceneAction = new TSceneAction();
//                                            tSceneAction.setAction(action);
//                                            tSceneAction.setActionID(tServerGroup.getId());
//                                            tSceneAction.setSceneNumber(scene.getSceneNumber());
//                                            tSceneAction.setNodeType(NodeTypeEnum.group.getValue());
//                                            SceneBusiness.addSceneAction(tSceneAction);
//                                        }
//                                    }
//                                }
//
//                            }
                        }
                    }
                }

            }
        }
        return null;
    }
}
