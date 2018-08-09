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

import com.bright.apollo.common.entity.TIntelligentFingerRemoteUser;
import com.bright.apollo.dao.device.sqlProvider.IntelligentFingerRemoteUserDynaSqlProvider;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年8月7日  
 *@Version:1.1.0  
 */
@Mapper
@Component	
public interface TIntelligentFingerRemoteUserMapper {

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Select("SELECT * from t_intelligent_finger_remote_user  where serialId=#{serialId} order by last_op_time desc ")
	@Results(value = { @Result(property = "userSerialid", column = "user_serialId"),
			@Result(property = "mobile", column = "mobile"),
			@Result(property = "serialid", column = "serialId"),
			@Result(property = "nickName", column = "nick_name"),
			@Result(property = "startTime", column = "start_time"),
			@Result(property = "endTime", column = "end_time"),
			@Result(property = "times", column = "times"),
			@Result(property = "useTimes", column = "use_times"),
			@Result(property = "pwd", column = "pwd"),
			@Result(property = "isend", column = "isEnd"),
			@Result(property = "ismax", column = "isMax"),
			@Result(property = "lastOpTime", column = "last_op_time")})
	List<TIntelligentFingerRemoteUser> queryIntelligentFingerRemoteUsersBySerialId(@Param("serialId") String serialId);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_intelligent_finger_remote_user a where a.serialId=#{serialId} order by user_serialId desc")
	List<TIntelligentFingerRemoteUser> queryTIntelligentFingerRemoteUsersBySerialId(@Param("serialId")String serialId);

	/**  
	 * @param fingerRemoteUser
	 * @return  
	 * @Description:  
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type=IntelligentFingerRemoteUserDynaSqlProvider.class,method="IntelligentFingerRemoteUser")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	Integer addTIntelligentFingerRemoteUser(TIntelligentFingerRemoteUser fingerRemoteUser);

	/**  
	 * @param fingerRemoteUser  
	 * @Description:  
	 */
	@UpdateProvider(type=IntelligentFingerRemoteUserDynaSqlProvider.class,method="updateTintelligentFingerRemoteUser")
	void updateTintelligentFingerRemoteUser(TIntelligentFingerRemoteUser fingerRemoteUser);

	/**  
	 * @param id
	 * @return  
	 * @Description:  
	 */
	@Select("SELECT * from t_intelligent_finger_remote_user  where id=#{id} order by last_op_time desc ")
	@Results(value = { @Result(property = "userSerialid", column = "user_serialId"),
			@Result(property = "mobile", column = "mobile"),
			@Result(property = "id", column = "id"),
			@Result(property = "serialid", column = "serialId"),
			@Result(property = "nickName", column = "nick_name"),
			@Result(property = "startTime", column = "start_time"),
			@Result(property = "endTime", column = "end_time"),
			@Result(property = "times", column = "times"),
			@Result(property = "useTimes", column = "use_times"),
			@Result(property = "pwd", column = "pwd"),
			@Result(property = "isend", column = "isEnd"),
			@Result(property = "ismax", column = "isMax"),
			@Result(property = "lastOpTime", column = "last_op_time")})
	TIntelligentFingerRemoteUser queryTintelligentFingerRemoteUserById(@Param("id")int id);

	/**  
	 * @param id  
	 * @Description:  
	 */
	@Delete("delete from t_intelligent_finger_remote_user where id =#{id}")
	void delTIntelligentFingerRemoteUserById(@Param("id")int id);

	/**  
	 * @param serialId
	 * @param pin
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_intelligent_finger_remote_user where serialId=#{serialId} and user_serialId=#{pin} and isEnd=0")
	@Results(value = { @Result(property = "userSerialid", column = "user_serialId"),
			@Result(property = "mobile", column = "mobile"),
			@Result(property = "id", column = "id"),
			@Result(property = "serialid", column = "serialId"),
			@Result(property = "nickName", column = "nick_name"),
			@Result(property = "startTime", column = "start_time"),
			@Result(property = "endTime", column = "end_time"),
			@Result(property = "times", column = "times"),
			@Result(property = "useTimes", column = "use_times"),
			@Result(property = "pwd", column = "pwd"),
			@Result(property = "isend", column = "isEnd"),
			@Result(property = "ismax", column = "isMax"),
			@Result(property = "lastOpTime", column = "last_op_time")})
	TIntelligentFingerRemoteUser queryTIntelligentFingerRemoteUserBySerialIdAndPin(@Param("serialId")String serialId,@Param("id") int pin);

 

}
