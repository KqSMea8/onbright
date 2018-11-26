package com.bright.apollo.dao.user.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TUserLocation;
import com.bright.apollo.dao.sqlProvider.TUserLocationProvider;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年11月23日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TUserLocationMapper {

	/**  
	 * @param tUserLocation
	 * @return  
	 * @Description:  
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type=TUserLocationProvider.class,method="addUserLocation")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int addUserLocation(TUserLocation tUserLocation);

	/**  
	 * @param location  
	 * @Description:  
	 */
	@Delete("delete from t_user_location where location_id=#{location}")
	void deleteUserLocation(@Param("location")Integer location);

}
