package com.bright.apollo.dao.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TUser;
import com.bright.apollo.dao.device.sqlProvider.TIntelligentFingerAuthDynaSqlProvider;
import com.bright.apollo.dao.device.sqlProvider.UserProvider;

@Mapper
@Component
public interface TUserMapper {

	@Select("select * from t_user where id=#{id}")
	@Results(value = { @Result(property = "userName", column = "user_name"),
			@Result(property = "openId", column = "open_id"),
			@Result(property = "lastOpTime", column = "last_op_time") })
	TUser getUserById(@Param("id") int id);

	@Select(" select * from t_user tu " + " inner join t_user_scene tus on tu.id=tus.user_id "
			+ " where scene_number = #{sceneNumber} ")
	List<TUser> getUsersBySceneNumber(@Param("sceneNumber") int sceneNumber);

	@Select("select * from t_user where user_name=#{userName}")
	TUser getUserByUserName(@Param("userName") String userName);

	@Select("select * from t_user where open_id=#{openId}")
	TUser getUserByOpenId(@Param("openId") String openId);

	/**
	 * @param mobile
	 * @param pwd
	 * @return
	 * @Description:
	 */
	@Insert("insert into t_user (user_name,password) values(#{mobile},#{pwd})")
	int addUser(@Param("mobile") String mobile, @Param("pwd") String pwd);

	/**
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@Select("select a.*" + " from t_user a ,t_user_scene b where b.scene_number=#{sceneNumber} and" + " b.user_id=a.id")
	@Results(value = { @Result(property = "userName", column = "user_name"), @Result(property = "id", column = "id"),
			@Result(property = "nickname", column = "nickname"),
			@Result(property = "headimgurl", column = "headimgurl"),
			@Result(property = "password", column = "password"), @Result(property = "openId", column = "open_id"),
			@Result(property = "lastOpTime", column = "last_op_time") })
	List<TUser> queryUserBySceneNumber(@Param("sceneNumber") Integer sceneNumber);

	/**
	 * @param user
	 * @Description:
	 */
	@UpdateProvider(type = UserProvider.class, method = "updateUser")
	void updateUser(TUser user);

	/**
	 * @param openid
	 * @param headimgurl
	 * @param nickname
	 * @return
	 * @Description:
	 */
	@Insert("insert into t_user (open_id,headimgurl,nickname) values(#{openid},#{headimgurl},#{nickname})")
	int saveUserByWeiXinInfo(@Param("openid") String openid, @Param("headimgurl") String headimgurl,
			@Param("nickname") String nickname);
	
	/**
	 * @param openid
	 * @return
	 * @Description:
	 */
	@Insert("insert into t_user (open_id) values(#{openid})")
	int saveUserOpenId(@Param("openid") String openid);
}