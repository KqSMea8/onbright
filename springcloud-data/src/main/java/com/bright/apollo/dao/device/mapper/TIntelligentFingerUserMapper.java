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
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TIntelligentFingerUser;
import com.bright.apollo.dao.device.sqlProvider.IntelligentFingerUserDynaSqlProvider;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月7日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TIntelligentFingerUserMapper {

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Select("select count(*) from t_intelligent_finger_user where serialId=#{serialId}")
	Integer queryCountIntelligentUserBySerialId(@Param("serialId")String serialId);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Select("SELECT * from t_intelligent_finger_user  where serialId=#{serialId} order by last_op_time desc ")
	@Results(value = { @Result(property = "id", column = "id"),
			@Result(property = "serialid", column = "serialId"),
			@Result(property = "nickName", column = "nick_name"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "mobile", column = "mobile"),
			@Result(property = "existForce", column = "exist_force"),
			@Result(property = "pin", column = "pin"),
			@Result(property = "identity", column = "identity")})
	List<TIntelligentFingerUser> queryIntelligentUserBySerialId(@Param("serialId")String serialId);

	/**  
	 * @param serialId
	 * @param pin
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_intelligent_finger_user where serialId=#{serialId} and pin=#{pin}")
	@Results(value = { @Result(property = "id", column = "id"),
			@Result(property = "serialid", column = "serialId"),
			@Result(property = "nickName", column = "nick_name"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "mobile", column = "mobile"),
			@Result(property = "existForce", column = "exist_force"),
			@Result(property = "pin", column = "pin"),
			@Result(property = "identity", column = "identity") })
	TIntelligentFingerUser queryIntelligentFingerUserBySerialIdAndPin(@Param("serialId")String serialId,@Param("pin") String pin);

	/**  
	 * @param fingerUser  
	 * @Description:  
	 */
	@Update("update t_intelligent_finger_user set mobile= #{mobile}" + ",nick_name= #{nickName }"
			+ ",exist_force= #{existForce}" + ",identity= #{identity}"
			+ " where  id = #{id}")
	void updatentelligentFingerUser(TIntelligentFingerUser fingerUser);

	/**  
	 * @param intelligentFingerUser
	 * @return  
	 * @Description:  
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type=IntelligentFingerUserDynaSqlProvider.class,method="addIntelligentFingerUser")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int addIntelligentFingerUser(TIntelligentFingerUser intelligentFingerUser);

	/**  
	 * @param id  
	 * @Description:  
	 */
	@Delete("delete from t_intelligent_finger_user where id=#{id}")
	void deleteIntelligentFingerUserById(@Param("id")Integer id);

}
