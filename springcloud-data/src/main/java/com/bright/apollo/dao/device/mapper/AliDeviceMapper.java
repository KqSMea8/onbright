package com.bright.apollo.dao.device.mapper;

import com.bright.apollo.common.entity.TAliDevTimer;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface AliDeviceMapper {

    @Select("select * from t_ali_device where obox_serial_id = #{oboxSerialId}")
    TAliDevice getAliDeviceBySerializeId(@Param("oboxSerialId") String oboxSerialId);

    @Select("select * from t_ali_device_us where device_serial_id = #{oboxSerialId}")
    TAliDeviceUS getAliUSDeviceBySerializeId(@Param("oboxSerialId") String oboxSerialId);

    @Select("select * from t_ali_device_timer where id = #{id}")
    TAliDevTimer getAliDevTimerByTimerId(@Param("id") int id);

    @Delete("delete from t_ali_device_timer where device_serial_id = #{deviceSerialId} and is_countdown = #{isCountdown}")
    void deleteAliDevTimerBySerializeIdAndType(@Param("deviceSerialId") String deviceSerialId,@Param("isCountdown") int isCountdown);

    @Results(value = {
            @Result(property = "oboxSerialId",column = "obox_serial_id"),
            @Result(property = "lastOpTime",column = "last_op_time")
    })
    @Select("select * from t_ali_device where productKey = #{productKey} and deviceName = #{deviceName} ")
    TAliDevice getAliDeviceByProductKeyAndDeviceName(@Param("productKey") String productKey,@Param("deviceName") String deviceName);

    @Select("select * from t_ali_device_us where productKey = #{productKey} and deviceName = #{deviceName}")
    TAliDeviceUS getAliUSDeviceByProductKeyAndDeviceName(@Param("productKey") String productKey,@Param("deviceName") String deviceName);

    @Update(" update t_ali_device set DeviceName = #{deviceName} " +
            " ,DeviceSecret = #{deviceSecret},obox_serial_id = #{oboxSerialId}" +
            " ,last_op_time = #{lastOpTime},productKey = #{productKey}" +
            " ,offline = #{offline} where id = #{id}")
    void updateAliDevice(TAliDevice aliDevice);

    @Update(" update t_ali_device_us set DeviceName = #{deviceName} " +
            " ,DeviceSecret = #{deviceSecret},obox_serial_id = #{oboxSerialId}" +
            " ,last_op_time = #{lastOpTime},productKey = #{productKey}" +
            " ,offline = #{offline} where id = #{id}")
    void updateAliUSDevice(TAliDeviceUS aliDeviceUS);
}
