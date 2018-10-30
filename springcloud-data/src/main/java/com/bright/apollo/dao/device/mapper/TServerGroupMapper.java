package com.bright.apollo.dao.device.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TServerGroup;

/**  
 *@Title:  
 *@Description:  
 *@Author:JettyLiu
 *@Since:2018年10月30日  
 *@Version:1.1.0  
 */
@Mapper
@Component
public interface TServerGroupMapper {

	/**  
	 * @param groupId
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_server_group where id= #{groupId}")
	@Results(value = {
			@Result(property = "id",column = "id"),
			@Result(property = "groupName",column = "group_name"),
			@Result(property = "last_op_time",column = "lastOpTime"),
			@Result(property = "groupState",column = "group_state"),
			@Result(property = "groupType",column = "group_type"),
			@Result(property = "groupChildType",column = "group_child_type"),
			@Result(property = "groupAddr",column = "group_addr"),
			@Result(property = "groupStyle",column = "group_style"),
	})
	TServerGroup querySererGroupById(@Param("groupId")Integer groupId);

	/**  
	 * @param addr
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_server_group where group_addr= #{addr}")
	@Results(value = {
			@Result(property = "id",column = "id"),
			@Result(property = "groupName",column = "group_name"),
			@Result(property = "last_op_time",column = "lastOpTime"),
			@Result(property = "groupState",column = "group_state"),
			@Result(property = "groupType",column = "group_type"),
			@Result(property = "groupChildType",column = "group_child_type"),
			@Result(property = "groupAddr",column = "group_addr"),
			@Result(property = "groupStyle",column = "group_style"),
	})
	List<TServerGroup> queryServerGroupByAddr(@Param("addr")String addr);

}
