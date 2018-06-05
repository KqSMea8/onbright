package com.bright.apollo.dao.user.mapper;

import org.apache.ibatis.annotations.*;

import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TUserMapper extends BaseMapper<TUser, TUserExample, Integer> {

    @Select("select * from t_user where id=#{id}")
    @Results(value = {
            @Result(property = "userName",column = "user_name"),
            @Result(property = "openId",column = "open_id"),
            @Result(property = "lastOpTime",column = "last_op_time")
    })
    TUser getUserById(@Param("id") int id);

    @Select(" select * from t_user tu " +
            " inner join t_user_scene tus on tu.id=tus.user_id " +
            " where scene_number = #{sceneNumber} ")
    List<TUser> getUsersBySceneNumber(@Param("sceneNumber") int sceneNumber);

    @Select("select * from t_user where user_name=#{userName}")
    TUser getUserByUserName(@Param("userName") String userName);

    @Select("select * from t_user where open_id=#{openId}")
    TUser getUserByOpenId(@Param("openId") String openId);
}