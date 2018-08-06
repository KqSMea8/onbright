package com.bright.apollo.dao.device.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TDeviceChannel;

@Mapper
@Component
public interface TDeviceChannelMapper   {

    @Delete("delete from t_device_channel where device_id = #{deviceId}")
    void deleteDeviceChannel(@Param("deviceId") int deviceId);

    @Select("insert into t_device_channel(device_id,\n" +
            "obox_id,\n" +
            "signal_intensity) values(#{deviceId},#{oboxId},#{signalIntensity})")
    void addDeviceChannel(TDeviceChannel deviceChannel);

    @Delete("delete from t_device_channel where obox_id = #{oboxId}")
    void delectDeviceChannelByOboxId(@Param("oboxId") int oboxId);

	/**  
	 * @param deviceId
	 * @param oboxId
	 * @return  
	 * @Description:  
	 */
    @Select("select * from t_device_channel where device_id=#{deviceId} and obox_id=#{oboxId}")
    @Results(value = {
			@Result(property = "oboxId",column = "obox_id"),
			@Result(property = "deviceId",column = "device_id"),
			@Result(property = "lastOpTime",column = "last_op_time"),
			@Result(property = "signalIntensity",column = "signal_intensity")
			})
	TDeviceChannel getDeviceChannelById(@Param("deviceId") Integer deviceId, @Param("oboxId") Integer oboxId);

	/**  
	 * @param tDeviceChannel  
	 * @Description:  
	 */
    @Update("update t_device_channel set signal_intensity= #{signalIntensity }" +
			" where obox_id = #{oboxId} and device_id = #{deviceId}")
	void updateDeviceChannel(TDeviceChannel tDeviceChannel);
}