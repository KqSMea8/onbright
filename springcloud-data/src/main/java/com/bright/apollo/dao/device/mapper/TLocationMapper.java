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
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TLocation;
import com.bright.apollo.dao.sqlProvider.LocationProvider;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年11月23日
 * @Version:1.1.0
 */
@Mapper
@Component
public interface TLocationMapper {

	/**
	 * @param building
	 * @param room
	 * @param userId
	 * @return
	 * @Description:
	 */
	@Select("select a.* from t_location a where a.building=#{building} and a.room=#{room} and  "
			+ "EXISTS (select 1 from t_user_location where a.id=location_id and user_id=#{userId} )")
	@Results(value = { @Result(property = "id", column = "id"), @Result(property = "building", column = "building"),
			@Result(property = "lastOpTime", column = "last_op_time"), @Result(property = "room", column = "room"),
			@Result(property = "downloadUrl", column = "download_url"),
			@Result(property = "license", column = "license"), @Result(property = "thumUrl", column = "thum_url") })
	TLocation queryLocat1ionByUserId(@Param("building") String building, @Param("room") String room,
			@Param("userId") Integer userId);

	/**
	 * @param tLocation
	 * @return
	 * @Description:
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type = LocationProvider.class, method = "addLocation")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int addLocation(TLocation tLocation);

	/**
	 * @param userId
	 * @param location
	 * @return
	 * @Description:
	 */
	@Select("select a.* from t_location a where  "
			+ "EXISTS (select 1 from t_user_location where a.id=location_id and user_id=#{userId} and location_id= #{location})")
	@Results(value = { @Result(property = "id", column = "id"), @Result(property = "building", column = "building"),
			@Result(property = "lastOpTime", column = "last_op_time"), @Result(property = "room", column = "room"),
			@Result(property = "status", column = "status"),
			@Result(property = "userName", column = "user_name"),
			@Result(property = "downloadUrl", column = "download_url"),
			@Result(property = "license", column = "license"), @Result(property = "thumUrl", column = "thum_url") })
	TLocation queryLocationByUserIdAndId(@Param("userId") Integer userId, @Param("location") Integer location);

	/**
	 * @param location
	 * @Description:
	 */
	@Delete("delete from t_location where id =#{location}")
	void deleteLocation(@Param("location") Integer location);

	/**
	 * @param tLocation
	 * @Description:
	 */
	@UpdateProvider(type = LocationProvider.class, method = "updateLocation")
	void updateLocation(TLocation tLocation);

	/**
	 * @param locationId
	 * @return
	 * @Description:
	 */
	@Select("select * from t_location where id=#{locationId} ")
	@Results(value = { @Result(property = "id", column = "id"), @Result(property = "building", column = "building"),
			@Result(property = "lastOpTime", column = "last_op_time"), @Result(property = "room", column = "room"),
			@Result(property = "downloadUrl", column = "download_url"),
			@Result(property = "status", column = "status"),
			@Result(property = "userName", column = "user_name"),
			@Result(property = "license", column = "license"), @Result(property = "thumUrl", column = "thum_url") })
	TLocation queryLocationById(@Param("locationId") Integer locationId);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	@Select("SELECT b.* FROM t_user_location AS a INNER JOIN "
			+ " t_location AS b ON b.id=a.location_id and a.user_id=#{userId}")
	@Results(value = { @Result(property = "id", column = "id"), @Result(property = "building", column = "building"),
			@Result(property = "lastOpTime", column = "last_op_time"), @Result(property = "room", column = "room"),
			@Result(property = "downloadUrl", column = "download_url"),
			@Result(property = "userName", column = "user_name"),
			@Result(property = "status", column = "status"),
			@Result(property = "license", column = "license"), @Result(property = "thumUrl", column = "thum_url") })
	List<TLocation> queryLocationByUser(@Param("userId") Integer userId);

}
