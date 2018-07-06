package com.bright.apollo.service.impl;

import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.dao.user.mapper.TUserSceneMapper;
import com.bright.apollo.service.UserSceneService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Service
public class UserSceneServiceImpl  implements UserSceneService {
	@Autowired
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
