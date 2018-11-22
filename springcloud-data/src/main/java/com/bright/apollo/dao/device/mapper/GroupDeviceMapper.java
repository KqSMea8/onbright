package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TGroupDevice;
import com.bright.apollo.dao.sqlProvider.AliDeviceConfigProvider;
import com.bright.apollo.dao.sqlProvider.GroupDeviceProvider;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月30日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface GroupDeviceMapper {

	/**  
	 * @param groupId
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_group_device where group_id=#{groupId}")
    @Results(value = {
			@Result(property = "groupId",column = "group_id"),
			@Result(property = "deviceSerialId",column = "device_serial_id"),
			@Result(property = "lastOpTime",column = "last_op_time")
			})
	List<TGroupDevice> queryDeviceGroupByGroupId(@Param("groupId")Integer groupId);

	/**  
	 * @param groupId
	 * @param deviceSerialId
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_group_device where group_id=#{groupId} and device_serial_id=#{deviceSerialId}")
    @Results(value = {
			@Result(property = "groupId",column = "group_id"),
			@Result(property = "deviceSerialId",column = "device_serial_id"),
			@Result(property = "lastOpTime",column = "last_op_time")
			})
	TGroupDevice queryDeviceGroup(@Param("groupId")Integer groupId, @Param("deviceSerialId")String deviceSerialId);

	/**  
	 * @param groupDevice
	 * @return  
	 * @Description:  
	 */
	@InsertProvider(type=GroupDeviceProvider.class,method="addDeviceGroup")
	int addDeviceGroup(TGroupDevice groupDevice);

	/**  
	 * @param groupId
	 * @param deviceSerialId
	 * @return  
	 * @Description:  
	 */
	@Delete("delete from t_group_device where device_serial_id = #{deviceSerialId} and group_id=#{groupId} ")
	int deleteDeviceGroup(@Param("groupId")Integer groupId, @Param("deviceSerialId")String deviceSerialId);

}
