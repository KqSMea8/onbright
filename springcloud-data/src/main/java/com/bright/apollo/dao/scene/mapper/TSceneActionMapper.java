package com.bright.apollo.dao.scene.mapper;

import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneActionExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TSceneActionMapper extends BaseMapper<TSceneAction, TSceneActionExample, Integer> {

	/**  
	 * @param list  
	 * @Description:  
	 */
	void batchAdd(List<TSceneAction> list);

	/**  
	 * @param list  
	 * @Description:  
	 */
	void batchUpdate(List<TSceneAction> list);

	@Delete("delete from t_scene_action where scene_number = #{sceneNumber}")
	void deleteSceneActionBySceneNumber(int sceneNumber);

	@Select("select * from t_scene_action where scene_number = #{sceneNumber}")
	List<TSceneAction> getSceneActionBySceneNumber(int sceneNumber);

	@Delete("delete t_scene_action where scene_number=#{sceneNumber} and actionId = #{actionID}")
	void deleteSceneActionByBySceneNumberAndActionId(int sceneNumber,String actionId);

	@Insert("insert into t_scene_action (actionID,\n" +
			"action,\n" +
			"last_op_time,\n" +
			"node_type,\n" +
			"scene_number,\n" +
			"preSet) " +
			"values(#{actionid},#{action},#{lastOpTime},#{nodeType},#{sceneNumber},#{preset})")
	void addSceneAction(TSceneAction sceneAction);

	@Select("select * from t_scene_action where scene_number = #{sceneNumber} and actionID = #{actionId}")
	TSceneAction getSceneActionBySceneNumberAndActionId(int sceneNumber,String actionId);

	@Update("update t_scene_action set scene_number = #{sceneNumber},\n" +
			"actionID = #{actionid},\n" +
			"action = #{action},\n" +
			"last_op_time = #{lastOpTime},\n" +
			"node_type = #{nodeType},\n" +
			"preSet = #{preset}" +
			"where id = #{id}")
	void updateSceneAction(TSceneAction sceneAction);

}