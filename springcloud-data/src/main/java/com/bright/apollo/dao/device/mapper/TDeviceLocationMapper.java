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
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type = DeviceLocationProvider.class, method = "addDeviceLocation")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
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
	void deleteDeviceLocation(@Param("id")Integer id);

}
