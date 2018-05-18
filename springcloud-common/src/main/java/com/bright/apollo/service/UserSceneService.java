package com.bright.apollo.service;

import com.bright.apollo.common.entity.TUserScene;

import java.util.List;

public interface UserSceneService {

    void deleteUserSceneBySceneNum(int sceneNumber);

    List<TUserScene> getUserSceneBySceneNum(int sceneNumber);

}
