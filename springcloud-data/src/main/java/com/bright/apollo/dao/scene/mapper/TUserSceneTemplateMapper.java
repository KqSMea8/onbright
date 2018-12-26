package com.bright.apollo.dao.scene.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TUserSceneTemplate;
import com.bright.apollo.dao.sqlProvider.UserSceneTemplateProvider;

/**
 * @Title:
 * @Description:
 * @Author:JettyLiu
 * @Since:2018年12月19日
 * @Version:1.1.0
 */
@Mapper
@Component
public interface TUserSceneTemplateMapper {

	/**
	 * @param tUserSceneTemplate
	 * @Description:
	 */
	@InsertProvider(type = UserSceneTemplateProvider.class, method = "addTemplate")
	void addTemplate(TUserSceneTemplate tUserSceneTemplate);

	/**
	 * @param userId
	 * @param modelName
	 * @return
	 * @Description:
	 */
	@Select("select * from t_user_scene_template where user_id=#{userId} and modelName=#{modelName}")
	@Results(value = { @Result(column = "user_id", property = "userId"),
			@Result(column = "modelName", property = "modelname"), @Result(column = "id", property = "id"),
			@Result(column = "scene_model", property = "sceneModel"),
			@Result(column = "last_op_time", property = "lastOpTime"),
			@Result(column = "device_model", property = "deviceModel") })
	List<TUserSceneTemplate> queryUserSceneTemplateByUserIdAndModelName(@Param("userId") Integer userId,
			@Param("modelName") String modelName);

	/**
	 * @param userId
	 * @param modelName
	 * @param sceneModel
	 * @param deviceModel
	 * @Description:
	 */
	@Update("update t_user_scene_template set scene_model=#{sceneModel},device_model=#{deviceModel}"
			+ " where user_id=#{userId} and modelName=#{modelName}")
	void updateTemplate(@Param("userId") Integer userId, @Param("modelName") String modelName,
			@Param("sceneModel") String sceneModel, @Param("deviceModel") String deviceModel);

	/**  
	 * @param userId
	 * @param modelName  
	 * @Description:  
	 */
	@Delete("delete from t_user_scene_template where user_id=#{userId} and modelName=#{modelName}")
	void deleteTemplate(@Param("userId")Integer userId, @Param("modelName")String modelName);

	/**  
	 * @param userId
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_user_scene_template where user_id=#{userId}")
	@Results(value = { @Result(column = "user_id", property = "userId"),
			@Result(column = "modelName", property = "modelname"), @Result(column = "id", property = "id"),
			@Result(column = "scene_model", property = "sceneModel"),
			@Result(column = "last_op_time", property = "lastOpTime"),
			@Result(column = "device_model", property = "deviceModel") })
	List<TUserSceneTemplate> queryTemplateByUserId(@Param("userId")Integer userId);

}
