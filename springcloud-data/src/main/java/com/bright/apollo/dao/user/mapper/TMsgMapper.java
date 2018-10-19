package com.bright.apollo.dao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TMsg;
import com.bright.apollo.dao.sqlProvider.TMsgProvider;
import com.bright.apollo.response.MsgExceptionDTO;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年10月18日
 * @Version:1.1.0
 */
@Mapper
@Component
public interface TMsgMapper {

	/**
	 * @param tMsg
	 * @Description:
	 */
	@SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = int.class)
	@InsertProvider(type = TMsgProvider.class, method = "addMsg")
	void addMsg(TMsg tMsg);

	/**
	 * @param userId
	 * @return
	 * @Description:
	 */
	@Select("select count(*) from t_msg a  where a.user_id=#{userId}")
	Integer countMsgList(@Param("userId") Integer userId);

	/**
	 * @param userId
	 * @param type
	 * @param start
	 * @param count
	 * @return
	 * @Description:
	 */
	@Select("select b.type,b.child_type,b.relevancy_id,a.send_time as time,"
			+ "a.content,a.id,a.state,b.device_name as `name`,b.device_serial_id as serialId,a.url  from t_msg a,t_exception b where a.user_id=#{userId} "
			+ "and a.type=#{type} and a.relevancy_id=b.id limit #{start},#{count}")
	@Results(value = {
            @Result(property = "type",column = "type"),
            @Result(property = "child_type",column = "child_type"),
            @Result(property = "relevancy_id",column = "relevancy_id"),
            @Result(property = "time",column = "time"),
            @Result(property = "content",column = "content"),
            @Result(property = "id",column = "id"),
            @Result(property = "state",column = "state"),
            @Result(property = "name",column = "name"),
            @Result(property = "serialId",column = "serialId"),
            @Result(property = "url",column = "url")
    })
	List<MsgExceptionDTO> queryMsgExceList(@Param("userId") Integer userId, @Param("type") Integer type,
			@Param("start") int start, @Param("count") int count);

	/**  
	 * @param userId
	 * @param type
	 * @param start
	 * @param count
	 * @return  
	 * @Description:  
	 */
	@Select("select b.type,b.child_type,b.relevancy_id,a.send_time as time,"
			+ "a.content,a.id,a.state,b.name as `name`,a.url  from t_msg a,t_system b where a.user_id=#{userId} "
			+ "and a.type=#{type} and a.relevancy_id=b.id limit #{start},#{count}")
	@Results(value = {
            @Result(property = "type",column = "type"),
            @Result(property = "child_type",column = "child_type"),
            @Result(property = "relevancy_id",column = "relevancy_id"),
            @Result(property = "time",column = "time"),
            @Result(property = "content",column = "content"),
            @Result(property = "id",column = "id"),
            @Result(property = "state",column = "state"),
            @Result(property = "name",column = "name"),
            @Result(property = "serialId",column = "serialId"),
            @Result(property = "url",column = "url")
    })
	List<MsgExceptionDTO> queryMsgSysList(@Param("userId") Integer userId, @Param("type") Integer type,
			@Param("start") int start, @Param("count") int count);

	/**  
	 * @param userId
	 * @param type
	 * @return  
	 * @Description:  
	 */
	@Select("select count(*) from t_msg a  where a.user_id=#{userId} and a.type=#{type}")
	Integer countMsgExceList(@Param("userId") Integer userId, @Param("type") Integer type);

	/**  
	 * @param id
	 * @param statue  
	 * @Description:  
	 */
	@Update("update set state=#{statue} from t_msg where id =#{id} ")
	void updateMsgState(@Param("id")Integer id,@Param("statue") int statue);

}
