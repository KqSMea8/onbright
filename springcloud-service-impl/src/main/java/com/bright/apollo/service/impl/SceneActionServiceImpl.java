package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.dao.scene.mapper.TSceneActionMapper;
import com.bright.apollo.service.SceneActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SceneActionServiceImpl implements SceneActionService {

    @Autowired
    private TSceneActionMapper sceneActionMapper;

    @Override
    public void deleteSceneActionBySceneNumber(int sceneNumber) {
        sceneActionMapper.deleteSceneActionBySceneNumber(sceneNumber);
    }

    @Override
    public List<TSceneAction> getSceneActionBySceneNumber(int sceneNumber) {
        return sceneActionMapper.getSceneActionBySceneNumber(sceneNumber);
    }

    @Override
    public void deleteSceneActionByBySceneNumberAndActionId(int sceneNumber, String actionId) {
        sceneActionMapper.deleteSceneActionByBySceneNumberAndActionId(sceneNumber,actionId);
    }

    @Override
    public void addSceneAction(TSceneAction sceneAction) {
        sceneActionMapper.addSceneAction(sceneAction);
    }

    @Override
    public TSceneAction getSceneActionBySceneNumberAndActionId(int sceneNumber, String actionId) {
        return sceneActionMapper.getSceneActionBySceneNumberAndActionId(sceneNumber,actionId);
    }

    @Override
    public void updateSceneAction(TSceneAction sceneAction) {
        sceneActionMapper.updateSceneAction(sceneAction);
    }
}
