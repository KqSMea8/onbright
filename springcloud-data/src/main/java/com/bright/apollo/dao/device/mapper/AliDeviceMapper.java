package com.bright.apollo.dao.device.mapper;

import com.bright.apollo.common.entity.TAliDevTimer;
import com.bright.apollo.common.entity.TAliDevice;
import com.bright.apollo.common.entity.TAliDeviceUS;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AliDeviceMapper {

    @Select("select * from t_ali_device where obox_serial_id = #{oboxSerialId}")
    TAliDevice getAliDeviceBySerializeId(String oboxSerialId);

    @Select("select * from t_ali_device_us where device_serial_id = #{oboxSerialId}")
    TAliDeviceUS getAliUSDeviceBySerializeId(String oboxSerialId);

    @Select("select * from t_ali_device_timer where id = #{id}")
    TAliDevTimer getAliDevTimerByTimerId(int id);

    @Delete("delete from t_ali_device_timer where device_serial_id = #{deviceSerialId} and is_countdown = #{isCountdown}")
    void deleteAliDevTimerBySerializeIdAndType(String deviceSerialId, int isCountdown);

    @Select("select * from t_ali_device where productKey = #{productKey} and deviceName = #{deviceName}")
    TAliDevice getAliDeviceByProductKeyAndDeviceName(String productKey, String deviceName);

    @Select("select * from t_ali_device_us where productKey = #{productKey} and deviceName = #{deviceName}")
    TAliDeviceUS getAliUSDeviceByProductKeyAndDeviceName(String productKey, String deviceName);

    @Update(" update t_ali_device set DeviceName = #{DeviceName} " +
            " ,DeviceSecret = #{DeviceSecret},obox_serial_id = #{oboxSerialId}" +
            " ,last_op_time = #{lastOpTime},productKey = #{productKey}" +
            " ,offline = #{offline} where id = #{id}")
    void updateAliDevice(TAliDevice aliDevice);

    @Update(" update t_ali_device_us set DeviceName = #{DeviceName} " +
            " ,DeviceSecret = #{DeviceSecret},obox_serial_id = #{oboxSerialId}" +
            " ,last_op_time = #{lastOpTime},productKey = #{productKey}" +
            " ,offline = #{offline} where id = #{id}")
    void updateAliUSDevice(TAliDeviceUS aliDeviceUS);
}
