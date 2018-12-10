package com.bright.apollo.dao.user.mapper;

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
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.dao.sqlProvider.TUserDeviceSqlProvider;

@Mapper
@Component
public interface TUserDeviceMapper  {

	/**  
	 * @param wxUserId
	 * @param mobileUserId
	 * @return  
	 * @Description:  
	 */
	List<TUserDevice> getListOfUserDevice(Integer wxUserId, Integer mobileUserId);

	/**  
	 * @param tUserDevices
	 * @return  
	 * @Description:  
	 */
	int insertBatch(List<TUserDevice> tUserDevices);

	/**  
	 * @param wxUserId
	 * @param mobileUserId
	 * @return  
	 * @Description:  
	 */
	List<TUserDevice> getListOverPrivilegeOfUserDevice(Integer wxUserId, Integer mobileUserId);

	/**  
	 * @param list
	 * @return  
	 * @Description:  
	 */
	int updateBatch(List<TUserDevice> list);

	@Delete("delete from t_user_device where device_serial_id=#{id}")
	void deleteTUserDevice(@Param("id") String id);

	@Select(" select * from t_user tuser " +
			" inner join t_user_device tud on tuser.id = tud.user_id " +
			" where id=#{configDeviceId} ")
	List<TUser> getUsersByDeviceId(@Param("configDeviceId") int configDeviceId);

	/**  
	 * @param tUserDevice
	 * @return  
	 * @Description:  
	 */
	@Insert("insert into t_user_device (device_serial_id,\n" +
			"user_id,\n" +
			"privilege) values(#{deviceSerialId},#{userId}," +
			"#{privilege})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	int addUserDevice(TUserDevice tUserDevice);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	@Select(" select * from t_user_device " +
			" where user_id=#{userId} ")
	List<TUserDevice> getListOfUserDeviceByUserId(@Param("userId") Integer userId);

	/**  
	 * @param userId
	 * @param device_serial_id
	 * @return  
	 * @Description:  
	 */
	@Select(" select * from t_user_device " +
			" where user_id=#{userId} and device_serial_id=#{device_serial_id}")
	@Results(value = {
            @Result(property = "id",column = "id"),
            @Result(property = "deviceSerialId",column = "device_serial_id"),
            @Result(property = "lastOpTime",column = "last_op_time"),
            @Result(property = "userId",column = "user_id"),
            @Result(property = "privilege",column = "privilege")
    })
	TUserDevice getUserDeviceByUserIdAndSerialId(@Param("userId") Integer userId,@Param("device_serial_id") String device_serial_id);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Select(" select * from t_user_device " +
			" where device_serial_id=#{serialId}")
	@Results(value = {
            @Result(property = "id",column = "id"),
            @Result(property = "deviceSerialId",column = "device_serial_id"),
            @Result(property = "lastOpTime",column = "last_op_time"),
            @Result(property = "userId",column = "user_id"),
            @Result(property = "privilege",column = "privilege")
    })
	List<TUserDevice> queryUserDevicesBySerialId(@Param("serialId")String serialId);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Delete("delete from t_user_device where device_serial_id=#{serialId}")
	int deleteUserDeviceBySerialId(@Param("serialId")String serialId);

	/**  
	 * @param oboxSerialId
	 * @return  
	 * @Description:  
	 */
	@Delete("DELETE FROM t_user_device WHERE id in (SELECT t.id from(SELECT"+
			" a.id FROM t_user_device AS a INNER JOIN t_obox_device_config"+
			" b ON a.device_serial_id = b.device_serial_id"+
			" AND b.obox_serial_id = #{oboxSerialId})t)")
	int deleteUserDeviceByOboxSerialId(@Param("oboxSerialId")String oboxSerialId);

	/**  
	 * @param userDevices  
	 * @Description:  
	 */
	@InsertProvider(type = TUserDeviceSqlProvider.class, method = "batchAddUserDevice")
	void batchAddUserDevice(@Param("userDevices")List<TUserDevice> userDevices);
 

}