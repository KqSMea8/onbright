package com.bright.apollo.dao.device.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import com.bright.apollo.common.entity.TDeviceChannel;
import com.bright.apollo.common.entity.TDeviceChannelExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TDeviceChannelMapper extends BaseMapper<TDeviceChannel, TDeviceChannelExample, Integer> {

    @Delete("delete from t_device_channel where device_id = #{deviceId}")
    void deleteDeviceChannel(int deviceId);

    @Select("insert into t_device_channel(device_id,\n" +
            "obox_id,\n" +
            "last_op_time,\n" +
            "signal_intensity) values(#{deviceId},#{oboxId},#{lastOpTime},#{signalIntensity})")
    void addDeviceChannel(TDeviceChannel deviceChannel);

    @Delete("delete from t_device_channel where obox_id = #{oboxId}")
    void delectDeviceChannelByOboxId(int oboxId);
}