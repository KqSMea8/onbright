package com.bright.apollo.dao.device.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TIntelligentFingerAuth;
import com.bright.apollo.dao.sqlProvider.TIntelligentFingerAuthDynaSqlProvider;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月6日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TIntelligentFingerAuthMapper {

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Select("select count(*) from t_intelligent_finger_auth where serialId=#{serialId}")
	Integer countFingerAuth(@Param(value="serialId")String serialId);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_intelligent_finger_auth where serialId=#{serialId}")
	@Results(value = { @Result(property = "id", column = "id"),
			@Result(property = "serialid", column = "serialId"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "pwd", column = "pwd")})
	TIntelligentFingerAuth queryIntelligentAuthBySerialId(@Param(value="serialId")String serialId);

	/**  
	 * @param auth
	 * @return  
	 * @Description:  
	 */
	@Insert("insert into t_intelligent_finger_auth(serialId," + "pwd) values(#{serialid},#{pwd})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int addIntelligentFingerAuth(TIntelligentFingerAuth auth);

	/**  
	 * @param fingerAuth  
	 * @Description:  
	 */
	@UpdateProvider(type=TIntelligentFingerAuthDynaSqlProvider.class,method="updateTintelligentFingerAuth")
	void updateTintelligentFingerAuth(TIntelligentFingerAuth fingerAuth);

}
