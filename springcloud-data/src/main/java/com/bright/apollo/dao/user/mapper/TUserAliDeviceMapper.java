package com.bright.apollo.dao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TUserAliDevice;
import com.bright.apollo.dao.sqlProvider.UserAliDeviceProvider;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月17日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TUserAliDeviceMapper {

	/**  
	 * @param deviceSerialId  
	 * @Description:  
	 */
	@Delete("delete from t_user_ali_device where device_serial_id = #{deviceSerialId}")
	public void deleteUserAliDev(@Param("deviceSerialId") String deviceSerialId);

	/**  
	 * @param tUserAliDev  
	 * @Description:  
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type = UserAliDeviceProvider.class, method = "addUserAliDev")
	public void addUserAliDev(TUserAliDevice tUserAliDev);

	/**  
	 * @param deviceSerialId  
	 * @Description:  
	 */
	@Select("select DISTINCT(a.user_id) as user_id from t_user_ali_device a,t_ali_device_config "
			+ "b where b.device_serial_id=#{deviceSerialId} "
			+ "and a.device_serial_id=b.device_serial_id")
	@Results(value = {
			@Result(property = "userId",column = "user_id")
	})
	public List<TUserAliDevice> queryAliUserId(@Param("deviceSerialId") String deviceSerialId);


	@Select("select user_id from t_user_ali_device "
			+ " where device_serial_id=#{deviceSerialId} ")
	@Results(value = {
			@Result(property = "userId",column = "user_id")
	})
	TUserAliDevice queryAliDeviceBySerialiId(String deviceSerialId);

}
