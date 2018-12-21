package com.bright.apollo.dao.device.mapper;

import com.bright.apollo.common.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    TYaoKongYunBrand getYaoKongYunByTIdAndDeviceType(@Param("tId") String tId,@Param("deviceType") String deviceType);

    @Select("select * from t_yaokonyun_device where use_time<50 ORDER BY last_op_time limit 0,1")
    @Results(value = {
            @Result(property = "lastOpTime",column = "last_op_time"),
            @Result(property = "useTime",column = "use_time"),
            @Result(property = "deviceId",column = "device_id"),
            @Result(property = "appId",column = "appid")
    })
    TYaokonyunDevice getYaoKongYunDevice();


    @Update(" update t_yaokonyun_device set use_time = 50 where id = #{deviceId}")
    void updateYaoKongYunDevice(@Param("deviceId") String deviceId);


    @Insert("insert into t_yaokonyun_device(last_op_time,\n" +
            "device_id,\n" +
            "appid) values(#{lastOpTime},#{deviceId},#{appId})")
    void addYaoKongYunDevice(TYaokonyunDevice yaokonyunDevice);

    @Select("select key_name,custom_name from t_yaokonyun_key_code where index = #{index}")
    @Results(value = {
            @Result(property = "keyName",column = "key_name"),
            @Result(property = "customName",column = "custom_name")
    })
    List<TYaokonyunKeyCode> getYaoKongKeyCodeByIndex(@Param("index")Integer index);

    @Select("select * from t_yaokonyun_device_type ")
    @Results(value = {
            @Result(property = "tId",column = "t_id"),
            @Result(property = "name",column = "name")
    })
    List<TYaokonyunDeviceType> getYaoKongYunDeviceType();

    @Insert("insert into t_yaokonyun_device_type(last_op_time,\n" +
            "t_id,\n" +
            "name) values(#{lastOpTime},#{tId},#{name})")
    void addTYaokonyunDeviceType(TYaokonyunDeviceType yaokonyunDeviceType);


    @Select("SELECT rmodel,be_rmodel,name,t_id,version FROM t_yaokonyun_remote_control  ")
    @Results(value = {
            @Result(property = "rmodel",column = "rmodel"),
            @Result(property = "name",column = "name"),
            @Result(property = "beRmodel",column = "be_rmodel"),
            @Result(property = "t_id",column = "t_id"),
            @Result(property = "version",column = "version")
    })
    List<TYaokonyunRemoteControl> getYaokonyunRemoteControlByIds();


    @Insert(" insert into t_yaokonyun_remote_control(t_id,version,name,be_rmodel, " +
            " rmodel,rdesc,order_no,zip,r_id,last_op_time,src) " +
            " values(#{t_id},#{version},#{name},#{beRmodel},#{rmodel},#{rdesc},#{order_no},#{zip},#{r_id},#{lastOpTime},#{src})")
    void addYaokonyunRemoteControl(TYaokonyunRemoteControl yaokonyunRemoteControl);

    @Select("SELECT * FROM t_yaokonyun_brand where device_type=#{deviceTypeId} and common = 1 ")
    @Results(value = {
            @Result(property = "lastOpTime",column = "last_op_time"),
            @Result(property = "bId",column = "b_id"),
            @Result(property = "name",column = "name"),
            @Result(property = "deviceType",column = "device_type"),
            @Result(property = "common",column = "common")
    })
    List<TYaoKongYunBrand> getYaoKongYunBrandByDeviceType(@Param("deviceTypeId") String deviceTypeId);

    @Insert(" insert into t_yaokonyun_brand(b_id,name,device_type, " +
            " common,last_op_time) " +
            " values(#{bId},#{name},#{deviceType},#{common},#{lastOpTime})")
    void addTYaoKongYunBrand(TYaoKongYunBrand yaoKongYunBrand);

    @Select("SELECT id,src FROM t_yaokonyun_remote_control where r_id = #{remoteId}  ")
    @Results(value = {
            @Result(property = "id",column = "id"),
            @Result(property = "src",column = "src")
    })
    TYaokonyunRemoteControl getYaokonyunRemoteControlByBrandId(@Param("remoteId")String remoteId);


    @Insert(" insert into t_yaokonyun_key_code(src,`index`,analysisSrc,`key`,serialId,key_name,custom_name,remote_id, " +
            " last_op_time,t_id,name,brandId,rmodel,version) " +
            " values(#{src},#{index},#{analysisSrc},#{key},#{serialId},#{keyName},#{customName},#{remoteId},#{lastOpTime},#{tId},#{name},#{brandId},#{rmodel},#{version})")
    void addTYaokonyunKeyCode(TYaokonyunKeyCode yaokonyunKeyCode);


    @Select("select src,`key` from t_yaokonyun_key_code where `index` = #{index}")
    @Results(value = {
            @Result(property = "src",column = "src"),
            @Result(property = "key",column = "key")
    })
    List<TYaokonyunKeyCode> getYaoKongKeyCodeByRemoteId(@Param("index")Integer index);


    @Delete(" delete from t_yaokonyun_key_code where serialId = #{serialId} and `index` = #{index} ")
    void deleteTYaokonyunKeyCode(@Param("serialId")String serialId,@Param("index")Integer index);

    @Delete(" delete from t_yaokonyun_key_code where serialId = #{serialId} and `index` = #{index} and `key` = #{key} ")
    void deleteTYaokonyunKeyCodeByKeyName(@Param("serialId")String serialId,@Param("index")Integer index,@Param("key")String keyName);

    @Delete(" delete from t_yaokonyun_key_code where serialId = #{serialId} and `index` = #{index} and `key` = #{key}")
    void deleteTYaokonyunKeyCodeByCustomName(@Param("serialId")String serialId,@Param("index")Integer index,@Param("key")String customName);

    @Delete(" delete from t_yaokonyun_key_code where serialId = #{serialId} ")
    void deleteTYaokonyunKeyCodeBySerialId(@Param("serialId")String serialId);

    @Select("select * from t_yaokonyun_key_code where serialId = #{serialId}")
    @Results(value = {
            @Result(property = "key",column = "key"),
            @Result(property = "index",column = "index"),
            @Result(property = "name",column = "name"),
            @Result(property = "tId",column = "t_id"),
            @Result(property = "keyName",column = "key_name"),
            @Result(property = "customName",column = "custom_name")
    })
    List<TYaokonyunKeyCode> getYaoKongKeyCodeBySerialId(@Param("serialId")String serialId);


    @Update(" update t_yaokonyun_key_code set name = #{name} where serialId = #{serialId} and `index` = #{index}")
    void updateYaoKongKeyCodeNameBySerialIdAndIndex(@Param("serialId")String serialId,@Param("index")String index,@Param("name")String name);


    @Select("select id,`src` from t_yaokonyun_key_code where `index` = #{index} and serialId = #{serialId} and `key` = #{key} ")
    @Results(value = {
            @Result(property = "id",column = "id"),
            @Result(property = "src",column = "src")
    })
    TYaokonyunKeyCode getYaoKongKeyCodeByKeyAndSerialIdAndIndex(@Param("index")Integer index,@Param("serialId")String serialId,@Param("key")String key);


    @Update(" update t_yaokonyun_key_code set src = #{codeSrc},`key` = #{key} where serialId = #{serialId} and `index` = #{index} ")
    void updateYaoKongKeyCodeNameBySerialIdAndIndexAndKey(@Param("serialId")String serialId,@Param("index")Integer index,@Param("key")String key,@Param("codeSrc")String codeSrc);



    @Select(" SELECT t_id,tyb.name,tykc.`index` FROM onbright_ali_new.t_user_ali_device tuad " +
            " inner join t_yaokonyun_key_code tykc on tuad.device_serial_id=tykc.serialId " +
            " inner join t_yaokonyun_brand tyb on tykc.brandId=tyb.b_id " +
            " where tuad.user_id = #{userId} " +
            " group by t_id ")

    List<Map<String, Object>> getUserIRDevice(@Param("userId")Integer userId);

    @Select("select `src`,`key`,t_id from t_yaokonyun_key_code where `index` = #{index} ")
    @Results(value = {
            @Result(property = "src",column = "src"),
            @Result(property = "key",column = "key")
    })
    List<TYaokonyunKeyCode> getIRDeviceByIndex(@Param("index")Integer index);


    @Select("select `src`,serialId,`index`,`key` from t_yaokonyun_key_code where `index` = #{index} and `key` = #{key} ")
    @Results(value = {
            @Result(property = "src",column = "src"),
            @Result(property = "serialId",column = "serialId"),
            @Result(property = "index",column = "index"),
            @Result(property = "key",column = "key")
    })
    TYaokonyunKeyCode getIRDeviceByIndexAndKey(@Param("index")Integer index,@Param("key")String key);

    @Select("select `src`,`key`,t_id from t_yaokonyun_key_code where `index` = #{index} ")
    @Results(value = {
            @Result(property = "src",column = "src"),
            @Result(property = "key",column = "key")
    })
    List<TYaokonyunKeyCode> getYaoKongKeyCodeBySerialIdAndIndex(@Param("index")Integer index,@Param("serialId")String serialId);

}
