package com.bright.apollo.handler;

import com.bright.apollo.bean.Message;
import com.bright.apollo.common.entity.TObox;
import com.bright.apollo.common.entity.TScene;
import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.service.SceneActionService;
import com.bright.apollo.service.SceneConditionService;
import com.bright.apollo.service.SceneService;
import com.bright.apollo.service.UserSceneService;
import com.bright.apollo.session.ClientSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BasicHandler {
    @Autowired
    private SceneService sceneService;

    @Autowired
    private SceneConditionService sceneConditionService;

    @Autowired
    private UserSceneService userSceneService;

    @Autowired
    private SceneActionService sceneActionService;

    public abstract Message<String> process(ClientSession clientSession, Message<String> msg) throws Exception;

    protected void deleteSceneNumber(List<TScene> scenes,TObox tObox){
        for (TScene tScene : scenes) {
            List<TSceneCondition> tSceneConditions = sceneConditionService.getSceneConditionBySceneNum(tScene.getSceneNumber());
            if (tSceneConditions != null) {
                sceneConditionService.deleteSceneConditionBySceneNum(tScene.getSceneNumber());
            }
            userSceneService.deleteUserSceneBySceneNum(tScene.getSceneNumber());
//                                SceneBusiness.deleteUserScene(tScene.getSceneNumber());
//                                SceneBusiness.deleteSceneActionsBySceneNumber(tScene.getSceneNumber());
            sceneActionService.deleteSceneActionBySceneNumber(tScene.getSceneNumber());
//                                SceneBusiness.deleteSceneBySceneNumber(tScene.getSceneNumber());
            sceneService.deleteSceneActionBySceneNumber(tScene.getSceneNumber());
//                                SceneBusiness.deleteSceneLocationBySceneNumber(tScene.getSceneNumber());
        }
        sceneService.deleteSceneByOboxSerialId(tObox.getOboxSerialId());
//                            OboxBusiness.delOboxScenes(tObox.getOboxSerialId());

    }
}
