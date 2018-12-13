package com.bright.apollo.dao.scene.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TSceneCondition;

@Mapper
@Component
public interface TSceneConditionMapper{

	/**
	 * @param list
	 * @Description:
	 */
	void batchAdd(List<TSceneCondition> list);

	/**
	 * @param list
	 * @Description:
	 */
	void batchUpdate(List<TSceneCondition> list);

	@Select("select * from t_scene_condition where scene_number = #{sceneNumber}")
	@Results(value = { @Result(column = "id", property = "id"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "serialId", property = "serialid"),
			@Result(column = "last_op_time", property = "lastOpTime"), @Result(column = "cond", property = "cond"),
			@Result(column = "condition_group", property = "conditionGroup") })
	List<TSceneCondition> getSceneConditionBySceneNum(@Param("sceneNumber") int sceneNumber);

	@Delete("delete from t_scene_condition where scene_number = #{sceneNumber}")
	void deleteSceneConditionBySceneNum(@Param("sceneNumber") int sceneNumber);

	@Delete("delete from t_scene_condition where scene_number = #{sceneNumber} and condition_group = #{conditionGroup}")
	void deleteSceneConditionBySceneNumberAndGroup(@Param("sceneNumber") int sceneNumber,
			@Param("conditionGroup") int conditionGroup);

	@Insert("insert into t_scene_condition (scene_number,\n" + "serialId,\n" + "cond,\n" + "condition_group) "
			+ "values (#{sceneNumber},#{serialid},#{cond},#{conditionGroup})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	int addSceneCondition(TSceneCondition sceneCondition);

	@Select("select * from t_scene_condition where serialId = #{serialId}")
	@Results(value = { @Result(column = "id", property = "id"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "serialId", property = "serialid"),
			@Result(column = "last_op_time", property = "lastOpTime"), @Result(column = "cond", property = "cond"),
			@Result(column = "condition_group", property = "conditionGroup") })
	List<TSceneCondition> getSceneConditionBySerialId(@Param("serialId") String serialId);

	@Select("select * from t_scene_condition where scene_number = #{sceneNumber} and condition_group = #{conditionGroup}")
	@Results(value = { @Result(column = "id", property = "id"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "serialId", property = "serialid"),
			@Result(column = "last_op_time", property = "lastOpTime"), @Result(column = "cond", property = "cond"),
			@Result(column = "condition_group", property = "conditionGroup") })
	List<TSceneCondition> getSceneConditionBySceneNumberAndGroup(@Param("sceneNumber") int sceneNumber,
			@Param("conditionGroup") int conditionGroup);

	/**
	 * @param sceneNumber
	 * @return
	 * @Description:
	 */
	@Select("select * from t_scene_condition where scene_number = #{sceneNumber} ")
	@Results(value = { @Result(column = "id", property = "id"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "serialId", property = "serialid"),
			@Result(column = "last_op_time", property = "lastOpTime"), @Result(column = "cond", property = "cond"),
			@Result(column = "condition_group", property = "conditionGroup") })
	List<TSceneCondition> getConditionsBySceneNumber(@Param("sceneNumber") Integer sceneNumber);

	/**  
	 * @param serialId
	 * @return  
	 * @Description:  
	 */
	@Delete("delete from t_scene_condition where serialId = #{serialId}")
	int deleteSceneConditionBySerialId(@Param("serialId")String serialId);

	/**  
	 * @param oboxSerialId  
	 * @Description:  
	 */
	@Delete("delete from t_scene_condition where scene_number in("
			+ "select scene_number from t_scene where obox_serial_id=#{oboxSerialId})")
	void deleteSceneConfitionByOboxSerialId(@Param("oboxSerialId")String oboxSerialId);

 

}