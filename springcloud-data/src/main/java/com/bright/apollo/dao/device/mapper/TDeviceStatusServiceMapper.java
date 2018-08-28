package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TDeviceStatus;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年8月28日
 * @Version:1.1.0
 */
@Mapper
@Component
public interface TDeviceStatusServiceMapper {

	/**
	 * @param serialId
	 * @param start
	 * @param count
	 * @return
	 * @Description:
	 */
	@Select("select device_state,last_op_time from ${tableName}"
			+ "  where device_serial_id=#{serialId} order by last_op_time desc limit #{start},#{count}")
	@Results(value = { @Result(property = "id", column = "id"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "lastOpTime", column = "last_op_time") })
	List<TDeviceStatus> queryDeviceStatusByCount(@Param("tableName") String tableName,
			@Param("serialId") String serialId, @Param("start") int start, @Param("count") int count);

	/**
	 * @param serialId
	 * @param from
	 * @param to
	 * @return
	 * @Description:
	 */
	@Select("select last_op_time,device_state From t_device_status"
			+ " where device_serial_id=#{serialId} and last_op_time>=from_unixtime(#{from}) "
			+ "and last_op_time<=from_unixtime(#{to}) group by hour(last_op_time) " + "order by last_op_time")
	@Results(value = { @Result(property = "id", column = "id"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "lastOpTime", column = "last_op_time") })
	List<TDeviceStatus> queryDeviceStatusByData(@Param("serialId") String serialId, @Param("from") long from,
			@Param("to") long to);

	/**
	 * @param serialId
	 * @param from
	 * @param to
	 * @return
	 * @Description:
	 */
	@Select("select device_state,last_op_time From t_device_status "
			+ "where device_serial_id=#{serialId} and last_op_time>=from_unixtime(#{from}) "
			+ "and last_op_time<=from_unixtime(#{to}) order by id desc")
	@Results(value = { @Result(property = "id", column = "id"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceState", column = "device_state"),
			@Result(property = "lastOpTime", column = "last_op_time") })
	List<TDeviceStatus> queryDeviceStatusByDataNoGroup(@Param("serialId") String serialId, @Param("from") long from,
			@Param("to") long to);

}
