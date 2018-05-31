package com.bright.apollo.dao.device.mapper;

import java.util.List;

import com.bright.apollo.dao.device.sqlProvider.OboxDeviceConfigSqlProvider;
import org.apache.ibatis.annotations.*;

import com.bright.apollo.common.entity.TOboxDeviceConfig;
import com.bright.apollo.common.entity.TOboxDeviceConfigExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TOboxDeviceConfigMapper extends BaseMapper<TOboxDeviceConfig, TOboxDeviceConfigExample, Integer> {

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
	List<TOboxDeviceConfig> queryDeviceByUserId(@Param("userId") Integer userId,@Param("pageStart") int pageStart,@Param("pageEnd") int pageEnd);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	int queryCountDeviceByUserId(Integer userId);

	@Select("select id from t_obox_device_config where obox_id = #{oboxId} and device_rf_addr = #{address}")
	TOboxDeviceConfig queryOboxConfigByAddr(@Param("oboxId") int oboxId,@Param("address") String address);

	@Update("update t_obox_device_config set obox_id= #{oboxId }" +
			",device_id= #{deviceId }" +
			",device_state= #{deviceState}" +
			",device_type= #{deviceType }" +
			",device_child_type= #{deviceChildType }" +
			",device_version= #{deviceVersion}" +
			",last_op_time= #{lastOpTime}" +
			",device_serial_id= #{deviceSerialId}" +
			",device_rf_addr= #{deviceRfAddr}" +
			",group_addr= #{groupAddr}" +
			",obox_serial_id= #{oboxSerialId}" +
			",online= #{online} where device_type = #{deviceType} and id = #{id}")
	void updateTOboxDeviceConfig(TOboxDeviceConfig tOboxDeviceConfig);


	@Select("select id,device_serial_id,device_rf_addr,group_addr from t_obox_device_config where device_serial_id = #{deviceSerialId}  ")
	TOboxDeviceConfig queryDeviceConfigBySerialID(@Param("deviceSerialId") String deviceSerialId);

	@Delete("delete from t_obox_device_config where id = #{id}")
	void deleteTOboxDeviceConfig(@Param("id") int id);

	@Insert("insert into t_obox_device_config(obox_id,\n" +
			"device_id,\n" +
			"device_state,\n" +
			"device_type,\n" +
			"device_child_type,\n" +
			"device_version,\n" +
			"last_op_time,\n" +
			"device_serial_id,\n" +
			"device_rf_addr,\n" +
			"group_addr,\n" +
			"obox_serial_id,\n" +
			"online) values(#{oboxId},#{deviceId},#{deviceState},#{deviceType},#{deviceChildType}" +
			",#{deviceVersion},#{lastOpTime},#{deviceSerialId},#{deviceRfAddr},#{groupAddr},#{oboxSerialId},#{online})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	int addTOboxDeviceConfig(TOboxDeviceConfig tOboxDeviceConfig);

	@Select("select * from t_obox_device_config where obox_id = #{oboxId}")
	List<TOboxDeviceConfig> getOboxDeviceConfigByOboxId(@Param("oboxId") int oboxId);

	@Delete("delete from t_obox_device_config where obox_id = #{oboxId}")
	void deleteTOboxDeviceConfigByOboxId(@Param("oboxId") int oboxId);

	@Delete("delete from t_obox_device_config where obox_id = #{oboxId} and device_rf_addr = #{nodeAddress}")
	void deleteTOboxDeviceConfigByOboxIdAndNodeAddress(@Param("oboxId") int oboxId,@Param("nodeAddress") String nodeAddress);

	@Select("select * from t_obox_device_config where obox_serial_id = #{oboxSerialId} and group_addr = #{groupAddress}")
	List<TOboxDeviceConfig> getTOboxDeviceConfigByOboxSerialIdAndGroupAddress(@Param("oboxSerialId") String oboxSerialId,@Param("groupAddress") String groupAddress);

	@Update("update t_obox_device_config set obox_id= #{oboxId }" +
			",device_id= #{deviceId }" +
			",device_state= #{deviceState}" +
			",device_type= #{deviceType }" +
			",device_child_type= #{deviceChildType }" +
			",device_version= #{deviceVersion}" +
			",last_op_time= #{lastOpTime}" +
			",device_serial_id= #{deviceSerialId}" +
			",device_rf_addr= #{deviceRfAddr}" +
			",group_addr= #{groupAddr}" +
			",obox_serial_id= #{oboxSerialId}" +
			",online= #{online} where device_type = #{deviceType} and id = #{id}")
	void updateTOboxDeviceConfigStatus(TOboxDeviceConfig tOboxDeviceConfig);

	@Select("select * from t_obox_device_config where device_serial_id= #{deviceSerialId}")
	TOboxDeviceConfig getTOboxDeviceConfigByDeviceSerialId(@Param("deviceSerialId") String deviceSerialId);

	@Select("select * from t_obox_device_config where device_serial_id= #{deviceSerialId} and device_rf_addr=#{address}")
	TOboxDeviceConfig getTOboxDeviceConfigByDeviceSerialIdAndAddress(@Param("deviceSerialId") String deviceSerialId,@Param("address") String address);

	@SelectProvider(type = OboxDeviceConfigSqlProvider.class, method = "getAllOboxDeviceConfig")
	List<TOboxDeviceConfig> getAllOboxDeviceConfig(@Param("deviceType") String deviceType);

	@Select("select * from t_obox_device_config where id = #{id}")
	TOboxDeviceConfig getOboxDeviceConfigById(@Param("id") int id);
}