package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.dao.scene.mapper.TSceneConditionMapper;
import com.bright.apollo.service.SceneConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SceneConditionServiceImpl implements SceneConditionService {
    @Autowired
    private TSceneConditionMapper sceneConditionMapper;
    @Override
    public List<TSceneCondition> getSceneConditionBySceneNum(int sceneNumber) {
        return sceneConditionMapper.getSceneConditionBySceneNum(sceneNumber);
    }

    @Override
    public void deleteSceneConditionBySceneNum(int sceneNumber) {
        sceneConditionMapper.deleteSceneConditionBySceneNum(sceneNumber);
    }

    @Override
    public void deleteSceneConditionBySceneNumberAndGroup(int sceneNumber, int conditionGroup) {
        sceneConditionMapper.deleteSceneConditionBySceneNumberAndGroup(sceneNumber,conditionGroup);
    }

    @Override
    public void addSceneCondition(TSceneCondition sceneCondition) {
        sceneConditionMapper.addSceneCondition(sceneCondition);
    }

    @Override
    public List<TSceneCondition> getSceneConditionBySerialId(String serialId) {
        return sceneConditionMapper.getSceneConditionBySerialId(serialId);
    }

    @Override
    public List<TSceneCondition> getSceneConditionBySceneNumberAndGroup(int sceneNumber, int conditionGroup) {
        return sceneConditionMapper.getSceneConditionBySceneNumberAndGroup(sceneNumber,conditionGroup);
    }
}
