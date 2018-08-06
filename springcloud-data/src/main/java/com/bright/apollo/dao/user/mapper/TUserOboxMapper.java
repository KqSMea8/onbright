package com.bright.apollo.dao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TUserObox;

@Mapper
@Component
public interface TUserOboxMapper {

	/**  
	 * @param wxUserId
	 * @param mobileUserId
	 * @return  
	 * @Description:  
	 */
	List<TUserObox> getListOfUserObox(Integer wxUserId, Integer mobileUserId);
	
	int insertBatch(List<TUserObox> list);
	
	int updateBatch(List<TUserObox> list);

	/**  
	 * @param wxUserId
	 * @param mobileUserId
	 * @return  
	 * @Description:  
	 */
	List<TUserObox> getListOverPrivilegeOfUserObox(Integer wxUserId, Integer mobileUserId);

	@Select("select * from t_user_obox where obox_serial_id = #{oboxSerialId}")
	@Results(value = {
			@Result(property = "id",column = "id"),
 			@Result(property = "lastOpTime",column = "last_op_time"),
 			@Result(property = "userId",column = "user_id"),
   			@Result(property = "oboxSerialId",column = " obox_serial_id")
	})
	List<TUserObox> getUserOboxBySerialId(@Param("oboxSerialId") String oboxSerialId);

	@Insert("insert into t_user_obox(obox_serial_id,\n" +
			"user_id,\n" +
			"privilege) values(#{oboxSerialId},#{userId},#{privilege})")
	int addUserObox(TUserObox userObox);

	@Delete("delete from t_user_obox where obox_serial_id = #{oboxSerialId}")
	void delectUserOboxByOboxSerialId(@Param("oboxSerialId") String oboxSerialId);

	/**  
	 * @param userId
	 * @param oboxSerialId
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_user_obox where obox_serial_id = #{oboxSerialId} and user_id=#{userId}")
	@Results(value = {
			@Result(property = "id",column = "id"),
 			@Result(property = "lastOpTime",column = "last_op_time"),
 			@Result(property = "userId",column = "user_id"),
   			@Result(property = "oboxSerialId",column = " obox_serial_id"),
   			@Result(property = "privilege",column = " privilege")
	})
	TUserObox getUserOboxByUserIdAndOboxSerialId(@Param("userId")Integer userId, @Param("oboxSerialId")String oboxSerialId);

	 
}