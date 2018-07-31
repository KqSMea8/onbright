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
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import com.bright.apollo.common.entity.TSceneAction;
import com.bright.apollo.common.entity.TSceneActionExample;
import com.bright.apollo.dao.mapper.base.BaseMapper;

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
	void deleteSceneActionBySceneNumber(@Param("sceneNumber") int sceneNumber);

	@Select("select * from t_scene_action where scene_number = #{sceneNumber}")
	@Results(value = { @Result(column = "id", property = "id"),
			@Result(column = "scene_number", property = "sceneNumber"),
			@Result(column = "actionID", property = "actionid"), @Result(column = "preSet", property = "preset"),
			@Result(column = "last_op_time", property = "lastOpTime"), @Result(column = "node_type", property = "nodeType"),
			@Result(column = "action", property = "action") })
	List<TSceneAction> getSceneActionBySceneNumber(@Param("sceneNumber") int sceneNumber);

	@Delete("delete t_scene_action where scene_number=#{sceneNumber} and actionID = #{actionId}")
	void deleteSceneActionByBySceneNumberAndActionId(@Param("sceneNumber") int sceneNumber,@Param("actionId") String actionId);

	@Insert("insert into t_scene_action (actionID,\n" +
			"action,\n" +
			"node_type,\n" +
			"scene_number,\n" +
			"preSet) " +
			"values(#{actionid},#{action},#{nodeType},#{sceneNumber},#{preset})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	int addSceneAction(TSceneAction sceneAction);

	@Select("select * from t_scene_action where scene_number = #{sceneNumber} and actionID = #{actionId}")
	TSceneAction getSceneActionBySceneNumberAndActionId(@Param("sceneNumber") int sceneNumber,@Param("actionId") String actionId);

	@Update("update t_scene_action set scene_number = #{sceneNumber},\n" +
			"actionID = #{actionid},\n" +
			"action = #{action},\n" +
			"last_op_time = #{lastOpTime},\n" +
			"node_type = #{nodeType},\n" +
			"preSet = #{preset}" +
			"where id = #{id}")
	void updateSceneAction(TSceneAction sceneAction);

}