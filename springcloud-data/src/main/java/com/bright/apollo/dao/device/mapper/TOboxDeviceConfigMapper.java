package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.dao.device.sqlProvider.OboxDeviceConfigSqlProvider;
 

@Mapper
@Component
public interface TOboxDeviceConfigMapper   {

	/**
	 * @param device
	 * @Description:
	 */
	void updateDeviceBySerialId(TOboxDeviceConfig device);

	/**
	 * @param userId
	 * @param pageStart
	 * @param pageEnd
	 * @return
	 * @Description:
	 */
	List<TOboxDeviceConfig> queryDeviceByUserId(@Param("userId") Integer userId, @Param("pageStart") int pageStart,
			@Param("pageEnd") int pageEnd);

	/**
	 * @param userId
	 * @return
	 * @Description:
	 */
	int queryCountDeviceByUserId(Integer userId);

	@Select("select * from t_obox_device_config where obox_id = #{oboxId} and device_rf_addr = #{address}")
	@Results(value = { @Result(property = "oboxId", column = "obox_id"),
			@Result(property = "deviceId", column = "device_id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceVersion", column = "device_version"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceRfAddr", column = "device_rf_addr"),
			@Result(property = "groupAddr", column = "group_addr"),
			@Result(property = "oboxSerialId", column = "obox_serial_id") })
	TOboxDeviceConfig queryOboxConfigByAddr(@Param("oboxId") int oboxId, @Param("address") String address);

	@Update("update t_obox_device_config set obox_id= #{oboxId }" + ",device_id= #{deviceId }"
			+ ",device_state= #{deviceState}" + ",device_type= #{deviceType }"
			+ ",device_child_type= #{deviceChildType }" + ",device_version= #{deviceVersion}"
			+ ",last_op_time= #{lastOpTime}" + ",device_serial_id= #{deviceSerialId}"
			+ ",device_rf_addr= #{deviceRfAddr}" + ",group_addr= #{groupAddr}" + ",obox_serial_id= #{oboxSerialId}"
			+ ",online= #{online} where device_type = #{deviceType} and id = #{id}")
	void updateTOboxDeviceConfig(TOboxDeviceConfig tOboxDeviceConfig);

	@Select("select * from t_obox_device_config where device_serial_id = #{deviceSerialId}  ")
	@Results(value = { @Result(property = "oboxId", column = "obox_id"),
			@Result(property = "deviceId", column = "device_id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceVersion", column = "device_version"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceRfAddr", column = "device_rf_addr"),
			@Result(property = "groupAddr", column = "group_addr"),
			@Result(property = "oboxSerialId", column = "obox_serial_id") })
	TOboxDeviceConfig queryDeviceConfigBySerialID(@Param("deviceSerialId") String deviceSerialId);

	@Delete("delete from t_obox_device_config where obox_id = #{id}")
	void deleteTOboxDeviceConfig(@Param("id") int id);

	@Insert("insert into t_obox_device_config(obox_id,\n" + "device_id,\n" + "device_state,\n" + "device_type,\n"
			+ "device_child_type,\n" + "device_version,\n" + "device_serial_id,\n" + "device_rf_addr,\n"
			+ "group_addr,\n" + "obox_serial_id,\n"
			+ "online) values(#{oboxId},#{deviceId},#{deviceState},#{deviceType},#{deviceChildType}"
			+ ",#{deviceVersion},#{deviceSerialId},#{deviceRfAddr},#{groupAddr},#{oboxSerialId},#{online})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int addTOboxDeviceConfig(TOboxDeviceConfig tOboxDeviceConfig);

	@Select("select * from t_obox_device_config where obox_id = #{oboxId}")
	List<TOboxDeviceConfig> getOboxDeviceConfigByOboxId(@Param("oboxId") int oboxId);

	@Delete("delete from t_obox_device_config where obox_id = #{oboxId}")
	void deleteTOboxDeviceConfigByOboxId(@Param("oboxId") int oboxId);

	@Delete("delete from t_obox_device_config where obox_id = #{oboxId} and device_rf_addr = #{nodeAddress}")
	void deleteTOboxDeviceConfigByOboxIdAndNodeAddress(@Param("oboxId") int oboxId,
			@Param("nodeAddress") String nodeAddress);

	@Select("select * from t_obox_device_config where obox_serial_id = #{oboxSerialId} and group_addr = #{groupAddress}")
	List<TOboxDeviceConfig> getTOboxDeviceConfigByOboxSerialIdAndGroupAddress(
			@Param("oboxSerialId") String oboxSerialId, @Param("groupAddress") String groupAddress);

	@Update("update t_obox_device_config set obox_id= #{oboxId }" + ",device_id= #{deviceId }"
			+ ",device_state= #{deviceState}" + ",device_type= #{deviceType }"
			+ ",device_child_type= #{deviceChildType }" + ",device_version= #{deviceVersion}"
			+ ",last_op_time= #{lastOpTime}" + ",device_serial_id= #{deviceSerialId}"
			+ ",device_rf_addr= #{deviceRfAddr}" + ",group_addr= #{groupAddr}" + ",obox_serial_id= #{oboxSerialId}"
			+ ",online= #{online} where device_type = #{deviceType} and id = #{id}")
	void updateTOboxDeviceConfigStatus(TOboxDeviceConfig tOboxDeviceConfig);

	@Select("select * from t_obox_device_config where device_serial_id= #{deviceSerialId}")
	@Results(value = { @Result(property = "oboxId", column = "obox_id"),
			@Result(property = "deviceId", column = "device_id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceVersion", column = "device_version"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceRfAddr", column = "device_rf_addr"),
			@Result(property = "groupAddr", column = "group_addr"),
			@Result(property = "oboxSerialId", column = "obox_serial_id") })
	TOboxDeviceConfig getTOboxDeviceConfigByDeviceSerialId(@Param("deviceSerialId") String deviceSerialId);

	@Select("select * from t_obox_device_config where obox_serial_id= #{deviceSerialId} and device_rf_addr=#{address}")
	@Results(value = { @Result(property = "oboxId", column = "obox_id"),
			@Result(property = "deviceId", column = "device_id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceVersion", column = "device_version"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceRfAddr", column = "device_rf_addr"),
			@Result(property = "groupAddr", column = "group_addr"),
			@Result(property = "oboxSerialId", column = "obox_serial_id") })
	TOboxDeviceConfig getTOboxDeviceConfigByDeviceSerialIdAndAddress(@Param("deviceSerialId") String deviceSerialId,
			@Param("address") String address);

	@SelectProvider(type = OboxDeviceConfigSqlProvider.class, method = "getAllOboxDeviceConfig")
	List<TOboxDeviceConfig> getAllOboxDeviceConfig(@Param("deviceType") String deviceType);

	@Select("select * from t_obox_device_config where id = #{id}")
	TOboxDeviceConfig getOboxDeviceConfigById(@Param("id") int id);

	@Select(" select * from t_obox_device_config todc "
			+ " inner join t_user_device tud on todc.device_serial_id = tud.device_serial_id"
			+ " inner join t_user tu on tu.id = tud.user_id" + " where tud.user_id = #{userId}")
	@Results(value = { @Result(property = "oboxId", column = "obox_id"),
			@Result(property = "deviceId", column = "device_id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceVersion", column = "device_version"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceRfAddr", column = "device_rf_addr"),
			@Result(property = "groupAddr", column = "group_addr"),
			@Result(property = "oboxSerialId", column = " obox_serial_id") })
	List<TOboxDeviceConfig> getOboxDeviceConfigByUserId(@Param("userId") Integer userId);

	/**
	 * @param oboxSerialId
	 * @return
	 * @Description:
	 */
	@Select(" select * from t_obox_device_config " + " where obox_serial_id=#{oboxSerialId}")
	@Results(value = { @Result(property = "oboxId", column = "obox_id"),
			@Result(property = "deviceId", column = "device_id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceVersion", column = "device_version"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceRfAddr", column = "device_rf_addr"),
			@Result(property = "groupAddr", column = "group_addr"),
			@Result(property = "oboxSerialId", column = " obox_serial_id") })
	List<TOboxDeviceConfig> getOboxDeviceConfigByOboxSerialId(@Param("oboxSerialId") String oboxSerialId);

	/**
	 * @param userId
	 * @return
	 * @Description:
	 */
	@Select(" select * from t_obox_device_config todc "
			+ " inner join t_user_device tud on todc.device_serial_id = tud.device_serial_id"
			+ " inner join t_user tu on tu.id = tud.user_id"
			+ " where tud.user_id = #{userId} group by todc.device_type")
	@Results(value = { @Result(property = "oboxId", column = "obox_id"),
			@Result(property = "deviceId", column = "device_id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceVersion", column = "device_version"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceRfAddr", column = "device_rf_addr"),
			@Result(property = "groupAddr", column = "group_addr"),
			@Result(property = "oboxSerialId", column = " obox_serial_id") })
	List<TOboxDeviceConfig> getDeviceTypeByUser(@Param("userId") Integer userId);

	/**
	 * @param userId
	 * @param deviceType
	 * @return
	 * @Description:
	 */
	@Select(" select * from t_obox_device_config todc "
			+ " inner join t_user_device tud on todc.device_serial_id = tud.device_serial_id"
			+ " inner join t_user tu on tu.id = tud.user_id"
			+ " where tud.user_id = #{userId} and todc.device_type=#{deviceType}")
	@Results(value = { @Result(property = "oboxId", column = "obox_id"),
			@Result(property = "deviceId", column = "device_id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceVersion", column = "device_version"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceRfAddr", column = "device_rf_addr"),
			@Result(property = "groupAddr", column = "group_addr"),
			@Result(property = "oboxSerialId", column = "obox_serial_id") })
	List<TOboxDeviceConfig> getDevciesByUserIdAndType(@Param("userId") Integer userId,
			@Param("deviceType") String deviceType);

	/**
	 * @param userId
	 * @param serialID
	 * @return
	 * @Description:
	 */
	@Select(" SELECT * FROM t_obox_device_config a " + 
			" WHERE a.device_serial_id =#{serialID} AND exists "
			+ " (SELECT 1 FROM t_user_device WHERE user_id =#{userId} and device_serial_id=a.device_serial_id)")
	@Results(value = { @Result(property = "oboxId", column = "obox_id"),
			@Result(property = "deviceId", column = "device_id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceVersion", column = "device_version"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceRfAddr", column = "device_rf_addr"),
			@Result(property = "groupAddr", column = "group_addr"),
			@Result(property = "oboxSerialId", column = " obox_serial_id") })
	TOboxDeviceConfig getDeviceByUserAndSerialId(@Param("userId") Integer userId, @Param("serialID") String serialID);

	/**  
	 * @param serialId  
	 * @Description:  
	 */
	@Delete("delete from t_obox_device_config where obox_serial_id = #{serialId}")
	void deleteTOboxDeviceConfigByOboxSerialId(@Param("serialId") String serialId);

	/**  
	 * @param id  
	 * @Description:  
	 */
	@Delete("delete from t_obox_device_config where id = #{id}")
	void deleteTOboxDeviceConfigById(@Param("serialId") Integer id);

}