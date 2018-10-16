package com.bright.apollo.dao.device.mapper;

import com.bright.apollo.common.entity.TAliDevTimer;
import com.bright.apollo.common.entity.TAliDeviceConfig;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AliDeviceConfigMapper {

    @Select("select * from t_ali_device_config where device_serial_id = #{deviceSerialId}")
    @Results(value = {
            @Result(property = "Id",column = "id"),
            @Result(property = "deviceSerialId",column = "device_serial_id"),
            @Result(property = "name",column = "name"),
            @Result(property = "type",column = "type"),
            @Result(property = "action",column = "action"),
            @Result(property = "state",column = "state"),
            @Result(property = "lastOpTime",column = "last_op_time")

    })
    TAliDeviceConfig getAliDeviceConfigByDeviceSerialId(@Param("deviceSerialId") String deviceSerialId);

    @Update(" update t_ali_device_config SET " +
            "    device_serial_id=#{deviceSerialId},\n" +
            "    type=#{type},\n" +
            "    action=#{action},\n" +
            "    state=#{state},\n" +
            "    last_op_time=#{lastOpTime},\n" +
            "    name=#{name} where id = #{id}")
    void update(TAliDeviceConfig aliDeviceConfig);

    @Select(" select tadc.* from t_user_ali_device tuad " +
            " inner join t_ali_device_config tadc on tuad.device_serial_id=tadc.device_serial_id where tuad.user_id = #{userId} ")
    @Results(value = {
            @Result(property = "Id",column = "id"),
            @Result(property = "deviceSerialId",column = "device_serial_id"),
            @Result(property = "name",column = "name"),
            @Result(property = "type",column = "type"),
            @Result(property = "action",column = "action"),
            @Result(property = "state",column = "state"),
            @Result(property = "lastOpTime",column = "last_op_time")

    })
    List<TAliDeviceConfig> getAliDeviceConfigByUserId(@Param("userId")Integer userId);

    @Insert("insert into t_ali_device_config(id,\n" +
            "device_serial_id,\n" +
            "type,\n" +
            "last_op_time,\n" +
            "action,state,name) values(#{Id},#{deviceSerialId},#{type},#{lastOpTime},#{action},#{state},#{name})")
    @Options(useGeneratedKeys=true, keyProperty="Id", keyColumn="id")
    int addAliDeviceConfig(TAliDeviceConfig aliDeviceConfig);
}
