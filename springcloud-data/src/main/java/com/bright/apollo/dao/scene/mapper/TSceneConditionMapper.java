package com.bright.apollo.dao.scene.mapper;

import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.common.entity.TSceneConditionExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TSceneConditionMapper extends BaseMapper<TSceneCondition, TSceneConditionExample, Integer> {

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
	List<TSceneCondition> getSceneConditionBySceneNum(@Param("sceneNumber") int sceneNumber);

	@Delete("delete from t_scene_condition where scene_number = #{sceneNumber}")
	void deleteSceneConditionBySceneNum(@Param("sceneNumber") int sceneNumber);

	@Delete("delete from t_scene_condition where scene_number = #{sceneNumber} and condition_group = #{conditionGroup}")
	void deleteSceneConditionBySceneNumberAndGroup(@Param("sceneNumber") int sceneNumber,@Param("conditionGroup") int conditionGroup);

	@Insert("insert into t_scene_condition (scene_number,\n" +
			"serialId,\n" +
			"cond,\n" +
			"condition_group) " +
			"values (#{sceneNumber},#{serialid},#{cond},#{conditionGroup})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	int addSceneCondition(TSceneCondition sceneCondition);

	@Select("select * from t_scene_condition where serialId = #{serialId}")
	List<TSceneCondition> getSceneConditionBySerialId(@Param("serialId") String serialId);

	@Select("select * from t_scene_condition where scene_number = #{sceneNumber} and condition_group = #{conditionGroup}")
	List<TSceneCondition> getSceneConditionBySceneNumberAndGroup(@Param("sceneNumber") int sceneNumber,@Param("conditionGroup") int conditionGroup);

	/**  
	 * @param sceneNumber
	 * @return  
	 * @Description:  
	 */
	@Select("select * from t_scene_condition where scene_number = #{sceneNumber} ")
	List<TSceneCondition> getConditionsBySceneNumber(Integer sceneNumber);

}