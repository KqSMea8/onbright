package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.dao.user.mapper.TUserSceneMapper;
import com.bright.apollo.service.UserSceneService;

import java.util.List;

public class UserSceneServiceImpl  implements UserSceneService {

    private TUserSceneMapper userSceneMapper;
    @Override
    public void deleteUserSceneBySceneNum(int sceneNumber) {
        userSceneMapper.deleteUserSceneBySceneNum(sceneNumber);
    }

    @Override
    public List<TUserScene> getUserSceneBySceneNum(int sceneNumber) {
        return userSceneMapper.getUserSceneBySceneNum(sceneNumber);
    }
}
