package com.bright.apollo.dao.device.mapper;

import com.bright.apollo.common.entity.TAliDeviceConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AliDeviceConfigMapper {

    @Select("select * from t_ali_device_config where device_serial_id = #{deviceSerialId}")
    TAliDeviceConfig getAliDeviceConfigByDeviceSerialId(String deviceSerialId);

    @Update(" update t_ali_device_config SET " +
            "    device_serial_id=#{deviceSerialId},\n" +
            "    type=#{type},\n" +
            "    action=#{action},\n" +
            "    state=#{state},\n" +
            "    last_op_time=#{lastOpTime},\n" +
            "    name=#{name} where id = #{id}")
    void update(TAliDeviceConfig aliDeviceConfig);
}
