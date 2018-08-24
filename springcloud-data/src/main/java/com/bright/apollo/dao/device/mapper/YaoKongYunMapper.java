package com.bright.apollo.dao.device.mapper;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.common.entity.TYaoKongYunBrand;
import com.bright.apollo.common.entity.TYaokonyunDevice;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface YaoKongYunMapper {

    @Select("select * from t_yaokonyun_brand where b_id = #{tId}")
    @Results(value = {
            @Result(property = "lastOpTime",column = "last_op_time"),
            @Result(property = "bId",column = "b_id"),
            @Result(property = "name",column = "name"),
            @Result(property = "deviceType",column = "device_type"),
            @Result(property = "common",column = "common")
    })
    List<TYaoKongYunBrand> getYaoKongYunByTId(@Param("tId") Integer tId);


    @Select("select * from t_yaokonyun_brand where b_id = #{tId} and device_type = #{deviceType}")
    @Results(value = {
            @Result(property = "lastOpTime",column = "last_op_time"),
            @Result(property = "bId",column = "b_id"),
            @Result(property = "name",column = "name"),
            @Result(property = "deviceType",column = "device_type"),
            @Result(property = "common",column = "common")
    })
    List<TYaoKongYunBrand> getYaoKongYunByTIdAndDeviceType(@Param("tId") String tId,@Param("deviceType") String deviceType);

    @Select("select appid,device_id,use_time from t_yaokonyun_device where use_time<50 ORDER BY last_op_time limit 0,1")
    @Results(value = {
            @Result(property = "lastOpTime",column = "last_op_time"),
            @Result(property = "useTime",column = "use_time"),
            @Result(property = "deviceId",column = "device_id"),
            @Result(property = "appid",column = "appId")
    })
    TYaokonyunDevice getYaoKongYunDevice();


    @Update(" update t_yaokonyun_device set use_time = 50 where id = #{deviceId}")
    void updateYaoKongYunDevice(@Param("deviceId") String deviceId);


    @Insert("insert into t_yaokonyun_device(last_op_time,\n" +
            "id,\n" +
            "device_id,\n" +
            "appid,\n" +
            "use_time) values(#{lastOpTime},#{id},#{deviceId},#{appId},#{useTime}")
    void addYaoKongYunDevice(TYaokonyunDevice yaokonyunDevice);

}
