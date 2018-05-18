package com.bright.apollo.service;

import com.bright.apollo.common.entity.TSceneCondition;

import java.util.List;

public interface SceneConditionService {

    List<TSceneCondition> getSceneConditionBySceneNum(int sceneNumber);

    void deleteSceneConditionBySceneNum(int sceneNumber);

    void deleteSceneConditionBySceneNumberAndGroup(int sceneNumber,int conditionGroup);

    void addSceneCondition(TSceneCondition sceneCondition);

    List<TSceneCondition> getSceneConditionBySerialId(String serialId);

    List<TSceneCondition> getSceneConditionBySceneNumberAndGroup(int sceneNumber,int conditionGroup);

}
