package com.bright.apollo.dao.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TUserMapper extends BaseMapper<TUser, TUserExample, Integer> {

    @Select("select * from t_user where id=#{id}")
    TUser getUserById(int id);

    @Select(" select * from t_user tu " +
            " inner join t_user_scene tus on tu.id=tus.user_id " +
            " where scene_number = #{sceneNumber} ")
    List<TUser> getUsersBySceneNumber(int sceneNumber);
}