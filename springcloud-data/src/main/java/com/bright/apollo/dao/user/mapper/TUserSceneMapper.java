package com.bright.apollo.dao.user.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import com.bright.apollo.common.entity.TUserScene;
import com.bright.apollo.common.entity.TUserSceneExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TUserSceneMapper extends BaseMapper<TUserScene, TUserSceneExample, Integer> {

    @Delete("delete from t_user_scene where scene_number = #{sceneNumber}")
    void deleteUserSceneBySceneNum(@Param("sceneNumber") int sceneNumber);

    @Select("select * from t_user_scene where scene_number = #{sceneNumber}")
    List<TUserScene> getUserSceneBySceneNum(@Param("sceneNumber") int sceneNumber);
}