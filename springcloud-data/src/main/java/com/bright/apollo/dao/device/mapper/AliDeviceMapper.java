package com.bright.apollo.dao.device.mapper;

import com.bright.apollo.common.entity.*;

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
	@Results(value = {
			@Result(property = "Id",column = "id"),
			@Result(property = "oboxSerialId",column = "obox_serial_id"),
			@Result(property = "lastOpTime",column = "last_op_time"),
			@Result(property = "deviceName",column = "DeviceName"),
			@Result(property = "deviceSecret",column = "DeviceSecret"),
			@Result(property = "productKey",column = "productKey"),
			@Result(property = "offline",column = "offline")
	})
    TAliDevice getAliDeviceBySerializeId(@Param("oboxSerialId") String oboxSerialId);

    @Select("select * from t_ali_device_us where device_serial_id = #{oboxSerialId}")
    TAliDeviceUS getAliUSDeviceBySerializeId(@Param("oboxSerialId") String oboxSerialId);

    @Select("select * from t_ali_device_timer where id = #{id}")
	@Results(value = {
			@Result(property = "Id",column = "id"),
			@Result(property = "deviceSerialId",column = "device_serial_id"),
			@Result(property = "lastOpTime",column = "last_op_time"),
			@Result(property = "timer",column = "timer"),
			@Result(property = "timerValue",column = "timer_value"),
			@Result(property = "isCountdown",column = "is_countdown"),
			@Result(property = "state",column = "state")
	})
    TAliDevTimer getAliDevTimerByTimerId(@Param("id") int id);

    @Delete("delete from t_ali_device_timer where device_serial_id = #{deviceSerialId} and is_countdown = #{isCountdown}")
    void deleteAliDevTimerBySerializeIdAndType(@Param("deviceSerialId") String deviceSerialId,@Param("isCountdown") int isCountdown);

	@Results(value = {
			@Result(property = "Id",column = "id"),
			@Result(property = "oboxSerialId",column = "obox_serial_id"),
			@Result(property = "lastOpTime",column = "last_op_time"),
			@Result(property = "deviceName",column = "DeviceName"),
			@Result(property = "deviceSecret",column = "DeviceSecret"),
			@Result(property = "productKey",column = "productKey"),
			@Result(property = "offline",column = "offline")
	})
    @Select("select * from t_ali_device where productKey = #{productKey} and deviceName = #{deviceName} ")
    TAliDevice getAliDeviceByProductKeyAndDeviceName(@Param("productKey") String productKey,@Param("deviceName") String deviceName);

    @Select("select * from t_ali_device_us where productKey = #{productKey} and deviceName = #{deviceName}")
    TAliDeviceUS getAliUSDeviceByProductKeyAndDeviceName(@Param("productKey") String productKey,@Param("deviceName") String deviceName);

    @Select("select * from t_ali_device_us where productKey = #{productKey} and device_serial_id = #{deviceSerialId}")
    List<TAliDeviceUS> getAliUSDeviceByProductKeyAndDeviceSerialId(@Param("productKey") String productKey,@Param("deviceSerialId") String deviceSerialId);

    
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

	/**  
	 * @param tAliDeviceUS
	 * @return  
	 * @Description:  
	 */
    @Insert("insert into t_ali_device_us(productKey,\n" +
			"deviceName,\n" +
			"deviceSecret,\n" +
			"deviceSerialId,\n" +
			"offline) values(#{productKey},#{deviceName},#{deviceSecret},#{deviceSerialId},#{offline}")
	@Options(useGeneratedKeys=true, keyProperty="Id", keyColumn="id")
	int addAliDevUS(TAliDeviceUS tAliDeviceUS);

	/**  
	 * @param tAliDevice
	 * @return  
	 * @Description:  
	 */
    @Insert("insert into t_ali_device(productKey,\n" +
			"deviceName,\n" +
			"deviceSecret,\n" +
			"obox_serial_id,\n" +
			"offline) values(#{productKey},#{deviceName},#{deviceSecret},#{oboxSerialId},#{offline})")
	@Options(useGeneratedKeys=true, keyProperty="Id", keyColumn="id")
	int addAliDev(TAliDevice tAliDevice);

	/**  
	 * @param productKey
	 * @param deviceSerialId
	 * @return  
	 * @Description:  
	 */
    @Results(value = {
    		@Result(property = "Id",column = "id"),
    		@Result(property = "oboxSerialId",column = "obox_serial_id"),
            @Result(property = "lastOpTime",column = "last_op_time"),
    		@Result(property = "deviceName",column = "DeviceName"),
    		@Result(property = "deviceSecret",column = "DeviceSecret"),
    		@Result(property = "productKey",column = "productKey"),
    		@Result(property = "offline",column = "offline")
    })
    @Select("select * from t_ali_device where productKey = #{productKey} and obox_serial_id = #{oboxSerialId}")
    List<TAliDevice> getAliDeviceByProductKeyAndDeviceSerialId(@Param("productKey")  String productKey, @Param("oboxSerialId")  String oboxSerialId);


	@Results(value = {
			@Result(property = "Id",column = "id"),
			@Result(property = "oboxSerialId",column = "obox_serial_id"),
			@Result(property = "lastOpTime",column = "last_op_time"),
			@Result(property = "deviceName",column = "DeviceName"),
			@Result(property = "deviceSecret",column = "DeviceSecret"),
			@Result(property = "productKey",column = "productKey"),
			@Result(property = "offline",column = "offline")
	})
	@Select("select * from t_ali_device where productKey = #{productKey}  ")
	List<TAliDevice> getAliDeviceByProductKey(@Param("productKey")  String productKey);


	@Select("select * from t_ali_device_timer where device_serial_id = #{oboxSerialId} and is_countdown = 0 ")
	@Results(value = {
			@Result(property = "Id",column = "id"),
			@Result(property = "deviceSerialId",column = "device_serial_id"),
			@Result(property = "lastOpTime",column = "last_op_time"),
			@Result(property = "timer",column = "timer"),
			@Result(property = "timerValue",column = "timer_value"),
			@Result(property = "isCountdown",column = "is_countdown"),
			@Result(property = "state",column = "state")
	})
	List<TAliDevTimer> getAliDevTimerByDeviceSerialId(@Param("oboxSerialId") String oboxSerialId);

	@Select("select * from t_ali_device_timer where device_serial_id = #{oboxSerialId} and is_countdown = 1 ")
	@Results(value = {
			@Result(property = "Id",column = "id"),
			@Result(property = "deviceSerialId",column = "device_serial_id"),
			@Result(property = "lastOpTime",column = "last_op_time"),
			@Result(property = "timer",column = "timer"),
			@Result(property = "timerValue",column = "timer_value"),
			@Result(property = "isCountdown",column = "is_countdown"),
			@Result(property = "state",column = "state")
	})
	TAliDevTimer getAliDevTimerByDeviceSerialIdAndCountDown(@Param("oboxSerialId") String oboxSerialId);


	@Select("select * from t_ali_device_timer where device_serial_id = #{oboxSerialId} and  id =#{id}")
	@Results(value = {
			@Result(property = "Id",column = "id"),
			@Result(property = "deviceSerialId",column = "device_serial_id"),
			@Result(property = "lastOpTime",column = "last_op_time"),
			@Result(property = "timer",column = "timer"),
			@Result(property = "timerValue",column = "timer_value"),
			@Result(property = "isCountdown",column = "is_countdown"),
			@Result(property = "state",column = "state")
	})
	TAliDevTimer getAliDevTimerByIdAndDeviceId(@Param("oboxSerialId") String oboxSerialId,@Param("id") Integer id);

	@Delete("delete from t_ali_device_timer where id = #{id} ")
	void deleteAliDevTimerById(@Param("id") Integer id);

	@Insert("insert into t_ali_device_timer(device_serial_id,\n" +
			"timer,\n" +
			"timer_value,\n" +
			"last_op_time,\n" +
			"is_countdown,state) values(#{deviceSerialId},#{timer},#{timerValue},#{lastOpTime},#{isCountdown},#{state})")
	@Options(useGeneratedKeys=true, keyProperty="Id", keyColumn="id")
	int addAliDevTimer(TAliDevTimer aliDevTimer);


	@Update(" update t_ali_device_timer set device_serial_id = #{deviceSerialId} " +
			" ,timer = #{timer},timer_value = #{timerValue}" +
			" ,last_op_time = #{lastOpTime},is_countdown = #{isCountdown}" +
			" ,state = #{state} where id = #{id}")
	void updateAliDevTimer(TAliDevTimer aliDevTimer);

	@Delete("delete from t_user_ali_device where device_serial_id = #{deviceSerialId} ")
	void deleteAliDeviceUser(@Param("deviceSerialId") String deviceSerialId);

	@Insert("insert into t_user_ali_device(user_id,\n" +
			"device_serial_id,\n" +
			"last_op_time ) values(#{userId},#{deviceSerialId},#{lastOpTime})")
	@Options(useGeneratedKeys=true, keyProperty="Id", keyColumn="id")
	int addAliDevUser(TUserAliDev userAliDev);

	@Select("select * from t_ali_device_timer where id = #{id}")
	@Results(value = {
			@Result(property = "Id",column = "id"),
			@Result(property = "deviceSerialId",column = "device_serial_id"),
			@Result(property = "lastOpTime",column = "last_op_time"),
			@Result(property = "timer",column = "timer"),
			@Result(property = "timerValue",column = "timer_value"),
			@Result(property = "isCountdown",column = "is_countdown"),
			@Result(property = "state",column = "state")
	})
	TAliDevTimer getAliDevTimerByDId(@Param("id") int id);

}
