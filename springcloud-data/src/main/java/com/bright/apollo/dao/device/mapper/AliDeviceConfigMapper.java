package com.bright.apollo.dao.device.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
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
}
