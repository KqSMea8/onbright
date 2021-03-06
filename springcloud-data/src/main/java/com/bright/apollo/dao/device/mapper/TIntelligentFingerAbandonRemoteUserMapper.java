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
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TIntelligentFingerAbandonRemoteUser;
import com.bright.apollo.dao.sqlProvider.IntelligentFingerRemoteUserDynaSqlProvider;
import com.bright.apollo.dao.sqlProvider.IntelligentFingerabandonRemoteUserDynaSqlProvider;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年8月8日
 * @Version:1.1.0
 */
@Mapper
@Component
public interface TIntelligentFingerAbandonRemoteUserMapper {

	/**
	 * @param serialId
	 * @return
	 * @Description:
	 */
	@Select("select a.* from t_intelligent_finger_abandon_remote_user a where a.serialId=#{serialId}")
	@Results(value = { @Result(property = "id", column = "id"),
			@Result(property = "userSerialid", column = "user_serialId"),
			@Result(property = "lastOpTime", column = "last_op_time"),
			@Result(property = "serialid", column = "serialId")})
	List<TIntelligentFingerAbandonRemoteUser> queryTIntelligentFingerAbandonRemoteUsersBySerialId(
			@Param("serialId") String serialId);

	/**  
	 * @param id  
	 * @Description:  
	 */
	@Delete("delete from t_intelligent_finger_abandon_remote_user where id=#{id}")
	void delIntelligentFingerAbandonRemoteUserById(@Param("id")Integer id);

	/**  
	 * @param abandonRemoteUser
	 * @return  
	 * @Description:  
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type=IntelligentFingerabandonRemoteUserDynaSqlProvider.class,method="addIntelligentFingerAbandonRemoteUser")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	Integer addIntelligentFingerAbandonRemoteUser(TIntelligentFingerAbandonRemoteUser abandonRemoteUser);

	/**  
	 * @param serialId
	 * @param pin  
	 * @Description:  
	 */
	@Delete("delete from t_intelligent_finger_abandon_remote_user where serialId=#{serialId} and user_serialId=#{pin}")
	void delIntelligentFingerAbandonRemoteUserBySerialIdAndPin(@Param("serialId")String serialId, @Param("pin")Integer pin);

}
