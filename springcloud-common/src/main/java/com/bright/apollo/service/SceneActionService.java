package com.bright.apollo.service;

import com.bright.apollo.common.entity.TSceneAction;

import java.util.List;

public interface SceneActionService {

    void deleteSceneActionBySceneNumber(int sceneNumber);

    List<TSceneAction> getSceneActionBySceneNumber(int sceneNumber);

    void deleteSceneActionByBySceneNumberAndActionId(int sceneNumber,String actionId);

    void addSceneAction(TSceneAction sceneAction);

    TSceneAction getSceneActionBySceneNumberAndActionId(int sceneNumber,String actionId);

    void updateSceneAction(TSceneAction sceneAction);
}
