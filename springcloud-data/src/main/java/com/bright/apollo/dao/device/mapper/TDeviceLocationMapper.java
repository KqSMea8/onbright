package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TDeviceLocation;
import com.bright.apollo.dao.sqlProvider.DeviceLocationProvider;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年11月23日
 * @Version:1.1.0
 */
@Mapper
@Component
public interface TDeviceLocationMapper {

	/**
	 * @param tDeviceLocation
	 * @return
	 * @Description:
	 */
	//@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	//@InsertProvider(type = DeviceLocationProvider.class, method = "addDeviceLocation")
	//@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	@Insert("insert into t_device_location(location,\n" +
            "x_axis,\n" +
            "y_axis,\n" +
            "serialId,\n" +
            "device_type) values(#{location},#{xAxis},#{yAxis},#{serialId},#{deviceType})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	int addDeviceLocation(TDeviceLocation tDeviceLocation);

	/**
	 * @param location
	 * @return
	 * @Description:
	 */
	@Select("select * from t_device_location where location=#{location}")
	@Results(value = { @Result(property = "id", column = "id"), @Result(property = "location", column = "location"),
			@Result(property = "xAxis", column = "x_axis"), @Result(property = "yAxis", column = "y_axis"),
			@Result(property = "serialId", column = "serialId"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "lastOpTime", column = "last_op_time") })
	List<TDeviceLocation> queryDevicesByLocation(@Param("location") Integer location);

	/**
	 * @param id
	 * @Description:
	 */
	@Delete("delete from t_device_location where id =#{id}")
	void deleteDeviceLocation(@Param("id") Integer id);

	/**
	 * @param location
	 * @param serialId
	 * @param type
	 * @return
	 * @Description:
	 */
	@Select("select * from t_device_location where location=#{location} and serialId=#{serialId} and device_type=#{type}")
	@Results(value = { @Result(property = "id", column = "id"), @Result(property = "location", column = "location"),
			@Result(property = "xAxis", column = "x_axis"), @Result(property = "yAxis", column = "y_axis"),
			@Result(property = "serialId", column = "serialId"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "lastOpTime", column = "last_op_time") })
	TDeviceLocation queryDevicesByLocationAndSerialIdAndType(@Param("location") Integer location,
			@Param("serialId") String serialId, @Param("type") String type);

	/**  
	 * @param location2  
	 * @Description:  
	 */
	@UpdateProvider(type = DeviceLocationProvider.class, method = "updateDeviceLocation")
	void updateDeviceLocation(TDeviceLocation tDeviceLocation);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	@Select("SELECT * from t_device_location a where exists"
			+ "(select location_id as location from t_user_location b "
			+ "where b.user_id=#{userId} and a.location=b.location_id )")
	@Results(value = { @Result(property = "id", column = "id"), @Result(property = "location", column = "location"),
			@Result(property = "xAxis", column = "x_axis"), @Result(property = "yAxis", column = "y_axis"),
			@Result(property = "serialId", column = "serialId"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "lastOpTime", column = "last_op_time") })
	List<TDeviceLocation> queryDevicesByUserId(@Param("userId")Integer userId);

	/**  
	 * @param userName
	 * @return  
	 * @Description:  
	 */
	@Select("SELECT * from t_device_location a where exists"
			+ "(select id as location from t_location b "
			+ "where b.user_name=#{userName} and a.location=b.id )")
	@Results(value = { @Result(property = "id", column = "id"), @Result(property = "location", column = "location"),
			@Result(property = "xAxis", column = "x_axis"), @Result(property = "yAxis", column = "y_axis"),
			@Result(property = "serialId", column = "serialId"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "lastOpTime", column = "last_op_time") })
	List<TDeviceLocation> queryDevicesByUserName(@Param("userName")String userName);

	/**  
	 * @param serialId
	 * @param userName
	 * @return  
	 * @Description:  
	 */
	@Select("SELECT * from t_device_location a where exists"
			+ "(select id as location from t_location b "
			+ "where b.user_name=#{userName} and a.location=b.id ) and "
			+ "a.serialId=#{serialId}")
	@Results(value = { @Result(property = "id", column = "id"), @Result(property = "location", column = "location"),
			@Result(property = "xAxis", column = "x_axis"), @Result(property = "yAxis", column = "y_axis"),
			@Result(property = "serialId", column = "serialId"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "lastOpTime", column = "last_op_time") })
	TDeviceLocation queryLocationDeviceBySerialIdAndUserName(@Param("serialId")String serialId, @Param("userName")String userName);

}
