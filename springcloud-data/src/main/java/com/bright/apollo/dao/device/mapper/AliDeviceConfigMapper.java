package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import com.bright.apollo.dao.sqlProvider.AliDeviceConfigProvider;

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

	/**  
	 * @param deviceId
	 * @return  
	 * @Description:  
	 */
	TAliDeviceConfig queryAliDevConfigBySerialId(String deviceId);

	/**  
	 * @param tAliDeviceConfig  
	 * @Description:  
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type=AliDeviceConfigProvider.class,method="addAliDevConfig")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void addAliDevConfig(TAliDeviceConfig tAliDeviceConfig);
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
