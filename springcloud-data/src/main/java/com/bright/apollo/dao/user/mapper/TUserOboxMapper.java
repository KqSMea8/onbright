package com.bright.apollo.dao.user.mapper;

import com.bright.apollo.common.entity.TUserObox;
import com.bright.apollo.common.entity.TUserOboxExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TUserOboxMapper extends BaseMapper<TUserObox, TUserOboxExample, Integer> {

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
	List<TUserObox> getUserOboxBySerialId(@Param("oboxSerialId") String oboxSerialId);

	@Insert("insert into t_user_obox(obox_serial_id,\n" +
			"user_id,\n" +
			"privilege) values(#{oboxSerialId},#{userId},#{privilege})")
	int addUserObox(TUserObox userObox);

	@Delete("delete from t_user_obox where obox_serial_id = #{oboxSerialId}")
	void delectUserOboxByOboxSerialId(@Param("oboxSerialId") String oboxSerialId);

	 
}