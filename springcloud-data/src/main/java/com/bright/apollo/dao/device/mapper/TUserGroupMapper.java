package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TUSerGroup;
import com.bright.apollo.dao.sqlProvider.USerGroupProvider;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月30日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TUserGroupMapper {

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_user_group where user_id= #{userId}")
	@Results(value = {
			@Result(property = "userId",column = "user_id"),
			@Result(property = "groupId",column = "group_id"),
			@Result(property = "last_op_time",column = "lastOpTime")
	})
	List<TUSerGroup> queryUserGroup(@Param("userId")Integer userId);

	/**  
	 * @param tUserGroup
	 * @return  
	 * @Description:  
	 */
	@InsertProvider(type=USerGroupProvider.class,method="addUserGroup")
	int addUserGroup(TUSerGroup tUserGroup);

}
