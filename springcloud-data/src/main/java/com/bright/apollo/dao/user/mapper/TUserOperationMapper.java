package com.bright.apollo.dao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TUserOperation;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年7月25日
 * @Version:1.1.0
 */
@Mapper
@Component
public interface TUserOperationMapper {

	/**
	 * @param fromDate
	 * @param toDate
	 * @param serialId
	 * @param startIndex
	 * @param countIndex
	 * @return
	 * @Description:
	 */
	@Select("select last_op_time,device_serial_id,device_state,device_type,"
			+ "device_child_type from t_user_operation where device_serial_id = #{serialId} "
			+ "and last_op_time>=from_unixtime(#{fromDate}) "
			+ "and last_op_time<=from_unixtime(#{toDate})  order by id desc limit #{startIndex},#{countIndex}")
	@Results(value = { @Result(property = "id", column = "id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceState", column = "device_state") })
	List<TUserOperation> getUserOperation(@Param("fromDate") Long fromDate, @Param("toDate") Long toDate,
			@Param("serialId") String serialId, @Param("startIndex") Integer startIndex,
			@Param("countIndex") Integer countIndex);

	/**
	 * @param tableName
	 * @param serialId
	 * @param day
	 * @return
	 * @Description:
	 */
	@Select("select  last_op_time ,device_serial_id,device_state,device_type,device_child_type from ${tableName}"
			+ " where device_serial_id = #{serialId} and DATE_FORMAT(last_op_time, '%Y%m%d')=#{day} order by last_op_time")
	@Results(value = { @Result(property = "id", column = "id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceState", column = "device_state") })
	List<TUserOperation> queryUserOperationByMonth(@Param("tableName") String tableName,
			@Param("serialId") String serialId, @Param("day") String day);

	/**
	 * @param tableName
	 * @return
	 * @Description:
	 */
	@Select("select DATE_FORMAT(last_op_time, '%Y%m%d') as day FROM ${tableName}"
			+ " GROUP BY  DATE_FORMAT(last_op_time, '%Y%m%d')")
	@Results(value = { @Result(property = "id", column = "id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceState", column = "device_state"), @Result(property = "day", column = "day") })
	List<TUserOperation> queryUserOperationByMonthDayList(@Param("tableName") String tableName);

	/**
	 * @param name
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@Select("select  last_op_time ,device_serial_id,device_state,device_type,device_child_type from "
			+ " ${tableName}  where device_serial_id = #{serialId}  order by last_op_time")
	@Results(value = { @Result(property = "id", column = "id"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "deviceType", column = "device_type"),
			@Result(property = "deviceChildType", column = "device_child_type"),
			@Result(property = "deviceSerialId", column = "device_serial_id"),
			@Result(property = "deviceState", column = "device_state"), @Result(property = "day", column = "day") })
	List<TUserOperation> queryUserOperation(@Param("tableName") String tableName, @Param("serialId") String serialId);

	/**
	 * @param tUserOperation
	 * @return
	 * @Description:
	 */
	@Insert("insert into t_user_operation(device_serial_id,\n" + "device_state,\n" + "device_type,\n"
			+ "device_child_type) values(#{deviceSerialId},#{deviceState},#{deviceType},#{deviceChildType})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int addUserOperation(TUserOperation tUserOperation);
}
