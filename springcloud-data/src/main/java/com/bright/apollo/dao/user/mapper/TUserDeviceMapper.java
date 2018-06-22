package com.bright.apollo.dao.user.mapper;

import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.common.entity.TUserDevice;
import com.bright.apollo.common.entity.TUserDeviceExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TUserDeviceMapper extends BaseMapper<TUserDevice, TUserDeviceExample, Integer> {

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

}