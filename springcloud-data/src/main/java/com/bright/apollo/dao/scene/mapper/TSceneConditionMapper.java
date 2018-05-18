package com.bright.apollo.dao.scene.mapper;

import com.bright.apollo.common.entity.TSceneCondition;
import com.bright.apollo.common.entity.TSceneConditionExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
	List<TSceneCondition> getSceneConditionBySceneNum(int sceneNumber);

	@Delete("delete from t_scene_condition where scene_number = #{sceneNumber}")
	void deleteSceneConditionBySceneNum(int sceneNumber);

	@Delete("delete from t_scene_condition where scene_number = #{sceneNumber} and condition_group = #{conditionGroup}")
	void deleteSceneConditionBySceneNumberAndGroup(int sceneNumber,int conditionGroup);

	@Insert("insert into t_scene_condition (scene_number,\n" +
			"serialId,\n" +
			"last_op_time,\n" +
			"cond,\n" +
			"condition_group) " +
			"values (#{sceneNumber},#{serialid},#{lastOpTime},#{cond},#{conditionGroup})")
	void addSceneCondition(TSceneCondition sceneCondition);

	@Select("select * from t_scene_condition where serialId = #{serialId}")
	List<TSceneCondition> getSceneConditionBySerialId(String serialId);

	@Select("select * from t_scene_condition where scene_number = #{sceneNumber} and condition_group = #{conditionGroup}")
	List<TSceneCondition> getSceneConditionBySceneNumberAndGroup(int sceneNumber, int conditionGroup);

}